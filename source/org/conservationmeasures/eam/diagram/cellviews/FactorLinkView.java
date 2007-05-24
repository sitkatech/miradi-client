/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

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
		return new EdgeHandle(this, context);
	}

	public static class EdgeHandle extends EdgeView.EdgeHandle
	{
		public EdgeHandle(EdgeView edge, GraphContext ctx)
		{
			super(edge, ctx);
		}
		
		public void mousePressed(MouseEvent event)
		{
			super.mousePressed(event);
		}

		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);
		}

		public void paint(Graphics g)
		{
			super.paint(g);
		}
	}
		
	protected static ArrowLineRenderer arrowLineRenderer = new ArrowLineRenderer();
}
