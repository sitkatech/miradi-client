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

public class DiagramNode extends EAMGraphCell
{
	public DiagramNode(int nodeType)
	{
		switch(nodeType)
		{
			case TYPE_TARGET:
				type = new NodeTypeTarget();
				break;
			case TYPE_INDIRECT_FACTOR:
				type = new NodeTypeIndirectFactor();
				break;
			case TYPE_DIRECT_THREAT:
				type = new NodeTypeDirectThreat();
				break;
			case TYPE_STRESS:
				type = new NodeTypeStress();
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
		if(isTarget())
			return TYPE_TARGET;
		if(isIndirectFactor())
			return TYPE_INDIRECT_FACTOR;
		if(isIntervention())
			return TYPE_INTERVENTION;
		if(isDirectThreat())
			return TYPE_DIRECT_THREAT;
		if(isStress())
			return TYPE_STRESS;
		return TYPE_INVALID;
	}

	public boolean isTarget()
	{
		return(type.isTarget());
	}
	
	public boolean isIndirectFactor()
	{
		return(type.isIndirectFactor());
	}
	
	public boolean isIntervention()
	{
		return(type.isIntervention());
	}
	
	public boolean isDirectThreat()
	{
		return(type.isDirectThreat());
	}
	
	public boolean isStress()
	{
		return(type.isStress());
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
	
	public NodeDataMap getNodeDataMap()
	{
		NodeDataMap dataBin = super.getNodeDataMap();
		dataBin.put(TYPE, new Integer(getNodeType()));
		return dataBin;
	}
	
	public static final int INVALID_ID = -1;
	
	public static final int TYPE_INVALID = -1;
	public static final int TYPE_TARGET = 1;
	public static final int TYPE_INDIRECT_FACTOR = 2;
	public static final int TYPE_INTERVENTION = 3;
	public static final int TYPE_DIRECT_THREAT = 4;
	public static final int TYPE_STRESS = 5;

	public static final String TYPE = "type";

	NodeType type;
	DefaultPort port;
}
