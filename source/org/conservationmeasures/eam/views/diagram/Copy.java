/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.TransferableMiradiList;
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
		try
		{
			copySelectedItemsToMiradiClipboard();
			copySelectedItemsToEAMClipboard();
			getProject().getDiagramClipboard().incrementPasteCount();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private void copySelectedItemsToMiradiClipboard() throws Exception
	{
		//FIXME temp swith beween transitions of two flavors
		if (TransferableEamList.IS_EAM_FLAVOR)
			return;
		
		EAMGraphCell[] selectedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		TransferableMiradiList miradiList = new TransferableMiradiList(getProject());
		miradiList.storeData(selectedCells);
		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(miradiList, getMainWindow());
	}
	
	//TODO this method is duplicate and will go away after new miradi flavor takes over
	public void copySelectedItemsToEAMClipboard()
	{
		//FIXME temp swith beween transitions of two flavors
		if (! TransferableEamList.IS_EAM_FLAVOR)
			return;
		
		EAMGraphCell[] selectedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		if(selectedCells.length == 0)
			return;
		TransferableEamList eamList = new TransferableEamList(getProject().getFilename(), selectedCells);
		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(eamList, getMainWindow());
	}
}
