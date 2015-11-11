/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

public class TimePeriodCostsMapsCache implements CommandExecutedListener
{
	public TimePeriodCostsMapsCache(Project projectToUse)
	{
		project = projectToUse;
		clear();
	}
	
	public void clear()
	{
		clearAllCachedData();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		clear();
	}

	private void clearAllCachedData()
	{
		totalTimePeriodCostsMapsByBaseObject = new HashMap<ORef, TimePeriodCostsMap>();
		whenTotalAsStringByBaseObject = new HashMap<ORef, String>();
		projectTotalsByBudgetMode = new HashMap<String, TimePeriodCostsMap>();
		diagramObjectTotalsByBudgetMode = new HashMap<DiagramTotalCacheKey, TimePeriodCostsMap>();
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

	public TimePeriodCostsMap calculateDiagramObjectTotals(DiagramObject baseObject, String workPlanBudgetMode) throws Exception
	{
		DiagramTotalCacheKey diagramTotalCacheKey = new DiagramTotalCacheKey(baseObject, workPlanBudgetMode);
		TimePeriodCostsMap result = diagramObjectTotalsByBudgetMode.get(diagramTotalCacheKey);
		if(result == null)
		{
			result = getProject().getProjectTotalCalculator().calculateDiagramObjectTotals(baseObject, workPlanBudgetMode);
			diagramObjectTotalsByBudgetMode.put(diagramTotalCacheKey, result);
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

	class DiagramTotalCacheKey
	{
		public DiagramTotalCacheKey(DiagramObject diagramObjectToUse, String workPlanBudgetModeToUse)
		{
			diagramObject = diagramObjectToUse;
			workPlanBudgetMode = workPlanBudgetModeToUse;
		}

		@Override
		public String toString()
		{
			return diagramObject.toString() + ":" + workPlanBudgetMode;
		}

		@Override
		public boolean equals(Object rawOther)
		{
			if(! (rawOther instanceof DiagramTotalCacheKey))
				return false;

			DiagramTotalCacheKey other = (DiagramTotalCacheKey)rawOther;
			if(!diagramObject.equals(other.diagramObject))
				return false;
			return (workPlanBudgetMode.equals(other.workPlanBudgetMode));
		}

		@Override
		public int hashCode()
		{
			return toString().hashCode();
		}

		private DiagramObject diagramObject;
		private String workPlanBudgetMode;
	}

	private Project project;
	private HashMap<ORef, TimePeriodCostsMap> totalTimePeriodCostsMapsByBaseObject;
	private HashMap<ORef, String> whenTotalAsStringByBaseObject;
	private HashMap<String, TimePeriodCostsMap> projectTotalsByBudgetMode;
	private HashMap<DiagramTotalCacheKey, TimePeriodCostsMap> diagramObjectTotalsByBudgetMode;
	private HashMap<String, TimePeriodCostsMap> totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag;
}
