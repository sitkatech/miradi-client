/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.views.ViewDoer;

public class ShowResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! inInDiagram())
			return false;

		if (getDiagramView().isResultsChainTab())
			return false;

		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		Factor[] selectedFactors = diagramPanel.getOnlySelectedFactors();
		if (selectedFactors.length != 1)
			return false;
		
		if (! selectedFactors[0].isStrategy())
			return false;

		DiagramModel diagramModel = diagramPanel.getDiagramModel();
		
		if (diagramModel.isResultsChain())
			return false;
		
		Strategy strategy = (Strategy) selectedFactors[0];	
		
		return strategy.getResultsChains().size() >= 1;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			setToNormalMode();
			showResultsChain(getDiagramView());
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public static void showResultsChain(DiagramView diagramView) throws CommandFailedException
	{
		DiagramPanel diagramPanel = diagramView.getDiagramPanel();
		Factor[] selectedFactors = diagramPanel.getOnlySelectedFactors();
		
		Strategy strategy = (Strategy) selectedFactors[0];
		ORefList resultsChains = strategy.getResultsChains();
		final int FIRST_IN_LIST = 0;
		ORef firstChain = resultsChains.get(FIRST_IN_LIST);
		diagramView.setDiagramTab(firstChain);
	}

	private void setToNormalMode() throws CommandFailedException
	{
		EAMAction actionShowFullModelMode = getDiagramView().getActions().get(ActionShowFullModelMode.class);
		actionShowFullModelMode.doAction();
	}
}
