/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.Indicator;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class IndicatorExporter extends BaseObjectExporter
{
	public IndicatorExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, IndicatorSchema.getObjectType());
	}

	@Override
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		final Indicator indicator = (Indicator) baseObject;
		writeMethodRefs(baseObjectSchema, indicator);
		writeThreshold(indicator);

		final String objectName = baseObjectSchema.getObjectName();
		writeRelevantStrategyIds(objectName, indicator);
		writeRelevantActivityIds(objectName, indicator);
	}

	private void writeMethodRefs(BaseObjectSchema baseObjectSchema, final Indicator indicator) throws Exception
	{
		getWriter().writeReflist(baseObjectSchema.getObjectName() + METHOD_IDS, METHOD, indicator.getMethodRefs());
	}

	@Override
	protected boolean doesFieldRequireSpecialHandling(final String tag)
	{
		if (tag.equals(Indicator.TAG_THRESHOLDS_MAP))
			return true;
		
		if (tag.equals(Indicator.TAG_THRESHOLD_DETAILS_MAP))
			return true;
		
		if (tag.equals(Indicator.TAG_METHOD_IDS))
			return true;

		if (tag.equals(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	private void writeThreshold(Indicator indicator) throws Exception
	{
		CodeToUserStringMap thresholdValues = indicator.getThresholdsMap().getCodeToUserStringMap();
		CodeToUserStringMap thresholdDetails = indicator.getThresholdDetailsMap();
		if (thresholdValues.size() == 0 && thresholdDetails.size() == 0)
			return;
		
		final String elementName = getWriter().appendChildNameToParentName(INDICATOR, THRESHOLDS);
		getWriter().writeStartElement(elementName);
		ChoiceQuestion question = StaticQuestionManager.getQuestion(StatusQuestion.class);
		CodeList allCodes = question.getAllCodes();
		for(int index = 0; index < allCodes.size(); ++index)
		{
			String code = allCodes.get(index);
			if (code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			
			getWriter().writeStartElement(THRESHOLD);
			getWriter().writeElement(STATUS_CODE, code);
			getWriter().writeElement(THRESHOLD_VALUE, thresholdValues.getUserString(code));
			getWriter().writeElement(THRESHOLD_DETAILS, thresholdDetails.getUserString(code));
			getWriter().writeEndElement(THRESHOLD);			
		}
		
		getWriter().writeEndElement(elementName);
	}

	private void writeRelevantStrategyIds(final String objectName, Indicator indicator) throws Exception
	{
		ORefList relevantStrategyRefs = indicator.getRelevantStrategyAndActivityRefs().getFilteredBy(StrategySchema.getObjectType());
		getWriter().writeReflist(objectName, RELEVANT_STRATEGY_IDS, StrategySchema.OBJECT_NAME, relevantStrategyRefs);
	}

	private void writeRelevantActivityIds(final String objectName, Indicator indicator) throws Exception
	{
		ORefList relevantActivityRefs = indicator.getRelevantStrategyAndActivityRefs().getFilteredBy(TaskSchema.getObjectType());
		getWriter().writeReflist(objectName, RELEVANT_ACTIVITY_IDS, ACTIVITY, relevantActivityRefs);
	}
}
