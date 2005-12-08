/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeDataHelper extends EAMTestCase 
{

	public TestNodeDataHelper(String name)
	{
		super(name);
	}
	
	
	public void setUp() throws Exception 
	{
		ConceptualModelTarget cmTarget1 = new ConceptualModelTarget();
		DiagramNode node1 = DiagramNode.wrapConceptualModelObject(cmTarget1);
		cmTarget1.setId(originalNodeId1);
		nodeLocation1 = new Point(nodeLocation1x,nodeLocation1y);
		
		ConceptualModelTarget cmTarget2 = new ConceptualModelTarget();
		DiagramNode node2 = DiagramNode.wrapConceptualModelObject(cmTarget2);
		cmTarget2.setId(originalNodeId2);
		nodeLocation2 = new Point(nodeLocation2x,nodeLocation2y);

		ConceptualModelTarget cmTarget3 = new ConceptualModelTarget();
		DiagramNode node3 = DiagramNode.wrapConceptualModelObject(cmTarget3);
		cmTarget3.setId(originalNodeId3);
		
		nodes = new Vector();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		
		super.setUp();
	}


	public void testBasics()
	{
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		assertEquals(originalNodeId1, dataHelper.getNewId(originalNodeId1));
		assertEquals(originalNodeId2, dataHelper.getNewId(originalNodeId2));
		assertEquals(originalNodeId3, dataHelper.getNewId(originalNodeId3));
		assertEquals(IdAssigner.INVALID_ID, dataHelper.getNewId(unknownNodeId));
	}
	
	public void testSetNewId()
	{
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		dataHelper.setNewId(originalNodeId1, newNodeId1);
		dataHelper.setNewId(originalNodeId2, newNodeId2);
		dataHelper.setNewId(originalNodeId3, newNodeId3);
		assertEquals(newNodeId1, dataHelper.getNewId(originalNodeId1));
		assertEquals(newNodeId2, dataHelper.getNewId(originalNodeId2));
		assertEquals(newNodeId3, dataHelper.getNewId(originalNodeId3));
	}
	
	public void testSetGetLocation()
	{
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		dataHelper.setOriginalLocation(originalNodeId1, nodeLocation1);
		int insertX = 0;
		int insertY = 0;
		Point insertionPoint = new Point(insertX, insertY);
		
		Point newNode1Location = dataHelper.getNewLocation(originalNodeId1, insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper.setOriginalLocation(originalNodeId2, nodeLocation2);
		newNode1Location = dataHelper.getNewLocation(originalNodeId1, insertionPoint);
		Point newNode2Location = dataHelper.getNewLocation(originalNodeId2, insertionPoint);
		assertEquals(insertX+(nodeLocation1x-nodeLocation2x), newNode1Location.x);
		assertEquals(insertY+(nodeLocation1y-nodeLocation2y), newNode1Location.y);
		assertEquals(insertX, newNode2Location.x);
		assertEquals(insertY, newNode2Location.y);

		insertX = 50;
		insertY = 50;
		insertionPoint.setLocation(insertX, insertY); 
		NodeDataHelper dataHelper2 = new NodeDataHelper(nodes);
		dataHelper2.setOriginalLocation(originalNodeId1, nodeLocation1);
		newNode1Location = dataHelper2.getNewLocation(originalNodeId1, insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper2.setOriginalLocation(originalNodeId2, nodeLocation2);
		newNode1Location = dataHelper2.getNewLocation(originalNodeId1, insertionPoint);
		int deltaX = 45;
		int deltaY = 40;
		assertEquals(nodeLocation1x+deltaX, newNode1Location.x);
		assertEquals(nodeLocation1y+deltaY, newNode1Location.y);

		newNode2Location = dataHelper2.getNewLocation(originalNodeId2, insertionPoint);
		assertEquals(nodeLocation2x+deltaX, newNode2Location.x);
		assertEquals(nodeLocation2y+deltaY, newNode2Location.y);
		
	}
	

	final int originalNodeId1 = 1;
	final int originalNodeId2 = 2;
	final int originalNodeId3 = 3;
	final int newNodeId1 = 5;
	final int newNodeId2 = 6;
	final int newNodeId3 = 7;
	final int unknownNodeId = 10;
	final int nodeLocation1x = 20;
	final int nodeLocation1y = 50;
	final int nodeLocation2x = 5;
	final int nodeLocation2y = 10;

	Vector nodes;
	Point nodeLocation1;
	Point nodeLocation2;
}
