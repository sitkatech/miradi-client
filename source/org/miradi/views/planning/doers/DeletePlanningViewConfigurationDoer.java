/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ViewData;

public class DeletePlanningViewConfigurationDoer extends AbstractPlanningViewConfigurationDoer
{
	public boolean isAvailable()
	{
		if(getProject().getPlanningViewConfigurationPool().getORefList().size() < 2)
			return false;
		
		return super.isAvailable();
	}

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
			ORef configurationRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
			
			selectOtherConfiguration(configurationRef);
			
			PlanningViewConfiguration configuration = (PlanningViewConfiguration) getProject().findObject(configurationRef);
			getProject().executeCommandsWithoutTransaction(configuration.createCommandsToClear());
			
			CommandDeleteObject deleteConfiguration = new CommandDeleteObject(configurationRef);
			getProject().executeCommand(deleteConfiguration);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	protected void selectOtherConfiguration(ORef itemNotToSelect) throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		ORefList existing = getProject().getPlanningViewConfigurationPool().getORefList();
		int next = (existing.find(itemNotToSelect) + 1) % existing.size();
		ORef refAsSelection = existing.get(next);
		CommandSetObjectData setCurrentCustomPlanRef = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, refAsSelection.toString());
		getProject().executeCommand(setCurrentCustomPlanRef);
	}
}
