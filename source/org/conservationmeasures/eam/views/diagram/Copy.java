/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Copy extends ProjectDoer
{
	public Copy()
	{
		super();
	}
	
	public Copy(Project project)
	{
		setProject(project);
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

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
