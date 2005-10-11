/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;

public class LinkageView extends EdgeView
{
	public LinkageView(Object edge)
	{
		super(edge);
	}

	public CellViewRenderer getRenderer()
	{
		return arrowLineRenderer;
	}

	protected static ArrowLineRenderer arrowLineRenderer = new ArrowLineRenderer();
}
