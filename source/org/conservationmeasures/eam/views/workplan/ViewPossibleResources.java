/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ViewPossibleResources extends ObjectsDoer
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
			WorkPlanView view = (WorkPlanView)getView();
			view.showResourceAddDialog();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		}
	}
}
