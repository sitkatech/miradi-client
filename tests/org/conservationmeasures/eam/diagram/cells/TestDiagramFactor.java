/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.jgraph.graph.GraphConstants;

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

		Cause cmDirectThreat = new Cause(takeNextModelNodeId());
		cmDirectThreat.increaseTargetCount();
		CreateFactorParameter createTarget = new CreateFactorParameter(new FactorTypeTarget());
		BaseId rawTargetId = project.createObject(ObjectType.FACTOR, BaseId.INVALID, createTarget);
		FactorId cmTargetId = new FactorId(rawTargetId.asInt());
		cmTarget = (Target)project.findNode(cmTargetId);
		
		intervention = project.createFactorCell(Factor.TYPE_STRATEGY);
		indirectFactor = project.createFactorCell(Factor.TYPE_CAUSE);
		directThreat  = project.createFactorCell(Factor.TYPE_CAUSE);
		target = project.createFactorCell(Factor.TYPE_TARGET);
		targetAttributeMap = target.getAttributes();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
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

	public void testIndicator()
	{
		IdList indicators = directThreat.getIndicators();
		assertEquals(0, indicators.size());
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
		target.setLocation(new Point(3, 4));
		target.setSize(new Dimension(300, 200));
		Rectangle2D bounds = GraphConstants.getBounds(targetAttributeMap);
		assertEquals("wrong x?", 3.0, bounds.getX(), TOLERANCE);
		assertEquals("wrong y?", 4.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong width", 300.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong height", 200.0, bounds.getHeight(), TOLERANCE);
		target.setSize(new Dimension(100, 50));
		bounds = GraphConstants.getBounds(targetAttributeMap);
		assertEquals("x changed?", 3.0, bounds.getX(), TOLERANCE);
		assertEquals("y changed?", 4.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong new width", 100.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong new height", 50.0, bounds.getHeight(), TOLERANCE);
		assertEquals("node size width incorrect?", 100.0, target.getSize().getWidth(), TOLERANCE);
		assertEquals("node size height incorrect?", 50.0, target.getSize().getHeight(), TOLERANCE);
	}
	
	//TODO diagramFactor conversion - remove commented code since DiagramFactor is cleared automatically
	//public void testBuildCommandsToClear() throws Exception
	//{
	//	Command[] commands = target.buildCommandsToClear();
	//	assertEquals(3, commands.length);
	//	int next = 0;
	//	assertEquals(CommandSetFactorSize.COMMAND_NAME, commands[next++].getCommandName());
	//	assertEquals(CommandDiagramMove.COMMAND_NAME, commands[next++].getCommandName());
	//	assertEquals(CommandSetObjectData.COMMAND_NAME, commands[next++].getCommandName());
	//}
	
	public void testJson() throws Exception
	{
		FactorCell factorCell = project.createFactorCell(Factor.TYPE_CAUSE);
		DiagramFactor diagramFactor = factorCell.getDiagramFactor();
		diagramFactor.setLocation(new Point(100, 200));
		diagramFactor.setSize(new Dimension(50, 75));
		
		DiagramFactor diagramFactor2 = new DiagramFactor(diagramFactor.getDiagramFactorId().asInt(), diagramFactor.toJson());
		
		assertEquals("location", diagramFactor.getLocation(), diagramFactor2.getLocation());
		assertEquals("size", diagramFactor.getSize(), diagramFactor2.getSize());
		assertEquals("id", diagramFactor.getDiagramFactorId(), diagramFactor2.getDiagramFactorId());
		assertEquals("wrapped id", diagramFactor.getWrappedId(), diagramFactor2.getWrappedId());
	}

	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
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
