/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.DiagramModelEvent;
import org.conservationmeasures.eam.diagram.DiagramModelListener;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;

public class LinkageMonitor implements DiagramModelListener
{
	public LinkageMonitor()
	{
	}

	public void nodeAdded(DiagramModelEvent event)
	{
	}

	public void nodeChanged(DiagramModelEvent event)
	{
	}

	public void nodeDeleted(DiagramModelEvent event)
	{
	}

	public void nodeMoved(DiagramModelEvent event)
	{
	}

	public void linkageAdded(DiagramModelEvent event)
	{
		DiagramLinkage linkage = event.getLinkage();
		DiagramNode from = linkage.getFromNode(); 
		DiagramNode to = linkage.getToNode();
		if(from.isFactor() && to.isTarget())
			((ConceptualModelFactor)from.getUnderlyingObject()).increaseTargetCount();
	}

	public void linkageDeleted(DiagramModelEvent event)
	{
		DiagramLinkage linkage = event.getLinkage();
		DiagramNode from = linkage.getFromNode(); 
		DiagramNode to = linkage.getToNode();
		if(from.isFactor() && to.isTarget())
			((ConceptualModelFactor)from.getUnderlyingObject()).decreaseTargetCount();
	}
}
