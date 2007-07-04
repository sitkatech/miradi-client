/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.DiagramObjectDeleteHelper;

abstract public class DeleteDiagramPageDoer extends DiagramPageDoer
{
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			DiagramPanel diagramPanel = getDiagramView().getCurrentDiagramPanel();
			DiagramObjectDeleteHelper deleteHelper = new DiagramObjectDeleteHelper(getProject(), diagramPanel);
			deleteHelper.deleteDiagram();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
}
