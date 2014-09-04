/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.utils.CodeList;

public class IndicatorPoolExporter extends BaseObjectPoolExporter
{
	public IndicatorPoolExporter(Xmpz1XmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, INDICATOR, IndicatorSchema.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);

		Indicator indicator = (Indicator) baseObject;
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_DETAIL);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_COMMENTS);
		writeCodeElementSameAsTag(indicator, Indicator.TAG_PRIORITY, new PriorityRatingQuestion());
		exportLastestFutureStatus(indicator);	
		writeProgressReportIds(indicator);
		writeExpenseAssignmentIds(indicator);
		writeResourceAssignmentIds(indicator);
		writeMeasurementIds(indicator.getMeasurementRefs());
		writeOptionalIds(Xmpz1XmlConstants.METHOD_IDS, Xmpz1XmlConstants.METHOD, indicator.getMethodRefs());
		writeOptionalThreshold(indicator);
		writeCodeElementSameAsTag(indicator, Indicator.TAG_RATING_SOURCE, StaticQuestionManager.getQuestion(RatingSourceQuestion.class));
		writeOptionalElementWithSameTag(indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENTS);
		writeOptionalCalculatedTimePeriodCosts(indicator);
	}

	private void exportLastestFutureStatus(Indicator indicator) throws Exception
	{
		ORef futureStatusRef = indicator.getLatestFutureStatusRef();
		if (futureStatusRef.isInvalid())
		{
			writeCodeElement("FutureStatusRating", new StatusQuestion(), StatusQuestion.UNSPECIFIED);
			return;
		}
		FutureStatus futureStatus = FutureStatus.find(getProject(), futureStatusRef);
		getWcsXmlExporter().writeOptionalElement(getWriter(), "IndicatorFutureStatusDate", futureStatus.getData(FutureStatusSchema.TAG_FUTURE_STATUS_DATE));
		getWcsXmlExporter().writeOptionalElement(getWriter(), "IndicatorFutureStatusSummary", futureStatus.getData(FutureStatusSchema.TAG_FUTURE_STATUS_SUMMARY));
		
		ChoiceItem ratingChoice = futureStatus.getChoiceItemData(FutureStatusSchema.TAG_FUTURE_STATUS_RATING);
		StatusQuestion question = new StatusQuestion();
		getWcsXmlExporter().writeElement(getWriter(), "IndicatorFutureStatusRating", question.convertToReadableCode(ratingChoice.getCode()));
		
		getWcsXmlExporter().writeOptionalElement(getWriter(), "IndicatorFutureStatusDetails", futureStatus.getData(FutureStatusSchema.TAG_FUTURE_STATUS_DETAIL));
		getWcsXmlExporter().writeOptionalElement(getWriter(), "IndicatorFutureStatusComments", futureStatus.getData(FutureStatusSchema.TAG_FUTURE_STATUS_COMMENTS));
	}
	
	private void writeOptionalThreshold(Indicator indicator) throws Exception
	{
		CodeToUserStringMap thresholdValues = indicator.getThresholdsMap().getCodeToUserStringMap();
		CodeToUserStringMap thresholdDetails = indicator.getThresholdDetailsMap();
		if (thresholdValues.size() == 0 && thresholdDetails.size() == 0)
			return;
		
		getWcsXmlExporter().writeStartElement(getWriter(), getWcsXmlExporter().createParentAndChildElementName(getPoolName(), THRESHOLDS));
		ChoiceQuestion question = StaticQuestionManager.getQuestion(StatusQuestion.class);
		CodeList allCodes = question.getAllCodes();
		for(int index = 0; index < allCodes.size(); ++index)
		{
			String code = allCodes.get(index);
			if (code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			
			getWcsXmlExporter().writeStartElement(getWriter(), THRESHOLD);
			getWcsXmlExporter().writeOptionalElement(getWriter(), STATUS_CODE, code);
			getWcsXmlExporter().writeOptionalElement(getWriter(), THRESHOLD_VALUE, thresholdValues.getUserString(code));
			getWcsXmlExporter().writeOptionalElement(getWriter(), THRESHOLD_DETAILS, thresholdDetails.getUserString(code));
			getWcsXmlExporter().writeEndElement(getWriter(), THRESHOLD);			
		}
		
		getWcsXmlExporter().writeEndElement(getWriter(), getWcsXmlExporter().createParentAndChildElementName(getPoolName(), THRESHOLDS));
	}

	private void writeMeasurementIds(ORefList measurementRefs) throws Exception
	{
		writeOptionalIds(Xmpz1XmlConstants.MEASUREMENT_IDS, Xmpz1XmlConstants.MEASUREMENT, measurementRefs);
	}
}
