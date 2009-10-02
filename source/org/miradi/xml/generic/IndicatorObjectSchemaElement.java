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

package org.miradi.xml.generic;

import org.miradi.objects.Indicator;

public class IndicatorObjectSchemaElement extends BaseObjectSchemaElement
{
	public IndicatorObjectSchemaElement()
	{
		super("Indicator");
		
		createTextField(Indicator.TAG_SHORT_LABEL);
		createTextField(Indicator.TAG_DETAIL);
		createTextField(Indicator.TAG_COMMENTS);
		createCodeField(Indicator.TAG_PRIORITY, XmlSchemaCreator.VOCABULARY_PRIORITY_RATING_CODE);
		createDateField(Indicator.TAG_FUTURE_STATUS_DATE);
		createTextField(Indicator.TAG_FUTURE_STATUS_SUMMARY);
		createCodeField(Indicator.TAG_FUTURE_STATUS_RATING, XmlSchemaCreator.VOCABULARY_STATUS_CODE);
		createTextField(Indicator.TAG_FUTURE_STATUS_DETAIL);
		createTextField(Indicator.TAG_FUTURE_STATUS_COMMENT);	
		createIdListField(Indicator.TAG_PROGRESS_REPORT_REFS, XmlSchemaCreator.PROGRESS_REPORT_ID_ELEMENT_NAME);
		createIdListField(Indicator.TAG_EXPENSE_ASSIGNMENT_REFS, XmlSchemaCreator.EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME);
		createIdListField(Indicator.TAG_RESOURCE_ASSIGNMENT_IDS, XmlSchemaCreator.RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME);
	}
}
