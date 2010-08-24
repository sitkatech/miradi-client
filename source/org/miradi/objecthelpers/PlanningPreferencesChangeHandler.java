/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningPreferencesChangeHandler implements CommandExecutedListener
{
	public PlanningPreferencesChangeHandler(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{		
			if (shouldCollapseAllBudgetColumns(event))
				collapseAllBudgetColumns();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	private boolean shouldCollapseAllBudgetColumns(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_WORKPLAN_END_DATE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_WORKPLAN_START_DATE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_START_DATE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_EXPECTED_END_DATE))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_FISCAL_YEAR_START))
			return true;

		return false;
	}
	
	private void collapseAllBudgetColumns() throws Exception
	{
		ORefList tableSettingsRefs = getProject().getTableSettingsPool().getORefList();
		for (int index = 0; index < tableSettingsRefs.size(); ++index)
		{
			ORef tableSettingsRef = tableSettingsRefs.get(index);
			CommandSetObjectData clearExpandedColumns = new CommandSetObjectData(tableSettingsRef, TableSettings.TAG_DATE_UNIT_LIST_DATA, new CodeList().toString());
			getProject().executeAsSideEffect(clearExpandedColumns);
		}
	}
	
	private Project getProject()
	{
		return project;
	}

	private Project project;
}
