/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ViewPossibleResources extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (!getProject().isOpen())
			return false;

		EAMObject selectedObject = getWorkPlanView().getSelectedObject();
		if (selectedObject == null)
			return false;

		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			getWorkPlanView().showResourceAddDialog();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new CommandFailedException(e);
		}
	}
	
	private WorkPlanView getWorkPlanView()
	{
		return (WorkPlanView)getView();
	}
}
