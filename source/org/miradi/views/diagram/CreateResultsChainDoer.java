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

import java.util.HashSet;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ResultsChainCreatorHelper;
import org.miradi.views.ViewDoer;

public class CreateResultsChainDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!getProject().isOpen())
			return false;
		
		if (! DiagramView.is(getView()))
			return false;
		
		if (getDiagramView().isStategyBrainstormMode())
			return false;
		
		return true;
	}

	@Override
	public void doIt() throws Exception
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
		DiagramModel diagramModel = diagramView.getDiagramModel();
		FactorCell[] selectedCells = getSelectedFactorCells(diagramView);
		ResultsChainCreatorHelper creatorHelper = new ResultsChainCreatorHelper(project, diagramModel, selectedCells);

		ORef newResultsChainRef = creatorHelper.createResultsChain();
		selectResultsChain(project, diagramView, newResultsChainRef);
		
		if (diagramModel != null && diagramModel.isConceptualModelDiagram() && wereAnyAnnotationsTransferred(project, newResultsChainRef))
			EAM.notifyDialog(EAM.text("Any Indicators or Objectives that were associated to Contributing Factors or Threats \n" +
									"have been moved to the corresponding Threat Reduction Result or Intermediate Result"));
	}

	private static boolean wereAnyAnnotationsTransferred(Project project, ORef newResultsChainRef)
	{
		ResultsChainDiagram resultsChain = ResultsChainDiagram.find(project, newResultsChainRef);
		HashSet<Factor> factors = new HashSet<Factor>();
		factors.addAll(resultsChain.getFactorsOfType(ThreatReductionResult.getObjectType()));
		factors.addAll(resultsChain.getFactorsOfType(IntermediateResult.getObjectType()));
		for (Factor factor :factors)
		{
			if (factor.getDirectOrIndirectIndicatorRefs().hasRefs())
				return true;
			
			if (factor.getObjectiveRefs().hasRefs())
				return true;
		}	
		
		return false;
	}

	private static FactorCell[] getSelectedFactorCells(DiagramView diagramView)
	{
		if (diagramView.getDiagramPanel().getCurrentDiagramComponent() == null)
			return new FactorCell[0];
		
		return diagramView.getDiagramPanel().getOnlySelectedFactorCells();
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
