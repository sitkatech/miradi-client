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

import org.miradi.objects.Desire;
import org.miradi.xml.wcs.WcsXmlConstants;

public class DesireSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public DesireSchemaElement(String objectTypeNameToUse)
	{
		super(objectTypeNameToUse);
		
		createOptionalTextField(Desire.TAG_SHORT_LABEL);
		createOptionalTextField(Desire.TAG_FULL_TEXT);
		createOptionalIdListField(WcsXmlConstants.RELEVANT_INDICATOR_IDS, WcsXmlConstants.INDICATOR);
		createOptionalIdListField(WcsXmlConstants.RELEVANT_STRATEGY_IDS, WcsXmlConstants.STRATEGY);
		createOptionalIdListField(WcsXmlConstants.RELEVANT_ACTIVITY_IDS, WcsXmlConstants.ACTIVITY);
		createOptionalIdListField(WcsXmlConstants.PROGRESS_PERCENT_IDS, XmlSchemaCreator.PROGRESS_PERCENT_ID_ELEMENT_NAME);
		createOptionalTextField(Desire.TAG_COMMENTS);
	}
}
