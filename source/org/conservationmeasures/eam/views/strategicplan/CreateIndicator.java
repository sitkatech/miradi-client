/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.views.ViewDoer;

public class CreateIndicator extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.INDICATOR);
		getProject().executeCommand(cmd);
		Indicator indicator = getProject().getIndicatorPool().find(cmd.getCreatedId());
		((StrategicPlanView)getView()).getModifyIndicatorDoer().modify(indicator);
	}

}
