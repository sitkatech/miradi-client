/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ProjectDoer;
import org.conservationmeasures.eam.views.summary.SummaryView;

public class ViewSummary extends ProjectDoer
{
	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			getProject().executeCommand(new CommandSwitchView(SummaryView.getViewName()));
		}
		catch(AlreadyInThatViewException ignore)
		{
			// not really a problem
		}
	}
}
