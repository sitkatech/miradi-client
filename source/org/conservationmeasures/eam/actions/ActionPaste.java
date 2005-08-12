/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.TransferableEamList;

public class ActionPaste extends LocationAction
{
	public ActionPaste(MainWindow mainWindow)
	{
		this(mainWindow, new Point(0,0));
	}
	
	public ActionPaste(MainWindow mainWindow, Point startPointToUse)
	{
		super(mainWindow, getLabel(), "icons/paste.gif");
		setInvocationPoint(startPointToUse);	
	}

	private static String getLabel()
	{
		return EAM.text("Action|Paste");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Paste the clipboard");
	}
	
	public void doAction(ActionEvent event) throws CommandFailedException
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try 
		{
			Transferable contents = clipboard.getContents(null);
			if(!contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor))
				return;
			TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.eamListDataFlavor);
			
			getProject().pasteCellsIntoProject(list, createAt);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		} 
	}

	public boolean shouldBeEnabled()
	{
		if(!getProject().isOpen())
			return false;
		return true;//TODO: Tie this to the clipboard contents
	}
	
}
