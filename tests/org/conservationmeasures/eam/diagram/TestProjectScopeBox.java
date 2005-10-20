/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestProjectScopeBox extends EAMTestCase
{
	public TestProjectScopeBox(String name)
	{
		super(name);
	}

	public void testGetBounds() throws Exception
	{
		DiagramModel model = new DiagramModel();
		model.clear();
		ProjectScopeBox scope = model.getProjectScopeBox();
		Rectangle2D noTargets = scope.getBounds();
		Rectangle allZeros = new Rectangle(0,0,0,0);
		assertEquals("not all zeros to start?", allZeros, noTargets);
		
		model.createNode(DiagramNode.TYPE_DIRECT_THREAT);
		Rectangle2D oneNonTarget = scope.getBounds();
		assertEquals("not all zeros with one non-target?", allZeros, oneNonTarget);

		DiagramNode target1 = model.createNode(DiagramNode.TYPE_TARGET);
		Rectangle2D oneTarget = scope.getBounds();
		assertTrue("didn't surround target?", oneTarget.contains(target1.getBounds()));

		model.moveNodes(100, 100, new int[] {target1.getId()});
		Rectangle2D movedTarget = scope.getBounds();
		assertTrue("didn't follow move?", movedTarget.contains(target1.getBounds()));
		assertNotEquals("still at x zero?", 0, (int)movedTarget.getX());
		assertNotEquals("still at y zero?", 0, (int)movedTarget.getY());
		
		DiagramNode target2 = model.createNode(DiagramNode.TYPE_TARGET);
		model.moveNodes(200, 200, new int[] {target2.getId()});
		model.updateCell(target2);
		Rectangle2D twoTargets = scope.getBounds();
		assertTrue("didn't surround target1?", twoTargets.contains(target1.getBounds()));
		assertTrue("didn't surround target2?", twoTargets.contains(target2.getBounds()));
	}
}
