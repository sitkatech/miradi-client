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

import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeIndirectFactor;
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
		node = new DiagramNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		attributeMap = node.getAttributes();
		super.setUp();
	}
	
	public void testPort()
	{
		assertEquals("port not first child?", node.getPort(), node.getFirstChild());
	}
	
	public void testText() throws Exception
	{
		node.setText(sampleText);
		assertTrue("Isn't a indirect factor?", node.isIndirectFactor());
		assertEquals(sampleText, GraphConstants.getValue(attributeMap));
	}
	
	public void testIds()
	{
		DiagramNode testNode = new DiagramNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		assertEquals(DiagramNode.INVALID_ID,node.getId());
		int id = 23;
		testNode.setId(id);
		assertEquals(id, testNode.getId());
	}
	
	public void testPriorities()
	{
		DiagramNode nodeDirectThreat = new DiagramNode(DiagramNode.TYPE_DIRECT_THREAT);
		assertTrue(nodeDirectThreat.canHavePriority());

		DiagramNode nodeStress = new DiagramNode(DiagramNode.TYPE_STRESS);
		assertTrue(nodeStress.canHavePriority());

		DiagramNode nodeIndirectFactor = new DiagramNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		assertFalse(nodeIndirectFactor.canHavePriority());

		DiagramNode nodeIntervention = new DiagramNode(DiagramNode.TYPE_INTERVENTION);
		assertFalse(nodeIntervention.canHavePriority());

		DiagramNode nodeTarget = new DiagramNode(DiagramNode.TYPE_TARGET);
		assertFalse(nodeTarget.canHavePriority());
	}
	
	public void testObjectives()
	{
		DiagramNode nodeDirectThreat = new DiagramNode(DiagramNode.TYPE_DIRECT_THREAT);
		assertTrue(nodeDirectThreat.canHaveObjectives());

		DiagramNode nodeStress = new DiagramNode(DiagramNode.TYPE_STRESS);
		assertTrue(nodeStress.canHaveObjectives());

		DiagramNode nodeIndirectFactor = new DiagramNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		assertTrue(nodeIndirectFactor.canHaveObjectives());

		DiagramNode nodeIntervention = new DiagramNode(DiagramNode.TYPE_INTERVENTION);
		assertTrue(nodeIntervention.canHaveObjectives());

		DiagramNode nodeTarget = new DiagramNode(DiagramNode.TYPE_TARGET);
		assertFalse(nodeTarget.canHaveObjectives());
	}

	public void testIndicator()
	{
		DiagramNode nodeDirectThreat = new DiagramNode(DiagramNode.TYPE_DIRECT_THREAT);
		Indicator indicator = nodeDirectThreat.getIndicator();
		assertFalse(indicator.hasIndicator());
		assertEquals(Indicator.INDICATOR_NONE_STRING, indicator.toString());
		int value = 2;
		indicator.setValue(value);
		assertTrue(indicator.hasIndicator());
		assertEquals(String.valueOf(value).toString(), indicator.toString());
	}
	
	public void testGoals()
	{
		DiagramNode nodeTarget = new DiagramNode(DiagramNode.TYPE_TARGET);
		assertTrue(nodeTarget.canHaveGoal());

		DiagramNode nodeDirectThreat = new DiagramNode(DiagramNode.TYPE_DIRECT_THREAT);
		assertFalse(nodeDirectThreat.canHaveGoal());

		DiagramNode nodeStress = new DiagramNode(DiagramNode.TYPE_STRESS);
		assertFalse(nodeStress.canHaveGoal());

		DiagramNode nodeIndirectFactor = new DiagramNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		assertFalse(nodeIndirectFactor.canHaveGoal());

		DiagramNode nodeIntervention = new DiagramNode(DiagramNode.TYPE_INTERVENTION);
		assertFalse(nodeIntervention.canHaveGoal());

	}
	
	
	public void testColors()
	{
		NodeTypeIndirectFactor factorType = new NodeTypeIndirectFactor();
		assertEquals("wrong color?", GraphConstants.getBackground(attributeMap), factorType.getColor());
	}
	
	public void testBounds()
	{
		node.setLocation(new Point(123, 456));
		Rectangle2D bounds = GraphConstants.getBounds(attributeMap);
		assertEquals("wrong x?", 123.0, bounds.getX(), TOLERANCE);
		assertEquals("wrong y?", 456.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong width", 120.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong height", 60.0, bounds.getHeight(), TOLERANCE);
	}
	
	public void testSize()
	{
		node.setLocation(new Point(3, 4));
		node.setSize(new Dimension(300, 200));
		Rectangle2D bounds = GraphConstants.getBounds(attributeMap);
		assertEquals("wrong x?", 3.0, bounds.getX(), TOLERANCE);
		assertEquals("wrong y?", 4.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong width", 300.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong height", 200.0, bounds.getHeight(), TOLERANCE);
		node.setSize(new Dimension(100, 50));
		bounds = GraphConstants.getBounds(attributeMap);
		assertEquals("x changed?", 3.0, bounds.getX(), TOLERANCE);
		assertEquals("y changed?", 4.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong new width", 100.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong new height", 50.0, bounds.getHeight(), TOLERANCE);
		assertEquals("node size width incorrect?", 100.0, node.getSize().getWidth(), TOLERANCE);
		assertEquals("node size height incorrect?", 50.0, node.getSize().getHeight(), TOLERANCE);
	}

	public void testFont()
	{
		Font nodeFont = GraphConstants.getFont(attributeMap);
		assertTrue("not bold?", nodeFont.isBold());
	}

	static final double TOLERANCE = 0.00;
	static final String sampleText = "<rest&relaxation>";
	
	DiagramNode node;
	Map attributeMap;
}
