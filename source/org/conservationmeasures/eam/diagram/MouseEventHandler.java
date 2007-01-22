/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.FactorMoveHandler;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.Properties;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;


public class MouseEventHandler implements MouseListener, GraphSelectionListener
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
		Object cellBeingPressed = getDiagram().getFirstCellForLocation(dragStartedAt.getX(), dragStartedAt.getY());
		if(cellBeingPressed == null)
		{
			dragStartedAt = null;
			return;
		}
		for(int i = 0; i < selectedCells.length; ++i)
		{
			if(((EAMGraphCell)selectedCells[i]).isFactor())
			{
				DiagramFactor factor = (DiagramFactor)selectedCells[i];
				factor.setPreviousLocation(factor.getLocation());
				factor.setPreviousSize(factor.getSize());
			}
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
		
		Vector selectedFactors = new Vector();
		for(int i = 0; i < selectedCells.length; ++i)
		{
			if(((EAMGraphCell)selectedCells[i]).isFactor())
				selectedFactors.add(selectedCells[i]);
		}
		
		if(selectedFactors.size() == 0)
			return;
		
		DiagramFactorId[] selectedFactorIds = new DiagramFactorId[selectedFactors.size()];
		for(int i = 0; i < selectedFactors.size(); ++i)
		{
			selectedFactorIds[i] = ((DiagramFactor)selectedFactors.get(i)).getDiagramFactorId();
		}

		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;
		
		if(deltaX == 0 && deltaY == 0)
			return;

		// adjust for snap
		DiagramFactor node = (DiagramFactor)selectedFactors.get(0);
		deltaX = node.getLocation().x - node.getPreviousLocation().x;
		deltaY = node.getLocation().y - node.getPreviousLocation().y;

		try
		{
			new FactorMoveHandler(getProject()).factorsWereMovedOrResized(deltaX, deltaY, selectedFactorIds);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error");
		}
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent event)
	{
		if(event.getClickCount() == 2)
		{
			try 
			{
				Point at = event.getPoint();
				Properties doer = new Properties(getDiagram());
				doer.setMainWindow(mainWindow);
				doer.setView(mainWindow.getCurrentView());
				doer.setLocation(at);
				doer.doIt();
			} 
			catch (CommandFailedException e) 
			{
				e.printStackTrace();
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
		selectedCells = getDiagram().getSelectionCells();
		getActions().updateActionStates();
		if(mainWindow.getCurrentView().cardName().equals(DiagramView.getViewName()))
		{
			DiagramView view = (DiagramView)mainWindow.getCurrentView();
			view.selectionWasChanged();
		}
	}

	MainWindow mainWindow;
	Point dragStartedAt;
	Object[] selectedCells;
}
