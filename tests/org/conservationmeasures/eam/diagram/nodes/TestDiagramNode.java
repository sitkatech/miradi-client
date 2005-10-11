/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeFactor;
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
		assertTrue("Isn't a factor?", node.isFactor());
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
	
	public void testColors()
	{
		NodeTypeFactor factorType = new NodeTypeFactor();
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
	
	public void testFont()
	{
		Font nodeFont = GraphConstants.getFont(attributeMap);
		assertTrue("not bold?", nodeFont.isBold());
	}

	static final double TOLERANCE = 0.01;
	static final String sampleText = "<rest&relaxation>";
	
	DiagramNode node;
	Map attributeMap;
}
