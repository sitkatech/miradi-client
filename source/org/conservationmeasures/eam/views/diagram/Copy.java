/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class Copy extends MainWindowDoer
{
	public Copy(MainWindow mainWIndow)
	{
		super(mainWIndow);
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
		EAMGraphCell[] selectedCells = getMainWindow().getProject().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;
		TransferableEamList eamList = new TransferableEamList(selectedCells);
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(eamList, EAM.mainWindow);
		getMainWindow().getActions().updateActionStates();
	}

}
