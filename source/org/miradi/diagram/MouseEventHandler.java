/* 
 * Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
 * (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.miradi.diagram;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Vector;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphLayoutCache;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.FactorMoveHandler;
import org.miradi.project.Project;
import org.miradi.utils.PointList;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.diagram.LinkBendPointsMoveHandler;
import org.miradi.views.diagram.NudgeDoer;
import org.miradi.views.diagram.PropertiesDoer;
import org.miradi.views.umbrella.UmbrellaView;


public class MouseEventHandler extends MouseAdapter implements GraphSelectionListener
{
	public MouseEventHandler(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		selectedCells = new Object[0];
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}

	private DiagramComponent getDiagram()
	{
		return mainWindow.getDiagramComponent();
	}

	//Note: customMarquee Handler will get notified first
	public void mousePressed(MouseEvent event)
	{
		selectedAndGroupBoxCoveredLinkCells = new HashSet();
		dragStartedAt = null;
		if(event.isPopupTrigger())
		{
			getDiagram().showContextMenu(event);
			return;
		}

		if (mainWindow.getDiagramComponent().isMarquee())
			return;
		
		startDragOperation(event);
	}

	private void startDragOperation(MouseEvent event)
	{
		dragStartedAt = getDiagram().getUnscaledPoint(event.getPoint());
//TODO getFirstCellForLocation does not return a bend point.
//		Object cellBeingPressed = getDiagram().getFirstCellForLocation(dragStartedAt.getX(), dragStartedAt.getY());
//		if(cellBeingPressed == null)
//		{
//			dragStartedAt = null;
//			return;
//		}
				
		try
		{
			HashSet<FactorCell> selectedFactorAndChildren = getDiagram().getOnlySelectedFactorAndGroupChildCells();
			selectedAndGroupBoxCoveredLinkCells.addAll(NudgeDoer.getAllLinksInGroupBoxes(getDiagram().getDiagramModel(), selectedFactorAndChildren));
			selectedAndGroupBoxCoveredLinkCells.addAll(getSelectedLinksWithSelectedBendPoints());
			GraphLayoutCache graphLayoutCache = getDiagram().getGraphLayoutCache();
			for(int i = 0; i < selectedCells.length; ++i)
			{
				EAMGraphCell selectedCell = (EAMGraphCell)selectedCells[i];
				if((selectedCell).isFactor())
				{
					FactorCell factor = (FactorCell)selectedCells[i];
					factor.setPreviousLocation(factor.getLocation());
					factor.setPreviousSize(factor.getSize());
					factor.setPreviousPortLocation(factor.getPortLocation(graphLayoutCache));
				}
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void mouseReleased(MouseEvent event)
	{
		mainWindow.getDiagramComponent().setMarquee(false);
		if(event.isPopupTrigger())
		{
			getDiagram().showContextMenu(event);
			dragStartedAt = null;
			return;
		}

		if(dragStartedAt == null)
			return;
		
		Point rawPoint = getDiagram().getUnscaledPoint(event.getPoint());
		Point snappedDragEndedAt = getProject().getSnapped(rawPoint);
		Point snappedDragStartAt = getProject().getSnapped(dragStartedAt);
		int deltaX = snappedDragEndedAt.x - snappedDragStartAt.x; 
		int deltaY = snappedDragEndedAt.y - snappedDragStartAt.y;

		moveHasHappened(deltaX, deltaY);
	}

	private void moveHasHappened(int deltaX, int deltaY)
	{
		getProject().recordCommand(new CommandBeginTransaction());
		try
		{
			Vector selectedFactorIds = new Vector();
			for(int i = 0; i < selectedCells.length; ++i)
			{
				EAMGraphCell selectedCell = (EAMGraphCell)selectedCells[i];
				if(selectedCell.isFactor())
					selectedFactorIds.add(((FactorCell)selectedCells[i]).getDiagramFactorId());
			}
			
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagram().getDiagramModel());
			DiagramFactorId[] selectedFactorIdsArray = (DiagramFactorId[]) selectedFactorIds.toArray(new DiagramFactorId[0]);
			factorMoveHandler.factorsWereMovedOrResized(selectedFactorIdsArray);
			moveLinkBendPointInGroupBoxes(deltaX, deltaY);
			
			synchronizeFactorAndLinkCellsWithStoredObjects();
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
		finally
		{
			getProject().recordCommand(new CommandEndTransaction());
		}
		
		mainWindow.getDiagramComponent().setMarquee(false);
	}

	private void synchronizeFactorAndLinkCellsWithStoredObjects() throws Exception
	{
		DiagramModel model = getDiagram().getDiagramModel();

		for(int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isFactor())
				model.updateCellFromDiagramFactor(cell.getDiagramFactor().getDiagramFactorId());
			else if(cell.isFactorLink())
				model.updateCellFromDiagramFactorLink(cell.getDiagramLink().getDiagramLinkageId());
			model.updateCell(cell);
		}
	}

	private void moveLinkBendPointInGroupBoxes(int deltaX, int deltaY) throws Exception
	{
		LinkBendPointsMoveHandler moveHandler = new LinkBendPointsMoveHandler(getProject());
		LinkCell[] linkCells = selectedAndGroupBoxCoveredLinkCells.toArray(new LinkCell[0]);
		for (int i = 0; i < linkCells.length; ++i)
		{
			EdgeView view = (EdgeView) getDiagram().getGraphLayoutCache().getMapping(linkCells[i], false);
			if (view == null)
			{
				if (!linkCells[i].getDiagramLink().isCoveredByGroupBoxLink())
					EAM.logWarning("Found Link (" + linkCells[i].getDiagramLink().getRef() + ")without a view and not covered by a group box link");
				
				continue;
			}
			
			PointList graphCurrentBendPoints = linkCells[i].getJGraphCurrentBendPoints(view);
			moveHandler.executeBendPointMoveCommand(linkCells[i].getDiagramLink(), graphCurrentBendPoints);
		}
	}
	
	public void mouseClicked(MouseEvent event)
	{
		if(isPropertiesEvent(event))
		{
			try 
			{
				Point at = event.getPoint();
				PropertiesDoer doer = new PropertiesDoer();
				doer.setMainWindow(mainWindow);
				doer.setView(mainWindow.getCurrentView());
				doer.setLocation(at);
				doer.doIt();
			} 
			catch (CommandFailedException e) 
			{
				EAM.logException(e);
			}
			event.consume();
		}
	}

	private boolean isPropertiesEvent(MouseEvent event)
	{
		if (event.getClickCount() != 2)
			return false;
		
		if (event.isControlDown())
			return false;
		
		if (event.isShiftDown())
			return false;
		
		return true;
	}

	// valueChanged is part of the GraphSelectionListener interface.
	// It is HORRIBLY named, so we delegate to a better-named method.
	// Don't put any code in this method. Put it in selectionChanged.
	public void valueChanged(GraphSelectionEvent event)
	{
		selectionChanged(event);
	}

	public void selectionChanged(GraphSelectionEvent event)
	{
		mainWindow.updateActionStates();
		UmbrellaView currentView = mainWindow.getCurrentView();
		if(currentView == null)
			return;
		if(currentView.cardName().equals(DiagramView.getViewName()))
		{
			selectedCells = getDiagram().getSelectionCells();
			DiagramView view = (DiagramView)mainWindow.getCurrentView();
			view.selectionWasChanged();
			updateBendPointSelection(event);
		}
	}
	
	private void updateBendPointSelection(GraphSelectionEvent event)
	{
		Object[] selectionTransitionCells = event.getCells();
		for(int i = 0; i < selectionTransitionCells.length; ++i)
		{
			Object rawCell = selectionTransitionCells[i];
			if(!(rawCell instanceof EAMGraphCell))
				continue;
			
			EAMGraphCell selectedCell = (EAMGraphCell)rawCell;
			if(selectedCell.isFactorLink() && ! event.isAddedCell(selectedCell))
			{
				LinkCell  link = (LinkCell)selectedCell;
				link.clearBendPointSelectionList();
			}
		}
	}

	private HashSet<LinkCell> getSelectedLinksWithSelectedBendPoints()
	{
		HashSet<LinkCell> linksWithBendPoints = new HashSet();
		for(int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell selectedCell = (EAMGraphCell)selectedCells[i];
			if(! selectedCell.isFactorLink())
				continue;

			LinkCell linkCell = (LinkCell) selectedCells[i];
			if (linkCell.getSelectedBendPointIndexes().length > 0)
				linksWithBendPoints.add(linkCell);
		}
		
		return linksWithBendPoints;
	}

	private MainWindow mainWindow;
	private Point dragStartedAt;
	private Object[] selectedCells;
	private HashSet<LinkCell> selectedAndGroupBoxCoveredLinkCells;
}
