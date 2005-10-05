/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeData extends EAMTestCase 
{

	public TestNodeData(String name) 
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		int nodeAType = Node.TYPE_FACTOR;
		Node nodeA = new Node(nodeAType);
		String nodeAText = "Node A";
		Point location = new Point(5,22);
		int id = 2;
		nodeA.setText(nodeAText);
		nodeA.setLocation(location);
		nodeA.setId(id);
		NodeData nodeAData = new NodeData(nodeA);
		
		assertEquals("Text incorrect", nodeAText, nodeAData.getText());
		assertEquals("location incorrect", location, nodeAData.getLocation());
		assertEquals("id incorrect", id, nodeAData.getId());
		assertEquals("type incorrect", nodeAType, nodeAData.getType());
	}

}
