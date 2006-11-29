/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class LinkCell extends EAMGraphCell implements Edge
{
	public LinkCell(DiagramFactorLink linkToUse)
	{
		link = linkToUse;
		fillConnectorAttributeMap("");
	}
	
	public boolean isFactorLink()
	{
		return true;
	}

	public DiagramFactorLink getDiagramFactorLink()
	{
		return link;
	}

	// BEGIN Edge Interface
	public Object getSource()
	{
		return link.getFromNode().getPort();
	}

	public Object getTarget()
	{
		return link.getToNode().getPort();
	}

	public void setSource(Object source)
	{
		// not allowed--ignore attempts to reset the source
	}

	public void setTarget(Object target)
	{
		// not allowed--ignore attempts to reset the target
	}
	// END Edge interface
	
	private void fillConnectorAttributeMap(String label)
	{
	    GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_SIMPLE);
	    GraphConstants.setValue(getAttributes(), label);
	    GraphConstants.setOpaque(getAttributes(), true);
	    GraphConstants.setBackground(getAttributes(), Color.BLACK);
	    GraphConstants.setForeground(getAttributes(), Color.BLACK);
	    GraphConstants.setGradientColor(getAttributes(), Color.BLACK); //Windows 2000 quirk required to see line.
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(getAttributes(), arrow);
		GraphConstants.setEndFill(getAttributes(), true);
	}



	DiagramFactorLink link;
}
