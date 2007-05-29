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
		moveSelectedNodes(deltaX, deltaY);
	}

	private boolean isFutureCellLocationInsideDiagramBounds(FactorCell factorCell, int deltaX, int deltaY)
	{
		Point cellLocation = factorCell.getLocation();
		int futureX = cellLocation.x + deltaX;
		int futureY = cellLocation.y + deltaY;
		
		if (futureX < 0 || futureY < 0)
			return false;
	
		return true;
	}
	
	private void moveSelectedNodes(int deltaX, int deltaY) throws CommandFailedException
	{
		FactorCell[] cells = getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
		LinkCell[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinkCells();
		DiagramFactorId[] ids = new DiagramFactorId[cells.length];
		for(int i = 0; i < cells.length; ++i)
		{
			ids[i] = cells[i].getDiagramFactorId();
			if (!isFutureCellLocationInsideDiagramBounds(cells[i], deltaX, deltaY))
				return;
		}
		
		getProject().recordCommand(new CommandBeginTransaction());
		try
		{
			getDiagramView().getDiagramPanel().moveFactors(deltaX, deltaY, ids);
			
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagramView().getDiagramModel());
			factorMoveHandler.factorsWereMovedOrResized(ids);
			new LinkBendPointsMoveHandler(getProject()).moveLinkBendPoints(links, deltaX, deltaY);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unable to move factors");
		}
		finally
		{
			getProject().recordCommand(new CommandEndTransaction());
		}
		
		
	}

	int direction;
}
