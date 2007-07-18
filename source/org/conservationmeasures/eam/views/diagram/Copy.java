/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class Copy extends ViewDoer
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
		if (! getProject().isOpen())
			return false;

		if (! isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		copySelectedItemsToEAMClipboard();
		getProject().getDiagramClipboard().incrementPasteCount();
		copySelectedItemsToMiradiClipboard();
	}

	private void copySelectedItemsToMiradiClipboard()
	{
		EAMGraphCell[] selectedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;

		//FIXME nima copy/paste now add deep copies of selected objects 
		TransferableEamList eamList = new TransferableEamList(getProject().getFilename(), selectedCells);		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(eamList, getMainWindow());
	}

	public void copySelectedItemsToEAMClipboard()
	{
		EAMGraphCell[] selectedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;
		TransferableEamList eamList = new TransferableEamList(getProject().getFilename(), selectedCells);
		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(eamList, getMainWindow());
	}
}
