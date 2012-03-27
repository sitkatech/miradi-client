/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.questions.DiagramLegendQuestion;

abstract public class DiagramObjectSchema extends BaseObjectSchema
{
	public DiagramObjectSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaIdList(DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DiagramFactor.getObjectType());
		createFieldSchemaIdList(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DiagramLink.getObjectType());
		createFieldSchemaSingleLineUserText(DiagramObject.TAG_SHORT_LABEL);
		createFieldSchemaMultiLineUserText(DiagramObject.TAG_DETAIL);
		createFieldSchemaCodeList(DiagramObject.TAG_HIDDEN_TYPES, getQuestion(DiagramLegendQuestion.class));
		createFieldSchemaReflist(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS);
		createFieldSchemaNumber(DiagramObject.TAG_ZOOM_SCALE);
		
		createPseudoStringField(DiagramObject.PSEUDO_COMBINED_LABEL);
	}
}
