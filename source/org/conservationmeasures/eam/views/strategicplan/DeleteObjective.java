/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteObjective extends ViewDoer
{
	public ObjectiveManagementPanel getObjectivePanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getObjectivePanel();
	}
	
	public boolean isAvailable()
	{
		if(getObjectivePanel() == null)
			return false;
		
		return getObjectivePanel().getSelectedObjective() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Objective objective = getObjectivePanel().getSelectedObjective();

		int idToRemove = objective.getId();
		ConceptualModelNode[] factorsThatUseThisObjective = getProject().findNodesThatUseThisObjective(idToRemove);
		if(factorsThatUseThisObjective.length > 0)
		{
			String[] dialogText = {
					"This Objective is assigned to one or more Factors.", 
					"Are you sure you want to delete it?", 
					};
			String[] buttons = {"Yes", "No", };
			if(!EAM.confirmDialog("Delete Objective", dialogText, buttons))
				return;
		}

		Command[] removeFromNodes = createCommandsToRemoveObjectivesFromNodes(idToRemove, factorsThatUseThisObjective);
		
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i = 0; i < removeFromNodes.length; ++i)
		{
			getProject().executeCommand(removeFromNodes[i]);
		}
		int type = objective.getType();
		int id = idToRemove;
		getProject().executeCommand(new CommandSetObjectData(type, id, EAMObject.TAG_LABEL, EAMObject.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, Objective.TAG_SHORT_LABEL, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, Objective.TAG_FULL_TEXT, ""));
		getProject().executeCommand(new CommandDeleteObject(type, id));
		
		getProject().executeCommand(new CommandEndTransaction());
	}

	
	
	private Command[] createCommandsToRemoveObjectivesFromNodes(int idToRemove, ConceptualModelNode[] nodesThatUseThisObjective) throws CommandFailedException
	{
		Command[] removeFromNodes = new Command[nodesThatUseThisObjective.length];
		try
		{
			for(int i = 0; i < removeFromNodes.length; ++i)
			{
				String tag = ConceptualModelNode.TAG_OBJECTIVE_IDS;
				ConceptualModelNode node = nodesThatUseThisObjective[i];
				removeFromNodes[i] = CommandSetObjectData.createRemoveIdCommand(node, tag, idToRemove);
			}
		}
		catch (ParseException e)
		{
			throw new CommandFailedException(e);
		}
		return removeFromNodes;
	}
	
}
