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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.project.FactorCommandHelper;

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
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		try 
		{
			Transferable contents = clipboard.getContents(null);
			if(!contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor))
				return;
			TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.eamListDataFlavor);
			if(!list.getProjectFileName().equals(getProject().getFilename()))
			{
				EAM.notifyDialog(EAM.text("Paste between different e-AM projects not yet supported"));
				return;
			}
			pasteCellsIntoProject(list);
			clipboard.incrementPasteCount();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		} 
	}

	public void pasteCellsIntoProject(TransferableEamList list) throws Exception 
	{
		new FactorCommandHelper(getProject()).pasteFactorsAndLinksIntoProject(list, getLocation());
	}
}
