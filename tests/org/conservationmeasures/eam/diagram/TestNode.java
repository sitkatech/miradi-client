/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.diagram.nodes.NodeType;
import org.conservationmeasures.eam.diagram.nodes.NodeTypeThreat;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestNode extends EAMTestCase
{
	public TestNode(String name)
	{
		super(name);
	}
	
	public void setUp()
	{
		threatType = new NodeTypeThreat();
		node = new Node(threatType);
		attributeMap = node.getMap();
		super.setUp();
	}
	
	public void testText() throws Exception
	{
		node.setText(sampleText);
		assertTrue("Isn't a threat?", node.isThreat());
		assertEquals(Node.HTML_BEFORE_TEXT + sanitizedText + Node.HTML_AFTER_TEXT, GraphConstants.getValue(attributeMap));
	}
	
	public void testColors()
	{
		assertEquals("wrong color?", GraphConstants.getBackground(attributeMap), threatType.getColor());
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
	static final String sanitizedText = "&lt;rest&amp;relaxation&gt;";
	
	NodeType threatType;
	Node node;
	Map attributeMap;
}
