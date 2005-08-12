/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.TransferableEamList;

public class Paste extends LocationDoer
{
	public Paste(BaseProject project, Point invocationPoint)
	{
		super(project, invocationPoint);
	}

	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		return true;//TODO: Tie this to the clipboard contents
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
			
			getProject().pasteCellsIntoProject(list, getLocation());

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		} 
	}

}
