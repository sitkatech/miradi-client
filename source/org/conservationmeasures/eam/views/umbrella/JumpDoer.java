/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.JumpLocation;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.interview.InterviewView;

public class JumpDoer extends MainWindowDoer
{
	public JumpDoer()
	{
	}
	
	public void setDestination(Class actionClass)
	{
		destination = createJumpLocation(actionClass);
	}

	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		return false;
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
	
	public JumpLocation createJumpLocation(Class jumpActionClass)
	{
		String view = InterviewView.getViewName();
		return new JumpLocation(view, jumpActionClass);
	}

	JumpLocation destination;
}
