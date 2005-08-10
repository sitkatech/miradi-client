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
		Node nodeA = new Node(Node.TYPE_THREAT);
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
		
		Node nodeB = new Node(Node.TYPE_THREAT);
		Linkage linkage = new Linkage(nodeA, nodeB);
		try
		{
			new NodeData(linkage);
			fail("Should have thrown since this is not a node");
		}
		catch (Exception expectedException)
		{
		}
	}

}
