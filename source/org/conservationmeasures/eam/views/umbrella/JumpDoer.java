/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class JumpDoer extends MainWindowDoer
{
	public JumpDoer()
	{
	}
	
	public void setDestination(String newDestination)
	{
		destination = newDestination;
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

	String destination;
}
