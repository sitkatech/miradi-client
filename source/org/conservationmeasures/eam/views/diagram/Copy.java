/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.views.ViewDoer;

public class Copy extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! getProject().isOpen())
			return false;

		if (! isInDiagram())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			copySelectedItemsToMiradiClipboard();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private void copySelectedItemsToMiradiClipboard() throws Exception
	{
		final DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		EAMGraphCell[] selectedCells = diagramPanel.getSelectedAndRelatedCells();
		TransferableMiradiList miradiList = new TransferableMiradiList(getProject(), diagramPanel.getDiagramObject().getRef());
		miradiList.storeData(selectedCells);
		
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		clipboard.setContents(miradiList, getMainWindow());
	}
	
	public  void decrementPasteCount()
	{
		getProject().getDiagramClipboard().decrementPasteCount();
	}
}
