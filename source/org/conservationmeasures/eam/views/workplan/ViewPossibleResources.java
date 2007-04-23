/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class ViewPossibleResources extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (!getProject().isOpen())
			return false;

		BaseObject selectedObject = getWorkPlanView().getSelectedObject();
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
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	private WorkPlanView getWorkPlanView()
	{
		return (WorkPlanView)getView();
	}
}
