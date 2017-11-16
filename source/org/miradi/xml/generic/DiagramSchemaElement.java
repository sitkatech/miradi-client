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

import org.miradi.objects.DiagramObject;

abstract public class DiagramSchemaElement extends BaseObjectSchemaElementWithLabel
{
	public DiagramSchemaElement(String objectTypeNameToUse)
	{
		super(objectTypeNameToUse);

		createOptionalTextField(DiagramObject.TAG_SHORT_LABEL);
		createOptionalTextField(DiagramObject.TAG_DETAIL);
		createOptionalNumericField(DiagramObject.TAG_ZOOM_SCALE);
		createIdListField(DiagramObject.TAG_DIAGRAM_FACTOR_IDS, "DiagramFactor");
		createIdListField(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, "DiagramLink");
		createOptionalCodeListField(XmlSchemaCreator.HIDDEN_TYPES_ELEMENT_NAME);
		createOptionalBooleanField(DiagramObject.TAG_IS_TAGGING_ENABLED);
		createIdListField(XmlConstants.SELECTED_TAGGED_OBJECT_SET_IDS, XmlSchemaCreator.TAGGED_OBJECT_SET_ELEMENT_NAME);
	}
}
