/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.TransferableEamList;

public class ActionCopy extends MainWindowAction
{
	public ActionCopy(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/copy.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Copy");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		Object[] selectedCells = mainWindow.getDiagramComponent().getSelectionCells();
		if(selectedCells.length == 0)
			return;
		TransferableEamList eamList = new TransferableEamList(selectedCells);
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(eamList, mainWindow);
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Copy the selection to the clipboard");
	}

}
