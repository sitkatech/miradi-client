/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.util.Vector;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeDataHelper extends EAMTestCase 
{

	public TestNodeDataHelper(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		int originalNode1 = 1;
		int originalNode2 = 2;
		int originalNode3 = 3;
		int newNode1 = 5;
		int newNode2 = 6;
		int newNode3 = 7;
		int unknownNode = 10;
		
		Node node1 = new Node(Node.TYPE_GOAL);
		node1.setId(originalNode1);
		Node node2 = new Node(Node.TYPE_GOAL);
		node2.setId(originalNode2);
		Node node3 = new Node(Node.TYPE_GOAL);
		node3.setId(originalNode3);
		
		Vector nodes = new Vector();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		assertEquals(originalNode1, dataHelper.getUpdatedId(originalNode1));
		assertEquals(originalNode2, dataHelper.getUpdatedId(originalNode2));
		assertEquals(originalNode3, dataHelper.getUpdatedId(originalNode3));
		assertEquals(Node.INVALID_ID, dataHelper.getUpdatedId(unknownNode));
		
		dataHelper.updateIds(originalNode1, newNode1);
		dataHelper.updateIds(originalNode2, newNode2);
		dataHelper.updateIds(originalNode3, newNode3);
		assertEquals(newNode1, dataHelper.getUpdatedId(originalNode1));
		assertEquals(newNode2, dataHelper.getUpdatedId(originalNode2));
		assertEquals(newNode3, dataHelper.getUpdatedId(originalNode3));
		
	}
}
