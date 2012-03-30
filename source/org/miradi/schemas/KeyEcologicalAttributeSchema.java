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

package org.miradi.schemas;

import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;

public class KeyEcologicalAttributeSchema extends BaseObjectSchema
{
	public KeyEcologicalAttributeSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaIdList(KeyEcologicalAttribute.TAG_INDICATOR_IDS, IndicatorSchema.getObjectType());
		createFieldSchemaMultiLineUserText(KeyEcologicalAttribute.TAG_DESCRIPTION);
		createFieldSchemaMultiLineUserText(KeyEcologicalAttribute.TAG_DETAILS);
		createFieldSchemaChoice(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, getQuestion(KeyEcologicalAttributeTypeQuestion.class));
		createFieldSchemaSingleLineUserText(KeyEcologicalAttribute.TAG_SHORT_LABEL);
		
		createPseudoFieldSchemaString(KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS);
	}
}
