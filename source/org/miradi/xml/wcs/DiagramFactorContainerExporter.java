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

import java.awt.Point;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;

public class DiagramFactorContainerExporter extends BaseObjectContainerExporter
{
	public DiagramFactorContainerExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, DIAGRAM_FACTOR, DiagramFactor.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		DiagramFactor diagramFactor = (DiagramFactor) baseObject;
		writeWrappedFactorId(diagramFactor);
		writeDiagramFactorLocation(diagramFactor);
		writeDiagramFactorSize(diagramFactor);
		getWcsXmlExporter().writeIds(DIAGRAM_FACTOR, GROUP_BOX_CHILDREN_IDS, WcsXmlConstants.DIAGRAM_FACTOR_ID_ELEMENT_NAME, diagramFactor.getGroupBoxChildrenRefs());
		exportTextBoxZOrder(diagramFactor);
		exportFontStylingElements(diagramFactor);
	}

	private void exportTextBoxZOrder(DiagramFactor diagramFactor) throws Exception
	{
		String zOrderCode = diagramFactor.getData(DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE);
		if (diagramFactor.isDefaultZOrder())
			zOrderCode = Z_ORDER_BACK_CODE;
		
		getWcsXmlExporter().writeOptionalElement(getWriter(), DIAGRAM_FACTOR + DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, zOrderCode);
	}
	
	private void exportFontStylingElements(DiagramFactor diagramFactor) throws Exception
	{
		final String STYLING_ELEMENT_NAME = DIAGRAM_FACTOR + STYLING;
		getWcsXmlExporter().writeStartElement(STYLING_ELEMENT_NAME);
		
		getWcsXmlExporter().writeStartElement(STYLING);
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_FONT_SIZE);
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_FONT_STYLE);
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR);
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_BACKGROUND_COLOR);
		getWcsXmlExporter().writeEndElement(STYLING);
		
		getWcsXmlExporter().writeEndElement(STYLING_ELEMENT_NAME);
		
	}

	private void writeWrappedFactorId(DiagramFactor diagramFactor) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_FACTOR + WRAPPED_FACTOR_ID_ELEMENT_NAME);
		getWcsXmlExporter().writeStartElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		Factor wrappedFactor = diagramFactor.getWrappedFactor();
		String factorTypeName = getWcsXmlExporter().getFactorTypeName(wrappedFactor);
		
		getWcsXmlExporter().writeElement(factorTypeName, ID_ELEMENT_NAME, diagramFactor.getWrappedId().toString());
		
		getWcsXmlExporter().writeEndElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		getWcsXmlExporter().writeEndElement(DIAGRAM_FACTOR + WRAPPED_FACTOR_ID_ELEMENT_NAME);
	}
	
	private void writeDiagramFactorLocation(DiagramFactor diagramFactor) throws Exception
	{
		String locationElementName = getWcsXmlExporter().createParentAndChildElementName(DIAGRAM_FACTOR, "Location");
		getWcsXmlExporter().writeStartElement(locationElementName);
		Point location = diagramFactor.getLocation();
		getWcsXmlExporter().writeDiagramPoint(location);
		getWcsXmlExporter().writeEndElement(locationElementName);
	}
	
	private void writeDiagramFactorSize(DiagramFactor diagramFactor) throws Exception
	{
		String sizeElementName = getWcsXmlExporter().createParentAndChildElementName(DIAGRAM_FACTOR, "Size");
		getWcsXmlExporter().writeStartElement(sizeElementName);
		getWcsXmlExporter().writeStartElement(DIAGRAM_SIZE_ELEMENT_NAME);
		getWcsXmlExporter().writeElement(getWriter(), WIDTH_ELEMENT_NAME, diagramFactor.getSize().width);		
		getWcsXmlExporter().writeElement(getWriter(), HEIGHT_ELEMENT_NAME, diagramFactor.getSize().height);
		getWcsXmlExporter().writeEndElement(DIAGRAM_SIZE_ELEMENT_NAME);
		getWcsXmlExporter().writeEndElement(sizeElementName);
	}
}
