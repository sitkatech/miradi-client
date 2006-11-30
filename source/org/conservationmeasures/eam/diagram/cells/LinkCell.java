/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class LinkCell extends EAMGraphCell implements Edge
{
	public LinkCell(DiagramModel modelToUse, DiagramFactorLink linkToUse, DiagramFactor fromToUse, DiagramFactor toToUse)
	{
		model = modelToUse;
		link = linkToUse;
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
		return link;
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
		FactorLinkId factorLinkId = getDiagramFactorLink().getWrappedId();
		return (FactorLink)model.getProject().findObject(ObjectType.MODEL_LINKAGE, factorLinkId);
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
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(getAttributes(), arrow);
		GraphConstants.setEndFill(getAttributes(), true);
	}


	DiagramModel model;
	DiagramFactorLink link;
	DiagramFactor from;
	DiagramFactor to;
}
