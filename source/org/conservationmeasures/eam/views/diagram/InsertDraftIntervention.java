/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.nodes.DiagramStrategy;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ViewData;

public class InsertDraftIntervention extends InsertNode
{
	public boolean isAvailable()
	{
		if (!super.isAvailable())
			return false;
		
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
		
			if(ViewData.MODE_STRATEGY_BRAINSTORM.equals(currentViewMode))
				return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		return false;
	}
	
	public NodeType getTypeToInsert()
	{
		return Factor.TYPE_INTERVENTION;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Draft Strategy");
	}

	void doExtraSetup(BaseId id) throws CommandFailedException
	{
		CommandSetObjectData setStatusCommand = new CommandSetObjectData(ObjectType.MODEL_NODE, id, Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
		getProject().executeCommand(setStatusCommand);
	}

	public void forceVisibleInLayerManager()
	{
		getProject().getLayerManager().setVisibility(DiagramStrategy.class, true);
	}
	
}
