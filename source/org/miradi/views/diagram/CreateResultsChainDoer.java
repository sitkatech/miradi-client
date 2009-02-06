/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ResultsChainCreatorHelper;
import org.miradi.views.ViewDoer;

public class CreateResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (!getProject().isOpen())
			return false;
		
		if (! getView().cardName().equals(DiagramView.getViewName()))
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

		ORef newResultsChainRef = creatorHelper.createResultsChain();
		selectResultsChain(project, diagramView, newResultsChainRef);
	}

	public static void selectResultsChain(Project project, DiagramView diagramView, ORef newResultsChainRef) throws Exception
	{
		int newTabIndex = diagramView.getTabIndex(newResultsChainRef);
		ViewData viewData = project.getViewData(diagramView.cardName());
		
		CommandSetObjectData setTabCommand = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_TAB, Integer.toString(newTabIndex));
		project.executeCommand(setTabCommand);
		
		CommandSetObjectData setCurrentDiagram = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_RESULTS_CHAIN_REF, newResultsChainRef);
		project.executeCommand(setCurrentDiagram);
	}	
}
