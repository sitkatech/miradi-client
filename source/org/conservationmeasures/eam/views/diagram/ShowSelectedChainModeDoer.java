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
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
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

		if(getProject().getOnlySelectedCells().length < 1)
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
			DiagramComponent diagram = view.getDiagramComponent();
			
			if (project.getOnlySelectedCells().length == 1)
				SelectChain.selectAllChainsRelatedToAllSelectedCells(diagram);

			DiagramFactor[] orphanedDaftStrats = getOrphanedDraftStrategies(project, view, diagram);
			DiagramFactor[] selectedNodes = project.getOnlySelectedFactors();
			
			BaseId viewId = getCurrentViewId();
			IdList nodeIdsToProcess = new IdList();
			
			addFactorsToList(selectedNodes, nodeIdsToProcess);
			addFactorsToList(orphanedDaftStrats, nodeIdsToProcess);
			
			project.executeCommand(new CommandBeginTransaction());
			project.executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
					ViewData.TAG_BRAINSTORM_NODE_IDS, nodeIdsToProcess.toString()));
			
			project.executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
					ViewData.TAG_CURRENT_MODE, ViewData.MODE_STRATEGY_BRAINSTORM));
			project.executeCommand(new CommandEndTransaction());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private void addFactorsToList(DiagramFactor[] orphanedDaftStrats, IdList selectedNodeIds)
	{
		for(int i = 0; i < orphanedDaftStrats.length; ++i)
		{
			selectedNodeIds.add(orphanedDaftStrats[i].getWrappedId());
		}
	}

	private DiagramFactor[] getOrphanedDraftStrategies(Project project, DiagramView view, DiagramComponent diagram)
	{
		Vector diagramFactors = new Vector();
		DiagramModel model = project.getDiagramModel();
		Factor[] factors = project.getFactorPool().getStrategies();
		for (int i=0; i<factors.length; ++i)
		{
			if (!factors[i].isStatusDraft())
				continue;
			DiagramFactor diagramFactor = model.getDiagramFactorByWrappedId(factors[i].getFactorId());
			if (model.getFactorLinks(diagramFactor).size() > 0) 
				continue;
			diagramFactors.add(diagramFactor);
		}
		return (DiagramFactor[])diagramFactors.toArray(new DiagramFactor[0]);
	}
	
	private BaseId getCurrentViewId() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getId();
	}
}
