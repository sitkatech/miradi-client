/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class ShowSelectedChainModeDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		try
		{
			//FIXME this check happens twice,  and also refactor this try into a method
			if(!isDiagramView())
				return false;
			ViewData viewData = getProject().getViewData(getView().cardName());
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
			if(ViewData.MODE_STRATEGY_BRAINSTORM.equals(currentViewMode))
				return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}

		if (!isDiagramView())
			return false;
		
		if (getDiagramView().isResultsChainTab())
			return false;
		
		DiagramView view = getDiagramView();
		DiagramPanel diagramPanel = view.getDiagramPanel();
		EAMGraphCell[] selectedCells = diagramPanel.getOnlySelectedCells();
		if(selectedCells.length < 1)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{	
			Project project = getMainWindow().getProject();
			DiagramView view = (DiagramView)getView();
			DiagramPanel diagramPanel = view.getDiagramPanel();
			DiagramComponent diagram = view.getDiagramComponent();
			
			if (diagramPanel.getOnlySelectedCells().length == 1)
				SelectChain.selectChainsRelatedToSelectedFactorsAndLinks(diagramPanel);

			FactorCell[] orphanedDaftStrats = getOrphanedDraftStrategies(project, view, diagram);
			FactorCell[] selectedNodes = diagramPanel.getOnlySelectedFactorCells();
			
			BaseId viewId = getCurrentViewId();
			ORefList nodeORefsToProcess = new ORefList();
			
			addFactorsToList(selectedNodes, nodeORefsToProcess);
			addFactorsToList(orphanedDaftStrats, nodeORefsToProcess);

			project.executeCommand(new CommandBeginTransaction());
			try 
			{
				project.executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
						ViewData.TAG_CHAIN_MODE_FACTOR_REFS, nodeORefsToProcess.toString()));
				
				project.executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
						ViewData.TAG_CURRENT_MODE, ViewData.MODE_STRATEGY_BRAINSTORM));
			}
			finally
			{
				project.executeCommand(new CommandEndTransaction());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private void addFactorsToList(FactorCell[] orphanedDaftStrats, ORefList selectedNodeORefs)
	{
		for(int i = 0; i < orphanedDaftStrats.length; ++i)
		{
			selectedNodeORefs.add(orphanedDaftStrats[i].getWrappedORef());
		}
	}

	private FactorCell[] getOrphanedDraftStrategies(Project project, DiagramView view, DiagramComponent diagram)
	{
		Vector diagramFactors = new Vector();
		DiagramModel model = view.getDiagramModel();
		Factor[] factors = project.getStrategyPool().getDraftStrategies();
		for (int i=0; i<factors.length; ++i)
		{
			FactorCell diagramFactor = model.getFactorCellByWrappedId(factors[i].getFactorId());
			if (model.getFactorLinks(diagramFactor).size() > 0) 
				continue;
			diagramFactors.add(diagramFactor);
		}
		return (FactorCell[])diagramFactors.toArray(new FactorCell[0]);
	}
	
	private BaseId getCurrentViewId() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getId();
	}
}
