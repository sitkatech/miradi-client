/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.TransferableEamList;

public class Paste extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents == null)
			return false;
		return contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor);
	}

	public void doIt() throws CommandFailedException
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try 
		{
			Transferable contents = clipboard.getContents(null);
			if(!contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor))
				return;
			TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.eamListDataFlavor);
			pasteCellsIntoProject(list);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		} 
	}

	public void pasteCellsIntoProject(TransferableEamList list) throws Exception 
	{
		getProject().pasteNodesAndLinksIntoProject(list, getLocation());
	}
}
