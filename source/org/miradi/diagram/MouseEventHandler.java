/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphLayoutCache;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramLink;
import org.miradi.project.FactorMoveHandler;
import org.miradi.project.Project;
import org.miradi.utils.CommandVector;
import org.miradi.utils.PointList;
import org.miradi.views.diagram.DiagramView;
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
		return mainWindow.getCurrentDiagramComponent();
	}

	//Note: customMarquee Handler will get notified first
	@Override
	public void mousePressed(MouseEvent event)
	{
		selectedAndGroupBoxCoveredLinkCells = new HashSet<LinkCell>();
		dragStartedAt = null;
		if(event.isPopupTrigger())
		{
			disableMarqueeWhichGetsEnabledOnLinuxForRightPress();
			getDiagram().showContextMenu(event);
			return;
		}

		if (mainWindow.getCurrentDiagramComponent().isMarquee())
			return;
		
		startDragOperation(event);
	}

	private void disableMarqueeWhichGetsEnabledOnLinuxForRightPress()
	{
		getDiagram().setMarquee(false);
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

	@Override
	public void mouseReleased(MouseEvent event)
	{
		mainWindow.getCurrentDiagramComponent().setMarquee(false);
		if(event.isPopupTrigger())
		{
			getDiagram().showContextMenu(event);
			dragStartedAt = null;
			return;
		}

		if(dragStartedAt == null)
			return;
		
		if(!dragStartedAt.equals(new Point(event.getX(), event.getY())))
			moveHasHappened();
	}

	private void moveHasHappened()
	{
		getProject().recordCommand(new CommandBeginTransaction());
		try
		{
			ORefList diagramFactorRefs = new ORefList();
			for(int i = 0; i < selectedCells.length; ++i)
			{
				EAMGraphCell selectedCell = (EAMGraphCell)selectedCells[i];
				if(selectedCell.isFactor())
				{
					FactorCell factorCell = (FactorCell)selectedCells[i];
					diagramFactorRefs.add(factorCell.getDiagramFactorRef());
				}
			}
			
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagram().getDiagramModel());
			factorMoveHandler.factorsWereMovedOrResized(diagramFactorRefs);
			moveBendPoints();
			factorMoveHandler.ensureLevelSegementToFirstBendPoint(diagramFactorRefs);
			
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
		
		mainWindow.getCurrentDiagramComponent().setMarquee(false);
	}

	private void synchronizeFactorAndLinkCellsWithStoredObjects() throws Exception
	{
		DiagramModel model = getDiagram().getDiagramModel();

		for(int i = 0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isFactor())
				model.updateCellFromDiagramFactor(cell.getDiagramFactor().getRef());
			else if(cell.isFactorLink())
				model.updateCellFromDiagramFactorLink(cell.getDiagramLink().getRef());
			model.updateCell(cell);
		}
	}

	private void moveBendPoints() throws Exception
	{
		CommandVector bendPointsMoveCommands = new CommandVector();
		LinkCell[] linkCells = selectedAndGroupBoxCoveredLinkCells.toArray(new LinkCell[0]);
		for (int i = 0; i < linkCells.length; ++i)
		{
			EdgeView view = (EdgeView) getDiagram().getGraphLayoutCache().getMapping(linkCells[i], false);
			DiagramLink diagramLink = linkCells[i].getDiagramLink();
			if(DiagramLink.find(getProject(), diagramLink.getRef()) == null)
			{
				// NOTE: MRD-3079, this was happening for: 
				// undo paste of so many bend points that the handles don't show, then click on a factor
				// The real solution would be to always unselect before removing cells from JGraph
				EAM.logDebug("Skipping bend point move for link that was deleted: " + diagramLink.getRef());
				continue;
			}
			if (view == null)
			{
				if (!diagramLink.isCoveredByGroupBoxLink())
					EAM.logWarning("Found Link (" + diagramLink.getRef() + ")without a view and not covered by a group box link");
				
				continue;
			}
			
			PointList graphCurrentBendPoints = linkCells[i].getJGraphCurrentBendPoints(view);			
			CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(diagramLink, DiagramLink.TAG_BEND_POINTS, graphCurrentBendPoints);
			bendPointsMoveCommands.add(bendPointMoveCommand);
		}
		
		getProject().executeCommands(bendPointsMoveCommands);
	}
	
	@Override
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
				if (doer.isAvailable())
					doer.safeDoIt();
			} 
			catch (Exception e) 
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
		try
		{
			selectionChanged(event);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	public void selectionChanged(GraphSelectionEvent event) throws Exception
	{
		UmbrellaView currentView = mainWindow.getCurrentView();
		if(currentView == null)
			return;
		if(DiagramView.is(currentView))
		{
			getDiagram().selectAllLinksAndTheirBendPointsInsideGroupBox(getDiagram().getOnlySelectedFactorAndGroupChildCells());
			selectedCells = getDiagram().getSelectionCells();

			if(Arrays.equals(previouslySelected, selectedCells))
				return;

			DiagramView view = (DiagramView)mainWindow.getCurrentView();
			view.selectionWasChanged();
			updateBendPointSelection(event);
			previouslySelected = getDiagram().getSelectionCells();

			mainWindow.updateActionStates();
			final DiagramComponent diagram = view.getCurrentDiagramComponent();
			diagram.forceRepaint();
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
		HashSet<LinkCell> linksWithBendPoints = new HashSet<LinkCell>();
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
	private Object[] previouslySelected;
}
