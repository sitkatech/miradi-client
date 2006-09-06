/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestProjectScopeBox extends EAMTestCase
{
	public TestProjectScopeBox(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = project.getDiagramModel();
		nodePool = model.getNodePool();
		idAssigner = new IdAssigner();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}

	public void testGetBounds() throws Exception
	{
		model.clear();
		ProjectScopeBox scope = model.getProjectScopeBox();
		Rectangle2D noTargets = scope.getBounds();
		Rectangle allZeros = new Rectangle(0,0,0,0);
		assertEquals("not all zeros to start?", allZeros, noTargets);
		
		createNode(DiagramNode.TYPE_FACTOR);
		Rectangle2D oneNonTarget = scope.getBounds();
		assertEquals("not all zeros with one non-target?", allZeros, oneNonTarget);

		DiagramNode target1 = createNode(DiagramNode.TYPE_TARGET);
		model.getProjectScopeBox().setVision("Sample Vision");
		Dimension targetSize = target1.getSize();
		Rectangle2D oneTarget = scope.getBounds();
		assertTrue("didn't surround target?", oneTarget.contains(target1.getBounds()));

		model.moveNodes(100, 100, new BaseId[] {target1.getDiagramNodeId()});
		Rectangle2D movedTarget = scope.getBounds();
		assertTrue("didn't follow move?", movedTarget.contains(target1.getBounds()));
		assertNotEquals("still at x zero?", 0, (int)movedTarget.getX());
		assertNotEquals("still at y zero?", 0, (int)movedTarget.getY());
		assertEquals("affected target?", targetSize, target1.getSize());
		
		DiagramNode target2 = createNode(DiagramNode.TYPE_TARGET);
		model.moveNodes(200, 200, new BaseId[] {target2.getDiagramNodeId()});
		model.updateCell(target2);
		Rectangle2D twoTargets = scope.getBounds();
		assertTrue("didn't surround target1?", twoTargets.contains(target1.getBounds()));
		assertTrue("didn't surround target2?", twoTargets.contains(target2.getBounds()));
	}

	private DiagramNode createNode(NodeType nodeType) throws Exception
	{
		ConceptualModelNode cmObject = ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), nodeType.toString());
		nodePool.put(cmObject);
		return model.createNode(cmObject.getModelNodeId());
	}

	ProjectForTesting project;
	NodePool nodePool;
	DiagramModel model;
	IdAssigner idAssigner;
}
