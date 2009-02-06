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
package org.miradi.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.jgraph.JGraph;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.AttributeMap.SerializablePoint2D;
import org.jgraph.plaf.basic.BasicGraphUI;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.EAM;
import org.miradi.utils.PointList;

public class EAMGraphUI extends BasicGraphUI
{
	protected MouseListener createMouseListener() 
	{
		return new DiagramMouseHandler();
    }
	
	public JGraph getGraph()
	{
		return graph;
	}
	
	public CellView getFocus()
	{
		return focus;
	}
	
	public CellHandle createHandle(GraphContext context) 
	{
		if (context != null && !context.isEmpty() && graph.isEnabled()) 
		{
			try 
			{
				return new CustomRootHandle(context);
			} 
			catch (NullPointerException e) 
			{
				EAM.logException(e);
			}
		}
		
		return null;
	}

	public boolean isFactorLinkView(CellView view)
	{
		if (view == null)
			return false;
		
		EAMGraphCell cell = (EAMGraphCell) view.getCell();
		return cell.isFactorLink();
	}

	class CustomRootHandle extends BasicGraphUI.RootHandle
	{
		public CustomRootHandle(GraphContext ctx)
		{
			super(ctx);
		}
		
		protected void setHandle(MouseEvent event)
		{
			for (int i = 0; i < views.length; ++i)
			{
				CellView cellView = views[i];
				EAMGraphCell cell = (EAMGraphCell) cellView.getCell();
				if (cell.isFactorLink())
				{
					activeHandle = null;
				}
			}
		}

		protected double getDxToStayAboveZero(double dx, double totDx)
		{
			for (int i = 0; i < views.length; ++i)
			{
				CellView cellView = views[i];
				EAMGraphCell cell = (EAMGraphCell) cellView.getCell();
				if (cell.isFactor())
				{
					dx = calculateDeltaXForFactor(dx, totDx, cell);
				}
				else if (cell.isFactorLink())
				{
					dx = calculateDeltaXForLink(dx, totDx, cell);
				}
			}
		
			return dx;
		}
		
		protected double getDyToStayAboveZero(double dy, double totDy)
		{
			for (int i = 0; i < views.length; ++i)
			{
				CellView cellView = views[i];
				EAMGraphCell cell = (EAMGraphCell) cellView.getCell();
				if (cell.isFactor())
				{
					dy = calculateDeltaYForFactor(dy, totDy, cell);
				}
				else if (cell.isFactorLink())
				{
					dy = calculateDeltaYForLink(dy, totDy, cell);
				}		
			}
			
			return dy;
		}

		private double calculateDeltaXForLink(double dx, double totDx, EAMGraphCell cell)
		{
			LinkCell linkCell = (LinkCell) cell;
			PointList bendPoints = linkCell.getDiagramLink().getBendPoints();
			int[] selectedIndexes = linkCell.getSelectedBendPointIndexes();
			for (int j = 0 ; j < selectedIndexes.length; ++j)
			{
				Point bendPoint = bendPoints.get(selectedIndexes[j]);
				Point2D.Double scaledBendPoint = getDiagram().getScaledPoint(bendPoint);
				double proposedX = scaledBendPoint.x + totDx; 
				dx = getProposedDelta(dx, proposedX);				
			}
			return dx;
		}

		private DiagramComponent getDiagram()
		{
			return (DiagramComponent)getGraph();
		}

		private double calculateDeltaXForFactor(double dx, double totDx, EAMGraphCell cell)
		{
			FactorCell factorCell = (FactorCell) cell;
			Rectangle2D bounds = getDiagram().getScaledRectangle(factorCell.getBounds());
			double proposedX = bounds.getX() + totDx; 
			dx = getProposedDelta(dx, proposedX);

			return dx;
		}

		private double calculateDeltaYForLink(double dy, double totDy, EAMGraphCell cell)
		{
			LinkCell linkCell = (LinkCell) cell;
			PointList bendPoints = linkCell.getDiagramLink().getBendPoints();
			int[] selectedIndexes = linkCell.getSelectedBendPointIndexes();
			for (int j = 0 ; j < selectedIndexes.length; ++j)
			{
				Point bendPoint = bendPoints.get(selectedIndexes[j]);
				Point2D.Double scaledBendPoint = getDiagram().getScaledPoint(bendPoint);
				double proposedY = scaledBendPoint.y + totDy; 
				dy = getProposedDelta(dy, proposedY);		
			}
			
			return dy;
		}

		private double calculateDeltaYForFactor(double dy, double totDy, EAMGraphCell cell)
		{
			FactorCell factorCell = (FactorCell) cell;
			Rectangle2D bounds = getDiagram().getScaledRectangle(factorCell.getBounds());
			double proposedY = bounds.getY() + totDy; 
			dy = getProposedDelta(dy, proposedY);

			return dy;
		}
		
		private double getProposedDelta(double delta, double proposedPosition)
		{
			if (proposedPosition < 0)
				return Math.min(0, delta - proposedPosition);
			
			return delta;
		}

		public void updateControlPoints(CellView[] viewsToUse, double deltaX, double deltaY)
		{			
			Vector allToTranslate = new Vector();
			for (int i = 0; i < viewsToUse.length; ++i)
			{
				if (isFactorView(viewsToUse[i]))
				{
					allToTranslate.add(viewsToUse[i]);
				}
				
				//TODO refactor this code
				if (isFactorLinkView(viewsToUse[i]))
				{	 
					EdgeView edge = (EdgeView) viewsToUse[i];
					LinkCell linkCell = (LinkCell) edge.getCell();
					List controlPoints = edge.getPoints();
					BendPointSelectionHelper bendSelectionHelper = linkCell.getBendPointSelectionHelper();
					int[] selectedBendPointIndexes = bendSelectionHelper.getSelectedIndexes();

					if (selectedBendPointIndexes.length > 0)
					{
						Point2D.Double startPoint = convertToPoint(controlPoints.get(0));
						if (startPoint != null)
						{
							startPoint.x = (int) startPoint.x + deltaX;
							startPoint.y = (int) startPoint.y + deltaY;
						}

						for (int j = 0; j < selectedBendPointIndexes.length; ++j)
						{
							Point2D.Double point = convertToPoint(controlPoints.get(selectedBendPointIndexes[j] + 1));
							if (point == null)
								continue;
								
							point.x = (int) (point.getX() + deltaX);
							point.y = (int) point.getY() + deltaY;
						}
						
						Point2D.Double endPoint = convertToPoint(controlPoints.get(controlPoints.size() - 1));
						if (endPoint != null)
						{
							endPoint.x = (int) endPoint.x + deltaX;
							endPoint.y = (int) endPoint.y + deltaY;
						}
					}
				}
			}
			
			CellView[] viewsToTranslate = (CellView[]) allToTranslate.toArray(new CellView[0]);
			GraphLayoutCache.translateViews(viewsToTranslate, deltaX, deltaY);
		}
		
		private boolean isFactorView(CellView view)
		{
			EAMGraphCell cell = (EAMGraphCell) view.getCell();
			return cell.isFactor();
		}

		private Point2D.Double convertToPoint(Object object)
		{
			if(object instanceof SerializablePoint2D)
				return (Point2D.Double) object;
			
			return null;
		}
	}
	
	class DiagramMouseHandler extends BasicGraphUI.MouseHandler
	{
        public void mousePressed(MouseEvent event)
		{
        	super.mousePressed(event);
			processMouseEvent(event);
		}

		public void mouseReleased(MouseEvent event)
		{
			// JGraph calls this with a null event if you press Escape
			if(event == null)
				return;
			
			if(!SwingUtilities.isRightMouseButton(event))
				super.mouseReleased(event);
		}
		
	    public void mouseDragged(MouseEvent event)
		{
			if(!SwingUtilities.isRightMouseButton(event))
				super.mouseDragged(event);
		}

		private boolean processMouseEvent(MouseEvent event)
		{
			if (event.isConsumed())
				return false;
			if(!getGraph().isEnabled())
				return false;
			if(!SwingUtilities.isRightMouseButton(event))
				return false;
			
			CellView thisCell = getGraph().getNextSelectableViewAt(getFocus(), event.getX(), event.getY());
			if(thisCell == null)
				return true;
			if(getGraph().isCellSelected(thisCell.getCell()))
				return true;
			
			replaceSelectionWithThisCell(thisCell);
			return false;
		}

		private void replaceSelectionWithThisCell(CellView thisCell)
		{
			cell = thisCell;
		}
	}
}
