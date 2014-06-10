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
import org.miradi.objects.Factor;


abstract public class TargetObjectSchemaElement extends FactorObjectSchemaElement
{
	public TargetObjectSchemaElement(String objectTypeNameToUse)
	{
		super(objectTypeNameToUse);
		
		createCodeField(XmlSchemaCreator.TARGET_STATUS_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_TARGET_STATUS);
		createCodeField(XmlSchemaCreator.TARGET_VIABILITY_MODE_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_TARGET_VIABILITY_MODE);
		createOptionalTextField(AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION);
		createOptionalIdListField(SUB_TARGET_IDS_ELEMENT, XmlSchemaCreator.SUB_TARGET_ID_ELEMENT_NAME);
		createOptionalIdListField(AbstractTarget.TAG_GOAL_IDS, XmlSchemaCreator.GOAL_ELEMENT_NAME);
		createOptionalIdListField(AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, XmlSchemaCreator.KEA_ID_ELEMENT_NAME);
		createOptionalIdListField(Factor.TAG_INDICATOR_IDS, INDICATOR_TYPE_NAME);
		createOptionalCodeField(TARGET_THREAT_RATING, XmlSchemaCreator.VOCABULARY_THREAT_RATING);
	}
}
