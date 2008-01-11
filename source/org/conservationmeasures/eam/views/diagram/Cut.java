/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

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
