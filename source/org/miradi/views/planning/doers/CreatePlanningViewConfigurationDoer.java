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
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class CreatePlanningViewConfigurationDoer extends ViewDoer
{
	public boolean isAvailable()
	{	
		if (! isPlanningView())
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			createPlanningViewConfiguration();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void createPlanningViewConfiguration() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		String visibleRowsAsString = RowManager.getVisibleRowCodes(viewData).toString();
		String visibleColsAsString = ColumnManager.getVisibleColumnCodes(viewData).toString();
		
		CommandCreateObject createConfiguration = new CommandCreateObject(PlanningViewConfiguration.getObjectType());
		getProject().executeCommand(createConfiguration);
		
		ORef newConfigurationRef = createConfiguration.getObjectRef();
		CommandSetObjectData setVisibleRowsCommand = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_ROW_CONFIGURATION, visibleRowsAsString);
		getProject().executeCommand(setVisibleRowsCommand);
		
		CommandSetObjectData setVisibleColsCommand = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_COL_CONFIGURATION, visibleColsAsString);
		getProject().executeCommand(setVisibleColsCommand);
		
		CommandSetObjectData selectCurrentConfiguration = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, newConfigurationRef);
		getProject().executeCommand(selectCurrentConfiguration);
		
		CommandSetObjectData setConfigurationLabel = new CommandSetObjectData(newConfigurationRef, PlanningViewConfiguration.TAG_LABEL, getConfigurationDefaultLabel(getProject()));
		getProject().executeCommand(setConfigurationLabel);
	}

	public static String getConfigurationDefaultLabel(Project project)
	{
		return "[" + EAM.text("PlanningSubViewName|Custom") + " " + project.getPlanningViewConfigurationPool().size() + "]"; 
	}
}
