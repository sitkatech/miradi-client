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
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class DiagramLinkSchema extends BaseObjectSchema
{
	public DiagramLinkSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaBaseId(DiagramLink.TAG_WRAPPED_ID, FactorLink.getObjectType());
		createFieldSchemaBaseId(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID, DiagramFactor.getObjectType());
		createFieldSchemaBaseId(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID, DiagramFactor.getObjectType());
		createFieldSchemaPointList(DiagramLink.TAG_BEND_POINTS);
		createFieldSchemaReflist(DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, XmpzXmlConstants.DIAGRAM_LINK);
		createFieldSchemaChoice(DiagramLink.TAG_COLOR, getQuestion(DiagramLinkColorQuestion.class));
		createFieldSchemaBoolean(DiagramLink.TAG_IS_BIDIRECTIONAL_LINK);
	}
}
