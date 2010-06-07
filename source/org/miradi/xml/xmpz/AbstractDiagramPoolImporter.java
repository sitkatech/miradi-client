/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.w3c.dom.Node;

abstract public class AbstractDiagramPoolImporter extends AbstractBaseObjectImporter
{
	public AbstractDiagramPoolImporter(XmpzXmlImporter importerToUse, String poolNameToUse, int diagramTypeToImport)
	{
		super(importerToUse, poolNameToUse, diagramTypeToImport);
	}
	
	@Override
	public void importElement() throws Exception
	{
		importObject();
	}

	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		//FIXME this method is still under construction. Need to deal with duplicate ids before moving on
		importField(node, destinationRef, ConceptualModelDiagram.TAG_SHORT_LABEL);
		importField(node, destinationRef, ConceptualModelDiagram.TAG_DETAIL);
		importCodeListField(node, XmlSchemaCreator.HIDDEN_TYPES_ELEMENT_NAME, destinationRef, DiagramObject.TAG_HIDDEN_TYPES);
		importIds(node, destinationRef, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DiagramFactor.getObjectType());

//		DiagramObject conceptualModel = (DiagramObject) baseObject;
//		writeIds(DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DIAGRAM_FACTOR, conceptualModel.getAllDiagramFactorRefs());
//		writeIds(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DIAGRAM_LINK, conceptualModel.getAllDiagramLinkRefs());
//		writeIds(WcsXmlConstants.SELECTED_TAGGED_OBJECT_SET_IDS, "TaggedObjectSet", conceptualModel.getSelectedTaggedObjectSetRefs());
	}
}
