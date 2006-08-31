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
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.ChainManager;
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

		BaseId idToRemove = objective.getId();
		ConceptualModelNodeSet factorsThatUseThisObjective = new ChainManager(getProject()).findNodesThatUseThisObjective(idToRemove);
		if(factorsThatUseThisObjective.size() > 0)
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
		BaseId id = idToRemove;
		getProject().executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, EAMBaseObject.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, Objective.TAG_SHORT_LABEL, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, Objective.TAG_FULL_TEXT, ""));
		getProject().executeCommand(new CommandDeleteObject(type, id));
		
		getProject().executeCommand(new CommandEndTransaction());
	}

	
	
	private Command[] createCommandsToRemoveObjectivesFromNodes(BaseId idToRemove, ConceptualModelNodeSet nodesThatUseThisObjective) throws CommandFailedException
	{
		ConceptualModelNode[] nodes = nodesThatUseThisObjective.toNodeArray();
		Command[] removeFromNodes = new Command[nodes.length];
		try
		{
			for(int i = 0; i < removeFromNodes.length; ++i)
			{
				String tag = ConceptualModelNode.TAG_OBJECTIVE_IDS;
				ConceptualModelNode node = nodes[i];
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
