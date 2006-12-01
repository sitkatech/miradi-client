/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

public class ViewPossibleTeamMembers extends ViewDoer
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
			SummaryView view = (SummaryView)getView();
			view.showTeamAddMembersDialog();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		}
	}

}
