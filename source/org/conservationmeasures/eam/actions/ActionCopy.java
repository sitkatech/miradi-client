/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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
	//	Toolkit toolkit = Toolkit.getDefaultToolkit();
	//	Clipboard clipboard = toolkit.getSystemClipboard();
	//	clipboard.setContents(tb, mainWindow);
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Copy the selection to the clipboard");
	}

}
