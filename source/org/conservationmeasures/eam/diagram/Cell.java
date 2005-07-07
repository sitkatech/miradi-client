/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.conservationmeasures.eam.diagram.cells.CellType;
import org.conservationmeasures.eam.diagram.cells.FlexibleCell;
import org.jgraph.graph.GraphConstants;
import org.martus.util.xml.XmlUtilities;

public class Cell
{
	public Cell(CellType cellType, Point2D snappedLocation, double scale, String text, Font baseFont)
	{
		cell = new FlexibleCell(cellType);
		map = new HashMap();
		
		setBounds(snappedLocation, scale);
		setColors(cellType);
		setFont(baseFont);
		setLabel(text);
	}

	private void setLabel(String text)
	{
		String formattedLabel = HTML_BEFORE_TEXT + XmlUtilities.getXmlEncoded(text) + HTML_AFTER_TEXT;
		GraphConstants.setValue(map, formattedLabel);
	}

	private void setFont(Font originalFont)
	{
		Font font = originalFont.deriveFont(Font.BOLD);
		GraphConstants.setFont(map, font);
	}

	private void setColors(CellType cellType)
	{
		Color color = cellType.getColor();
		GraphConstants.setBorderColor(map, Color.black);
		GraphConstants.setBackground(map, color);
		GraphConstants.setForeground(map, Color.black);
		GraphConstants.setOpaque(map, true);
	}

	private void setBounds(Point2D snappedLocation, double scale)
	{
		double width = 120 * scale;
		double height = 60 * scale;
		Dimension size = new Dimension((int)width, (int)height);
		Point location = new Point((int)snappedLocation.getX(), (int)snappedLocation.getY());
		GraphConstants.setBounds(map, new Rectangle(location, size));
	}
	
	Hashtable getNestedAttributeMap()
	{
		Hashtable nest = new Hashtable();
		nest.put(getCell(), getMap());
		return nest;
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public FlexibleCell getCell()
	{
		return cell;
	}
	
	public static final String HTML_AFTER_TEXT = "</font></div></html>";
	public static final String HTML_BEFORE_TEXT = "<html><div align='center'><font size='4'>";

	FlexibleCell cell;
	HashMap map;
}