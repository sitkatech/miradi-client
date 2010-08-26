/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.CodeList;

public class IndicatorPoolExporter extends BaseObjectPoolExporter
{
	public IndicatorPoolExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, INDICATOR, Indicator.getObjectType());
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
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_DATE);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_SUMMARY);
		writeCodeElementSameAsTag(indicator, Indicator.TAG_FUTURE_STATUS_RATING, new StatusQuestion());
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_DETAIL);
		writeOptionalElementWithSameTag(baseObject, Indicator.TAG_FUTURE_STATUS_COMMENT);	
		writeProgressReportIds(indicator);
		writeExpenseAssignmentIds(indicator);
		writeResourceAssignmentIds(indicator);
		writeMeasurementIds(indicator.getMeasurementRefs());
		writeOptionalIds(WcsXmlConstants.METHOD_IDS, WcsXmlConstants.METHOD, indicator.getMethodRefs());
		writeOptionalThreshold(indicator);
	}
	
	private void writeOptionalThreshold(Indicator indicator) throws Exception
	{
		StringMap thresholdValues = indicator.getThreshold().getStringMap();
		StringMap thresholdDetails = indicator.getThresholdDetails();
		if (thresholdValues.size() == 0 && thresholdDetails.size() == 0)
			return;
		
		getWcsXmlExporter().writeStartElement(getWriter(), getWcsXmlExporter().createParentAndChildElementName(getPoolName(), THRESHOLDS));
		ChoiceQuestion question = getProject().getQuestion(StatusQuestion.class);
		CodeList allCodes = question.getAllCodes();
		for(int index = 0; index < allCodes.size(); ++index)
		{
			String code = allCodes.get(index);
			if (code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			
			getWcsXmlExporter().writeStartElement(getWriter(), THRESHOLD);
			getWcsXmlExporter().writeOptionalElement(getWriter(), STATUS_CODE, code);
			getWcsXmlExporter().writeOptionalElement(getWriter(), THRESHOLD_VALUE, thresholdValues.get(code));
			getWcsXmlExporter().writeOptionalElement(getWriter(), THRESHOLD_DETAILS, thresholdDetails.get(code));
			getWcsXmlExporter().writeEndElement(getWriter(), THRESHOLD);			
		}
		
		getWcsXmlExporter().writeEndElement(getWriter(), getWcsXmlExporter().createParentAndChildElementName(getPoolName(), THRESHOLDS));
	}

	private void writeMeasurementIds(ORefList measurementRefs) throws Exception
	{
		writeOptionalIds(WcsXmlConstants.MEASUREMENT_IDS, WcsXmlConstants.MEASUREMENT, measurementRefs);
	}
}
