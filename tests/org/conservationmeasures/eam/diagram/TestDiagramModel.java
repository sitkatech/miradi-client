/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramModel extends EAMTestCase
{
	public TestDiagramModel(String name)
	{
		super(name);
	}

	public void testIsNode() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createNode(Node.TYPE_THREAT);
		Node goal = model.createNode(Node.TYPE_GOAL);
		Linkage link = model.createLinkage(Node.INVALID_ID, threat.getId(), goal.getId());
		assertTrue("threat isn't a node?", model.isNode(threat));
		assertTrue("goal isn't a node?", model.isNode(goal));
		assertFalse("linkage is a node?", model.isNode(link));
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createNode(Node.TYPE_THREAT);
		model.deleteNode(threat);
		Node newThreat = model.createNode(Node.TYPE_THREAT);
		Node goal = model.createNode(Node.TYPE_GOAL);
		assertFalse("already linked?", model.hasLinkage(newThreat, goal));
		model.createLinkage(Node.INVALID_ID, newThreat.getId(), goal.getId());
		assertTrue("not linked?", model.hasLinkage(newThreat, goal));
		assertTrue("reverse link not detected?", model.hasLinkage(goal, newThreat));
	}
	
	public void testGetLinkages() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat1 = model.createNode(Node.TYPE_THREAT);
		Node threat2 = model.createNode(Node.TYPE_THREAT);
		Node goal = model.createNode(Node.TYPE_GOAL);
		Linkage linkage1 = model.createLinkage(Node.INVALID_ID, threat1.getId(), goal.getId());
		Linkage linkage2 = model.createLinkage(Node.INVALID_ID, threat2.getId(), goal.getId());
		Set found = model.getLinkages(goal);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}
	
	public void testUpdatesNextId() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.createNodeAtId(Node.TYPE_THREAT, 125);
		Node goal = model.createNodeAtId(Node.TYPE_GOAL, -1);
		assertEquals("didn't use next available id?", 126, goal.getId());
	}
	
	public void testCreateNode() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.createNode(Node.TYPE_GOAL);		
		Node nodeToDelete = model.createNode(Node.TYPE_GOAL);		
		model.createNode(Node.TYPE_GOAL);		
		model.createNode(Node.TYPE_GOAL);		
		model.deleteNode(nodeToDelete);
		model.createNodeAtId(Node.TYPE_GOAL, nodeToDelete.getId()); //simulates an undo
		Node nodeAfterUndo = model.createNode(Node.TYPE_GOAL);
		assertEquals("Id should be 4", 4, nodeAfterUndo.getId());
	}
	
	public void testGetAllNodes() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node node1 = model.createNode(Node.TYPE_GOAL);		
		Node node2 = model.createNode(Node.TYPE_GOAL);		
		model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		Node node3 = model.createNode(Node.TYPE_GOAL);		
		
		Vector nodes = model.getAllNodes();
		assertEquals(3, nodes.size());
		assertContains(node1, nodes);
		assertContains(node2, nodes);
		assertContains(node3, nodes);
	}
	
	public void testActionsFiring()
	{
		DiagramModel model = new DiagramModel();
		TestTableModel testModel = new TestTableModel();
		testModel.addListener(model);
		Node nodeCreated = model.createNode(Node.TYPE_GOAL);
		assertEquals("test model wasn't notified of add action?", 1, testModel.nodeAdded);
		model.updateCell(nodeCreated);
		assertEquals("test model wasn't notified of modify action?", 1, testModel.nodeChanged);
		model.deleteNode(nodeCreated);
		assertEquals("test model wasn't notified of delete action?", 1, testModel.nodeDeleted);
	}
	
	class TestTableModel implements DiagramModelListener
	{
		
		public void addListener(DiagramModel model)
		{
			model.addDiagramModelListener(this);
		}

		public void nodeAdded(DiagramModelEvent event) 
		{
			nodeAdded++;
		}

		public void nodeDeleted(DiagramModelEvent event) 
		{
			nodeDeleted++;
		}

		public void nodeChanged(DiagramModelEvent event) 
		{
			nodeChanged++;
		}
		
		int nodeAdded = 0;
		int nodeDeleted = 0;
		int nodeChanged = 0;
	}

	
}
