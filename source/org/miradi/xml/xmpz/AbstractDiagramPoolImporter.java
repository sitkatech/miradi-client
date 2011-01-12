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
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

abstract public class AbstractDiagramPoolImporter extends AbstractBaseObjectPoolImporter
{
	public AbstractDiagramPoolImporter(XmpzXmlImporter importerToUse, String poolNameToUse, int diagramTypeToImport)
	{
		super(importerToUse, poolNameToUse, diagramTypeToImport);
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importField(node, destinationRef, ConceptualModelDiagram.TAG_SHORT_LABEL);
		importField(node, destinationRef, ConceptualModelDiagram.TAG_DETAIL);
		importField(node, destinationRef, ConceptualModelDiagram.TAG_ZOOM_SCALE);
		importCodeListField(node, destinationRef, DiagramObject.TAG_HIDDEN_TYPES);
		importIds(node, destinationRef, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DiagramFactor.getObjectType(), XmpzXmlConstants.DIAGRAM_FACTOR);
		importIds(node, destinationRef, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DiagramLink.getObjectType(), XmpzXmlConstants.DIAGRAM_LINK);		
		importRefs(node, XmpzXmlConstants.SELECTED_TAGGED_OBJECT_SET_IDS, destinationRef, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, TaggedObjectSet.getObjectType(), XmpzXmlConstants.TAGGED_OBJECT_SET_ELEMENT_NAME);
	}
}
