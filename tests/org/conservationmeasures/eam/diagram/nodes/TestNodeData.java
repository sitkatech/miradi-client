/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

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
		new NodeData(nodeA);
		
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
