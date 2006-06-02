/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateObjective extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.OBJECTIVE);
		getProject().executeCommand(cmd);
		Objective objective = getProject().getObjectivePool().find(cmd.getCreatedId());
		((StrategicPlanView)getView()).getModifyObjectiveDoer().modify(objective);
	}

}
