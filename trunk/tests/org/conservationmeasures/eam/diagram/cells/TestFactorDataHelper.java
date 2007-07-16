/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.cells;

import java.awt.Point;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
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

		diagramFactorId1 = project.createAndAddFactorToDiagram(ObjectType.TARGET); 
		nodeLocation1 = new Point(nodeLocation1x,nodeLocation1y);

		diagramFactorId2 = project.createAndAddFactorToDiagram(ObjectType.TARGET);
		nodeLocation2 = new Point(nodeLocation2x,nodeLocation2y);
		
		diagramFactorId3 = project.createAndAddFactorToDiagram(ObjectType.TARGET);
		
		diagramFactorIds = project.getAllDiagramFactorIds(); 

		super.setUp();
	}


	public void testBasics()
	{
		FactorDataHelper dataHelper = new FactorDataHelper(diagramFactorIds);
		assertEquals(diagramFactorId1, dataHelper.getNewId(diagramFactorId1));
		assertEquals(diagramFactorId2, dataHelper.getNewId(diagramFactorId2));
		assertEquals(diagramFactorId3, dataHelper.getNewId(diagramFactorId3));
		assertEquals(BaseId.INVALID, dataHelper.getNewId(unknownDiagramId));
	}
	
	public void testSetNewId()
	{
		FactorDataHelper dataHelper = new FactorDataHelper(diagramFactorIds);
		dataHelper.setNewId(diagramFactorId1, newNodeId1);
		dataHelper.setNewId(diagramFactorId2, newNodeId2);
		dataHelper.setNewId(diagramFactorId3, newNodeId3);
		assertEquals(newNodeId1, dataHelper.getNewId(diagramFactorId1));
		assertEquals(newNodeId2, dataHelper.getNewId(diagramFactorId2));
		assertEquals(newNodeId3, dataHelper.getNewId(diagramFactorId3));
	}
	
	public void testSetGetLocation()
	{
		FactorDataHelper dataHelper = new FactorDataHelper(diagramFactorIds);
		dataHelper.setOriginalLocation(diagramFactorId1, nodeLocation1);
		int insertX = 0;
		int insertY = 0;
		Point insertionPoint = new Point(insertX, insertY);
		
		Point newNode1Location = dataHelper.getNewLocation(diagramFactorId1, insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper.setOriginalLocation(diagramFactorId2, nodeLocation2);
		newNode1Location = dataHelper.getNewLocation(diagramFactorId1, insertionPoint);
		Point newNode2Location = dataHelper.getNewLocation(diagramFactorId2, insertionPoint);
		assertEquals(insertX+(nodeLocation1x-nodeLocation2x), newNode1Location.x);
		assertEquals(insertY+(nodeLocation1y-nodeLocation2y), newNode1Location.y);
		assertEquals(insertX, newNode2Location.x);
		assertEquals(insertY, newNode2Location.y);

		insertX = 50;
		insertY = 50;
		insertionPoint.setLocation(insertX, insertY); 
		FactorDataHelper dataHelper2 = new FactorDataHelper(diagramFactorIds);
		dataHelper2.setOriginalLocation(diagramFactorId1, nodeLocation1);
		newNode1Location = dataHelper2.getNewLocation(diagramFactorId1, insertionPoint);
		assertEquals(insertX, newNode1Location.x);
		assertEquals(insertY, newNode1Location.y);
		
		dataHelper2.setOriginalLocation(diagramFactorId2, nodeLocation2);
		newNode1Location = dataHelper2.getNewLocation(diagramFactorId1, insertionPoint);
		int deltaX = 45;
		int deltaY = 40;
		assertEquals(nodeLocation1x+deltaX, newNode1Location.x);
		assertEquals(nodeLocation1y+deltaY, newNode1Location.y);

		newNode2Location = dataHelper2.getNewLocation(diagramFactorId2, insertionPoint);
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
	
	DiagramFactorId diagramFactorId1;
	DiagramFactorId diagramFactorId2;
	DiagramFactorId diagramFactorId3;

	DiagramFactorId[] diagramFactorIds;
	Point nodeLocation1;
	Point nodeLocation2;
	
	ProjectForTesting project;
}
