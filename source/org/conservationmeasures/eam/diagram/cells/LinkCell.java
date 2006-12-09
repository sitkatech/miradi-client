/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.renderers.ArrowLineRenderer;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class LinkCell extends EAMGraphCell implements Edge
{
	public LinkCell(FactorLink linkToUse, DiagramFactorLink diagramLinkToUse, DiagramFactor fromToUse, DiagramFactor toToUse)
	{
		link = linkToUse;
		diagramLink = diagramLinkToUse;
		from = fromToUse;
		to = toToUse;
		fillConnectorAttributeMap("");
	}
	
	public boolean isFactorLink()
	{
		return true;
	}
	
	public DiagramFactorLink getDiagramFactorLink()
	{
		return diagramLink;
	}

	public DiagramFactor getFrom()
	{
		return from;
	}
	
	public DiagramFactor getTo()
	{
		return to;
	}
	
	public FactorLink getFactorLink()
	{
		return link;
	}
	
	public void update(DiagramComponent diagram)
	{
		boolean linksVisible = diagram.getProject().getLayerManager().areFactorLinksVisible();
		boolean isSelected = diagram.isCellSelected(this);
		boolean isAttachedFactorSelected = diagram.isCellSelected(getFrom()) || diagram.isCellSelected(getTo());
		if(linksVisible || isSelected || isAttachedFactorSelected)
			setTail(GraphConstants.ARROW_NONE);
		else
			setTail(ArrowLineRenderer.ARROW_JUST_LINE);
	}
	
	private void setTail(int arrowStyle)
	{
		GraphConstants.setLineBegin(getAttributes(), arrowStyle);
		GraphConstants.setBeginFill(getAttributes(), false);
		GraphConstants.setBeginSize(getAttributes(), 10);
		
	}

	// BEGIN Edge Interface
	public Object getSource()
	{
		return from.getPort();
	}

	public Object getTarget()
	{
		return to.getPort();
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
		GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_TECHNICAL);
		GraphConstants.setEndFill(getAttributes(), true);
	}


	FactorLink link;
	DiagramFactorLink diagramLink;
	DiagramFactor from;
	DiagramFactor to;
}
