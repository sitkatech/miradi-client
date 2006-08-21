/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;
import org.json.JSONObject;

public class TestDiagramNode extends EAMTestCase
{
	public TestDiagramNode(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention(idAssigner.takeNextId());
		ConceptualModelFactor cmIndirectFactor = new ConceptualModelFactor(idAssigner.takeNextId(), DiagramNode.TYPE_INDIRECT_FACTOR);
		ConceptualModelFactor cmDirectThreat = new ConceptualModelFactor(idAssigner.takeNextId(), DiagramNode.TYPE_DIRECT_THREAT);
		cmTarget = new ConceptualModelTarget(idAssigner.takeNextId());
		
		intervention = DiagramNode.wrapConceptualModelObject(cmIntervention);
		indirectFactor = DiagramNode.wrapConceptualModelObject(cmIndirectFactor);
		directThreat = DiagramNode.wrapConceptualModelObject(cmDirectThreat);
		target = DiagramNode.wrapConceptualModelObject(cmTarget);
		targetAttributeMap = target.getAttributes();
		super.setUp();
	}
	
	public void testPort()
	{
		assertEquals("port not first child?", target.getPort(), target.getFirstChild());
	}
	
	public void testPriorities()
	{
		assertEquals("Lost priority?", null, directThreat.getThreatRating());
		assertTrue(directThreat.canHaveThreatRating());
		assertTrue(indirectFactor.canHaveThreatRating());
		assertFalse(intervention.canHaveThreatRating());
		assertFalse(target.canHaveThreatRating());
	}
	
	public void testObjectives()
	{
		assertTrue(directThreat.canHaveObjectives());
		assertTrue(indirectFactor.canHaveObjectives());
		assertTrue(intervention.canHaveObjectives());
		assertFalse(target.canHaveObjectives());
	}

	public void testIndicator()
	{
		BaseId indicator = directThreat.getIndicatorId();
		assertEquals(BaseId.INVALID, indicator);
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

	public void testFont()
	{
		Font nodeFont = GraphConstants.getFont(targetAttributeMap);
		assertTrue("not bold?", nodeFont.isBold());
	}
	
	public void testBuildCommandsToClear() throws Exception
	{
		Command[] commands = target.buildCommandsToClear();
		assertEquals(3, commands.length);
		int next = 0;
		assertEquals(CommandSetNodeSize.COMMAND_NAME, commands[next++].getCommandName());
		assertEquals(CommandDiagramMove.COMMAND_NAME, commands[next++].getCommandName());
		assertEquals(CommandSetObjectData.COMMAND_NAME, commands[next++].getCommandName());
	}
	
	public void testJson() throws Exception
	{
		target.setLocation(new Point(100, 200));
		target.setSize(new Dimension(50, 75));
		
		DiagramNode got = new DiagramTarget(cmTarget);
		JSONObject json = target.toJson();
		got.fillFrom(json);
		
		assertEquals("location", target.getLocation(), got.getLocation());
		assertEquals("size", target.getSize(), got.getSize());
	}

	static final double TOLERANCE = 0.00;
	
	ConceptualModelTarget cmTarget;
	DiagramNode intervention;
	DiagramNode indirectFactor;
	DiagramNode directThreat;
	DiagramNode target;
	Map targetAttributeMap;
}
