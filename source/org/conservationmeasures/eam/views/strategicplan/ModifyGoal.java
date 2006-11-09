package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.views.ViewDoer;

public class ModifyGoal extends ViewDoer
{
	public GoalPoolTablePanel getGoalPanel()
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
		
		try
		{
			Goal goal = getGoalPanel().getSelectedGoal();
			getView().modifyObject(goal);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
