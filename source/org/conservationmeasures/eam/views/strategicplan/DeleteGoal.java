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
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteGoal extends ViewDoer
{
	public GoalManagementPanel getGoalPanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getGoalPanel();
	}
	
	public boolean isAvailable()
	{
		if(getGoalPanel() == null)
			return false;
		
		return getGoalPanel().getSelectedGoal() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Goal goal = getGoalPanel().getSelectedGoal();

		BaseId idToRemove = goal.getId();
		ConceptualModelNodeSet targetsThatUseThisGoal = findTargetsThatUseThisGoal(idToRemove);
		if(targetsThatUseThisGoal.size() > 0)
		{
			String[] dialogText = {
					"This Goal is assigned to one or more Targets.", 
					"Are you sure you want to delete it?", 
					};
			String[] buttons = {"Yes", "No", };
			if(!EAM.confirmDialog("Delete Goal", dialogText, buttons))
				return;
		}

		Command[] removeFromNodes = createCommandsToRemoveGoalsFromTargets(idToRemove, targetsThatUseThisGoal);
		
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i = 0; i < removeFromNodes.length; ++i)
		{
			getProject().executeCommand(removeFromNodes[i]);
		}
		int type = goal.getType();
		BaseId id = idToRemove;
		getProject().executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, EAMBaseObject.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, Goal.TAG_SHORT_LABEL, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, Goal.TAG_FULL_TEXT, ""));
		getProject().executeCommand(new CommandDeleteObject(type, id));
		
		getProject().executeCommand(new CommandEndTransaction());
	}

	private ConceptualModelNodeSet findTargetsThatUseThisGoal(BaseId goalId)
	{
		ConceptualModelNodeSet result = new ConceptualModelNodeSet();
		ConceptualModelNode[] targets = getProject().getNodePool().getTargets();
		for(int i = 0; i < targets.length; ++i)
		{
			ConceptualModelTarget target = (ConceptualModelTarget)targets[i];
			if(target.getGoals().contains(goalId))
				result.attemptToAdd(target);
		}
		
		return result;
	}
	
	
	private Command[] createCommandsToRemoveGoalsFromTargets(BaseId idToRemove, ConceptualModelNodeSet targetsThatUseThisGoal) throws CommandFailedException
	{
		ConceptualModelNode[] nodes = targetsThatUseThisGoal.toNodeArray();
		Command[] removeFromNodes = new Command[nodes.length];
		try
		{
			for(int i = 0; i < removeFromNodes.length; ++i)
			{
				String tag = ConceptualModelNode.TAG_GOAL_IDS;
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
