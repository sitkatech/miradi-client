/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.actions.ActionCopy;
import org.miradi.actions.ActionDelete;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;

public class Cut extends ViewDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		if (! isInDiagram())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		Copy copyDoer = (Copy)getView().getDoer(ActionCopy.class);
		DeleteSelectedItemDoer deleteDoer = (DeleteSelectedItemDoer)getView().getDoer(ActionDelete.class);
		copyDoer.doIt();
		deleteDoer.doIt();
		copyDoer.decrementPasteCount();
	}

}
