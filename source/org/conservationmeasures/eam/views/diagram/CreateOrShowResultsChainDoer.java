/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.ViewDoer;

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
		

		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		Factor[] selectedFactors = diagramPanel.getOnlySelectedFactors();
		if (selectedFactors.length != 1)
			return false;
		
		if (! selectedFactors[0].isStrategy())
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
			Strategy strategy = (Strategy) selectedFactors[0];
			
			if (strategy.getResultsChains().size() == 0)
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
}
