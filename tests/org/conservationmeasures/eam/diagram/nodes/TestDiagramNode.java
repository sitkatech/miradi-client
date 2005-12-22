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

import org.conservationmeasures.eam.annotations.Indicator;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestDiagramNode extends EAMTestCase
{
	public TestDiagramNode(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention();
		ConceptualModelFactor cmIndirectFactor = new ConceptualModelFactor(DiagramNode.TYPE_INDIRECT_FACTOR);
		ConceptualModelFactor cmDirectThreat = new ConceptualModelFactor(DiagramNode.TYPE_DIRECT_THREAT);
		ConceptualModelFactor cmStress = new ConceptualModelFactor(DiagramNode.TYPE_STRESS);
		ConceptualModelTarget cmTarget = new ConceptualModelTarget();

		intervention = DiagramNode.wrapConceptualModelObject(cmIntervention);
		indirectFactor = DiagramNode.wrapConceptualModelObject(cmIndirectFactor);
		directThreat = DiagramNode.wrapConceptualModelObject(cmDirectThreat);
		stress = DiagramNode.wrapConceptualModelObject(cmStress);
		target = DiagramNode.wrapConceptualModelObject(cmTarget);
		targetAttributeMap = target.getAttributes();
		super.setUp();
	}
	
	public void testPort()
	{
		assertEquals("port not first child?", target.getPort(), target.getFirstChild());
	}
	
	public void testText() throws Exception
	{
		target.setText(sampleText);
		assertEquals(sampleText, GraphConstants.getValue(targetAttributeMap));
	}
	
	public void testPriorities()
	{
		assertTrue(directThreat.canHavePriority());
		assertTrue(stress.canHavePriority());
		assertTrue(indirectFactor.canHavePriority());
		assertFalse(intervention.canHavePriority());
		assertFalse(target.canHavePriority());
	}
	
	public void testObjectives()
	{
		assertTrue(directThreat.canHaveObjectives());
		assertTrue(stress.canHaveObjectives());
		assertTrue(indirectFactor.canHaveObjectives());
		assertTrue(intervention.canHaveObjectives());
		assertFalse(target.canHaveObjectives());
	}

	public void testIndicator()
	{
		Indicator indicator = directThreat.getIndicator();
		assertFalse(indicator.hasIndicator());
		assertEquals(Indicator.INDICATOR_NONE_STRING, indicator.toString());
		int value = 2;
		indicator.setValue(value);
		assertTrue(indicator.hasIndicator());
		assertEquals(String.valueOf(value).toString(), indicator.toString());
	}
	
	public void testGoals()
	{
		assertTrue(target.canHaveGoal());
		assertFalse(directThreat.canHaveGoal());
		assertFalse(stress.canHaveGoal());
		assertFalse(indirectFactor.canHaveGoal());
		assertFalse(intervention.canHaveGoal());
	}
	
	
	public void testColors()
	{
		assertEquals("wrong color?", GraphConstants.getBackground(targetAttributeMap), DiagramTarget.COLOR_TARGET);
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

	static final double TOLERANCE = 0.00;
	static final String sampleText = "<rest&relaxation>";
	
	DiagramNode intervention;
	DiagramNode indirectFactor;
	DiagramNode directThreat;
	DiagramNode stress;
	DiagramNode target;
	Map targetAttributeMap;
}
