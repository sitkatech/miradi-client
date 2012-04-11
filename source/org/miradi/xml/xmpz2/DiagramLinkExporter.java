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

package org.miradi.xml.xmpz2;

import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.schemas.BaseObjectSchema;

public class DiagramLinkExporter extends BaseObjectExporter
{
	public DiagramLinkExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		DiagramLink diagramLink = (DiagramLink) baseObject;
		writeFromDiagramFactorId(diagramLink);
		writeToDiagramFactorId(diagramLink);
		writeDiagramLinkBendPoints(diagramLink);
		
		final String objectName = diagramLink.getSchema().getObjectName();
		getWriter().writeNonOptionalReflist(objectName + GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID, DIAGRAM_LINK, diagramLink.getGroupedDiagramLinkRefs());
		writeBidirectionalCode(objectName, diagramLink);
	}

	private void writeBidirectionalCode(String poolName, DiagramLink diagramLink) throws Exception
	{
		String NON_BIDIRECTIONAL_LINK = "0";
		String isBidirectional = NON_BIDIRECTIONAL_LINK; 
		if (diagramLink.isBidirectional())
			isBidirectional = DiagramLink.BIDIRECTIONAL_LINK;
		
		getWriter().writeElement(poolName + DiagramLink.TAG_IS_BIDIRECTIONAL_LINK, isBidirectional);
	}
	
	private void writeDiagramLinkBendPoints(DiagramLink diagramLink) throws Exception
	{
		getWriter().writePointList(DIAGRAM_LINK, diagramLink.getBendPoints());
	}

	private void writeFromDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		getWriter().writeStartElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
		getWriter().writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor fromFactor = diagramLink.getFromDiagramFactor().getWrappedFactor();
		String fromFactorTypeName = getFactorTypeName(fromFactor);
		getWriter().writeElement(fromFactorTypeName, ID_ELEMENT_NAME, diagramLink.getFromDiagramFactorId().toString());
		
		getWriter().writeEndElement(LINKABLE_FACTOR_ID);
		getWriter().writeEndElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
	}
	
	private void writeToDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		getWriter().writeStartElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
		getWriter().writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor toFactor = diagramLink.getToDiagramFactor().getWrappedFactor();
		String toFactorTypeName = getFactorTypeName(toFactor);
		getWriter().writeElement(toFactorTypeName, ID_ELEMENT_NAME, diagramLink.getToDiagramFactorId().toString());
		
		getWriter().writeEndElement(LINKABLE_FACTOR_ID);
		getWriter().writeEndElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(final String tag)
	{
		if (tag.equals(DiagramLink.TAG_WRAPPED_ID))
			return true;
		
		if (tag.equals(DiagramLink.TAG_IS_BIDIRECTIONAL_LINK))
			return true;
		
		if (tag.equals(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID))
			return true;
		
		if (tag.equals(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID))
			return true;
		
		if (tag.equals(DiagramLink.TAG_BEND_POINTS))
			return true;
		
		if (tag.equals(DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}
}
