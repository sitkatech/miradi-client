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

import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;
import org.miradi.views.diagram.doers.SelectChainDoer;

public class ShowSelectedChainModeDoer extends ViewDoer
{
	public boolean isAvailable()
	{		
		if(! isInDiagram())
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
			DiagramComponent diagram = view.getCurrentDiagramComponent();
			
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
			getMainWindow().updateActionsAndStatusBar();
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
			selectedNodeORefs.add(orphanedDaftStrats[i].getWrappedFactorRef());
		}
	}

	private FactorCell[] getOrphanedDraftStrategies(Project project, DiagramView view, DiagramComponent diagram)
	{
		Vector orphanedDraftStrategies = new Vector();
		DiagramModel model = view.getDiagramModel();
		Factor[] draftStrategies = project.getStrategyPool().getDraftStrategies();
		for (int i=0; i<draftStrategies.length; ++i)
		{
			FactorCell draftStrategyCell = model.getFactorCellByWrappedRef(draftStrategies[i].getRef());
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
			if (!isDraft(otherEnd.getWrappedFactorRef()))
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
