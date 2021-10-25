/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.project;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;

import java.util.HashMap;
import java.util.Set;

public class ProjectTotalCalculator implements CommandExecutedListener
{
	public ProjectTotalCalculator(Project projectToUse, ProjectTotalCalculatorStrategy projectTotalCalculatorStrategyToUse)
	{
		project = projectToUse;
		projectTotalCalculatorStrategy = projectTotalCalculatorStrategyToUse;
		modeToTimePeriodPlannedCostsMapMap = new HashMap<String, TimePeriodCostsMap>();
		modeToTimePeriodAssignedCostsMapMap = new HashMap<String, TimePeriodCostsMap>();
	}
	
	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		// TODO: Only clear the cache when it actually needs it
		clear();
	}
	
	public void clear()
	{
		modeToTimePeriodAssignedCostsMapMap.clear();
		modeToTimePeriodPlannedCostsMapMap.clear();
	}

	public TimePeriodCostsMap calculateProjectPlannedTotals() throws Exception
	{
		String workPlanBudgetMode = getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		if(!modeToTimePeriodPlannedCostsMapMap.containsKey(workPlanBudgetMode))
			modeToTimePeriodPlannedCostsMapMap.put(workPlanBudgetMode, computeTotalTimePeriodPlannedCostsMap());
		
		return modeToTimePeriodPlannedCostsMapMap.get(workPlanBudgetMode);
	}

	public TimePeriodCostsMap calculateProjectAssignedTotals() throws Exception
	{
		String workPlanBudgetMode = getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		if(!modeToTimePeriodAssignedCostsMapMap.containsKey(workPlanBudgetMode))
			modeToTimePeriodAssignedCostsMapMap.put(workPlanBudgetMode, computeTotalTimePeriodAssignedCostsMap());

		return modeToTimePeriodAssignedCostsMapMap.get(workPlanBudgetMode);
	}

	public TimePeriodCostsMap calculateDiagramObjectPlannedTotals(DiagramObject diagramObject) throws Exception
	{
		if (projectTotalCalculatorStrategy.shouldOnlyIncludeMonitoringData())
			return getTotalTimePeriodPlannedCostsMap(projectTotalCalculatorStrategy.getMonitoringData(getProject(), diagramObject.getRef()));

		if (projectTotalCalculatorStrategy.shouldOnlyIncludeActionsData())
			return getTotalTimePeriodPlannedCostsMap(projectTotalCalculatorStrategy.getActionsData(getProject(), diagramObject.getRef()));

		return getTotalTimePeriodPlannedCostsMap(projectTotalCalculatorStrategy.getAllData(getProject(), diagramObject.getRef()));
	}

	public TimePeriodCostsMap calculateDiagramObjectAssignedTotals(DiagramObject diagramObject) throws Exception
	{
		if (projectTotalCalculatorStrategy.shouldOnlyIncludeMonitoringData())
			return getTotalTimePeriodAssignedCostsMap(projectTotalCalculatorStrategy.getMonitoringData(getProject(), diagramObject.getRef()));

		if (projectTotalCalculatorStrategy.shouldOnlyIncludeActionsData())
			return getTotalTimePeriodAssignedCostsMap(projectTotalCalculatorStrategy.getActionsData(getProject(), diagramObject.getRef()));

		return getTotalTimePeriodAssignedCostsMap(projectTotalCalculatorStrategy.getAllData(getProject(), diagramObject.getRef()));
	}

	public TimePeriodCostsMap getTotalTimePeriodCostsMapForPlans(BaseObject baseObject) throws Exception
	{
		return baseObject.getTotalTimePeriodCostsMapForPlans();
	}

	public TimePeriodCostsMap getTotalTimePeriodCostsMapForAssignments(BaseObject baseObject) throws Exception
	{
		return baseObject.getTotalTimePeriodCostsMapForAssignments();
	}

	public String getTimeframeRollupAsString(BaseObject baseObject)
	{
		return baseObject.getTimeframeRollupAsString();
	}

	public String getAssignedWhenRollupAsString(BaseObject baseObject)
	{
		return baseObject.getAssignedWhenRollupAsString();
	}

	public TimePeriodCostsMap getTotalTimePeriodCostsMapForChildTasks(BaseObject baseObject, String tag) throws Exception
	{
		return baseObject.getTotalTimePeriodCostsMapForChildTasks(tag);
	}

	private TimePeriodCostsMap computeTotalTimePeriodPlannedCostsMap() throws Exception
	{
		if (projectTotalCalculatorStrategy.shouldOnlyIncludeMonitoringData())
			return getTotalTimePeriodPlannedCostsMap(projectTotalCalculatorStrategy.getMonitoringData(getProject()));
		
		if (projectTotalCalculatorStrategy.shouldOnlyIncludeActionsData())
			return getTotalTimePeriodPlannedCostsMap(projectTotalCalculatorStrategy.getActionsData(getProject()));

		return getTotalTimePeriodPlannedCostsMap(projectTotalCalculatorStrategy.getAllData(getProject()));
	}

	private TimePeriodCostsMap computeTotalTimePeriodAssignedCostsMap() throws Exception
	{
		if (projectTotalCalculatorStrategy.shouldOnlyIncludeMonitoringData())
			return getTotalTimePeriodAssignedCostsMap(projectTotalCalculatorStrategy.getMonitoringData(getProject()));

		if (projectTotalCalculatorStrategy.shouldOnlyIncludeActionsData())
			return getTotalTimePeriodAssignedCostsMap(projectTotalCalculatorStrategy.getActionsData(getProject()));

		return getTotalTimePeriodAssignedCostsMap(projectTotalCalculatorStrategy.getAllData(getProject()));
	}

	private TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(Set<BaseObject> baseObjects) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		for(BaseObject baseObject : baseObjects)
		{
			TimePeriodCostsMap indicatorTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMapForPlans();
			totalTimePeriodCostsMap.mergeAll(indicatorTimePeriodCostsMap);
		}
		
		return totalTimePeriodCostsMap;
	}

	private TimePeriodCostsMap getTotalTimePeriodAssignedCostsMap(Set<BaseObject> baseObjects) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		for(BaseObject baseObject : baseObjects)
		{
			TimePeriodCostsMap indicatorTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMapForAssignments();
			totalTimePeriodCostsMap.mergeAll(indicatorTimePeriodCostsMap);
		}

		return totalTimePeriodCostsMap;
	}

	private Project getProject()
	{
		return project;
	}

	public ProjectTotalCalculatorStrategy getProjectTotalCalculatorStrategy() { return projectTotalCalculatorStrategy; }

	public void setProjectTotalCalculatorStrategy(ProjectTotalCalculatorStrategy projectTotalCalculatorStrategyToUse)
	{
		clear();
		projectTotalCalculatorStrategy = projectTotalCalculatorStrategyToUse;
	}

	private Project project;
	private ProjectTotalCalculatorStrategy projectTotalCalculatorStrategy;
	private HashMap<String, TimePeriodCostsMap> modeToTimePeriodAssignedCostsMapMap;
	private HashMap<String, TimePeriodCostsMap> modeToTimePeriodPlannedCostsMapMap;
}
