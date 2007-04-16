/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (getDiagramView().isResultsChainTab())
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		DiagramView view = getDiagramView();
		
		DiagramObject diagramObject = view.getCurrentDiagramPanel().getDiagramObject();
		CommandDeleteObject deleteResultsChain = new CommandDeleteObject(diagramObject.getRef());
		getProject().executeCommand(deleteResultsChain);
	}
}
