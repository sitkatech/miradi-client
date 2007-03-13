/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.FactorMoveHandler;
import org.conservationmeasures.eam.views.ProjectDoer;

public class NudgeDoer extends ProjectDoer
{

	public NudgeDoer(int directionToNudge)
	{
		direction = directionToNudge;
	}
	
	public boolean isAvailable()
	{
		return getProject().getOnlySelectedFactors().length > 0;
	}

	public void doIt() throws CommandFailedException
	{
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

	private void moveSelectedNodes(int deltaX, int deltaY) throws CommandFailedException
	{
		FactorCell[] cells = getProject().getOnlySelectedFactors();

		DiagramFactorId[] ids = new DiagramFactorId[cells.length];
		for(int i = 0; i < cells.length; ++i)
		{
			ids[i] = cells[i].getDiagramFactorId(); 
		}
		try
		{
			getProject().moveFactors(deltaX, deltaY, ids);
			new FactorMoveHandler(getProject()).factorsWereMovedOrResized(ids);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new CommandFailedException("Unable to move factors");
		}
	}

	int direction;
}
