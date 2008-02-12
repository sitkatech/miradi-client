/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.ProjectScopeBox;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.ProjectForTesting;

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
		idAssigner = new IdAssigner();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}

	public void testGetBounds() throws Exception
	{
		ProjectScopeBox scope = model.getProjectScopeBox();
		Rectangle2D noTargets = scope.getBounds();
		Rectangle allZeros = new Rectangle(0,0,0,0);
		assertEquals("not all zeros to start?", allZeros, noTargets);
		
		project.createFactorCell(ObjectType.CAUSE);
		Rectangle2D oneNonTarget = scope.getBounds();
		assertEquals("not all zeros with one non-target?", allZeros, oneNonTarget);

		FactorCell target1 = project.createFactorCell(ObjectType.TARGET);
		project.setMetadata(ProjectMetadata.TAG_PROJECT_VISION, "Sample Vision");
		Dimension targetSize = target1.getSize();
		Rectangle2D oneTarget = scope.getBounds();
		assertTrue("didn't surround target?", oneTarget.contains(target1.getBounds()));

		model.moveFactors(100, 100, new DiagramFactorId[] {target1.getDiagramFactorId()});
		Rectangle2D movedTarget = scope.getBounds();
		assertTrue("didn't follow move?", movedTarget.contains(target1.getBounds()));
		assertNotEquals("still at x zero?", 0, (int)movedTarget.getX());
		assertNotEquals("still at y zero?", 0, (int)movedTarget.getY());
		assertEquals("affected target?", targetSize, target1.getSize());
		
		FactorCell target2 = project.createFactorCell(ObjectType.TARGET);
		model.moveFactors(200, 200, new DiagramFactorId[] {target2.getDiagramFactorId()});
		model.updateCell(target2);
		Rectangle2D twoTargets = scope.getBounds();
		assertTrue("didn't surround target1?", twoTargets.contains(target1.getBounds()));
		assertTrue("didn't surround target2?", twoTargets.contains(target2.getBounds()));
	}

	ProjectForTesting project;
	DiagramModel model;
	IdAssigner idAssigner;
}
;