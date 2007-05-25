/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.diagram.BendPointSelectionHelper;
import org.jgraph.JGraph;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;

public class EdgeHandleWithBendPointSelection extends EdgeView.EdgeHandle
{
	public EdgeHandleWithBendPointSelection(EdgeView edge, GraphContext ctx)
	{
		super(edge, ctx);
		
		graph = ctx.getGraph();
		bendSelectionHelper = new BendPointSelectionHelper();
	}
	
	public void mousePressed(MouseEvent event)
	{
		super.mousePressed(event);
		bendSelectionHelper.mousePressed(event, currentPoint);
		graph.repaint();
	}

	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		Point2D[] selectionList = bendSelectionHelper.getSelectionList();
		for (int i = 0; i < selectionList.length; ++i)
		{
			g2.setColor(graph.getHighlightColor());
			g2.drawRect((int)selectionList[i].getX() - 10, (int)selectionList[i].getY() - 10, 18, 18);
		}
		
		
	}
	
	JGraph graph;
	BendPointSelectionHelper bendSelectionHelper;
}