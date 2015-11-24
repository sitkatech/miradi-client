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
		totalTimePeriodPlannedCostsMapsByBaseObject = new HashMap<ORef, TimePeriodCostsMap>();
		totalTimePeriodAssignedCostsMapsByBaseObject = new HashMap<ORef, TimePeriodCostsMap>();
		plannedWhenTotalAsStringByBaseObject = new HashMap<ORef, String>();
		assignedWhenTotalAsStringByBaseObject = new HashMap<ORef, String>();
		projectPlannedTotalsByBudgetMode = new HashMap<String, TimePeriodCostsMap>();
		projectAssignedTotalsByBudgetMode = new HashMap<String, TimePeriodCostsMap>();
		diagramObjectPlannedTotalsByBudgetMode = new HashMap<DiagramTotalCacheKey, TimePeriodCostsMap>();
		diagramObjectAssignedTotalsByBudgetMode = new HashMap<DiagramTotalCacheKey, TimePeriodCostsMap>();
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

	public TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(BaseObject baseObject) throws Exception
	{
		ORef ref = baseObject.getRef();
		TimePeriodCostsMap result = totalTimePeriodPlannedCostsMapsByBaseObject.get(ref);
		if(result == null)
		{
			result = baseObject.getTotalTimePeriodCostsMapForPlans();
			totalTimePeriodPlannedCostsMapsByBaseObject.put(ref, result);
		}
		
		return result;
	}

	public TimePeriodCostsMap getTotalTimePeriodAssignedCostsMap(BaseObject baseObject) throws Exception
	{
		ORef ref = baseObject.getRef();
		TimePeriodCostsMap result = totalTimePeriodAssignedCostsMapsByBaseObject.get(ref);
		if(result == null)
		{
			result = baseObject.getTotalTimePeriodCostsMapForAssignments();
			totalTimePeriodAssignedCostsMapsByBaseObject.put(ref, result);
		}

		return result;
	}

	public String getPlannedWhenTotalAsString(BaseObject baseObject)
	{
		ORef ref = baseObject.getRef();
		String result = plannedWhenTotalAsStringByBaseObject.get(ref);
		if(result == null)
		{
			result = baseObject.getPlannedWhenTotalAsString();
			plannedWhenTotalAsStringByBaseObject.put(ref, result);
		}
		
		return result;
	}

	public String getAssignedWhenTotalAsString(BaseObject baseObject)
	{
		ORef ref = baseObject.getRef();
		String result = assignedWhenTotalAsStringByBaseObject.get(ref);
		if(result == null)
		{
			result = baseObject.getAssignedWhenTotalAsString();
			assignedWhenTotalAsStringByBaseObject.put(ref, result);
		}

		return result;
	}

	public TimePeriodCostsMap calculateProjectPlannedTotals(String workPlanBudgetMode) throws Exception
	{
		TimePeriodCostsMap result = projectPlannedTotalsByBudgetMode.get(workPlanBudgetMode);
		if(result == null)
		{
			result = getProject().getProjectTotalCalculator().calculateProjectPlannedTotals(workPlanBudgetMode);
			projectPlannedTotalsByBudgetMode.put(workPlanBudgetMode, result);
		}
		
		return result;
	}

	public TimePeriodCostsMap calculateProjectAssignedTotals(String workPlanBudgetMode) throws Exception
	{
		TimePeriodCostsMap result = projectAssignedTotalsByBudgetMode.get(workPlanBudgetMode);
		if(result == null)
		{
			result = getProject().getProjectTotalCalculator().calculateProjectAssignedTotals(workPlanBudgetMode);
			projectAssignedTotalsByBudgetMode.put(workPlanBudgetMode, result);
		}

		return result;
	}

	public TimePeriodCostsMap calculateDiagramObjectPlannedTotals(DiagramObject baseObject, String workPlanBudgetMode) throws Exception
	{
		DiagramTotalCacheKey diagramTotalCacheKey = new DiagramTotalCacheKey(baseObject, workPlanBudgetMode);
		TimePeriodCostsMap result = diagramObjectPlannedTotalsByBudgetMode.get(diagramTotalCacheKey);
		if(result == null)
		{
			result = getProject().getProjectTotalCalculator().calculateDiagramObjectPlannedTotals(baseObject, workPlanBudgetMode);
			diagramObjectPlannedTotalsByBudgetMode.put(diagramTotalCacheKey, result);
		}

		return result;
	}

	public TimePeriodCostsMap calculateDiagramObjectAssignedTotals(DiagramObject baseObject, String workPlanBudgetMode) throws Exception
	{
		DiagramTotalCacheKey diagramTotalCacheKey = new DiagramTotalCacheKey(baseObject, workPlanBudgetMode);
		TimePeriodCostsMap result = diagramObjectAssignedTotalsByBudgetMode.get(diagramTotalCacheKey);
		if(result == null)
		{
			result = getProject().getProjectTotalCalculator().calculateDiagramObjectAssignedTotals(baseObject, workPlanBudgetMode);
			diagramObjectAssignedTotalsByBudgetMode.put(diagramTotalCacheKey, result);
		}

		return result;
	}

	public TimePeriodCostsMap getTotalTimePeriodAssignedCostsMapForSubTasks(BaseObject baseObjectForRow, String assignmentsTag) throws Exception
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
	private HashMap<ORef, TimePeriodCostsMap> totalTimePeriodPlannedCostsMapsByBaseObject;
	private HashMap<ORef, TimePeriodCostsMap> totalTimePeriodAssignedCostsMapsByBaseObject;
	private HashMap<ORef, String> plannedWhenTotalAsStringByBaseObject;
	private HashMap<ORef, String> assignedWhenTotalAsStringByBaseObject;
	private HashMap<String, TimePeriodCostsMap> projectPlannedTotalsByBudgetMode;
	private HashMap<String, TimePeriodCostsMap> projectAssignedTotalsByBudgetMode;
	private HashMap<DiagramTotalCacheKey, TimePeriodCostsMap> diagramObjectPlannedTotalsByBudgetMode;
	private HashMap<DiagramTotalCacheKey, TimePeriodCostsMap> diagramObjectAssignedTotalsByBudgetMode;
	private HashMap<String, TimePeriodCostsMap> totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag;
}
