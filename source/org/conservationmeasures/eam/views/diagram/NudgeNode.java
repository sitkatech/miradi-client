/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.event.KeyEvent;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ProjectDoer;

public class NudgeNode extends ProjectDoer
{

	public NudgeNode(int directionToNudge)
	{
		direction = directionToNudge;
	}
	
	public boolean isAvailable()
	{
		return getProject().getOnlySelectedNodes().length > 0;
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
		EAMGraphCell[] cells = getProject().getOnlySelectedNodes();

		int[] ids = new int[cells.length];
		for(int i = 0; i < cells.length; ++i)
		{
			ids[i] = cells[i].getId(); 
		}
		try
		{
			getProject().moveNodes(deltaX, deltaY, ids);
			getProject().nodesWereMovedOrResized(deltaX, deltaY, ids);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new CommandFailedException("Unable to move nodes");
		}
	}

	int direction;
}
