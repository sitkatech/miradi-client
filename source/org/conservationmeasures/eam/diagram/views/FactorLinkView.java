/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.views;

import org.conservationmeasures.eam.diagram.renderers.ArrowLineRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;

public class FactorLinkView extends EdgeView
{
	public FactorLinkView(Object edge)
	{
		super(edge);
	}

	public CellViewRenderer getRenderer()
	{
		return arrowLineRenderer;
	}

	protected static ArrowLineRenderer arrowLineRenderer = new ArrowLineRenderer();
}
