/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

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
		DiagramModel model = new DiagramModel();

		Node node1 = model.createNode(Node.TYPE_GOAL);
		Node node2 =  model.createNode(Node.TYPE_INTERVENTION);
		Node node3 =  model.createNode(Node.TYPE_THREAT);
		
		Linkage linkage1 = model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		Linkage linkage2 = model.createLinkage(Node.INVALID_ID, node1.getId(), node3.getId());
		
		EAMGraphCell[] selectedCells = {linkage1};
		Vector selectedItems = BaseProject.getAllSelectedCellsWithLinkages(selectedCells, model);
		assertEquals(1, selectedItems.size());
		assertContains(linkage1, selectedItems);
		
		selectedCells[0] = node2;
		selectedItems = BaseProject.getAllSelectedCellsWithLinkages(selectedCells, model);
		assertEquals(2, selectedItems.size());
		assertContains(node2, selectedItems);
		assertContains(linkage1, selectedItems);
		
		selectedCells[0] = node1;
		selectedItems = BaseProject.getAllSelectedCellsWithLinkages(selectedCells, model);
		assertEquals(3, selectedItems.size());
		assertContains(node1, selectedItems);
		assertContains(linkage1, selectedItems);
		assertContains(linkage2, selectedItems);
	}
}
