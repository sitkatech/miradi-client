/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ViewData;

public class DeletePlanningViewConfigurationDoer extends AbstractPlanningViewConfigurationDoer
{
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try 
		{
			deleteConfiguration();
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	private void deleteConfiguration()
	{
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			String oRefAsString = viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
			ORef configurationRef = ORef.createFromString(oRefAsString);
			CommandDeleteObject deleteConfiguration = new CommandDeleteObject(configurationRef);
			getProject().executeCommand(deleteConfiguration);
			
			setSelection(viewData);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
}
