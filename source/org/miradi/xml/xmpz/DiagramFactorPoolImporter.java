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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

//FIXME this class is still under construction
public class DiagramFactorPoolImporter extends AbstractBaseObjectImporter
{
	public DiagramFactorPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.DIAGRAM_FACTOR, DiagramFactor.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);

	}
	
	@Override
	protected CreateObjectParameter getExtraInfo(Node parentNode) throws Exception
	{
		ORef wrappedRef = importWrappedRef(parentNode);
		
		return new CreateDiagramFactorParameter(wrappedRef);
	}
	
	private ORef importWrappedRef(Node parentNode) throws Exception
	{
		Node node = getImporter().getNode(parentNode, getPoolName() + WcsXmlConstants.WRAPPED_FACTOR_ID_ELEMENT_NAME);
		Node node2 = getImporter().getNode(node, WcsXmlConstants.WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		Node typedIdNode = node2.getFirstChild();
		
		BaseId wrappedId = new BaseId(typedIdNode.getTextContent());
		
		return new ORef(getNodeType(typedIdNode), wrappedId);
	}

	private int getNodeType(Node typedIdNode)
	{
		String nodeName = typedIdNode.getNodeName();
		String objectTypeName = nodeName.replaceFirst("Id", "");
		
		if (objectTypeName.equals(WcsXmlConstants.SCOPE_BOX))
			return ScopeBox.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.BIODIVERSITY_TARGET))
			return Target.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.HUMAN_WELFARE_TARGET))
			return HumanWelfareTarget.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.CAUSE) || objectTypeName.equals(WcsXmlConstants.THREAT))
			return Cause.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.STRATEGY))
			return Strategy.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.INTERMEDIATE_RESULTS))
			return IntermediateResult.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.THREAT_REDUCTION_RESULTS))
			return ThreatReductionResult.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.TEXT_BOX))
			return TextBox.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.GROUP_BOX))
			return GroupBox.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.TASK))
			return Task.getObjectType();
		
		if (objectTypeName.equals(WcsXmlConstants.STRESS))
			return Stress.getObjectType();
		
		EAM.logError("Could not find type for node: " + objectTypeName);
		return ObjectType.FAKE;
	}
	
	/*
	 * 
	 * 	@Override
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
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		
		Factor wrappedFactor = diagramFactor.getWrappedFactor();
		if (wrappedFactor.isGroupBox() || wrappedFactor.isTextBox())
			writeOptionalCodeElementSameAsTag(diagramFactor, DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion());
		
		getWcsXmlExporter().writeEndElement(STYLING);
		
		getWcsXmlExporter().writeEndElement(STYLING_ELEMENT_NAME);
	}

	private void writeWrappedFactorId(DiagramFactor diagramFactor) throws Exception
	{
		getWcsXmlExporter().writeStartElement(DIAGRAM_FACTOR + WRAPPED_FACTOR_ID_ELEMENT_NAME);
		getWcsXmlExporter().writeStartElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		Factor wrappedFactor = diagramFactor.getWrappedFactor();
		String factorTypeName = getFactorTypeName(wrappedFactor);
		
		getWcsXmlExporter().writeElement(factorTypeName, ID_ELEMENT_NAME, diagramFactor.getWrappedId().toString());
		
		getWcsXmlExporter().writeEndElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		getWcsXmlExporter().writeEndElement(DIAGRAM_FACTOR + WRAPPED_FACTOR_ID_ELEMENT_NAME);
	}
	
	private void writeDiagramFactorLocation(DiagramFactor diagramFactor) throws Exception
	{
		String locationElementName = getWcsXmlExporter().createParentAndChildElementName(DIAGRAM_FACTOR, "Location");
		getWcsXmlExporter().writeStartElement(locationElementName);
		Point location = diagramFactor.getLocation();
		writeDiagramPoint(location);
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

	 */
}
