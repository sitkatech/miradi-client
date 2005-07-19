/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

public class Node extends EAMGraphCell
{
	public Node(NodeType nodeType)
	{
		type = nodeType;
		port = new DefaultPort();
		add(port);
		
		setColors(nodeType);
		setFont();
		setLocation(new Point(0, 0));
		setSize(new Dimension(120, 60));
		setText("");
	}
	
	public boolean isNode()
	{
		return true;
	}

	public boolean isGoal()
	{
		return(type.isGoal());
	}
	
	public boolean isThreat()
	{
		return(type.isThreat());
	}
	
	public boolean isIntervention()
	{
		return(type.isIntervention());
	}
	
	public DefaultPort getPort()
	{
		return port;
	}
	
	private void setFont()
	{
//		Font font = originalFont.deriveFont(Font.BOLD);
//		GraphConstants.setFont(map, font);
	}

	private void setColors(NodeType cellType)
	{
		Color color = cellType.getColor();
		GraphConstants.setBorderColor(getAttributes(), Color.black);
		GraphConstants.setBackground(getAttributes(), color);
		GraphConstants.setForeground(getAttributes(), Color.black);
		GraphConstants.setOpaque(getAttributes(), true);
	}
	
	private void setSize(Dimension size)
	{
		Point location = getLocation();
		Rectangle bounds = new Rectangle(location, size);
		GraphConstants.setBounds(getAttributes(), bounds);
	}

	NodeType type;
	DefaultPort port;
}
