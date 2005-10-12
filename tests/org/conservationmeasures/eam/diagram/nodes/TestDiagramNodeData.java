/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramNodeData extends EAMTestCase 
{

	public TestDiagramNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		int nodeAType = DiagramNode.TYPE_INDIRECT_FACTOR;
		DiagramNode nodeA = new DiagramNode(nodeAType);
		String nodeAText = "Node A";
		Point location = new Point(5,22);
		int id = 2;
		nodeA.setText(nodeAText);
		nodeA.setLocation(location);
		nodeA.setId(id);
		NodeData nodeAData = new NodeData(nodeA);
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getString(NodeData.TEXT));
		assertEquals("location incorrect", location, nodeAData.getPoint(NodeData.LOCATION));
		assertEquals("id incorrect", id, nodeAData.getInt(NodeData.ID));
		assertEquals("type incorrect", nodeAType, nodeAData.getInt(NodeData.TYPE));
	}

}
