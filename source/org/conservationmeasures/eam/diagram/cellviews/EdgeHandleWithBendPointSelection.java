/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.conservationmeasures.eam.diagram.BendPointSelectionHelper;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.JGraph;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;

public class EdgeHandleWithBendPointSelection extends EdgeView.EdgeHandle
{
	public EdgeHandleWithBendPointSelection(EdgeView edge, GraphContext ctx)
	{
		super(edge, ctx);
		
		graph = ctx.getGraph();
		linkCell = (LinkCell) edge.getCell();
		bendSelectionHelper = linkCell.getBendPointSelectionHelper();
	}
	
	public void mousePressed(MouseEvent event)
	{
		super.mousePressed(event);
		bendSelectionHelper.mousePressed(event, currentIndex);
		graph.repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		PointList bendPoints = linkCell.getDiagramFactorLink().getBendPoints();
		int[] selectedIndexes = bendSelectionHelper.getSelectedIndexes();
		for (int i = 0; i < selectedIndexes.length; ++i)
		{
			Point point = bendPoints.get(selectedIndexes[i]);
			g2.setColor(graph.getHighlightColor());
			g2.drawRect((int)point.getX() - 10, (int)point.getY() - 10, 18, 18);
		}
	}
	
	JGraph graph;
	BendPointSelectionHelper bendSelectionHelper;
	LinkCell linkCell;
}