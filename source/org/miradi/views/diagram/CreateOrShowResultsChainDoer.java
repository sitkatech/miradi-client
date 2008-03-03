/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.views.ViewDoer;

public class CreateOrShowResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! isInDiagram())
			return false;

		if (getDiagramView().isResultsChainTab())
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
			DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
			Factor[] selectedFactors = diagramPanel.getOnlySelectedFactors();
			if (shouldCreateResultsChain(selectedFactors))
				CreateResultsChainDoer.createResultsChain(getProject(), getDiagramView());
			else
				ShowResultsChainDoer.showResultsChain(getDiagramView());
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

	private boolean shouldCreateResultsChain(Factor[] selectedFactors)
	{
		if (selectedFactors.length == 0)
			return true;
		
		if (!selectedFactors[0].isStrategy())
			return true;
		
		Strategy strategy = (Strategy) selectedFactors[0];
		return strategy.getResultsChains().size() == 0;
	}
}
