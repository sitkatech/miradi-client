/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class DeleteGoal extends ObjectsDoer
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
		Goal goal = (Goal)getObjects()[0];
		BaseId idToRemove = goal.getId();
		
		String[] dialogText = {
				"Are you sure you want to delete this Goal?", 
		};
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete Goal", dialogText, buttons))
			return;

		String tag = ConceptualModelNode.TAG_GOAL_IDS;
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			getProject().executeCommand(CommandSetObjectData.createRemoveIdCommand(node, tag, idToRemove));
			ConceptualModelNodeSet nodesThatUseThisGoal = new ChainManager(getProject()).findNodesThatUseThisGoal(idToRemove);
			if(nodesThatUseThisGoal.size() == 0)
				deleteGoal(goal);
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
	
	void deleteGoal(Goal goal) throws CommandFailedException
	{
		int type = goal.getType();
		BaseId id = goal.getId();
		getProject().executeCommand(new CommandSetObjectData(type, id, goal.TAG_LABEL, goal.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, goal.TAG_SHORT_LABEL, ""));
	}

}
