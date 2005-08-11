/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;

public class ActionPaste extends ProjectAction
{
	public ActionPaste(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), "icons/paste.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Paste");
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
			getProject().getDiagramModel().pasteCellsIntoProject(list);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		} 
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Paste the clipboard");
	}

}
