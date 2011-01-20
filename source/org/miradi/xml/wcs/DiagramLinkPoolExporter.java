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
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.utils.PointList;

public class DiagramLinkPoolExporter extends BaseObjectPoolExporter
{
	public DiagramLinkPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, DIAGRAM_LINK, DiagramLink.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		DiagramLink diagramLink = (DiagramLink) baseObject;
		writeFromDiagramFactorId(diagramLink);
		writeToDiagramFactorId(diagramLink);
		writeDiagramLinkBendPoints(diagramLink);
		
		writeIds(GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID, XmpzXmlConstants.DIAGRAM_LINK, diagramLink.getGroupedDiagramLinkRefs());
		writeCodeElement(DiagramLink.TAG_COLOR, new DiagramLinkColorQuestion(), diagramLink.getColorChoiceItem().getCode());
		writeBidirectionalCode(diagramLink);
	}

	private void writeBidirectionalCode(DiagramLink diagramLink) throws Exception
	{
		String NON_BIDIRECTIONAL_LINK = "0";
		String isBidirectional = NON_BIDIRECTIONAL_LINK; 
		if (diagramLink.isBidirectional())
			isBidirectional = DiagramLink.BIDIRECTIONAL_LINK;
		
		getWcsXmlExporter().writeOptionalElement(getWriter(), getPoolName() + DiagramLink.TAG_IS_BIDIRECTIONAL_LINK, isBidirectional);
	}
	
	private void writeDiagramLinkBendPoints(DiagramLink diagramLink) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_LINK + BEND_POINTS_ELEMENT_NAME);
		PointList bendPoints = diagramLink.getBendPoints();
		for (int index = 0; index < bendPoints.size(); ++index)
		{
			writeDiagramPoint(bendPoints.get(index));
		}
		
		getWcsXmlExporter().writeEndElement(DIAGRAM_LINK + BEND_POINTS_ELEMENT_NAME);
	}

	private void writeFromDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
		getWcsXmlExporter().writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor fromFactor = diagramLink.getFromDiagramFactor().getWrappedFactor();
		String fromFactorTypeName = getFactorTypeName(fromFactor);
		getWcsXmlExporter().writeElement(fromFactorTypeName, ID_ELEMENT_NAME, diagramLink.getFromDiagramFactorId().toString());
		
		getWcsXmlExporter().writeEndElement(LINKABLE_FACTOR_ID);
		getWcsXmlExporter().writeEndElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
	}
	
	private void writeToDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
		getWcsXmlExporter().writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor toFactor = diagramLink.getToDiagramFactor().getWrappedFactor();
		String toFactorTypeName = getFactorTypeName(toFactor);
		getWcsXmlExporter().writeElement(toFactorTypeName, ID_ELEMENT_NAME, diagramLink.getToDiagramFactorId().toString());
		
		getWcsXmlExporter().writeEndElement(LINKABLE_FACTOR_ID);
		getWcsXmlExporter().writeEndElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
	}
}
