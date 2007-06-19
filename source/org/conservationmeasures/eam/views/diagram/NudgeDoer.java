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
import org.conservationmeasures.eam.dialogs.DiagramPanel;
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
		//TODO nima refactor extract this loop in a method
		for(int i = 0; i < cells.length; ++i)
		{
			ids[i] = cells[i].getDiagramFactorId();
			if (!isFutureCellLocationInsideDiagramBounds(cells[i].getLocation(), deltaX, deltaY))
				return;			
		}
		
		if (containsBendPointsMovableOutSideBounds(links, deltaX, deltaY))
			return;
		
		getProject().recordCommand(new CommandBeginTransaction());
		try
		{
			diagramPanel.moveFactors(deltaX, deltaY, ids);
			
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagramView().getDiagramModel());
			factorMoveHandler.factorsWereMovedOrResized(ids);
			moveBendPoints(diagramPanel, links, deltaY, deltaX);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unable to move selected items");
		}
		finally
		{
			getProject().recordCommand(new CommandEndTransaction());
		}
		
		
	}

	private void moveBendPoints(DiagramPanel diagramPanel, LinkCell[] links, int deltaY, int deltaX) throws Exception
	{
		LinkBendPointsMoveHandler bendPointsMoveHandler = new LinkBendPointsMoveHandler(diagramPanel.getdiagramComponent(), getProject());
		bendPointsMoveHandler.moveLinkBendPoints(links, deltaX, deltaY);
	}

	private boolean containsBendPointsMovableOutSideBounds(LinkCell[] links, int deltaX, int deltaY)
	{
		for (int i = 0; i < links.length; ++i)
		{
			LinkCell linkCell = links[i];
			if (containsOutOfBoundsMovableBendPoint(linkCell, deltaX, deltaY))
				return true;
		}
		return false;
	}

	private boolean containsOutOfBoundsMovableBendPoint(LinkCell linkCell, int deltaX, int deltaY)
	{
		PointList bendPoints = linkCell.getDiagramFactorLink().getBendPoints();
		for (int i = 0; i < bendPoints.size(); ++i)
		{
			if (!isFutureCellLocationInsideDiagramBounds(bendPoints.get(i), deltaX, deltaY))
				return true;
		}
		return false;
	}

	int direction;
}
