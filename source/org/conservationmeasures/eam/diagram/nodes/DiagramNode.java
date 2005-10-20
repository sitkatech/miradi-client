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
import java.util.Vector;

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
		
		if(canHavePriority(type))
			setNodePriority(ThreatPriority.createPriorityNone());
		objective = new Objective();
		indicator = new Indicator();
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
	
	public ThreatPriority getThreatPriority()
	{
		return threatPriority;
	}
	
	public void setNodePriority(ThreatPriority priorityToUse)
	{
		threatPriority = priorityToUse;
	}
	
	public boolean canHavePriority()
	{
		return canHavePriority(type);
	}
	
	private boolean canHavePriority(NodeType nodeType)
	{
		if(nodeType.isDirectThreat())
			return true;
		if(nodeType.isStress())
			return true;
		return false;
	}

	public Indicator getIndicator()
	{
		return indicator;
	}
	
	public void setIndicator(Indicator indicatorToUse)
	{
		indicator = indicatorToUse;
	}
	

	public boolean canHaveObjective()
	{
		return canHaveObjective(type);
	}

	private boolean canHaveObjective(NodeType nodeType)
	{
		if(nodeType.isDirectThreat())
			return true;
		if(nodeType.isIndirectFactor())
			return true;
		if(nodeType.isIntervention())
			return true;
		if(nodeType.isStress())
			return true;
		return false;
	}
	
	public Vector getObjectives()
	{
		//TODO: These will be replaced by real user entered data from a wizard
		Vector objectives = new Vector();
		if(type.isDirectThreat())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("1a"));
			objectives.add(new Objective("1b"));
			objectives.add(new Objective("2"));
		}
		else if (type.isIndirectFactor())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("A"));
			objectives.add(new Objective("B"));
			objectives.add(new Objective("C"));
			objectives.add(new Objective("D"));
		}
		else if (type.isStress())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("1"));
			objectives.add(new Objective("2"));
			objectives.add(new Objective("3"));
		}
		else if (type.isIntervention())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("a"));
			objectives.add(new Objective("b"));
			objectives.add(new Objective("c"));
		}
		return objectives;
	}

	public Objective getObjective()
	{
		return objective;
	}

	public void setObjective(Objective objectiveToUse)
	{
		objective = objectiveToUse;
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
	
	public NodeDataMap createNodeDataMap()
	{
		NodeDataMap dataMap = super.createNodeDataMap();
		dataMap.put(TYPE, new Integer(getNodeType()));
		
		ThreatPriority priority = getThreatPriority();
		int priorityValue = ThreatPriority.PRIORITY_NOT_USED;
		if(priority != null)
			priorityValue = priority.getValue();
		dataMap.put(PRIORITY, new Integer(priorityValue));
		return dataMap;
	}
	
	public static final int INVALID_ID = -1;
	
	public static final int TYPE_INVALID = -1;
	public static final int TYPE_TARGET = 1;
	public static final int TYPE_INDIRECT_FACTOR = 2;
	public static final int TYPE_INTERVENTION = 3;
	public static final int TYPE_DIRECT_THREAT = 4;
	public static final int TYPE_STRESS = 5;

	public static final String TYPE = "type";
	public static final String PRIORITY = "priority";

	NodeType type;
	DefaultPort port;
	ThreatPriority threatPriority;
	Indicator indicator;
	Objective objective;
}
