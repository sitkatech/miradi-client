/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestBaseProject extends EAMTestCase
{
	public TestBaseProject(String name)
	{
		super(name);
	}

	public void testGetAllSelectedCellsWithLinkages() throws Exception
	{
		BaseProject project = new BaseProject();
		DiagramModel model = project.getDiagramModel();

		Node node1 = model.createNode(Node.TYPE_GOAL);
		Node node2 =  model.createNode(Node.TYPE_INTERVENTION);
		Node node3 =  model.createNode(Node.TYPE_THREAT);
		
		Linkage linkage1 = model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		Linkage linkage2 = model.createLinkage(Node.INVALID_ID, node1.getId(), node3.getId());
		
		EAMGraphCell[] selectedCells = {linkage1};
		Vector selectedItems = project.getAllSelectedCellsWithLinkages(selectedCells);
		assertEquals(1, selectedItems.size());
		assertContains(linkage1, selectedItems);
		
		selectedCells[0] = node2;
		selectedItems = project.getAllSelectedCellsWithLinkages(selectedCells);
		assertEquals(2, selectedItems.size());
		assertContains(node2, selectedItems);
		assertContains(linkage1, selectedItems);
		
		selectedCells[0] = node1;
		selectedItems = project.getAllSelectedCellsWithLinkages(selectedCells);
		assertEquals(3, selectedItems.size());
		assertContains(node1, selectedItems);
		assertContains(linkage1, selectedItems);
		assertContains(linkage2, selectedItems);
	}
	
	public void testPasteCells() throws Exception
	{
		BaseProject project = new BaseProject();
		DiagramModel model = project.getDiagramModel();

		Node node1 = model.createNode(Node.TYPE_GOAL);
		Node node2 =  model.createNode(Node.TYPE_INTERVENTION);
		Node node3 =  model.createNode(Node.TYPE_THREAT);
		
		model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		model.createLinkage(Node.INVALID_ID, node1.getId(), node3.getId());
		
		Vector cellVector = project.getAllSelectedCellsWithLinkages(new EAMGraphCell[]{node1});
		Object[] selectedCells = cellVector.toArray(new EAMGraphCell[0]);
		TransferableEamList transferableList = new TransferableEamList(selectedCells);
		assertEquals(3, model.getAllNodes().size());
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(1, model.getLinkages(node2).size());
		assertEquals(1, model.getLinkages(node3).size());
		
		project.pasteCellsIntoProject(transferableList, new Point(5,5));
		Vector nodes = model.getAllNodes();
		assertEquals(4, nodes.size());
		for(int i = 0; i < nodes.size(); ++i)
		{
			assertEquals(2, model.getLinkages((Node)nodes.get(i)).size());
		}
		
		//Test when a pasted item has linkages to a previously deleted node
		model.deleteNode(node2);
		project.pasteCellsIntoProject(transferableList, new Point(5,5));
		assertEquals(2, model.getLinkages(node1).size());
		assertEquals(3, model.getLinkages(node3).size());
	}


}
