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
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramModel extends EAMTestCase
{
	public TestDiagramModel(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = project.getDiagramModel();
		idAssigner = new IdAssigner();
	}

	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testGetChainIds() throws Exception
	{
		int[] linkagePairs = {
				11, 21,		21, 31, 	31, 41,
				12, 21, 	22, 31, 	32, 41,
				13, 22, 	23, 32, 	33, 42,
				13, 23, 	23, 33, 	33, 43,
				24, 34, 	34, 44,
				25, 35, 	35, 45,
				26, 36, 	36, 35, 	35, 34,
		};
		
		SampleDiagramBuilder.buildNodeGrid(project, 7, linkagePairs);
		
		int[][] expectedNodesInChain = {
				{ 31,  11, 12, 13, 21, 22, 41 },
				{ 32,  13, 23, 41 },
				{ 33,  13, 23, 42, 43 },
				{ 34,  24, 25, 26, 35, 36, 44 },
				{ 35,  25, 26, 34, 36, 45 },
				{ 36,  26, 35 },
		};
		for(int threatIndex = 0; threatIndex < expectedNodesInChain.length; ++threatIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInChain[threatIndex];
			BaseId threatId = new BaseId(expectedChainNodeIds[0]);
			ConceptualModelNode cmNode = (ConceptualModelNode)project.findObject(ObjectType.MODEL_NODE, threatId);
			ConceptualModelNodeSet gotChainNodes = model.getDirectThreatChainNodes(cmNode);
			assertEquals("wrong direct threat chain nodes for " + threatId + "?", findNodes(expectedChainNodeIds), gotChainNodes);
		}

		int[][] expectedNodesInFullChain = {
				{ 37 },
				{ 11, 21, 31, 41 },
				{ 13, 22, 23, 31, 32, 33, 41, 42, 43 },
				{ 21, 11, 12, 31, 41 },
				{ 23, 13, 32, 33, 41, 42, 43 },
				{ 41, 11, 12, 13, 21, 22, 23, 31, 32 },
		};

		for(int nodeIndex = 0; nodeIndex < expectedNodesInFullChain.length; ++nodeIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInFullChain[nodeIndex];
			BaseId threatId = new BaseId(expectedChainNodeIds[0]);
			ConceptualModelNode cmNode = (ConceptualModelNode)project.findObject(ObjectType.MODEL_NODE, threatId);
			ConceptualModelNodeSet gotChainNodes = model.getAllNodesInChain(cmNode);
			assertEquals("wrong chain nodes for " + threatId + "?", findNodes(expectedChainNodeIds), gotChainNodes);
		}
	}
	
	public ConceptualModelNodeSet findNodes(int[] values)
	{
		ConceptualModelNodeSet result = new ConceptualModelNodeSet();
		for(int i = 0; i < values.length; ++i)
			result.attemptToAdd(project.findNode(new BaseId(values[i])));
		return result;
	}

	public void testIsNode() throws Exception
	{
		DiagramNode factor = createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = createNode(DiagramNode.TYPE_TARGET);
		DiagramLinkage link = createLinkage(BaseId.INVALID, factor.getDiagramNodeId(), target.getDiagramNodeId());
		assertTrue("factor isn't a node?", factor.isNode());
		assertTrue("target isn't a node?", target.isNode());
		assertFalse("linkage is a node?", link.isNode());
	}
	
	public void testCounts()throws Exception
	{
		DiagramNode factor = createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = createNode(DiagramNode.TYPE_TARGET);
		createLinkage(BaseId.INVALID, factor.getDiagramNodeId(), target.getDiagramNodeId());
		assertEquals(2, model.getNodeCount());
		assertEquals(1, model.getLinkageCount());
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramNode factor = createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		model.deleteNode(factor);
		DiagramNode newFactor = createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = createNode(DiagramNode.TYPE_TARGET);
		assertFalse("already linked?", model.hasLinkage(newFactor, target));
		createLinkage(BaseId.INVALID, newFactor.getDiagramNodeId(), target.getDiagramNodeId());
		assertTrue("not linked?", model.hasLinkage(newFactor, target));
		assertTrue("reverse link not detected?", model.hasLinkage(target, newFactor));
	}
	
	public void testGetLinkages() throws Exception
	{
		DiagramNode factor1 = createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode factor2 = createNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		DiagramNode target = createNode(DiagramNode.TYPE_TARGET);
		DiagramLinkage linkage1 = createLinkage(idAssigner.takeNextId(), factor1.getDiagramNodeId(), target.getDiagramNodeId());
		DiagramLinkage linkage2 = createLinkage(idAssigner.takeNextId(), factor2.getDiagramNodeId(), target.getDiagramNodeId());
		Set found = model.getLinkages(target);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}
	
	
	public void testCreateNode() throws Exception
	{
		createTarget();		
		DiagramNode nodeToDelete = createTarget();
		createTarget();		
		DiagramNode lastCreated = createTarget();		
		model.deleteNode(nodeToDelete);
		ConceptualModelTarget cmTargetToUndo = new ConceptualModelTarget(nodeToDelete.getDiagramNodeId());
		project.getNodePool().put(cmTargetToUndo);
		
		model.createNode(cmTargetToUndo.getId()); //simulates an undo
		DiagramNode nodeAfterUndo = createNode(DiagramNode.TYPE_TARGET);
		assertTrue("reused an id?", nodeAfterUndo.getDiagramNodeId().asInt() > lastCreated.getDiagramNodeId().asInt());
	}

	public void testGetAllNodes() throws Exception
	{
		DiagramNode node1 = createNode(DiagramNode.TYPE_TARGET);		
		DiagramNode node2 = createNode(DiagramNode.TYPE_TARGET);		
		createLinkage(BaseId.INVALID, node1.getDiagramNodeId(), node2.getDiagramNodeId());
		DiagramNode node3 = createNode(DiagramNode.TYPE_TARGET);		
		
		Vector nodes = model.getAllNodes();
		assertEquals(3, nodes.size());
		assertContains(node1, nodes);
		assertContains(node2, nodes);
		assertContains(node3, nodes);
	}
	
	public void testGetAllLinkages() throws Exception
	{
		DiagramNode node1 = createNode(DiagramNode.TYPE_TARGET);		
		DiagramNode node2 = createNode(DiagramNode.TYPE_TARGET);		
		DiagramLinkage link1 = createLinkage(idAssigner.takeNextId(), node1.getDiagramNodeId(), node2.getDiagramNodeId());
		DiagramNode node3 = createNode(DiagramNode.TYPE_TARGET);		
		DiagramLinkage link2 = createLinkage(idAssigner.takeNextId(), node1.getDiagramNodeId(), node3.getDiagramNodeId());
		
		Vector linkages = model.getAllLinkages();
		assertEquals(2, linkages.size());
		assertContains(link1, linkages);
		assertContains(link2, linkages);
	}
	
	public void testFillFrom() throws Exception
	{
		DiagramNode node1 = createNode(DiagramNode.TYPE_TARGET);		
		DiagramNode node2 = createNode(DiagramNode.TYPE_TARGET);		
		DiagramLinkage link1 = createLinkage(idAssigner.takeNextId(), node1.getDiagramNodeId(), node2.getDiagramNodeId());

		DiagramModel copy = new DiagramModel(project);
		copy.fillFrom(model.toJson());
		
		assertNotNull("missing node1?", copy.getNodeById(node1.getDiagramNodeId()));
		assertNotNull("missing node2?", copy.getNodeById(node2.getDiagramNodeId()));
		assertNotNull("missing linkage?", copy.getLinkageById(link1.getDiagramLinkageId()));
	}
	
	public void testActionsFiring() throws Exception
	{
		TestTableModel testModel = new TestTableModel();
		testModel.addListener(model);
		DiagramNode node1 = createNode(DiagramNode.TYPE_TARGET);
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

		DiagramNode node2 = createNode(DiagramNode.TYPE_TARGET);
		DiagramNode node3 = createNode(DiagramNode.TYPE_TARGET);
		DiagramLinkage link1 = createLinkage(BaseId.INVALID, node2.getDiagramNodeId(), node3.getDiagramNodeId());
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
		
		model.nodesWereMoved(new BaseId[] {node2.getDiagramNodeId(), node3.getDiagramNodeId()});
		assertEquals("move nodes did a node add notify?", 3, testModel.nodeAdded);
		assertEquals("move nodes did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("move nodes did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("node move didn't do all node move notify's?", 2, testModel.nodeMoved);
		assertEquals("move nodes did a link add notify?", 1, testModel.linkAdded);
		assertEquals("move nodes did a link delete notify?", 1, testModel.linkDeleted);
		
	}
	
	private DiagramNode createTarget() throws Exception
	{
		ConceptualModelNode cmTarget = new ConceptualModelTarget(idAssigner.takeNextId());
		project.getNodePool().put(cmTarget);
		return model.createNode(cmTarget.getId());
	}
	
	private DiagramNode createNode(NodeType nodeType) throws Exception
	{
		ConceptualModelNode cmObject = ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), nodeType);
		project.getNodePool().put(cmObject);
		return model.createNode(cmObject.getId());
	}
	
	private DiagramLinkage createLinkage(BaseId id, BaseId fromId, BaseId toId) throws Exception
	{
		ConceptualModelLinkage cmLinkage = new ConceptualModelLinkage(id, fromId, toId);
		model.getLinkagePool().put(cmLinkage);
		return model.createLinkage(cmLinkage);
	}


	static class TestTableModel implements DiagramModelListener
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

	ProjectForTesting project;
	DiagramModel model;
	IdAssigner idAssigner;
}
