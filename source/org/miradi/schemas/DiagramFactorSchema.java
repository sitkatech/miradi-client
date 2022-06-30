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

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.questions.*;

public class DiagramFactorSchema extends BaseObjectSchema
{
	public DiagramFactorSchema()
	{
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaDimension(DiagramFactor.TAG_SIZE);
		createFieldSchemaPoint(DiagramFactor.TAG_LOCATION);
		createFieldSchemaRequiredRef(DiagramFactor.TAG_WRAPPED_REF);
		createFieldSchemaChoice(DiagramFactor.TAG_FONT_SIZE, DiagramFactorFontSizeQuestion.class);
		createFieldSchemaChoice(DiagramFactor.TAG_FOREGROUND_COLOR, DiagramFactorFontColorQuestion.class);
		createFieldSchemaChoice(DiagramFactor.TAG_FONT_STYLE, DiagramFactorFontStyleQuestion.class);
		createFieldSchemaReflist(DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, DIAGRAM_FACTOR);
		createFieldSchemaChoice(DiagramFactor.TAG_BACKGROUND_COLOR, DiagramFactorBackgroundQuestion.class);
		// TODO: field deprecated and will be removed in later release......only here to support migrations
		createFieldSchemaRequiredChoice(DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.class);
		createFieldSchemaReflist(DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, TAGGED_OBJECT_SET_ELEMENT_NAME);
		createFieldSchemaInteger(DiagramFactor.TAG_Z_INDEX);
	}

	public static int getObjectType()
	{
		return ObjectType.DIAGRAM_FACTOR;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "DiagramFactor";
}
