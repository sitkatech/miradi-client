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

import org.miradi.objects.AbstractTarget;


abstract public class TargetObjectSchemaElement extends FactorObjectSchemaElement
{
	public TargetObjectSchemaElement(String objectTypeNameToUse)
	{
		super(objectTypeNameToUse);
		
		createCodeListField(XmlSchemaCreator.TARGET_STATUS_ELEMENT_NAME);
		createCodeListField(XmlSchemaCreator.TARGET_VIABILITY_MODE_ELEMENT_NAME);
		createTextField(AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION);
		createIdListField("SubTargetIds", XmlSchemaCreator.SUB_TARGET_ID_ELEMENT_NAME);
		createIdListField(AbstractTarget.TAG_GOAL_IDS, XmlSchemaCreator.GOAL_ELEMENT_NAME);
		createIdListField(AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, XmlSchemaCreator.KEA_ID_ELEMENT_NAME);
	}
}
