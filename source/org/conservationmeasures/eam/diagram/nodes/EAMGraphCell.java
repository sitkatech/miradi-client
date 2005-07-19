/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

public class EAMGraphCell extends DefaultGraphCell
{
	public EAMGraphCell()
	{
	}

	public boolean isNode()
	{
		return false;
	}
	
	public boolean isLinkage()
	{
		return false;
	}
	
	public Point getLocation()
	{
		Rectangle2D bounds = GraphConstants.getBounds(getAttributes());
		if(bounds == null)
			return new Point(0, 0);
		
		return new Point((int)bounds.getX(), (int)bounds.getY());
	}

	public void setLocation(Point2D snappedLocation)
	{
		Rectangle2D bounds = GraphConstants.getBounds(getAttributes());
		if(bounds == null)
			bounds = new Rectangle(0, 0, 0, 0);
		
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		Dimension size = new Dimension((int)width, (int)height);
		Point location = new Point((int)snappedLocation.getX(), (int)snappedLocation.getY());
		GraphConstants.setBounds(getAttributes(), new Rectangle(location, size));
	}
	
	public Hashtable getNestedAttributeMap()
	{
		Hashtable nest = new Hashtable();
		nest.put(this, getAttributes());
		return nest;
	}

	public void setText(String text)
	{
		GraphConstants.setValue(getAttributes(), text);
	}

	public String getText()
	{
		return (String)GraphConstants.getValue(getAttributes());
	}

}
