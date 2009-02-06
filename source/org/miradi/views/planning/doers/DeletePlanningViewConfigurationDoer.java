/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
