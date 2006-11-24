/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.FactorType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
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
	
	public void testGetNodesInChain() throws Exception
	{
		int[] linkagePairs = getLinkagePairs();
		SampleDiagramBuilder.buildNodeGrid(project, 7, linkagePairs);
		int[][] expectedNodesInChain = getExpectedNodesInChain();
		
		for(int threatIndex = 0; threatIndex < expectedNodesInChain.length; ++threatIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInChain[threatIndex];
			BaseId threatId = new BaseId(expectedChainNodeIds[0]);
			Factor cmNode = (Factor)project.findObject(ObjectType.MODEL_NODE, threatId);
			
			ConceptualModelNodeSet gotChainNodes = model.getNodesInChain(cmNode);
			
			assertEquals("wrong direct threat chain nodes for " + threatId + "?", findNodes(expectedChainNodeIds), gotChainNodes);
		}
	}

	private int[][] getExpectedNodesInChain()
	{
		int[][] expectedNodesInChain = {
				{ 31,  11, 12, 13, 21, 22, 41 },
				{ 32,  13, 23, 41 },
				{ 33,  13, 23, 42, 43 },
				{ 34,  24, 25, 26, 35, 36, 44 },
				{ 35,  25, 26, 34, 36, 45 },
				// 36 is actually an INDIRECT FACTOR!
		};
		return expectedNodesInChain;
		
	}
	
	private int[] getLinkagePairs()
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
		return linkagePairs;
	}
	
	public void testGetChainIds() throws Exception
	{
		int[] linkagePairs = getLinkagePairs();
		SampleDiagramBuilder.buildNodeGrid(project, 7, linkagePairs);
		int[][] expectedNodesInChain = getExpectedNodesInChain();
				
		for(int threatIndex = 0; threatIndex < expectedNodesInChain.length; ++threatIndex)
		{
			int[] expectedChainNodeIds = expectedNodesInChain[threatIndex];
			BaseId threatId = new BaseId(expectedChainNodeIds[0]);
			Factor cmNode = (Factor)project.findObject(ObjectType.MODEL_NODE, threatId);
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
			Factor cmNode = (Factor)project.findObject(ObjectType.MODEL_NODE, threatId);
			ConceptualModelNodeSet gotChainNodes = model.getAllUpstreamDownstreamNodes(cmNode);
			assertEquals("wrong chain nodes for " + threatId + "?", findNodes(expectedChainNodeIds), gotChainNodes);
		}
	}
	
	public ConceptualModelNodeSet findNodes(int[] values)
	{
		ConceptualModelNodeSet result = new ConceptualModelNodeSet();
		for(int i = 0; i < values.length; ++i)
			result.attemptToAdd(project.findNode(new ModelNodeId(values[i])));
		return result;
	}

	public void testIsNode() throws Exception
	{
		DiagramNode factor = createNode(Factor.TYPE_CAUSE);
		DiagramNode target = createNode(Factor.TYPE_TARGET);
		DiagramFactorLink link = createLinkage(new ModelLinkageId(BaseId.INVALID.asInt()), factor.getWrappedId(), target.getWrappedId());
		assertTrue("factor isn't a node?", factor.isNode());
		assertTrue("target isn't a node?", target.isNode());
		assertFalse("link is a node?", link.isNode());
	}
	
	public void testCounts()throws Exception
	{
		DiagramNode factor = createNode(Factor.TYPE_CAUSE);
		DiagramNode target = createNode(Factor.TYPE_TARGET);
		createLinkage(new ModelLinkageId(BaseId.INVALID.asInt()), factor.getWrappedId(), target.getWrappedId());
		assertEquals(2, model.getNodeCount());
		assertEquals(1, model.getLinkageCount());
	}
	
	public void testHasLinkage() throws Exception
	{
		DiagramNode factor = createNode(Factor.TYPE_CAUSE);
		model.deleteNode(factor);
		DiagramNode newFactor = createNode(Factor.TYPE_CAUSE);
		DiagramNode target = createNode(Factor.TYPE_TARGET);
		assertFalse("already linked?", model.hasLinkage(newFactor, target));
		createLinkage(new ModelLinkageId(BaseId.INVALID.asInt()), newFactor.getWrappedId(), target.getWrappedId());
		assertTrue("not linked?", model.hasLinkage(newFactor, target));
		assertTrue("reverse link not detected?", model.hasLinkage(target, newFactor));
	}
	
	public void testGetLinkages() throws Exception
	{
		DiagramNode factor1 = createNode(Factor.TYPE_CAUSE);
		DiagramNode factor2 = createNode(Factor.TYPE_CAUSE);
		DiagramNode target = createNode(Factor.TYPE_TARGET);
		DiagramFactorLink linkage1 = createLinkage(takeNextLinkageId(), factor1.getWrappedId(), target.getWrappedId());
		DiagramFactorLink linkage2 = createLinkage(takeNextLinkageId(), factor2.getWrappedId(), target.getWrappedId());
		Set found = model.getLinkages(target);
		assertEquals("Didn't see both links?", 2, found.size());
		assertTrue("missed first?", found.contains(linkage1));
		assertTrue("missed second?", found.contains(linkage2));
	}

	private ModelLinkageId takeNextLinkageId()
	{
		return new ModelLinkageId(idAssigner.takeNextId().asInt());
	}
	
	
	public void testCreateNode() throws Exception
	{
		createTarget();		
		DiagramNode nodeToDelete = createTarget();
		createTarget();		
		DiagramNode lastCreated = createTarget();		
		model.deleteNode(nodeToDelete);
		Target cmTargetToUndo = new Target(nodeToDelete.getWrappedId());
		
		model.createNode(cmTargetToUndo.getModelNodeId()); //simulates an undo
		DiagramNode nodeAfterUndo = createNode(Factor.TYPE_TARGET);
		assertTrue("reused an id?", nodeAfterUndo.getDiagramNodeId().asInt() > lastCreated.getDiagramNodeId().asInt());
	}

	public void testGetAllNodes() throws Exception
	{
		DiagramNode node1 = createNode(Factor.TYPE_TARGET);		
		DiagramNode node2 = createNode(Factor.TYPE_TARGET);		
		createLinkage(new ModelLinkageId(BaseId.INVALID.asInt()), node1.getWrappedId(), node2.getWrappedId());
		DiagramNode node3 = createNode(Factor.TYPE_TARGET);		
		
		Vector nodes = model.getAllNodes();
		assertEquals(3, nodes.size());
		assertContains(node1, nodes);
		assertContains(node2, nodes);
		assertContains(node3, nodes);
	}
	
	public void testGetAllLinkages() throws Exception
	{
		DiagramNode node1 = createNode(Factor.TYPE_TARGET);		
		DiagramNode node2 = createNode(Factor.TYPE_TARGET);		
		DiagramFactorLink link1 = createLinkage(takeNextLinkageId(), node1.getWrappedId(), node2.getWrappedId());
		DiagramNode node3 = createNode(Factor.TYPE_TARGET);		
		DiagramFactorLink link2 = createLinkage(takeNextLinkageId(), node1.getWrappedId(), node3.getWrappedId());
		
		Vector linkages = model.getAllLinkages();
		assertEquals(2, linkages.size());
		assertContains(link1, linkages);
		assertContains(link2, linkages);
	}
	
	public void testFillFrom() throws Exception
	{
		DiagramNode node1 = createNode(Factor.TYPE_TARGET);		
		DiagramNode node2 = createNode(Factor.TYPE_TARGET);		
		DiagramFactorLink link1 = createLinkage(takeNextLinkageId(), node1.getWrappedId(), node2.getWrappedId());

		DiagramModel copy = new DiagramModel(project);
		copy.fillFrom(model.toJson());
		
		assertNotNull("missing node1?", copy.getNodeById(node1.getDiagramNodeId()));
		assertNotNull("missing node2?", copy.getNodeById(node2.getDiagramNodeId()));
		assertNotNull("missing link?", copy.getLinkageById(link1.getDiagramLinkageId()));
	}
	
	public void testActionsFiring() throws Exception
	{
		TestTableModel testModel = new TestTableModel();
		testModel.addListener(model);
		DiagramNode node1 = createNode(Factor.TYPE_TARGET);
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

		DiagramNode node2 = createNode(Factor.TYPE_TARGET);
		DiagramNode node3 = createNode(Factor.TYPE_TARGET);
		DiagramFactorLink link1 = createLinkage(new ModelLinkageId(BaseId.INVALID.asInt()), node2.getWrappedId(), node3.getWrappedId());
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
		assertEquals("test model linkDeleted action not called?",1, testModel.linkDeleted);
		
		model.nodesWereMoved(new DiagramNodeId[] {node2.getDiagramNodeId(), node3.getDiagramNodeId()});
		assertEquals("move nodes did a node add notify?", 3, testModel.nodeAdded);
		assertEquals("move nodes did a node delete notify?", 1, testModel.nodeDeleted);
		assertEquals("move nodes did a node change notify?", 1, testModel.nodeChanged);
		assertEquals("node move didn't do all node move notify's?", 2, testModel.nodeMoved);
		assertEquals("move nodes did a link add notify?", 1, testModel.linkAdded);
		assertEquals("move nodes did a link delete notify?", 1, testModel.linkDeleted);
		
	}
	
	private DiagramNode createTarget() throws Exception
	{
		Factor cmTarget = new Target(takeNextModelNodeId());
		project.getNodePool().put(cmTarget);
		return model.createNode(cmTarget.getModelNodeId());
	}
	
	private DiagramNode createNode(FactorType nodeType) throws Exception
	{
		ModelNodeId id = takeNextModelNodeId();
		CreateModelNodeParameter parameter = new CreateModelNodeParameter(nodeType);
		Factor cmObject = Factor.createConceptualModelObject(id, parameter);
		project.getNodePool().put(cmObject);
		return model.createNode(cmObject.getModelNodeId());
	}
	
	private DiagramFactorLink createLinkage(ModelLinkageId id, ModelNodeId fromId, ModelNodeId toId) throws Exception
	{
		FactorLink cmLinkage = new FactorLink(id, fromId, toId);
		model.getLinkagePool().put(cmLinkage);
		return model.createLinkage(cmLinkage);
	}


	private ModelNodeId takeNextModelNodeId()
	{
		return new ModelNodeId(idAssigner.takeNextId().asInt());
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
