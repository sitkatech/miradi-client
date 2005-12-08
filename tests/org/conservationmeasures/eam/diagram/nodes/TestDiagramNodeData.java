/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.nodes.types.NodeType;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		NodeType nodeAType = DiagramNode.TYPE_STRESS;
		DiagramNode nodeA = DiagramNode.wrapConceptualModelObject(new ConceptualModelFactor(nodeAType));
		String nodeAText = "Node A";
		Point location = new Point(5,22);
		int id = 2;
		nodeA.setText(nodeAText);
		nodeA.setLocation(location);
		nodeA.setId(id);
		NodeDataMap nodeAData = nodeA.createNodeDataMap();
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(DiagramNode.TAG_VISIBLE_LABEL));
		assertEquals("location incorrect", location, nodeAData.getPoint(DiagramNode.TAG_LOCATION));
		assertEquals("id incorrect", id, nodeAData.getInt(DiagramNode.TAG_ID));
		assertEquals("type incorrect", nodeAType, nodeAData.getNodeType());
		assertEquals("Priority default not None?", ThreatPriority.createPriorityNone().getValue(), nodeAData.getInt(DiagramNode.TAG_PRIORITY));
	}
	
	public void testNoneThreatNode()  throws Exception
	{
		DiagramNode nodeB = DiagramNode.wrapConceptualModelObject(new ConceptualModelTarget());
		DataMap nodeBData = nodeB.createNodeDataMap();
		assertEquals("Priority of a non threat not PRIORITY_NOT_USED?", ThreatPriority.PRIORITY_NOT_USED, nodeBData.getInt(DiagramNode.TAG_PRIORITY));
	}

}
