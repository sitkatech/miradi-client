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
	public Node(int nodeType)
	{
		switch(nodeType)
		{
			case TYPE_GOAL:
				type = new NodeTypeGoal();
				break;
			case TYPE_THREAT:
				type = new NodeTypeThreat();
				break;
			case TYPE_INTERVENTION:
				type = new NodeTypeIntervention();
				break;
			default:
				throw new RuntimeException("Unknown node type: " + nodeType);
		}
		port = new DefaultPort();
		add(port);
		
		setColors(type);
		setFont();
		setLocation(new Point(0, 0));
		setSize(new Dimension(120, 60));
		setText("");
	}
	
	public boolean isNode()
	{
		return true;
	}
	
	public int getNodeType()
	{
		if(isGoal())
			return TYPE_GOAL;
		if(isThreat())
			return TYPE_THREAT;
		if(isIntervention())
			return TYPE_INTERVENTION;
		return TYPE_INVALID;
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
	
	public static final int INVALID_ID = -1;
	
	public static final int TYPE_INVALID = -1;
	public static final int TYPE_GOAL = 1;
	public static final int TYPE_THREAT = 2;
	public static final int TYPE_INTERVENTION = 3;

	NodeType type;
	DefaultPort port;
}
