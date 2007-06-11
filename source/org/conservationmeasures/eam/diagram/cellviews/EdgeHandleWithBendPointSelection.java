/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.conservationmeasures.eam.diagram.BendPointSelectionHelper;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.plaf.basic.BasicGraphUI.RootHandle;

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
		if (mouseStart.equals(mouseEnd))
			generateMouseWasClicked(event);
		
		super.mouseReleased(event);
	}

	public void generateMouseWasClicked(MouseEvent event)
	{
		bendSelectionHelper.mouseClicked(event, currentIndex);
		bendPointSelectionChanged();
	}

	public void mousePressed(MouseEvent event)
	{
		super.mousePressed(event);
		mouseStart = event.getPoint();
		if (isPortPoint(event))
			return;
		
		bendSelectionHelper.mouseWasPressed(event, currentIndex);
		bendPointSelectionChanged();
	}
	
	private boolean isPortPoint(MouseEvent event)
	{	
		final int FIRST_INDEX = 0;
		if (r[FIRST_INDEX].contains(event.getPoint()))
			return true;
		
		final int LAST_INDEX = r.length - 1;
		if (r[LAST_INDEX].contains(event.getPoint()))
			return true;
		
		return false;
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
			//FIXME nima this to deal with undoing a created(selected) bend point
			//the undo deletes the bend point but does not remove it from the selection list
			//and we try to paint it.
			// update : now if an undo/redo changes the size of the selection list, it clears selections then adds
			if (selectedIndexes[i] >= bendPoints.size())
				continue;
			
			Point point = bendPoints.get(selectedIndexes[i]);
			g2.setColor(Color.BLACK);
			g2.setStroke(getSelectionStroke());
			g2.drawRect((int)point.getX() - 10, (int)point.getY() - 10, 20, 20);
		}
	}

	Stroke getSelectionStroke()
	{
		float[] dash = {5f};
		return new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
	}
	
	public void updatePoints(Point2D currentPointToUse, Point2D p)
	{
		BasicGraphUI graphUi = (BasicGraphUI) graph.getUI();
		RootHandle rootHandle =  (RootHandle) graphUi.getHandle();

		int deltaX = (int) p.getX() - (int) currentPointToUse.getX();
		int deltaY = (int) p.getY() - (int) currentPointToUse.getY();
		CellView[] cellView = {edge};
		rootHandle.updateControlPoints(cellView, deltaX, deltaY);
	}
	
	DiagramComponent diagram;
	BendPointSelectionHelper bendSelectionHelper;
	LinkCell linkCell;
	Point mouseStart;
}