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

import java.util.HashMap;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class TimePeriodCostsMapsCache implements CommandExecutedListener
{
	public TimePeriodCostsMapsCache(Project projectToUse)
	{
		project = projectToUse;
		clearAllCachedData();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		clearAllCachedData();
	}

	private void clearAllCachedData()
	{
		totalTimePeriodCostsMapsByBaseObject = new HashMap<ORef, TimePeriodCostsMap>();
		whenTotalAsStringByBaseObject = new HashMap<ORef, String>();
		projectTotalsByBudgetMode = new HashMap<String, TimePeriodCostsMap>();
		totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag = new HashMap<String, TimePeriodCostsMap>();
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
		ORef ref = baseObject.getRef();
		TimePeriodCostsMap result = totalTimePeriodCostsMapsByBaseObject.get(ref);
		if(result == null)
		{
			result = baseObject.getTotalTimePeriodCostsMap();
			totalTimePeriodCostsMapsByBaseObject.put(ref, result);
		}
		
		return result;
	}

	public String getWhenTotalAsString(BaseObject baseObject)
	{
		ORef ref = baseObject.getRef();
		String result = whenTotalAsStringByBaseObject.get(ref);
		if(result == null)
		{
			result = baseObject.getWhenTotalAsString();
			whenTotalAsStringByBaseObject.put(ref, result);
		}
		
		return result;
	}

	public TimePeriodCostsMap calculateProjectTotals(String workPlanBudgetMode) throws Exception
	{
		TimePeriodCostsMap result = projectTotalsByBudgetMode.get(workPlanBudgetMode);
		if(result == null)
		{
			result = getProject().getProjectTotalCalculator().calculateProjectTotals(workPlanBudgetMode);
			projectTotalsByBudgetMode.put(workPlanBudgetMode, result);
		}
		
		return result;
	}

	public TimePeriodCostsMap getTotalTimePeriodCostsMapForSubTasks(BaseObject baseObjectForRow, String assignmentsTag) throws Exception
	{
		String key = baseObjectForRow.getRef().toString() + " " + assignmentsTag;
		TimePeriodCostsMap result = totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag.get(key);
		if(result == null)
		{
			ORefList subTaskRefs = baseObjectForRow.getSubTaskRefs();
			result = baseObjectForRow.getTotalTimePeriodCostsMapForSubTasks(subTaskRefs, assignmentsTag);
			totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag.put(key, result);
		}
		
		return result;
	}

	private Project project;
	private HashMap<ORef, TimePeriodCostsMap> totalTimePeriodCostsMapsByBaseObject;
	private HashMap<ORef, String> whenTotalAsStringByBaseObject;
	private HashMap<String, TimePeriodCostsMap> projectTotalsByBudgetMode;
	private HashMap<String, TimePeriodCostsMap> totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag;
}
