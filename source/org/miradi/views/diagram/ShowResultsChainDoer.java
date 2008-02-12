/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.actions.ActionShowFullModelMode;
import org.miradi.actions.EAMAction;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramModel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Factor;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.views.ViewDoer;

public class ShowResultsChainDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (! isInDiagram())
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
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public static void showResultsChain(DiagramView diagramView) throws Exception
	{
		DiagramPanel diagramPanel = diagramView.getDiagramPanel();
		Factor[] selectedFactors = diagramPanel.getOnlySelectedFactors();
		
		Strategy strategy = (Strategy) selectedFactors[0];
		ORefList resultsChains = strategy.getResultsChains();
		ORef firstChain = resultsChains.getRefForType(ResultsChainDiagram.getObjectType());
		
		CreateResultsChainDoer.selectResultsChain(diagramView.getProject(), diagramView, firstChain);
	}

	private void setToNormalMode() throws CommandFailedException
	{
		EAMAction actionShowFullModelMode = getDiagramView().getActions().get(ActionShowFullModelMode.class);
		actionShowFullModelMode.doAction();
	}
}
