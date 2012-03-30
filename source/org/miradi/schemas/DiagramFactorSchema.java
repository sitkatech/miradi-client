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

import org.miradi.objects.DiagramFactor;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.xml.wcs.XmpzXmlConstants;

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
		createFieldSchemaRef(DiagramFactor.TAG_WRAPPED_REF);
		createFieldSchemaChoice(DiagramFactor.TAG_FONT_SIZE, getQuestion(DiagramFactorFontSizeQuestion.class));
		createFieldSchemaChoice(DiagramFactor.TAG_FOREGROUND_COLOR, getQuestion(DiagramFactorFontColorQuestion.class));
		createFieldSchemaChoice(DiagramFactor.TAG_FONT_STYLE, getQuestion(DiagramFactorFontStyleQuestion.class));
		createFieldSchemaReflist(DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, XmpzXmlConstants.DIAGRAM_FACTOR + XmpzXmlConstants.ID);
		createFieldSchemaChoice(DiagramFactor.TAG_BACKGROUND_COLOR, getQuestion(DiagramFactorBackgroundQuestion.class));
		createFieldSchemaChoice(DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, getQuestion(TextBoxZOrderQuestion.class));
	}
}
