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
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.xml.generic.XmlSchemaCreator;

public class DiagramObjectPoolExporter extends BaseObjectPoolExporter
{
	public DiagramObjectPoolExporter(WcsXmlExporter wcsXmlExporterToUse, String containerNameToUse, int objectTypeToUse)
	{
		super(wcsXmlExporterToUse, containerNameToUse, objectTypeToUse);
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		DiagramObject conceptualModel = (DiagramObject) baseObject;
		writeOptionalElementWithSameTag(conceptualModel, ConceptualModelDiagram.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(conceptualModel, ConceptualModelDiagram.TAG_DETAIL);
		writeIds(DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DIAGRAM_FACTOR, conceptualModel.getAllDiagramFactorRefs());
		writeIds(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DIAGRAM_LINK, conceptualModel.getAllDiagramLinkRefs());
		writeCodeListElement(XmlSchemaCreator.HIDDEN_TYPES_ELEMENT_NAME, conceptualModel, DiagramObject.TAG_HIDDEN_TYPES);
		writeIds(WcsXmlConstants.SELECTED_TAGGED_OBJECT_SET_IDS, "TaggedObjectSetId", conceptualModel.getSelectedTaggedObjectSetRefs());
	}
}
