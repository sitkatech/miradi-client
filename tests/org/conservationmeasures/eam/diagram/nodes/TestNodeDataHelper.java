/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.DiagramNode;
import org.conservationmeasures.eam.diagram.cells.NodeDataHelper;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestNodeDataHelper extends EAMTestCase 
{

	public TestNodeDataHelper(String name)
	{
		super(name);
	}
	
	
	public void setUp() throws Exception 
	{
		DiagramNodeId nodeId1 = new DiagramNodeId(44);
		DiagramNodeId nodeId2 = new DiagramNodeId(25);
		DiagramNodeId nodeId3 = new DiagramNodeId(6346);

		Target cmTarget1 = new Target(originalNodeId1);
		node1 = DiagramNode.wrapConceptualModelObject(nodeId1, cmTarget1);
		nodeLocation1 = new Point(nodeLocation1x,nodeLocation1y);
		
		Target cmTarget2 = new Target(originalNodeId2);
		node2 = DiagramNode.wrapConceptualModelObject(nodeId2, cmTarget2);
		nodeLocation2 = new Point(nodeLocation2x,nodeLocation2y);

		Target cmTarget3 = new Target(originalNodeId3);
		node3 = DiagramNode.wrapConceptualModelObject(nodeId3, cmTarget3);
		
		nodes = new Vector();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		
		super.setUp();
	}


	public void testBasics()
	{
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		assertEquals(node1.getDiagramNodeId(), dataHelper.getNewId(node1.getDiagramNodeId()));
		assertEquals(node2.getDiagramNodeId(), dataHelper.getNewId(node2.getDiagramNodeId()));
		assertEquals(node3.getDiagramNodeId(), dataHelper.getNewId(node3.getDiagramNodeId()));
		assertEquals(BaseId.INVALID, dataHelper.getNewId(unknownDiagramId));
	}
	
	public void testSetNewId()
	{
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		dataHelper.setNewId(node1.getDiagramNodeId(), newNodeId1);
		dataHelper.setNewId(node2.getDiagramNodeId(), newNodeId2);
		dataHelper.setNewId(node3.getDiagramNodeId(), newNodeId3);
		assertEquals(newNodeId1, dataHelper.getNewId(node1.getDiagramNodeId()));
		assertEquals(newNodeId2, dataHelper.getNewId(node2.getDiagramNodeId()));
		assertEquals(newNodeId3, dataHelper.getNewId(node3.getDiagramNodeId()));
	}
	
	public void testSetGetLocation()
	{
		NodeDataHelper dataHelper = new NodeDataHelper(nodes);
		dataHelper.setOriginalLocation(node1.getDiagramNodeId(), nodeLocation1);
		int insertX = 0;
		int insertY = 0;
		Point insertionPoint = new Point(insertX, insertY);
		
		Point newNode1Location = dataHelper.getNewLocation(node1.getDiagramNodeId(), insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper.setOriginalLocation(node2.getDiagramNodeId(), nodeLocation2);
		newNode1Location = dataHelper.getNewLocation(node1.getDiagramNodeId(), insertionPoint);
		Point newNode2Location = dataHelper.getNewLocation(node2.getDiagramNodeId(), insertionPoint);
		assertEquals(insertX+(nodeLocation1x-nodeLocation2x), newNode1Location.x);
		assertEquals(insertY+(nodeLocation1y-nodeLocation2y), newNode1Location.y);
		assertEquals(insertX, newNode2Location.x);
		assertEquals(insertY, newNode2Location.y);

		insertX = 50;
		insertY = 50;
		insertionPoint.setLocation(insertX, insertY); 
		NodeDataHelper dataHelper2 = new NodeDataHelper(nodes);
		dataHelper2.setOriginalLocation(node1.getDiagramNodeId(), nodeLocation1);
		newNode1Location = dataHelper2.getNewLocation(node1.getDiagramNodeId(), insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper2.setOriginalLocation(node2.getDiagramNodeId(), nodeLocation2);
		newNode1Location = dataHelper2.getNewLocation(node1.getDiagramNodeId(), insertionPoint);
		int deltaX = 45;
		int deltaY = 40;
		assertEquals(nodeLocation1x+deltaX, newNode1Location.x);
		assertEquals(nodeLocation1y+deltaY, newNode1Location.y);

		newNode2Location = dataHelper2.getNewLocation(node2.getDiagramNodeId(), insertionPoint);
		assertEquals(nodeLocation2x+deltaX, newNode2Location.x);
		assertEquals(nodeLocation2y+deltaY, newNode2Location.y);
		
	}
	

	final ModelNodeId originalNodeId1 = new ModelNodeId(1);
	final ModelNodeId originalNodeId2 = new ModelNodeId(2);
	final ModelNodeId originalNodeId3 = new ModelNodeId(3);
	final DiagramNodeId newNodeId1 = new DiagramNodeId(5);
	final DiagramNodeId newNodeId2 = new DiagramNodeId(6);
	final DiagramNodeId newNodeId3 = new DiagramNodeId(7);
	final ModelNodeId unknownModelId = new ModelNodeId(10);
	final DiagramNodeId unknownDiagramId = new DiagramNodeId(11);
	final int nodeLocation1x = 20;
	final int nodeLocation1y = 50;
	final int nodeLocation2x = 5;
	final int nodeLocation2y = 10;
	
	DiagramNode node1;
	DiagramNode node2;
	DiagramNode node3;

	Vector nodes;
	Point nodeLocation1;
	Point nodeLocation2;
}
