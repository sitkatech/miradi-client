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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.GroupBox;
import org.miradi.xml.generic.XmlSchemaCreator;

public class GroupBoxContainerExporter extends FactorContainerExporter
{
	public GroupBoxContainerExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, GROUP_BOX, GroupBox.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
				
		GroupBox groupBox = (GroupBox) baseObject;
		writeCodeElementSameAsTag(groupBox, XmlSchemaCreator.DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME);
		writeCodeElementSameAsTag(groupBox, XmlSchemaCreator.DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME);
		writeCodeElementSameAsTag(groupBox, XmlSchemaCreator.DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME);
		writeCodeElementSameAsTag(groupBox, XmlSchemaCreator.DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME);
	}
}
