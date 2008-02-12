/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.cellviews;


import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.miradi.diagram.renderers.ArrowLineRenderer;

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
	
	public CellHandle getHandle(GraphContext context)
	{
		edgeHandleWithBendPointSelection = new EdgeHandleWithBendPointSelection(this, context);
		return edgeHandleWithBendPointSelection;
	}
	
	EdgeHandleWithBendPointSelection edgeHandleWithBendPointSelection;
	protected static ArrowLineRenderer arrowLineRenderer = new ArrowLineRenderer();
}
