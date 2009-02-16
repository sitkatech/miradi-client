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
import org.miradi.diagram.PersistentDiagramModel;
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

		PersistentDiagramModel diagramModel = diagramPanel.getDiagramModel();
		
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

	private void setToNormalMode() throws Exception
	{
		ShowFullModelModeDoer.showFullModelMode(getProject(), getDiagramView().getCurrentDiagramComponent());
	}
}
