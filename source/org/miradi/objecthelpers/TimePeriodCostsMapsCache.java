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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.project.ProjectTotalCalculator;
import org.miradi.project.ProjectTotalCalculatorStrategy;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.schemas.TableSettingsSchema;

import java.util.HashMap;

public class TimePeriodCostsMapsCache implements CommandExecutedListener
{
	public TimePeriodCostsMapsCache(Project projectToUse)
	{
		project = projectToUse;
		ProjectTotalCalculatorStrategy projectTotalCalculatorStrategy = new ProjectTotalCalculatorStrategy(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE);
		projectTotalCalculator = new ProjectTotalCalculator(projectToUse, projectTotalCalculatorStrategy);
		clear();
	}

	public void clear()
	{
		clearAllCachedData();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		clear();

		if (doesCommandImpactWorkPlanBudgetMode(event))
		{
			ProjectTotalCalculatorStrategy currentCalculatorStrategy = projectTotalCalculator.getProjectTotalCalculatorStrategy();
			CommandSetObjectData command = (CommandSetObjectData) event.getCommand();
			currentCalculatorStrategy.setWorkPlanBudgetMode(command.getDataValue());
		}
	}

	public void setProjectTotalCalculatorStrategy(ProjectTotalCalculatorStrategy projectTotalCalculatorStrategyToUse)
	{
		clear();

		projectTotalCalculator.disable();
		projectTotalCalculator = new ProjectTotalCalculator(getProject(), projectTotalCalculatorStrategyToUse);
		projectTotalCalculator.enable();
	}

	private boolean doesCommandImpactWorkPlanBudgetMode(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(TableSettingsSchema.getObjectType(), TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE);
	}

	private void clearAllCachedData()
	{
		projectTotalCalculator.clear();
		totalTimePeriodPlannedCostsMapsByBaseObject = new HashMap<ORef, TimePeriodCostsMap>();
		totalTimePeriodAssignedCostsMapsByBaseObject = new HashMap<ORef, TimePeriodCostsMap>();
		timeframeTotalAsStringByBaseObject = new HashMap<ORef, String>();
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
		projectTotalCalculator.enable();
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
		projectTotalCalculator.disable();
	}
	
	private Project getProject()
	{
		return project;
	}

	public void initializeWorkPlanBudgetMode()
	{
		String workPlanBudgetMode = WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE;

		try
		{
			TableSettings tableSettings = TableSettings.findOrCreate(getProject(), WorkPlanTreeTablePanel.getTabSpecificModelIdentifier());
			workPlanBudgetMode = tableSettings.getData(TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		ProjectTotalCalculatorStrategy currentCalculatorStrategy = projectTotalCalculator.getProjectTotalCalculatorStrategy();
		currentCalculatorStrategy.setWorkPlanBudgetMode(workPlanBudgetMode);
	}

	public String getWorkPlanBudgetMode()
	{
		return projectTotalCalculator.getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
	}

	public ProjectTotalCalculator getProjectTotalCalculator()
	{
		return projectTotalCalculator;
	}

	public TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(BaseObject baseObject) throws Exception
	{
		ORef ref = baseObject.getRef();
		TimePeriodCostsMap result = totalTimePeriodPlannedCostsMapsByBaseObject.get(ref);
		if(result == null)
		{
			result = getProjectTotalCalculator().getTotalTimePeriodCostsMapForPlans(baseObject);
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
			result = getProjectTotalCalculator().getTotalTimePeriodCostsMapForAssignments(baseObject);
			totalTimePeriodAssignedCostsMapsByBaseObject.put(ref, result);
		}

		return result;
	}

	public String getTimeframeTotalAsString(BaseObject baseObject)
	{
		ORef ref = baseObject.getRef();
		String result = timeframeTotalAsStringByBaseObject.get(ref);
		if(result == null)
		{
			result = getProjectTotalCalculator().getTimeframeRollupAsString(baseObject);
			timeframeTotalAsStringByBaseObject.put(ref, result);
		}
		
		return result;
	}

	public String getAssignedWhenTotalAsString(BaseObject baseObject)
	{
		ORef ref = baseObject.getRef();
		String result = assignedWhenTotalAsStringByBaseObject.get(ref);
		if(result == null)
		{
			result = getProjectTotalCalculator().getAssignedWhenRollupAsString(baseObject);
			assignedWhenTotalAsStringByBaseObject.put(ref, result);
		}

		return result;
	}

	public TimePeriodCostsMap calculateProjectPlannedTotals() throws Exception
	{
		String workPlanBudgetMode = getProjectTotalCalculator().getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		TimePeriodCostsMap result = projectPlannedTotalsByBudgetMode.get(workPlanBudgetMode);
		if(result == null)
		{
			result = getProjectTotalCalculator().calculateProjectPlannedTotals();
			projectPlannedTotalsByBudgetMode.put(workPlanBudgetMode, result);
		}
		
		return result;
	}

	public TimePeriodCostsMap calculateProjectAssignedTotals() throws Exception
	{
		String workPlanBudgetMode = getProjectTotalCalculator().getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		TimePeriodCostsMap result = projectAssignedTotalsByBudgetMode.get(workPlanBudgetMode);
		if(result == null)
		{
			result = getProjectTotalCalculator().calculateProjectAssignedTotals();
			projectAssignedTotalsByBudgetMode.put(workPlanBudgetMode, result);
		}

		return result;
	}

	public TimePeriodCostsMap calculateDiagramObjectPlannedTotals(DiagramObject baseObject) throws Exception
	{
		String workPlanBudgetMode = getProjectTotalCalculator().getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		DiagramTotalCacheKey diagramTotalCacheKey = new DiagramTotalCacheKey(baseObject, workPlanBudgetMode);
		TimePeriodCostsMap result = diagramObjectPlannedTotalsByBudgetMode.get(diagramTotalCacheKey);
		if(result == null)
		{
			result = getProjectTotalCalculator().calculateDiagramObjectPlannedTotals(baseObject);
			diagramObjectPlannedTotalsByBudgetMode.put(diagramTotalCacheKey, result);
		}

		return result;
	}

	public TimePeriodCostsMap calculateDiagramObjectAssignedTotals(DiagramObject baseObject) throws Exception
	{
		String workPlanBudgetMode = getProjectTotalCalculator().getProjectTotalCalculatorStrategy().getWorkPlanBudgetMode();
		DiagramTotalCacheKey diagramTotalCacheKey = new DiagramTotalCacheKey(baseObject, workPlanBudgetMode);
		TimePeriodCostsMap result = diagramObjectAssignedTotalsByBudgetMode.get(diagramTotalCacheKey);
		if(result == null)
		{
			result = getProjectTotalCalculator().calculateDiagramObjectAssignedTotals(baseObject);
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
			result = getProjectTotalCalculator().getTotalTimePeriodCostsMapForChildTasks(baseObjectForRow, assignmentsTag);
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
	private ProjectTotalCalculator projectTotalCalculator;
	private HashMap<ORef, TimePeriodCostsMap> totalTimePeriodPlannedCostsMapsByBaseObject;
	private HashMap<ORef, TimePeriodCostsMap> totalTimePeriodAssignedCostsMapsByBaseObject;
	private HashMap<ORef, String> timeframeTotalAsStringByBaseObject;
	private HashMap<ORef, String> assignedWhenTotalAsStringByBaseObject;
	private HashMap<String, TimePeriodCostsMap> projectPlannedTotalsByBudgetMode;
	private HashMap<String, TimePeriodCostsMap> projectAssignedTotalsByBudgetMode;
	private HashMap<DiagramTotalCacheKey, TimePeriodCostsMap> diagramObjectPlannedTotalsByBudgetMode;
	private HashMap<DiagramTotalCacheKey, TimePeriodCostsMap> diagramObjectAssignedTotalsByBudgetMode;
	private HashMap<String, TimePeriodCostsMap> totalTimePeriodCostsMapForSubTasksByBaseObjectAndAssignmentsTag;
}
