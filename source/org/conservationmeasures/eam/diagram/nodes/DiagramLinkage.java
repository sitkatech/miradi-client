/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.project.IdAssigner;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class DiagramLinkage extends EAMGraphCell implements Edge
{
	public DiagramLinkage(DiagramNode from, DiagramNode to)
	{
		setSource(from.getPort());
		setTarget(to.getPort());
		id = IdAssigner.INVALID_ID;
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

	public DiagramNode getFromNode()
	{
		return (DiagramNode)fromPort.getParent();
	}
	public DiagramNode getToNode()
	{
		return (DiagramNode)toPort.getParent();
	}
	
	private void fillConnectorAttributeMap(String label)
	{
	    GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_SIMPLE);
	    GraphConstants.setValue(getAttributes(), label);
	    GraphConstants.setOpaque(getAttributes(), true);
	    GraphConstants.setBackground(getAttributes(), Color.BLACK);
	    GraphConstants.setForeground(getAttributes(), Color.BLACK);
	    GraphConstants.setGradientColor(getAttributes(), Color.BLACK); //Windows 2000 quirk required to see line.
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

	public void setId(int idToUse)
	{
		id = idToUse;
	}

	public int getId()
	{
		return id;
	}

	private DefaultPort fromPort;
	private DefaultPort toPort;
	protected int id;
}
