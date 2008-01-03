/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.FactorMoveHandler;
import org.conservationmeasures.eam.utils.PointList;

public class NudgeDoer extends LocationDoer
{

	public NudgeDoer(int directionToNudge)
	{
		direction = directionToNudge;
	}
	
	public boolean isAvailable()
	{
		if (!isDiagramView())
			return false;
		
		return isAnythingSelectedInDiagram();
	}

	private boolean isAnythingSelectedInDiagram()
	{
		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		return diagramPanel.getOnlySelectedFactorCells().length > 0 || diagramPanel.getOnlySelectedLinkCells().length > 0;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		int deltaX = 0;
		int deltaY = 0;
		switch(direction)
		{
			case KeyEvent.VK_UP:
				deltaY = -getProject().getGridSize();
				break;
			case KeyEvent.VK_DOWN:
				deltaY = getProject().getGridSize();
				break;
			case KeyEvent.VK_LEFT:
				deltaX = -getProject().getGridSize();
				break;
			case KeyEvent.VK_RIGHT:
				deltaX = getProject().getGridSize();
				break;
		}
		EAM.logVerbose("NudgeNodes ("+deltaX + ","+deltaY+")");
		moveSelectedItems(deltaX, deltaY);
	}

	private boolean isFutureCellLocationInsideDiagramBounds(Point currentLocation, int deltaX, int deltaY)
	{
		int futureX = currentLocation.x + deltaX;
		int futureY = currentLocation.y + deltaY;
		
		if (futureX < 0 || futureY < 0)
			return false;
	
		return true;
	}
	
	private void moveSelectedItems(int deltaX, int deltaY) throws CommandFailedException
	{
		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		FactorCell[] cells = diagramPanel.getOnlySelectedFactorCells();
		LinkCell[] links = diagramPanel.getOnlySelectedLinkCells();
		DiagramFactorId[] ids = new DiagramFactorId[cells.length];
		for(int i = 0; i < cells.length; ++i)
		{
			ids[i] = cells[i].getDiagramFactorId();
			if (!isFutureCellLocationInsideDiagramBounds(cells[i].getLocation(), deltaX, deltaY))
				return;			
		}
		
		if (wouldMoveBendPointsOutOfBounds(links, deltaX, deltaY))
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			diagramPanel.moveFactors(deltaX, deltaY, ids);
			
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagramView().getDiagramModel());
			factorMoveHandler.factorsWereMovedOrResized(ids);
			moveBendPoints(links, deltaY, deltaX);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unable to move selected items");
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
		
		
	}

	private void moveBendPoints(LinkCell[] links, int deltaY, int deltaX) throws Exception
	{
		LinkBendPointsMoveHandler bendPointsMoveHandler = new LinkBendPointsMoveHandler(getProject());
		bendPointsMoveHandler.moveLinkBendPoints(links, deltaX, deltaY);
	}

	private boolean wouldMoveBendPointsOutOfBounds(LinkCell[] links, int deltaX, int deltaY)
	{
		for (int i = 0; i < links.length; ++i)
		{
			LinkCell linkCell = links[i];
			if (wouldMoveBendPointsOutOfBounds(linkCell, deltaX, deltaY))
				return true;
		}
		return false;
	}

	private boolean wouldMoveBendPointsOutOfBounds(LinkCell linkCell, int deltaX, int deltaY)
	{
		PointList bendPoints = linkCell.getDiagramLink().getBendPoints();
		int[] selectedIndexes = linkCell.getSelectedBendPointIndexes();
		for (int i = 0; i < selectedIndexes.length; ++i)
		{
			int selectionIndex = selectedIndexes[i];
			Point selectedBendPoint = bendPoints.get(selectionIndex);
			if (!isFutureCellLocationInsideDiagramBounds(selectedBendPoint, deltaX, deltaY))
				return true;
		}
		return false;
	}

	int direction;
}
