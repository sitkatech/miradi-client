/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class DeleteIndicator extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ConceptualModelNode node = getSelectedNode();
		Indicator indicator = (Indicator)getObjects()[0];
		BaseId idToRemove = indicator.getId();
		
		String[] dialogText = {
				"Are you sure you want to delete this Indicator?", 
		};
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete Indicator", dialogText, buttons))
			return;

		String tag = ConceptualModelNode.TAG_INDICATOR_IDS;
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(node, tag, idToRemove));
			ConceptualModelNodeSet nodesThatUseThisIndicator = new ChainManager(getProject()).findNodesThatUseThisIndicator(idToRemove);
			if(nodesThatUseThisIndicator.size() == 0)
				deleteIndicator(indicator);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	
	public ConceptualModelNode getSelectedNode()
	{
		EAMObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(selected.getType() != ObjectType.MODEL_NODE)
			return null;
		
		return (ConceptualModelNode)selected;
	}
	
	void deleteIndicator(Indicator indicator) throws CommandFailedException
	{
		getProject().executeCommands(indicator.createCommandsToClear());
		getProject().executeCommand(new CommandDeleteObject(indicator.getType(), indicator.getId()));
		
	
	}

}
