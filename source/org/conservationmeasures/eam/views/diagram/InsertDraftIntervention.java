/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ViewData;

public class InsertDraftIntervention extends InsertNode
{
	public boolean isAvailable()
	{
		super.isAvailable();
		try
		{
			ViewData viewData = getProject().getViewData(getView().cardName());
			String currentViewMode = viewData.getData(ViewData.TAG_CURRENT_MODE);
			System.out.println(currentViewMode);
			
			if(ViewData.MODE_STRATEGY_BRAINSTORM.equals(currentViewMode))
				return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
		return false;
	}
	
	public NodeType getTypeToInsert()
	{
		return DiagramNode.TYPE_INTERVENTION;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Draft Intervention");
	}

	void doExtraSetup(BaseId id) throws CommandFailedException
	{
		CommandSetObjectData setStatusCommand = new CommandSetObjectData(ObjectType.MODEL_NODE, id, ConceptualModelIntervention.TAG_STATUS, ConceptualModelIntervention.STATUS_DRAFT);
		getProject().executeCommand(setStatusCommand);
	}
	
}
