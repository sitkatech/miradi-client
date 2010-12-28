/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram.cellviews;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.plaf.basic.BasicGraphUI.RootHandle;
import org.miradi.diagram.BendPointSelectionHelper;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.EAM;
import org.miradi.utils.PointList;

public class EdgeHandleWithBendPointSelection extends EdgeView.EdgeHandle
{
	public EdgeHandleWithBendPointSelection(EdgeView edge, GraphContext ctx)
	{
		super(edge, ctx);
		
		diagram = (DiagramComponent) ctx.getGraph();
		linkCell = (LinkCell) edge.getCell();
		bendSelectionHelper = linkCell.getBendPointSelectionHelper();
	}
	
	public boolean isAddPointEvent(MouseEvent event)
	{
		return false;
	}

	public boolean isRemovePointEvent(MouseEvent event)
	{
		return false;
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
		
		if(bendSelectionHelper.mouseWasPressed(event, currentIndex))
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
		PointList bendPoints = linkCell.getDiagramLink().getBendPoints();
		if(bendPoints.size() == 0)
		{
			// NOTE: This happens if you undo the creation of a bend point that is now selected
			// The real solution would be to always unselect before removing cells from JGraph
			EAM.logDebug("Attempted to paint bend points that don't exist: " + linkCell.getDiagramLink().getRef());
			return;
		}
		int[] selectedIndexes = bendSelectionHelper.getSelectedIndexes();
		for (int i = 0; i < selectedIndexes.length; ++i)
		{
			Point point = bendPoints.get(selectedIndexes[i]);
			Point2D scaledPoint = diagram.getScaledPoint(point);
			g2.setColor(Color.BLACK);
			g2.setStroke(getSelectionStroke());
			g2.drawRect((int)scaledPoint.getX() - 10, (int)scaledPoint.getY() - 10, 20, 20);
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