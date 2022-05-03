/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.schemas;

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
		
		createOwnedFieldSchemaIdList(DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DiagramFactorSchema.getObjectType());
		createOwnedFieldSchemaIdList(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DiagramLinkSchema.getObjectType());
		createFieldSchemaSingleLineUserText(DiagramObject.TAG_SHORT_LABEL);
		createFieldSchemaMultiLineUserText(DiagramObject.TAG_DETAIL);
		createFieldSchemaMultiLineUserText(DiagramObject.TAG_COMMENTS);
		createFieldSchemaCodeList(DiagramObject.TAG_HIDDEN_TYPES, getQuestion(DiagramLegendQuestion.class));
		createFieldSchemaBoolean(DiagramObject.TAG_IS_TAGGING_ENABLED);
		createFieldSchemaReflist(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, TAGGED_OBJECT_SET_ELEMENT_NAME);
		createFieldSchemaBoolean(DiagramObject.TAG_IS_PROGRESS_STATUS_DISPLAY_ENABLED);
		createFieldSchemaBoolean(DiagramObject.TAG_IS_RESULT_STATUS_DISPLAY_ENABLED);
		createFieldSchemaNumber(DiagramObject.TAG_ZOOM_SCALE);

		createExtendedProgressReportSchema();

		createPseudoFieldSchemaString(DiagramObject.PSEUDO_COMBINED_LABEL);
	}
}
