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
		Node target = model.createNode(Node.TYPE_TARGET);
		Linkage link = model.createLinkage(Node.INVALID_ID, threat.getId(), target.getId());
		assertTrue("threat isn't a node?", threat.isNode());
		assertTrue("target isn't a node?", target.isNode());
		assertFalse("linkage is a node?", link.isNode());
	}
	
	public void testCounts()throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createNode(Node.TYPE_THREAT);
		Node target = model.createNode(Node.TYPE_TARGET);
		model.createLinkage(Node.INVALID_ID, threat.getId(), target.getId());
		assertEquals(2, model.getNodeCount());
		assertEquals(3, model.getCellCount());
		assertEquals(1, model.getLinkageCount());
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat = model.createNode(Node.TYPE_THREAT);
		model.deleteNode(threat);
		Node newThreat = model.createNode(Node.TYPE_THREAT);
		Node target = model.createNode(Node.TYPE_TARGET);
		assertFalse("already linked?", model.hasLinkage(newThreat, target));
		model.createLinkage(Node.INVALID_ID, newThreat.getId(), target.getId());
		assertTrue("not linked?", model.hasLinkage(newThreat, target));
		assertTrue("reverse link not detected?", model.hasLinkage(target, newThreat));
	}
	
	public void testGetLinkages() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node threat1 = model.createNode(Node.TYPE_THREAT);
		Node threat2 = model.createNode(Node.TYPE_THREAT);
		Node target = model.createNode(Node.TYPE_TARGET);
		Linkage linkage1 = model.createLinkage(Node.INVALID_ID, threat1.getId(), target.getId());
		Linkage linkage2 = model.createLinkage(Node.INVALID_ID, threat2.getId(), target.getId());
		Set found = model.getLinkages(target);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}
	
	
	public void testUpdatesNextId() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.createNodeAtId(Node.TYPE_THREAT, 125);
		Node target = model.createNodeAtId(Node.TYPE_TARGET, -1);
		assertEquals("didn't use next available id?", 126, target.getId());
	}
	
	public void testCreateNode() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.createNode(Node.TYPE_TARGET);		
		Node nodeToDelete = model.createNode(Node.TYPE_TARGET);		
		model.createNode(Node.TYPE_TARGET);		
		model.createNode(Node.TYPE_TARGET);		
		model.deleteNode(nodeToDelete);
		model.createNodeAtId(Node.TYPE_TARGET, nodeToDelete.getId()); //simulates an undo
		Node nodeAfterUndo = model.createNode(Node.TYPE_TARGET);
		assertEquals("Id should be 4", 4, nodeAfterUndo.getId());
	}
	
	public void testGetAllNodes() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node node1 = model.createNode(Node.TYPE_TARGET);		
		Node node2 = model.createNode(Node.TYPE_TARGET);		
		model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		Node node3 = model.createNode(Node.TYPE_TARGET);		
		
		Vector nodes = model.getAllNodes();
		assertEquals(3, nodes.size());
		assertContains(node1, nodes);
		assertContains(node2, nodes);
		assertContains(node3, nodes);
	}
	
	public void testGetAllLinkages() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node node1 = model.createNode(Node.TYPE_TARGET);		
		Node node2 = model.createNode(Node.TYPE_TARGET);		
		Linkage link1 = model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		Node node3 = model.createNode(Node.TYPE_TARGET);		
		Linkage link2 = model.createLinkage(Node.INVALID_ID, node1.getId(), node3.getId());
		
		Vector linkages = model.getAllLinkages();
		assertEquals(2, linkages.size());
		assertContains(link1, linkages);
		assertContains(link2, linkages);
	}
	
	public void testGetNodeByIndex() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node node1 = model.createNode(Node.TYPE_TARGET);	
		model.deleteNode(node1);
		Node node2 = model.createNode(Node.TYPE_TARGET);		
		Node node3 = model.createNode(Node.TYPE_TARGET);		
		model.createLinkage(Node.INVALID_ID, node2.getId(), node3.getId());
		Node node4 = model.createNode(Node.TYPE_TARGET);		
		model.createLinkage(Node.INVALID_ID, node3.getId(), node4.getId());

		assertEquals(node2, model.getNodeByIndex(0));
		assertEquals(node3, model.getNodeByIndex(1));
		assertEquals(node4, model.getNodeByIndex(2));
		try 
		{
			model.getNodeByIndex(3);
			fail("Should have thrown an exception for an invalid index");
		} 
		catch (Exception expected) 
		{
		}
	}

	public void testGetLinkageByIndex() throws Exception
	{
		DiagramModel model = new DiagramModel();
		Node node1 = model.createNode(Node.TYPE_TARGET);		
		Node node2 = model.createNode(Node.TYPE_TARGET);		
		Linkage link1 = model.createLinkage(Node.INVALID_ID, node1.getId(), node2.getId());
		Node node3 = model.createNode(Node.TYPE_TARGET);		
		Linkage link2 = model.createLinkage(Node.INVALID_ID, node1.getId(), node3.getId());
		model.deleteLinkage(link1);
		Linkage link3 = model.createLinkage(Node.INVALID_ID, node3.getId(), node1.getId());

		assertEquals(link2, model.getLinkageByIndex(0));
		assertEquals(link3, model.getLinkageByIndex(1));
		try 
		{
			model.getLinkageByIndex(2);
			fail("Should have thrown an exception for an invalid index");
		} 
		catch (Exception expected) 
		{
		}
	}
	
	public void testActionsFiring() throws Exception
	{
		DiagramModel model = new DiagramModel();
		TestTableModel testModel = new TestTableModel();
		testModel.addListener(model);
		Node node1 = model.createNode(Node.TYPE_TARGET);
		assertEquals("test model wasn't notified of add action?", 1, testModel.nodeAdded);
		assertEquals(0, testModel.nodeChanged);
		assertEquals(0, testModel.nodeDeleted);
		assertEquals(0, testModel.linkAdded);
		assertEquals(0, testModel.linkDeleted);
		
		model.updateCell(node1);
		assertEquals(1, testModel.nodeAdded);
		assertEquals(1, testModel.nodeChanged);
		assertEquals("test model fired a delete action for a modify?", 0, testModel.nodeDeleted);
		assertEquals(0, testModel.linkAdded);
		assertEquals(0, testModel.linkDeleted);
		
		model.deleteNode(node1);
		assertEquals(1, testModel.nodeDeleted);
		assertEquals(1, testModel.nodeAdded);
		assertEquals("test model modify action called for delete?", 1, testModel.nodeChanged);
		assertEquals(0, testModel.linkAdded);
		assertEquals(0, testModel.linkDeleted);

		Node node2 = model.createNode(Node.TYPE_TARGET);
		Node node3 = model.createNode(Node.TYPE_TARGET);
		Linkage link1 = model.createLinkage(Node.INVALID_ID, node2.getId(), node3.getId());
		assertEquals(3, testModel.nodeAdded);
		assertEquals(1, testModel.nodeDeleted);
		assertEquals(1, testModel.nodeChanged);
		assertEquals("test model linkageAdded action not called for addition of a link?", 1, testModel.linkAdded);
		assertEquals(0, testModel.linkDeleted);
		model.deleteLinkage(link1);
		assertEquals(3, testModel.nodeAdded);
		assertEquals(1, testModel.nodeDeleted);
		assertEquals(1, testModel.nodeChanged);
		assertEquals(1, testModel.linkAdded);
		assertEquals("test model linkagaDeleted action not called?",1, testModel.linkDeleted);
		
		
		
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
	}

	
}
