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
package org.miradi.diagram.cells;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.jgraph.graph.GraphConstants;
import org.miradi.ids.IdAssigner;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;

public class TestDiagramFactor extends EAMTestCase
{
	public TestDiagramFactor(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		idAssigner = new IdAssigner();

		ORef rawTargetRef = project.createObject(ObjectType.TARGET);
		cmTarget = (Target) Factor.findFactor(project, rawTargetRef);
		
		intervention = project.createFactorCell(ObjectType.STRATEGY);
		indirectFactor = project.createFactorCell(ObjectType.CAUSE);
		directThreat  = project.createFactorCell(ObjectType.CAUSE);
		target = project.createFactorCell(ObjectType.TARGET);
		targetAttributeMap = target.getAttributes();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}

	public void testPort()
	{
		assertEquals("port not first child?", target.getPort(), target.getFirstChild());
	}
	
	public void testObjectives()
	{
		assertTrue("direct threat can have objectives?", directThreat.canHaveObjectives());
		assertTrue("indirect threat can have objectives?", indirectFactor.canHaveObjectives());
		assertTrue(intervention.canHaveObjectives());
		assertFalse(target.canHaveObjectives());
	}

	public void testIndicator() throws Exception
	{
		EAM.setMainWindow(new MainWindow(project));
		IdList indicators = directThreat.getIndicators();
		assertEquals(0, indicators.size());
		EAM.setMainWindow(null);
	}
	
	public void testGoals()
	{
		assertTrue(target.canHaveGoal());
		assertFalse(directThreat.canHaveGoal());
		assertFalse(indirectFactor.canHaveGoal());
		assertFalse(intervention.canHaveGoal());
	}
	
	
	public void testBounds()
	{
		target.setLocation(new Point(123, 456));
		Rectangle2D bounds = GraphConstants.getBounds(targetAttributeMap);
		assertEquals("wrong x?", 123.0, bounds.getX(), TOLERANCE);
		assertEquals("wrong y?", 456.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong width", 120.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong height", 60.0, bounds.getHeight(), TOLERANCE);
	}
	
	public void testSize()
	{
		//Note: the expceted sizes are expected to be in even incr of the DefaultGridSize.
		//TODO: test code shuold be converted to use the DefaultGridSize
		target.setLocation(new Point(3, 4));
		target.setSize(new Dimension(300, 200));
		Rectangle2D bounds = GraphConstants.getBounds(targetAttributeMap);
		assertEquals("wrong x?", 3.0, bounds.getX(), TOLERANCE);
		assertEquals("wrong y?", 4.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong width", 300.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong height", 210.0, bounds.getHeight(), TOLERANCE);
		target.setSize(new Dimension(100, 50));
		bounds = GraphConstants.getBounds(targetAttributeMap);
		assertEquals("x changed?", 3.0, bounds.getX(), TOLERANCE);
		assertEquals("y changed?", 4.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong new width", 90.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong new height", 60.0, bounds.getHeight(), TOLERANCE);
		assertEquals("node size width incorrect?", 90.0, target.getSize().getWidth(), TOLERANCE);
		assertEquals("node size height incorrect?", 60.0, target.getSize().getHeight(), TOLERANCE);
	}
	
	public void testJson() throws Exception
	{
		FactorCell factorCell = project.createFactorCell(ObjectType.CAUSE);
		DiagramFactor diagramFactor = factorCell.getDiagramFactor();
		diagramFactor.setLocation(new Point(100, 200));
		diagramFactor.setSize(new Dimension(50, 75));
		
		DiagramFactor diagramFactor2 = new DiagramFactor(project.getObjectManager(), diagramFactor.getDiagramFactorId().asInt(), diagramFactor.toJson());
		
		assertEquals("location", diagramFactor.getLocation(), diagramFactor2.getLocation());
		assertEquals("size", diagramFactor.getSize(), diagramFactor2.getSize());
		assertEquals("id", diagramFactor.getDiagramFactorId(), diagramFactor2.getDiagramFactorId());
		assertEquals("wrapped id", diagramFactor.getWrappedId(), diagramFactor2.getWrappedId());
	}

	static final double TOLERANCE = 0.00;
	
	ProjectForTesting project;
	IdAssigner idAssigner;
	Target cmTarget;
	FactorCell intervention;
	FactorCell indirectFactor;
	FactorCell directThreat;
	FactorCell target;
	Map targetAttributeMap;
}
