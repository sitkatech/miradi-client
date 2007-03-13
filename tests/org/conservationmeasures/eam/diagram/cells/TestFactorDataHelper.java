/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestFactorDataHelper extends EAMTestCase 
{

	public TestFactorDataHelper(String name)
	{
		super(name);
	}
	
	
	public void setUp() throws Exception 
	{
		project = new ProjectForTesting(getName());

		node1 = project.createFactorCell(Factor.TYPE_TARGET); 
		nodeLocation1 = new Point(nodeLocation1x,nodeLocation1y);

		node2 = project.createFactorCell(Factor.TYPE_TARGET);
		nodeLocation2 = new Point(nodeLocation2x,nodeLocation2y);
		node3 = project.createFactorCell(Factor.TYPE_TARGET);
		
		nodes = new Vector();
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		
		super.setUp();
	}


	public void testBasics()
	{
		FactorDataHelper dataHelper = new FactorDataHelper(nodes);
		assertEquals(node1.getDiagramFactorId(), dataHelper.getNewId(node1.getDiagramFactorId()));
		assertEquals(node2.getDiagramFactorId(), dataHelper.getNewId(node2.getDiagramFactorId()));
		assertEquals(node3.getDiagramFactorId(), dataHelper.getNewId(node3.getDiagramFactorId()));
		assertEquals(BaseId.INVALID, dataHelper.getNewId(unknownDiagramId));
	}
	
	public void testSetNewId()
	{
		FactorDataHelper dataHelper = new FactorDataHelper(nodes);
		dataHelper.setNewId(node1.getDiagramFactorId(), newNodeId1);
		dataHelper.setNewId(node2.getDiagramFactorId(), newNodeId2);
		dataHelper.setNewId(node3.getDiagramFactorId(), newNodeId3);
		assertEquals(newNodeId1, dataHelper.getNewId(node1.getDiagramFactorId()));
		assertEquals(newNodeId2, dataHelper.getNewId(node2.getDiagramFactorId()));
		assertEquals(newNodeId3, dataHelper.getNewId(node3.getDiagramFactorId()));
	}
	
	public void testSetGetLocation()
	{
		FactorDataHelper dataHelper = new FactorDataHelper(nodes);
		dataHelper.setOriginalLocation(node1.getDiagramFactorId(), nodeLocation1);
		int insertX = 0;
		int insertY = 0;
		Point insertionPoint = new Point(insertX, insertY);
		
		Point newNode1Location = dataHelper.getNewLocation(node1.getDiagramFactorId(), insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper.setOriginalLocation(node2.getDiagramFactorId(), nodeLocation2);
		newNode1Location = dataHelper.getNewLocation(node1.getDiagramFactorId(), insertionPoint);
		Point newNode2Location = dataHelper.getNewLocation(node2.getDiagramFactorId(), insertionPoint);
		assertEquals(insertX+(nodeLocation1x-nodeLocation2x), newNode1Location.x);
		assertEquals(insertY+(nodeLocation1y-nodeLocation2y), newNode1Location.y);
		assertEquals(insertX, newNode2Location.x);
		assertEquals(insertY, newNode2Location.y);

		insertX = 50;
		insertY = 50;
		insertionPoint.setLocation(insertX, insertY); 
		FactorDataHelper dataHelper2 = new FactorDataHelper(nodes);
		dataHelper2.setOriginalLocation(node1.getDiagramFactorId(), nodeLocation1);
		newNode1Location = dataHelper2.getNewLocation(node1.getDiagramFactorId(), insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper2.setOriginalLocation(node2.getDiagramFactorId(), nodeLocation2);
		newNode1Location = dataHelper2.getNewLocation(node1.getDiagramFactorId(), insertionPoint);
		int deltaX = 45;
		int deltaY = 40;
		assertEquals(nodeLocation1x+deltaX, newNode1Location.x);
		assertEquals(nodeLocation1y+deltaY, newNode1Location.y);

		newNode2Location = dataHelper2.getNewLocation(node2.getDiagramFactorId(), insertionPoint);
		assertEquals(nodeLocation2x+deltaX, newNode2Location.x);
		assertEquals(nodeLocation2y+deltaY, newNode2Location.y);
		
	}
	

	final FactorId originalNodeId1 = new FactorId(1);
	final FactorId originalNodeId2 = new FactorId(2);
	final FactorId originalNodeId3 = new FactorId(3);
	final DiagramFactorId newNodeId1 = new DiagramFactorId(5);
	final DiagramFactorId newNodeId2 = new DiagramFactorId(6);
	final DiagramFactorId newNodeId3 = new DiagramFactorId(7);
	final FactorId unknownModelId = new FactorId(10);
	final DiagramFactorId unknownDiagramId = new DiagramFactorId(11);
	final int nodeLocation1x = 20;
	final int nodeLocation1y = 50;
	final int nodeLocation2x = 5;
	final int nodeLocation2y = 10;
	
	FactorCell node1;
	FactorCell node2;
	FactorCell node3;

	Vector nodes;
	Point nodeLocation1;
	Point nodeLocation2;
	
	ProjectForTesting project;
}
