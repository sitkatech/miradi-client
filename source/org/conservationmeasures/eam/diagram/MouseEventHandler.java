/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import org.conservationmeasures.eam.actions.Actions;
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

	Project getProject()
	{
		return mainWindow.getProject();
	}

	DiagramComponent getDiagram()
	{
		return mainWindow.getDiagramComponent();
	}

	Actions getActions()
	{
		return mainWindow.getActions();
	}

	public void mousePressed(MouseEvent event)
	{
		dragStartedAt = null;
		if(event.isPopupTrigger())
		{
			getDiagram().showContextMenu(event);
			return;
		}

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
		if(event.isPopupTrigger())
		{
			getDiagram().showContextMenu(event);
			dragStartedAt = null;
			return;
		}

		if(dragStartedAt == null)
			return;
		
		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;

		if(deltaX == 0 && deltaY == 0)
			return;
		
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

				if(selectedCell.isFactorLink())
					moveBendPoints((LinkCell) selectedCells[i], deltaX, deltaY);
			}

			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagram().getDiagramModel());
			DiagramFactorId[] selectedFactorIdsArray = (DiagramFactorId[]) selectedFactorIds.toArray(new DiagramFactorId[0]);
			factorMoveHandler.factorsWereMovedOrResized(selectedFactorIdsArray);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error");
		}
		finally
		{
			getProject().recordCommand(new CommandEndTransaction());
		}
		
		mainWindow.getDiagramComponent().setMarquee(false);
	}

	public void mouseClicked(MouseEvent event)
	{
		if(event.getClickCount() == 2)
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

	private void moveBendPoints(LinkCell linkCell, int deltaX, int deltaY) throws CommandFailedException
	{
		if (mainWindow.getDiagramComponent().isMarquee())
			return;
		
		LinkBendPointsMoveHandler moveHandler = new LinkBendPointsMoveHandler(getProject());
		moveHandler.moveBendPoints(linkCell, deltaX, deltaY);
	}

	MainWindow mainWindow;
	Point dragStartedAt;
	Object[] selectedCells;
}
