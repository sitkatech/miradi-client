/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.generic;

import org.miradi.objects.DiagramLink;

public class DiagramLinkSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public DiagramLinkSchemaElement()
	{
		super(XmlConstants.DIAGRAM_LINK);

		createLinkableFactorIdField(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		createLinkableFactorIdField(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
		createDiagramPointListField(DiagramLink.TAG_BEND_POINTS);
		createIdListField(XmlConstants.GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID, XmlConstants.DIAGRAM_LINK);
		createCodeField(XmlSchemaCreator.DIAGRAM_LINK_COLOR_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_DIAGRAM_LINK_COLOR);
		createOptionalBooleanField(DiagramLink.TAG_IS_BIDIRECTIONAL_LINK);
	}
}
