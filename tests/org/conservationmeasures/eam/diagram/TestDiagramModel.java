/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
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
		DiagramNode factor = model.createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramLinkage link = model.createLinkage(DiagramNode.INVALID_ID, factor.getId(), target.getId());
		assertTrue("factor isn't a node?", factor.isNode());
		assertTrue("target isn't a node?", target.isNode());
		assertFalse("linkage is a node?", link.isNode());
	}
	
	public void testCounts()throws Exception
	{
		DiagramModel model = new DiagramModel();
		DiagramNode factor = model.createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = model.createNode(DiagramNode.TYPE_TARGET);
		model.createLinkage(DiagramNode.INVALID_ID, factor.getId(), target.getId());
		assertEquals(2, model.getNodeCount());
		assertEquals(1, model.getLinkageCount());
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramModel model = new DiagramModel();
		DiagramNode factor = model.createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		model.deleteNode(factor);
		DiagramNode newFactor = model.createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = model.createNode(DiagramNode.TYPE_TARGET);
		assertFalse("already linked?", model.hasLinkage(newFactor, target));
		model.createLinkage(DiagramNode.INVALID_ID, newFactor.getId(), target.getId());
		assertTrue("not linked?", model.hasLinkage(newFactor, target));
		assertTrue("reverse link not detected?", model.hasLinkage(target, newFactor));
	}
	
	public void testGetLinkages() throws Exception
	{
		DiagramModel model = new DiagramModel();
		DiagramNode factor1 = model.createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode factor2 = model.createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramLinkage linkage1 = model.createLinkage(DiagramNode.INVALID_ID, factor1.getId(), target.getId());
		DiagramLinkage linkage2 = model.createLinkage(DiagramNode.INVALID_ID, factor2.getId(), target.getId());
		Set found = model.getLinkages(target);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}
	
	
	public void testUpdatesNextId() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.createNodeAtId(DiagramNode.TYPE_INDIRECT_FACTOR, 125);
		DiagramNode target = model.createNodeAtId(DiagramNode.TYPE_TARGET, -1);
		assertEquals("didn't use next available id?", 126, target.getId());
	}
	
	public void testCreateNode() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.createNode(DiagramNode.TYPE_TARGET);		
		DiagramNode nodeToDelete = model.createNode(DiagramNode.TYPE_TARGET);		
		model.createNode(DiagramNode.TYPE_TARGET);		
		model.createNode(DiagramNode.TYPE_TARGET);		
		model.deleteNode(nodeToDelete);
		model.createNodeAtId(DiagramNode.TYPE_TARGET, nodeToDelete.getId()); //simulates an undo
		DiagramNode nodeAfterUndo = model.createNode(DiagramNode.TYPE_TARGET);
		assertEquals("Id should be 4", 4, nodeAfterUndo.getId());
	}
	
	public void testGetAllNodes() throws Exception
	{
		DiagramModel model = new DiagramModel();
		DiagramNode node1 = model.createNode(DiagramNode.TYPE_TARGET);		
		DiagramNode node2 = model.createNode(DiagramNode.TYPE_TARGET);		
		model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node2.getId());
		DiagramNode node3 = model.createNode(DiagramNode.TYPE_TARGET);		
		
		Vector nodes = model.getAllNodes();
		assertEquals(3, nodes.size());
		assertContains(node1, nodes);
		assertContains(node2, nodes);
		assertContains(node3, nodes);
	}
	
	public void testGetAllLinkages() throws Exception
	{
		DiagramModel model = new DiagramModel();
		DiagramNode node1 = model.createNode(DiagramNode.TYPE_TARGET);		
		DiagramNode node2 = model.createNode(DiagramNode.TYPE_TARGET);		
		DiagramLinkage link1 = model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node2.getId());
		DiagramNode node3 = model.createNode(DiagramNode.TYPE_TARGET);		
		DiagramLinkage link2 = model.createLinkage(DiagramNode.INVALID_ID, node1.getId(), node3.getId());
		
		Vector linkages = model.getAllLinkages();
		assertEquals(2, linkages.size());
		assertContains(link1, linkages);
		assertContains(link2, linkages);
	}
	
	public void testActionsFiring() throws Exception
	{
		DiagramModel model = new DiagramModel();
		TestTableModel testModel = new TestTableModel();
		testModel.addListener(model);
		DiagramNode node1 = model.createNode(DiagramNode.TYPE_TARGET);
		assertEquals("test model wasn't notified of add action?", 1, testModel.nodeAdded);
		assertEquals("node changed already called?", 0, testModel.nodeChanged);
		assertEquals("node deleted already called?", 0, testModel.nodeDeleted);
		assertEquals("node add did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("added already called?", 0, testModel.linkAdded);
		assertEquals("linkage deleted already called?", 0, testModel.linkDeleted);
		
		model.updateCell(node1);
		assertEquals("update did a node add notify?", 1, testModel.nodeAdded);
		assertEquals("update didn't do a node changed notify?", 1, testModel.nodeChanged);
		assertEquals("test model fired a delete action for a modify?", 0, testModel.nodeDeleted);
		assertEquals("update did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("update did a link add notify?", 0, testModel.linkAdded);
		assertEquals("update did a linkdeleted notify?", 0, testModel.linkDeleted);
		
		model.deleteNode(node1);
		assertEquals("delete node didn't notify?", 1, testModel.nodeDeleted);
		assertEquals("delete node triggered a node add notify?", 1, testModel.nodeAdded);
		assertEquals("test model modify action called for delete?", 1, testModel.nodeChanged);
		assertEquals("node delete did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("delete node did a link add notify?", 0, testModel.linkAdded);
		assertEquals("delete node did a link delete notify?", 0, testModel.linkDeleted);

		DiagramNode node2 = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramNode node3 = model.createNode(DiagramNode.TYPE_TARGET);
		DiagramLinkage link1 = model.createLinkage(DiagramNode.INVALID_ID, node2.getId(), node3.getId());
		assertEquals("didn't do more node add notify's?", 3, testModel.nodeAdded);
		assertEquals("add link did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("add link did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("link add did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("test model linkageAdded action not called for addition of a link?", 1, testModel.linkAdded);
		assertEquals("add link did a delete link notify?", 0, testModel.linkDeleted);

		model.deleteLinkage(link1);
		assertEquals("link delete did a node add notify?", 3, testModel.nodeAdded);
		assertEquals("link delete did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("link delete did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("link delete did a node move notify?", 0, testModel.nodeMoved);
		assertEquals("link delete did a link add notify?", 1, testModel.linkAdded);
		assertEquals("test model linkageDeleted action not called?",1, testModel.linkDeleted);
		
		model.nodesWereMoved(1, 1, new int[] {node2.getId(), node3.getId()});
		assertEquals("move nodes did a node add notify?", 3, testModel.nodeAdded);
		assertEquals("move nodes did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("move nodes did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("node move didn't do all node move notify's?", 2, testModel.nodeMoved);
		assertEquals("move nodes did a link add notify?", 1, testModel.linkAdded);
		assertEquals("move nodes did a link delete notify?", 1, testModel.linkDeleted);
		
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
		
		public void nodeMoved(DiagramModelEvent event) 
		{
			nodeMoved++;
		}

		public void linkageAdded(DiagramModelEvent event) 
		{
			linkAdded++;
		}

		public void linkageDeleted(DiagramModelEvent event) 
		{
			linkDeleted++;
		}

		int nodeAdded = 0;
		int nodeDeleted = 0;
		int nodeChanged = 0;
		int linkAdded = 0;
		int linkDeleted = 0;
		int nodeMoved = 0;
	}

	
}
