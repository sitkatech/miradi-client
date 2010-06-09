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

import java.awt.Dimension;
import java.awt.Point;

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
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

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
		
		importDiagramFactorLocation(node, destinationRef);
		importDiagramFactorSize(node, destinationRef);
		importRefs(node, WcsXmlConstants.GROUP_BOX_CHILDREN_IDS, destinationRef, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, DiagramFactor.getObjectType(), WcsXmlConstants.DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		importCodeField(node, getPoolName(), destinationRef, DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, new TextBoxZOrderQuestion());
		importFontStylingElements(node, destinationRef);
	}
	
	private void importFontStylingElements(Node node, ORef destinationRef) throws Exception
	{
		Node diagramFactorSyleNode = getImporter().getNode(node, getPoolName() + WcsXmlConstants.STYLING);
		Node style = getImporter().getNode(diagramFactorSyleNode, WcsXmlConstants.STYLING);
		importCodeField(style, WcsXmlConstants.DIAGRAM_FACTOR, destinationRef, DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		importCodeField(style, WcsXmlConstants.DIAGRAM_FACTOR, destinationRef, DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		importCodeField(style, WcsXmlConstants.DIAGRAM_FACTOR, destinationRef, DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		importCodeField(style, WcsXmlConstants.DIAGRAM_FACTOR, destinationRef, DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion());
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

	private void importDiagramFactorSize(Node node, ORef destinationRef) throws Exception
	{
		Node diagramFactorSizeNode = getImporter().getNode(node, getPoolName()+ WcsXmlConstants.SIZE);
		Node sizNode = getImporter().getNode(diagramFactorSizeNode, WcsXmlConstants.DIAGRAM_SIZE_ELEMENT_NAME);
		Node widthNode = getImporter().getNode(sizNode, WcsXmlConstants.WIDTH_ELEMENT_NAME);
		Node heightNode = getImporter().getNode(sizNode, WcsXmlConstants.HEIGHT_ELEMENT_NAME);
		int width = extractNodeTextContentAsInt(widthNode);
		int height = extractNodeTextContentAsInt(heightNode);
		String dimensionAsString = EnhancedJsonObject.convertFromDimension(new Dimension(width, height));
		getImporter().setData(destinationRef, DiagramFactor.TAG_SIZE, dimensionAsString);
	}
	
	private void importDiagramFactorLocation(Node node, ORef destinationRef) throws Exception
	{
		Node locationNode = getImporter().getNode(node, getPoolName()+ WcsXmlConstants.LOCATION);
		Node pointNode = getImporter().getNode(locationNode, WcsXmlConstants.DIAGRAM_POINT_ELEMENT_NAME);
		Node xNode = getImporter().getNode(pointNode, WcsXmlConstants.X_ELEMENT_NAME);
		Node yNode = getImporter().getNode(pointNode, WcsXmlConstants.Y_ELEMENT_NAME);
		int x = extractNodeTextContentAsInt(xNode);
		int y = extractNodeTextContentAsInt(yNode);
		String pointAsString = EnhancedJsonObject.convertFromPoint(new Point(x, y));
		getImporter().setData(destinationRef, DiagramFactor.TAG_LOCATION, pointAsString);
	}

	private int extractNodeTextContentAsInt(Node node)
	{
		try
		{
			return Integer.parseInt(node.getTextContent());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return 0;
		}
	}
}
