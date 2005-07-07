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

import org.conservationmeasures.eam.diagram.cells.CellType;
import org.conservationmeasures.eam.diagram.cells.CellTypeThreat;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.jgraph.graph.GraphConstants;

public class TestCell extends EAMTestCase
{
	public TestCell(String name)
	{
		super(name);
	}
	
	public void setUp()
	{
		threatType = new CellTypeThreat();
		Point location = new Point(123, 456);
		Font baseFont = new Font("serif", 10, 5);
		cell = new Cell(threatType, location, 2.0, sampleText, baseFont);
		attributeMap = cell.getMap();
		super.setUp();
	}
	
	public void testText() throws Exception
	{
		assertTrue("Isn't a threat?", cell.getCell().isThreat());
		assertEquals(Cell.HTML_BEFORE_TEXT + sanitizedText + Cell.HTML_AFTER_TEXT, GraphConstants.getValue(attributeMap));
	}
	
	public void testColors()
	{
		assertEquals("wrong color?", GraphConstants.getBackground(attributeMap), threatType.getColor());
	}
	
	public void testBounds()
	{
		Rectangle2D bounds = GraphConstants.getBounds(attributeMap);
		assertEquals("wrong x?", 123.0, bounds.getX(), TOLERANCE);
		assertEquals("wrong y?", 456.0, bounds.getY(), TOLERANCE);
		assertEquals("wrong width", 240.0, bounds.getWidth(), TOLERANCE);
		assertEquals("wrong height", 120.0, bounds.getHeight(), TOLERANCE);
	}
	
	public void testFont()
	{
		Font cellFont = GraphConstants.getFont(attributeMap);
		assertTrue("not bold?", cellFont.isBold());
	}

	static final double TOLERANCE = 0.01;
	static final String sampleText = "<rest&relaxation>";
	static final String sanitizedText = "&lt;rest&amp;relaxation&gt;";
	
	CellType threatType;
	Cell cell;
	Map attributeMap;
}
