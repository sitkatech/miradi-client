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

import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.xml.wcs.WcsXmlConstants;

public class DiagramLinkSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public DiagramLinkSchemaElement()
	{
		super(WcsXmlConstants.DIAGRAM_LINK);

		createLinkableFactorIdField(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		createLinkableFactorIdField(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
		createDiagramPointListField(DiagramLink.TAG_BEND_POINTS);
		createIdListField(WcsXmlConstants.GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID, WcsXmlConstants.DIAGRAM_LINK);
		createCodeField(XmlSchemaCreator.DIAGRAM_LINK_COLOR_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_DIAGRAM_LINK_COLOR);
		createOptionalBooleanField(FactorLink.TAG_BIDIRECTIONAL_LINK);
	}
}
