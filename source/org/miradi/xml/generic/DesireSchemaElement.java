/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.objects.Desire;

public class DesireSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public DesireSchemaElement(String objectTypeNameToUse)
	{
		super(objectTypeNameToUse);
		
		createOptionalTextField(Desire.TAG_SHORT_LABEL);
		createOptionalTextField(Desire.TAG_FULL_TEXT);
		createOptionalIdListField(XmlConstants.RELEVANT_INDICATOR_IDS, XmlConstants.INDICATOR);
		createOptionalIdListField(XmlConstants.RELEVANT_STRATEGY_IDS, XmlConstants.STRATEGY);
		createOptionalIdListField(XmlConstants.RELEVANT_ACTIVITY_IDS, XmlConstants.ACTIVITY);
		createOptionalIdListField(XmlConstants.PROGRESS_PERCENT_IDS, XmlSchemaCreator.PROGRESS_PERCENT_ID_ELEMENT_NAME);
		createOptionalTextField(Desire.TAG_COMMENTS);
	}
}
