/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz1;

import java.awt.Dimension;
import java.awt.Point;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.StringUtilities;
import org.miradi.xml.generic.LegacyDiagramFactorFontStyleQuestion;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.w3c.dom.Node;

public class DiagramFactorPoolImporter extends AbstractBaseObjectPoolImporter
{
	public DiagramFactorPoolImporter(Xmpz1XmlImporter importerToUse)
	{
		super(importerToUse, DIAGRAM_FACTOR, DiagramFactorSchema.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importDiagramFactorLocation(node, destinationRef);
		importDiagramFactorSize(node, destinationRef);
		importRefs(node, GROUP_BOX_CHILDREN_IDS, destinationRef, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, DiagramFactorSchema.getObjectType(), DIAGRAM_FACTOR);
		importCodeField(node, destinationRef, DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, new TextBoxZOrderQuestion());
		importFontStylingElements(node, destinationRef);
	}
	
	private void importFontStylingElements(Node node, ORef destinationRef) throws Exception
	{
		Node diagramFactorStyleNode = getImporter().getNamedChildNode(node, getPoolName() + STYLING);
		Node style = getImporter().getNamedChildNode(diagramFactorStyleNode, STYLING);
		importCodeField(style, destinationRef, DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		importCodeField(style, destinationRef, DiagramFactor.TAG_FONT_STYLE, new LegacyDiagramFactorFontStyleQuestion());
		importCodeField(style, destinationRef, DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		importCodeField(style, destinationRef, DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion());
	}

	@Override
	protected void postCreateFix(ORef ref, Node node) throws Exception
	{
		ORef wrappedRef = importWrappedRef(getImporter(), getPoolName(),  node);
		getProject().setObjectData(ref, DiagramFactor.TAG_WRAPPED_REF, wrappedRef.toString());
	}

	private  static ORef importWrappedRef(Xmpz1XmlImporter importer, String poolName, Node parentNode) throws Exception
	{
		Node wrappedFactorIdNode = importer.getNamedChildNode(parentNode, poolName + WRAPPED_FACTOR_ID_ELEMENT_NAME);

		return importWrappedRef(importer, wrappedFactorIdNode);
	}

	public static ORef importWrappedRef(Xmpz1XmlImporter importer, Node wrappedFactorIdNode) throws Exception
	{
		Node wrappedByDiagamFactorIdNode = importer.getNamedChildNode(wrappedFactorIdNode, WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		return getWrappedRef(importer, wrappedByDiagamFactorIdNode);
	}

	public static ORef getWrappedRef(Xmpz1XmlImporter importer, Node wrappedByDiagamFactorIdNode) throws Exception
	{
		String oredWrappedFactorNames = createOredWrappableFactorNames();
		XPathExpression expression = importer.getXPath().compile(oredWrappedFactorNames);
		Node node =  (Node) expression.evaluate(wrappedByDiagamFactorIdNode, XPathConstants.NODE);
		BaseId wrappedId = new BaseId(node.getTextContent());
		final int objectTypeOfNode = getObjectTypeOfNode(node);
		
		return new ORef(objectTypeOfNode, wrappedId);
	}

	private static int getObjectTypeOfNode(Node typedIdNode)
	{
		String nodeName = typedIdNode.getNodeName();
		String objectTypeName = removeAppendedId(nodeName);
		
		if (objectTypeName.equals(SCOPE_BOX))
			return ScopeBoxSchema.getObjectType();
		
		if (objectTypeName.equals(BIODIVERSITY_TARGET))
			return TargetSchema.getObjectType();
		
		if (objectTypeName.equals(HUMAN_WELFARE_TARGET))
			return HumanWelfareTargetSchema.getObjectType();
		
		if (objectTypeName.equals(CAUSE) || objectTypeName.equals(THREAT))
			return CauseSchema.getObjectType();
		
		if (objectTypeName.equals(STRATEGY))
			return StrategySchema.getObjectType();
		
		if (objectTypeName.equals(INTERMEDIATE_RESULTS))
			return IntermediateResultSchema.getObjectType();
		
		if (objectTypeName.equals(THREAT_REDUCTION_RESULTS))
			return ThreatReductionResultSchema.getObjectType();
		
		if (objectTypeName.equals(TEXT_BOX))
			return TextBoxSchema.getObjectType();
		
		if (objectTypeName.equals(GROUP_BOX))
			return GroupBoxSchema.getObjectType();
		
		if (isTask(objectTypeName))
			return TaskSchema.getObjectType();
		
		if (objectTypeName.equals(STRESS))
			return StressSchema.getObjectType();
		
		EAM.logError("Could not find type for node: " + objectTypeName);
		return ObjectType.FAKE;
	}
	
	public static boolean isTask(String objectTypeName)
	{
		if (objectTypeName.equals(TaskSchema.ACTIVITY_NAME))
			return true;
		
		if (objectTypeName.equals(TaskSchema.METHOD_NAME))
			return true;
		
		return objectTypeName.equals(Xmpz2XmlConstants.TASK);
	}

	private static String removeAppendedId(String nodeName)
	{
		return nodeName.replaceFirst(ID, "");
	}

	private void importDiagramFactorSize(Node node, ORef destinationRef) throws Exception
	{
		Node diagramFactorSizeNode = getImporter().getNamedChildNode(node, getPoolName()+ SIZE);
		Node sizNode = getImporter().getNamedChildNode(diagramFactorSizeNode, DIAGRAM_SIZE_ELEMENT_NAME);
		Node widthNode = getImporter().getNamedChildNode(sizNode, WIDTH_ELEMENT_NAME);
		Node heightNode = getImporter().getNamedChildNode(sizNode, HEIGHT_ELEMENT_NAME);
		int width = extractNodeTextContentAsInt(widthNode);
		int height = extractNodeTextContentAsInt(heightNode);
		String dimensionAsString = EnhancedJsonObject.convertFromDimension(new Dimension(width, height));
		getImporter().setData(destinationRef, DiagramFactor.TAG_SIZE, dimensionAsString);
	}
	
	private void importDiagramFactorLocation(Node node, ORef destinationRef) throws Exception
	{
		Node locationNode = getImporter().getNamedChildNode(node, getPoolName()+ LOCATION);
		Node pointNode = getImporter().getNamedChildNode(locationNode, DIAGRAM_POINT_ELEMENT_NAME);
		Point point = extractPointFromNode(pointNode);
		String pointAsString = EnhancedJsonObject.convertFromPoint(point);
		getImporter().setData(destinationRef, DiagramFactor.TAG_LOCATION, pointAsString);
	}
	
	public static String createOredWrappableFactorNames()
	{
		return StringUtilities.joinListItems(getWrappableFactorNames(), PREFIX, "|", ID);
	}
	
	public static String[] getWrappableFactorNames()
	{
		return new String[]{
				BIODIVERSITY_TARGET, 
				HUMAN_WELFARE_TARGET, 
				CAUSE, 
				STRATEGY, 
				THREAT_REDUCTION_RESULTS, 
				INTERMEDIATE_RESULTS,
				GROUP_BOX,
				TEXT_BOX,
				SCOPE_BOX,
				ACTIVITY,
				STRESS,
		};
	}
}
