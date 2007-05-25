/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;


import org.conservationmeasures.eam.diagram.renderers.ArrowLineRenderer;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;

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
		return new EdgeHandleWithBendPointSelection(this, context);
	}

	protected static ArrowLineRenderer arrowLineRenderer = new ArrowLineRenderer();
}
