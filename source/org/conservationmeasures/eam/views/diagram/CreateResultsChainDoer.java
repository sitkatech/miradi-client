/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ResultsChainCreatorHelper;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! inInDiagram())
			return false;
		
		if (getDiagramView().isStategyBrainstormMode())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			createResultsChain(getProject(), getDiagramView());
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

	public static void createResultsChain(Project project, DiagramView diagramView) throws Exception
	{
		ResultsChainCreatorHelper creatorHelper = new ResultsChainCreatorHelper(project, diagramView.getDiagramPanel());

		BaseId newResultsChainId = creatorHelper.createResultsChain();
		int newTabIndex = diagramView.getTabIndex(new ORef(ObjectType.RESULTS_CHAIN_DIAGRAM, newResultsChainId));
		
		ViewData viewData = project.getViewData(diagramView.cardName());
		CommandSetObjectData setTabCommand = new CommandSetObjectData(ObjectType.VIEW_DATA, viewData.getId(), ViewData.TAG_CURRENT_TAB, Integer.toString(newTabIndex));
		project.executeCommand(setTabCommand);
	}	
	
}
