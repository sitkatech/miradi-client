/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Component;
import java.awt.Graphics;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeRenderer;

public class ArrowLineRenderer extends EdgeRenderer
{
	public Component getRendererComponent(JGraph graphToUse, CellView cellView, 
					boolean sel, boolean hasFocus, boolean previewMode)
	{
		ArrowLineRenderer renderer = 
			(ArrowLineRenderer)super.getRendererComponent(graphToUse, cellView, sel, hasFocus, previewMode);
		
		DiagramLinkage linkage = (DiagramLinkage)cellView.getCell();
		DiagramNode from = linkage.getFromNode();
		DiagramNode to = linkage.getToNode();
		DiagramComponent diagram = (DiagramComponent)graphToUse;
		boolean bothNodesVisible = diagram.isNodeVisible(from) && diagram.isNodeVisible(to);
		renderer.isVisible = bothNodesVisible;
		return renderer;
	}

	public void paint(Graphics arg0)
	{
		if(!isVisible)
			return;
		
		super.paint(arg0);
	}
	
	boolean isVisible;
}
