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
import org.miradi.objects.FactorLink;
import org.miradi.utils.PointList;
import org.miradi.xml.wcs.BaseObjectPoolExporter;;

public class DiagramLinkPoolExporter extends BaseObjectPoolExporter
{
	public DiagramLinkPoolExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, DIAGRAM_LINK, DiagramLink.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		DiagramLink diagramLink = (DiagramLink) baseObject;
		writeWrappedFactorLinkId(diagramLink);
		writeFromDiagramFactorId(diagramLink);
		writeToDiagramFactorId(diagramLink);
		writeDiagramLinkBendPoints(diagramLink);
		
		writeIds(GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID, WcsXmlConstants.DIAGRAM_LINK_ID_ELEMENT_NAME, diagramLink.getGroupedDiagramLinkRefs());
		writeCodeElement(DiagramLink.TAG_COLOR, diagramLink.getColorChoiceItem().getCode());
		writeBidirectionalCode(diagramLink);
	}

	private void writeBidirectionalCode(DiagramLink diagramLink) throws Exception
	{
		FactorLink wrappedFactorLink = diagramLink.getWrappedFactorLink();
		String isBidirectional = wrappedFactorLink.getData(FactorLink.TAG_BIDIRECTIONAL_LINK);
		writeOptionalCodeElement(FactorLink.TAG_BIDIRECTIONAL_LINK, isBidirectional);
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
		getWcsXmlExporter().writeElement(fromFactorTypeName, ID_ELEMENT_NAME, diagramLink.getWrappedId().toString());
		
		getWcsXmlExporter().writeEndElement(LINKABLE_FACTOR_ID);
		getWcsXmlExporter().writeEndElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
	}
	
	private void writeToDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
		getWcsXmlExporter().writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor toFactor = diagramLink.getToDiagramFactor().getWrappedFactor();
		String toFactorTypeName = getFactorTypeName(toFactor);
		getWcsXmlExporter().writeElement(toFactorTypeName, ID_ELEMENT_NAME, diagramLink.getWrappedId().toString());
		
		getWcsXmlExporter().writeEndElement(LINKABLE_FACTOR_ID);
		getWcsXmlExporter().writeEndElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
	}

	private void writeWrappedFactorLinkId(DiagramLink diagramLink) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_LINK + WRAPPED_FACTOR_LINK_ID_ELEMENT_NAME);
		getWcsXmlExporter().writeStartElement(WRAPPED_BY_DIAGRAM_LINK_ID_ELEMENT_NAME);
		
		getWcsXmlExporter().writeElement("FactorLink", ID_ELEMENT_NAME, diagramLink.getWrappedId().toString());
		
		getWcsXmlExporter().writeEndElement(WRAPPED_BY_DIAGRAM_LINK_ID_ELEMENT_NAME);
		getWcsXmlExporter().writeEndElement(DIAGRAM_LINK + WRAPPED_FACTOR_LINK_ID_ELEMENT_NAME);
	}
}
