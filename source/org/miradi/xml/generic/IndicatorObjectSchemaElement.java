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

package org.miradi.xml.generic;

import org.miradi.migrations.IndicatorFutureStatusTagsToFutureStatusTagsMap;
import org.miradi.objects.Indicator;
import org.miradi.xml.wcs.Xmpz1XmlConstants;

public class IndicatorObjectSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public IndicatorObjectSchemaElement()
	{
		super(Xmpz1XmlConstants.INDICATOR);
		
		createOptionalTextField(Indicator.TAG_SHORT_LABEL);
		createOptionalTextField(Indicator.TAG_DETAIL);
		createOptionalTextField(Indicator.TAG_COMMENTS);
		createCodeField(Indicator.TAG_PRIORITY, XmlSchemaCreator.VOCABULARY_PRIORITY_RATING_CODE);
		createOptionalDateField(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DATE);
		createOptionalTextField(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_SUMMARY);
		createCodeField(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_RATING, XmlSchemaCreator.VOCABULARY_MEASUREMENT_STATUS);
		createOptionalTextField(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DETAIL);
		createOptionalTextField(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_COMMENTS);	
		createOptionalIdListField(Xmpz1XmlConstants.PROGRESS_REPORT_IDS, XmlSchemaCreator.PROGRESS_REPORT_ID_ELEMENT_NAME);
		createOptionalIdListField(Xmpz1XmlConstants.EXPENSE_IDS, XmlSchemaCreator.EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME);
		createOptionalIdListField(Indicator.TAG_RESOURCE_ASSIGNMENT_IDS, XmlSchemaCreator.RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME);
		createOptionalIdListField(Xmpz1XmlConstants.MEASUREMENT_IDS, XmlSchemaCreator.MEASUREMENT_ID_ELEMENT_NAME);
		createOptionalIdListField(Xmpz1XmlConstants.METHOD_IDS, XmlSchemaCreator.METHOD);
		createOptionalThresholdsField();
		createOptionalCodeField(Indicator.TAG_RATING_SOURCE, XmlSchemaCreator.VOCABULARY_RATING_SOURCE);
		createOptionalTextField(Indicator.TAG_VIABILITY_RATINGS_COMMENTS);
		createOptionalCalculatedTimePeriodCosts();
	}
}
