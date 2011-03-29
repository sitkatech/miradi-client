/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class TimePeriodCostsMapsCache implements CommandExecutedListener
{
	public TimePeriodCostsMapsCache(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		// FIXME: Update cache status here
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	private Project getProject()
	{
		return project;
	}

	public TimePeriodCostsMap getTotalTimePeriodCostsMap(BaseObject baseObject) throws Exception
	{
		return baseObject.getTotalTimePeriodCostsMap();
	}

	public String getWhenTotalAsString(BaseObject baseObject)
	{
		return baseObject.getWhenTotalAsString();
	}

	public TimePeriodCostsMap calculateProjectTotals(String workPlanBudgetMode) throws Exception
	{
		return getProject().getProjectTotalCalculator().calculateProjectTotals(workPlanBudgetMode);
	}

	private Project project;
}
