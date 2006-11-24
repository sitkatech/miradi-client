/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ConceptualModelCause;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		ModelNodeId wrappedId = new ModelNodeId(2);
		ConceptualModelCause cmFactor = new ConceptualModelCause(wrappedId);
		DiagramNodeId nodeId = new DiagramNodeId(44);
		DiagramNode nodeA = DiagramNode.wrapConceptualModelObject(nodeId, cmFactor);
		String nodeAText = "Node A";
		nodeA.setLabel(nodeAText);
		Point location = new Point(5,22);
		nodeA.setLocation(location);
		NodeDataMap nodeAData = nodeA.createNodeDataMap();
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(DiagramNode.TAG_VISIBLE_LABEL));
		assertEquals("location incorrect", location, nodeAData.getPoint(DiagramNode.TAG_LOCATION));
		assertEquals("id incorrect", nodeId, nodeAData.getId(DiagramNode.TAG_ID));
		assertEquals("wrapped id incorrect", wrappedId, nodeAData.getId(DiagramNode.TAG_WRAPPED_ID));
	}
	
}
