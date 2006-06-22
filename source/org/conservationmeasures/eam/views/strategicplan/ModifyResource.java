/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;



import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.ViewDoer;

public class ModifyResource extends ViewDoer
{
	public ModifyResource()
	{
	}
	
	public ResourceManagementPanel getResourcePanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getResourcePanel();
	}
	
	public boolean isAvailable()
	{
		if(getResourcePanel() == null)
			return false;
		
		return getResourcePanel().getSelectedResource() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		try
		{
			ProjectResource resource = getResourcePanel().getSelectedResource();
			getView().modifyObject(resource);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

}
