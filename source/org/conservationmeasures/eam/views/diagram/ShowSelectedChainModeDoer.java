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
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.diagram.doers.SelectChainDoer;

public class ShowSelectedChainModeDoer extends ViewDoer
{
	public boolean isAvailable()
	{		
		if(! isDiagramView())
			return false;

		if (! isInDefaultMode())
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
		
		getMainWindow().preventActionUpdates();
		try
		{	
			Project project = getMainWindow().getProject();
			DiagramView view = (DiagramView)getView();
			DiagramPanel diagramPanel = view.getDiagramPanel();
			DiagramComponent diagram = view.getDiagramComponent();
			
			if (diagramPanel.getOnlySelectedCells().length == 1)
				SelectChainDoer.selectChainsRelatedToSelectedFactorsAndLinks(diagramPanel);

			FactorCell[] orphanedDaftStrats = getOrphanedDraftStrategies(project, view, diagram);
			FactorCell[] selectedNodes = diagramPanel.getOnlySelectedFactorCells();
			
			BaseId viewId = getCurrentViewId();
			ORefList nodeORefsToProcess = new ORefList();
			
			addFactorsToList(selectedNodes, nodeORefsToProcess);
			addFactorsToList(orphanedDaftStrats, nodeORefsToProcess);

			changeMode(project, viewId, nodeORefsToProcess);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getMainWindow().allowActionUpdates();
		}
	}

	private void changeMode(Project project, BaseId viewId, ORefList nodeORefsToProcess) throws CommandFailedException
	{
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

	private void addFactorsToList(FactorCell[] orphanedDaftStrats, ORefList selectedNodeORefs)
	{
		for(int i = 0; i < orphanedDaftStrats.length; ++i)
		{
			selectedNodeORefs.add(orphanedDaftStrats[i].getWrappedORef());
		}
	}

	private FactorCell[] getOrphanedDraftStrategies(Project project, DiagramView view, DiagramComponent diagram)
	{
		Vector orphanedDraftStrategies = new Vector();
		DiagramModel model = view.getDiagramModel();
		Factor[] draftStrategies = project.getStrategyPool().getDraftStrategies();
		for (int i=0; i<draftStrategies.length; ++i)
		{
			FactorCell draftStrategyCell = model.getFactorCellByWrappedId(draftStrategies[i].getFactorId());
			if (isOrphan(model, draftStrategyCell))
				orphanedDraftStrategies.add(draftStrategyCell);
		}
		return (FactorCell[])orphanedDraftStrategies.toArray(new FactorCell[0]);
	}
	
	private boolean isOrphan(DiagramModel model, FactorCell draftStrategyCell)
	{
		if (draftStrategyCell == null)
			return false;
		
		if (isLinkedToNonDraft(model, draftStrategyCell))
			return false;
		
		return true;
	}
	
	private boolean isLinkedToNonDraft(DiagramModel model, FactorCell draftStrategyCell)
	{
		LinkCell[] linkCells = (LinkCell[]) model.getFactorLinks(draftStrategyCell).toArray(new LinkCell[0]);
		for (int i = 0; i < linkCells.length; ++i)
		{
			LinkCell thisLinkCell = linkCells[i];
			FactorCell otherEnd = getOppositeLinkEnd(thisLinkCell, draftStrategyCell);	
			if (!isDraft(otherEnd.getWrappedORef()))
				return true;
		}
		
		return false;
	}
	
	private FactorCell getOppositeLinkEnd(LinkCell thisLinkCell, FactorCell draftStrategyCell)
	{
		if (draftStrategyCell.equals(thisLinkCell.getFrom()))
			return thisLinkCell.getTo();
		
		return thisLinkCell.getFrom();
	}

	private boolean isDraft(ORef wrappedRef)
	{
		BaseObject baseObject = getProject().findObject(wrappedRef);
		if (baseObject.getType() != Strategy.getObjectType())
			return false;
		
		Strategy strategy = (Strategy) baseObject;
		return strategy.isStatusDraft();
	}

	private BaseId getCurrentViewId() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getId();
	}
	
	private boolean isInDefaultMode()
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
		
		return true;
	}


}
