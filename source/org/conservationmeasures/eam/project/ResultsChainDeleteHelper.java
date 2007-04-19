/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramObject;

public class ResultsChainDeleteHelper
{

	public ResultsChainDeleteHelper(Project projectToUse, DiagramPanel diagramPanelToUse)
	{
		project = projectToUse;
		diagramPanel = diagramPanelToUse;
	}
	
	public void deleteResultsChain() throws CommandFailedException
	{
		DiagramObject diagramObject = diagramPanel.getDiagramObject();
		CommandDeleteObject deleteResultsChain = new CommandDeleteObject(diagramObject.getRef());
		project.executeCommand(deleteResultsChain);
	}
	
	private Project project;
	private DiagramPanel diagramPanel;
}
