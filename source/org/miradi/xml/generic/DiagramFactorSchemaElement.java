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

import org.miradi.objects.DiagramFactor;
import org.miradi.xml.wcs.WcsXmlConstants;

public class DiagramFactorSchemaElement extends BaseObjectSchemaElement
{
	public DiagramFactorSchemaElement()
	{
		super(WcsXmlConstants.DIAGRAM_FACTOR);
		
		createWrappedByDiagramFactorIdField("WrappedFactorId");
		createDiagramPointField(DiagramFactor.TAG_LOCATION);
		createDiagramSizeField(DiagramFactor.TAG_SIZE);
		createIdListField("GroupBoxChildrenIds", "DiagramFactor");
		createCodeListField(XmlSchemaCreator.DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME);
		createCodeListField(XmlSchemaCreator.DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME);		
		createTextField(DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE);		
		createCodeListField(XmlSchemaCreator.DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME);
		createCodeListField(XmlSchemaCreator.DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME);
	}
}
