/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.ToolTipManager;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.FactorMoveHandler;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.LinkBendPointsMoveHandler;
import org.conservationmeasures.eam.views.diagram.NudgeDoer;
import org.conservationmeasures.eam.views.diagram.PropertiesDoer;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;


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
		dragStartedAt = event.getPoint();
//TODO commented by Nima - consult with kevin - getFirstCellForLocation does not return a bend point.
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

			for(int i = 0; i < selectedCells.length; ++i)
			{
				EAMGraphCell selectedCell = (EAMGraphCell)selectedCells[i];
				if((selectedCell).isFactor())
				{
					FactorCell factor = (FactorCell)selectedCells[i];
					factor.setPreviousLocation(factor.getLocation());
					factor.setPreviousSize(factor.getSize());
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
		
		Point snappedDragEndedAt = getProject().getSnapped(event.getPoint());
		Point snappedDragStartAt = getProject().getSnapped(dragStartedAt);
		int deltaX = snappedDragEndedAt.x - snappedDragStartAt.x; 
		int deltaY = snappedDragEndedAt.y - snappedDragStartAt.y;
		if(deltaX == 0 && deltaY == 0)
			return;
		
		moveHasHappened(deltaX, deltaY);
	}

	public void mouseEntered(MouseEvent arg0)
	{
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		savedToolTipInitialDelay = toolTipManager.getInitialDelay();
		savedToolTipReshowDelay = toolTipManager.getReshowDelay();
		savedToolTipDismissDelay = toolTipManager.getDismissDelay();
		toolTipManager.setInitialDelay(TOOLTIP_DEFAULT_MILLIS);
		toolTipManager.setReshowDelay(TOOLTIP_ALWAYS_WAIT_BEFORE_RESHOW);
		toolTipManager.setDismissDelay(TOOLTIP_NEVER_AUTO_DISMISS);

		super.mouseEntered(arg0);
	}

	public void mouseExited(MouseEvent arg0)
	{
		super.mouseExited(arg0);
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		toolTipManager.setInitialDelay(savedToolTipInitialDelay);
		toolTipManager.setReshowDelay(savedToolTipReshowDelay);
		toolTipManager.setDismissDelay(savedToolTipDismissDelay);
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
			
			moveLinkBendPointInGroupBoxes(deltaX, deltaY);
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagram().getDiagramModel());
			DiagramFactorId[] selectedFactorIdsArray = (DiagramFactorId[]) selectedFactorIds.toArray(new DiagramFactorId[0]);
			factorMoveHandler.factorsWereMovedOrResized(selectedFactorIdsArray);
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

	private void moveLinkBendPointInGroupBoxes(int deltaX, int deltaY) throws Exception
	{
		LinkCell[] linkCells = selectedAndGroupBoxCoveredLinkCells.toArray(new LinkCell[0]);
		for (int i = 0; i < linkCells.length; ++i)
		{
			LinkBendPointsMoveHandler moveHandler = new LinkBendPointsMoveHandler(getProject());
			moveHandler.moveBendPoints(linkCells[i], deltaX, deltaY);
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
			EAMGraphCell selectedCell = (EAMGraphCell)selectionTransitionCells[i];
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

	private static final int TOOLTIP_DEFAULT_MILLIS = 1000;
	private static final int TOOLTIP_ALWAYS_WAIT_BEFORE_RESHOW = 0;
	private static final int TOOLTIP_NEVER_AUTO_DISMISS = Integer.MAX_VALUE;

	private MainWindow mainWindow;
	private Point dragStartedAt;
	private Object[] selectedCells;
	private HashSet<LinkCell> selectedAndGroupBoxCoveredLinkCells;
	private int savedToolTipInitialDelay;
	private int savedToolTipReshowDelay;
	private int savedToolTipDismissDelay;
}
