/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class Cut extends MainWindowDoer
{
	public Cut(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public boolean isAvailable()
	{
		if(!getMainWindow().getProject().isOpen())
			return false;

		EAMGraphCell[] selected = getMainWindow().getProject().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		new Copy(getMainWindow()).doIt();
		new Delete(getMainWindow().getProject()).doIt();
	}

}
