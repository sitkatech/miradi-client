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
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;

public class EdgeHandleWithBendPointSelection extends EdgeView.EdgeHandle
{
	public EdgeHandleWithBendPointSelection(EdgeView edge, GraphContext ctx)
	{
		super(edge, ctx);
		
		diagram = (DiagramComponent) ctx.getGraph();
		linkCell = (LinkCell) edge.getCell();
		bendSelectionHelper = linkCell.getBendPointSelectionHelper();
	}
	
	public void mouseReleased(MouseEvent event)
	{
		Point mouseEnd = event.getPoint();
		if (sameLocationAsStart(mouseEnd))
			fakeMouseClicked(event);
		
		super.mouseReleased(event);
	}

	private boolean sameLocationAsStart(Point endLocation)
	{
		if (mouseStart.x != endLocation.x)
			return false;
		
		if (mouseStart.y != endLocation.y)
			return false;
		
		return true;
	}

	public void fakeMouseClicked(MouseEvent event)
	{
		bendSelectionHelper.mouseClicked(event, currentIndex);
		bendPointSelectionChanged();
	}

	public void mousePressed(MouseEvent event)
	{
		super.mousePressed(event);
		mouseStart = event.getPoint();
		bendSelectionHelper.mouseWasPressed(event, currentIndex);
		bendPointSelectionChanged();
	}
	
	private void bendPointSelectionChanged()
	{
		diagram.getMainWindow().updateActionStates();
		diagram.repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		PointList bendPoints = linkCell.getDiagramFactorLink().getBendPoints();
		int[] selectedIndexes = bendSelectionHelper.getSelectedIndexes();
		for (int i = 0; i < selectedIndexes.length; ++i)
		{
			//FIXME this to deal with undoing a created(selected) bend point
			//the undo deletes the bend point but does not remove it from the selection list
			//and we try to paint it. 
			if (selectedIndexes[i] >= bendPoints.size())
				continue;
			
			Point point = bendPoints.get(selectedIndexes[i]);
			g2.setColor(diagram.getHighlightColor());
			g2.drawRect((int)point.getX() - 10, (int)point.getY() - 10, 18, 18);
		}
	}
	
	DiagramComponent diagram;
	BendPointSelectionHelper bendSelectionHelper;
	LinkCell linkCell;
	Point mouseStart;
}