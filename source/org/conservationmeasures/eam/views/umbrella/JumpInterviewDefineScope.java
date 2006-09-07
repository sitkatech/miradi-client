/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ProjectDoer;

public class JumpInterviewDefineScope extends ProjectDoer
{
	public JumpInterviewDefineScope()
	{
	}

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
//		getProject().executeCommand(new CommandBeginTransaction());
//		try
//		{
//			getProject().executeCommand(new CommandSwitchView(InterviewView.getViewName()));
//		}
//		finally
//		{
//			getProject().executeCommand(new CommandEndTransaction());
//		}
	}

}
