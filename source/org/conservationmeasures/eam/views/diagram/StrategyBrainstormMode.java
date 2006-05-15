/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

public class StrategyBrainstormMode extends ViewDoer
{
	public boolean isAvailable()
	{
		try
		{
			ViewData viewData = getProject().getViewData(getView().cardName());
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
			if(DiagramView.MODE_STRATEGY_BRAINSTORM.equals(currentViewMode))
				return false;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}

		if(getProject().getOnlySelectedNodes().length < 1)
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
			int viewId = getCurrentViewId();

			DiagramNode[] selectedNodes = project.getOnlySelectedNodes();
			IdList selectedNodeIds = new IdList();
			for(int i = 0; i < selectedNodes.length; ++i)
			{
				selectedNodeIds.add(selectedNodes[i].getId());
			}
			project.executeCommand(new CommandBeginTransaction());
			project.executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
					ViewData.TAG_BRAINSTORM_NODE_IDS, selectedNodeIds.toString()));
			
			project.executeCommand(new CommandSetObjectData(ObjectType.VIEW_DATA, viewId, 
					ViewData.TAG_CURRENT_MODE, DiagramView.MODE_STRATEGY_BRAINSTORM));
			project.executeCommand(new CommandEndTransaction());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	private int getCurrentViewId() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getId();
	}
}
