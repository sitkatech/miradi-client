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
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class DeleteObjective extends ObjectsDoer
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
		Objective objective = (Objective)getObjects()[0];
		BaseId idToRemove = objective.getId();
		
		String[] dialogText = {
				"Are you sure you want to delete this Objective?", 
		};
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete Objective", dialogText, buttons))
			return;

		String tag = ConceptualModelNode.TAG_OBJECTIVE_IDS;
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(node, tag, idToRemove));
			ConceptualModelNodeSet nodesThatUseThisObjective = new ChainManager(getProject()).findNodesThatUseThisObjective(idToRemove);
			if(nodesThatUseThisObjective.size() == 0)
				deleteObjective(objective);
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
	
	void deleteObjective(Objective objective) throws CommandFailedException
	{
		getProject().executeCommands(objective.createCommandsToClear());
		getProject().executeCommand(new CommandDeleteObject(objective.getType(), objective.getId()));
	}

}
