/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.diagram;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.ProjectScopeBox;
import org.miradi.ids.IdAssigner;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORefList;
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

		target1.getDiagramFactor().setLocation(new Point(100, 200));
		target1.updateFromDiagramFactor();
		
		model.factorsWereMoved(new ORefList(target1.getDiagramFactorRef()));
		Rectangle2D movedTarget = scope.getBounds();
		assertTrue("didn't follow move? " + movedTarget + " doesn't contain " + target1.getBounds(), movedTarget.contains(target1.getBounds()));
		assertNotEquals("still at x zero?", 0, (int)movedTarget.getX());
		assertNotEquals("still at y zero?", 0, (int)movedTarget.getY());
		assertEquals("affected target?", targetSize, target1.getSize());
		
		FactorCell target2 = project.createFactorCell(ObjectType.TARGET);
		target2.getDiagramFactor().setLocation(new Point(200, 300));
		target2.updateFromDiagramFactor();
		model.factorsWereMoved(new ORefList(target2.getDiagramFactorRef()));
		Rectangle2D twoTargets = scope.getBounds();
		assertTrue("didn't surround target1?", twoTargets.contains(target1.getBounds()));
		assertTrue("didn't surround target2?", twoTargets.contains(target2.getBounds()));
	}

	ProjectForTesting project;
	DiagramModel model;
	IdAssigner idAssigner;
}
;