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
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Copy extends ProjectDoer
{
	public Copy(BaseProject project)
	{
		super(project);
		// TODO Auto-generated constructor stub
	}

	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		// TODO: Note that changing the selection DOESN'T currently 
		// call this method
		EAMGraphCell[] selected = getProject().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		EAMGraphCell[] selectedCells = getProject().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;
		TransferableEamList eamList = new TransferableEamList(selectedCells);
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(eamList, EAM.mainWindow);
	}

}
