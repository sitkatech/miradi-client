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

public class DiagramFactorSchemaElement extends BaseObjectSchemaElement
{
	public DiagramFactorSchemaElement()
	{
		super("DiagramFactor");
		createWrappedByDiagramFactorIdField(DiagramFactor.TAG_WRAPPED_REF);
		createDiagramPointField(DiagramFactor.TAG_LOCATION);
		createDiagramSizeField(DiagramFactor.TAG_SIZE);
		createIdListField(DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, "DiagramFactor");
//		TAG_FONT_SIZE
//		TAG_FONT_STYLE
//		TAG_TEXT_BOX_Z_ORDER_CODE
//		TAG_FOREGROUND_COLOR
//		TAG_BACKGROUND_COLOR
	}

}
