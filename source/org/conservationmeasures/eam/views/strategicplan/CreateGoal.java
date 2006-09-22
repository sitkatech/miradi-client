package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateGoal extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			CommandCreateObject cmd = new CommandCreateObject(ObjectType.GOAL);
			getProject().executeCommand(cmd);
			Goal goal = (Goal)getProject().findObject(ObjectType.GOAL, cmd.getCreatedId());
			getView().modifyObject(goal);
			getView().selectObject(goal);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
