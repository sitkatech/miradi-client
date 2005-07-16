/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class Linkage extends EAMGraphCell implements Edge
{
	public Linkage(Node from, Node to)
	{
		setSource(from.getPort());
		setTarget(to.getPort());
		String label = "";
		fillConnectorAttributeMap(label);
	}
	
	public boolean isLinkage()
	{
		return true;
	}
	
	public ConnectionSet getConnectionSet()
	{
		return new ConnectionSet(this, fromPort, toPort);		
	}

	public Node getFromNode()
	{
		return (Node)fromPort.getParent();
	}
	public Node getToNode()
	{
		return (Node)toPort.getParent();
	}
	
	private void fillConnectorAttributeMap(String label)
	{
	    GraphConstants.setLineEnd(getMap(), GraphConstants.ARROW_SIMPLE);
	    GraphConstants.setValue(getMap(), label);
	    GraphConstants.setOpaque(getMap(), true);
	    GraphConstants.setBackground(getMap(), Color.CYAN);
	    GraphConstants.setForeground(getMap(), Color.BLACK);
//		Font font = getFont().deriveFont(Font.BOLD);
//		GraphConstants.setFont(thisMap, font);
	}

	public Object getSource()
	{
		return fromPort;
	}

	public Object getTarget()
	{
		return toPort;
	}

	public void setSource(Object source)
	{
		fromPort = (DefaultPort)source;
	}

	public void setTarget(Object target)
	{
		toPort = (DefaultPort)target;
	}

	private DefaultPort fromPort;
	private DefaultPort toPort;
}
