/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.diagram.cellviews.FactorLinkView;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.JGraph;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.AttributeMap.SerializablePoint2D;
import org.jgraph.plaf.basic.BasicGraphUI;

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
		EAMGraphCell cell = (EAMGraphCell) view.getCell();
		return cell.isFactorLink();
	}

	class CustomRootHandle extends BasicGraphUI.RootHandle
	{
		public CustomRootHandle(GraphContext ctx)
		{
			super(ctx);
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
				
				//TODO nima refactor this code
				if (isFactorLinkView(viewsToUse[i]))
				{	 
					EdgeView edge = (EdgeView) viewsToUse[i];
					LinkCell linkCell = (LinkCell) edge.getCell();
					List points = edge.getPoints();
					BendPointSelectionHelper bendSelectionHelper = linkCell.getBendPointSelectionHelper();
					int[] selectedIndexes = bendSelectionHelper.getSelectedIndexes();

					if (selectedIndexes.length > 0)
					{

						Point2D.Double startPoint = convertToPoint(points.get(0));
						if (startPoint != null)
						{
							startPoint.x = (int) startPoint.x + deltaX;
							startPoint.y = (int) startPoint.y + deltaY;
						}

						for (int j = 0; j < selectedIndexes.length; ++j)
						{
							Point2D.Double point = convertToPoint(points.get(selectedIndexes[j] + 1));
							if (point == null)
								continue;

							point.x = (int) (point.getX() + deltaX);
							point.y = (int) point.getY() + deltaY;
						}
						
						Point2D.Double endPoint = convertToPoint(points.get(points.size() - 1));
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
			if(!this.processMouseEvent(event))
				super.mousePressed(event);
			
			if (SwingUtilities.isRightMouseButton(event))
				updateBendPointSelection(event);
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
		
		private void updateBendPointSelection(MouseEvent event)
		{
			CellView thisCell = getGraph().getNextSelectableViewAt(getFocus(), event.getX(), event.getY());
			if (! isFactorLinkView(thisCell))
				return;
			
			FactorLinkView linkView = (FactorLinkView) thisCell;
			LinkCell linkCell = (LinkCell) linkView.getCell();
			PointList bendPoints = linkCell.getDiagramFactorLink().getBendPoints();
			BendPointSelectionHelper selectionHelper = linkCell.getBendPointSelectionHelper();
			for (int i = 0; i < bendPoints.size(); ++i)
			{
				Point bendPoint = bendPoints.get(i);
				//TODO nima is this range ok, what is the bend point height/2? 
				if (bendPoint.distance(event.getPoint()) < 20)
				{
					selectionHelper.addToSelectionIndexList(i);
					repaintGraph();
					return;
				}
			}
			
		}

		private void repaintGraph()
		{
			getGraph().paint(getGraph().getGraphics());
		}
	}
}
