/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Cut extends ProjectDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		EAMGraphCell[] selected = getProject().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		new Copy(getProject()).doIt();
		new Delete(getProject()).doIt();
	}

}
