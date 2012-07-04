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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.BaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

abstract public class AbstractDiagramObjectExporter extends BaseObjectExporter
{
	public AbstractDiagramObjectExporter(Xmpz2XmlWriter writerToUse, final int objectTypeToUse)
	{
		super(writerToUse, objectTypeToUse);
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject, final BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		DiagramObject conceptualModel = (DiagramObject) baseObject;
		
		final String objectName = baseObjectSchema.getXmpz2ElementName();
		getWriter().writeNonOptionalReflist(objectName + DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DIAGRAM_FACTOR, conceptualModel.getAllDiagramFactorRefs());
		getWriter().writeNonOptionalReflist(objectName + DIAGRAM_LINK_IDS, DIAGRAM_LINK, conceptualModel.getAllDiagramLinkRefs());
		getWriter().writeNonOptionalReflist(objectName + SELECTED_TAGGED_OBJECT_SET_IDS, TAGGED_OBJECT_SET_ELEMENT_NAME, conceptualModel.getSelectedTaggedObjectSetRefs());
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(ConceptualModelDiagram.TAG_DIAGRAM_FACTOR_IDS))
			return true;
		
		if (tag.equals(ConceptualModelDiagram.TAG_DIAGRAM_FACTOR_LINK_IDS))
			return true;
		
		if (tag.equals(ConceptualModelDiagram.TAG_SELECTED_TAGGED_OBJECT_SET_REFS))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
}
