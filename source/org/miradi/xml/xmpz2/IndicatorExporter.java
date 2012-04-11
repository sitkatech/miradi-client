/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2;

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.CodeList;

public class IndicatorExporter extends BaseObjectExporter
{
	public IndicatorExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}

	@Override
	protected void writeFields(final BaseObject baseObject,	BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		final Indicator indicator = (Indicator) baseObject;
		writeMethodRefs(baseObjectSchema, indicator);
		writeThreshold(indicator);
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
		ChoiceQuestion question = getWriter().getProject().getQuestion(StatusQuestion.class);
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
}
