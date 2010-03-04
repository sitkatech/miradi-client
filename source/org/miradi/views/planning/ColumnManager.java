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
package org.miradi.views.planning;

import org.miradi.dialogs.planning.MonitoringRowColumnProvider;
import org.miradi.dialogs.planning.ProgressReportRowColumnProvider;
import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;

public class ColumnManager
{
	public static CodeList getGoalColumns()
	{
		String[] list = {
			Goal.PSEUDO_TAG_FACTOR,
			Goal.TAG_FULL_TEXT,
			// % complete,
			// Budget total,
		};
		return new CodeList(list);
	}

	public static CodeList getObjectiveColumns()
	{
		String[] list = {
				Objective.PSEUDO_TAG_FACTOR,
				Objective.TAG_FULL_TEXT,
				// % complete,
				// Budget total,
			};
			return new CodeList(list);
	}

	public static CodeList getStrategyColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				Strategy.PSEUDO_TAG_TAXONOMY_CODE_VALUE,
				Strategy.TAG_TEXT,
//				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
//				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
//				Task.PSEUDO_TAG_TASK_TOTAL, 
		};
			
		return new CodeList(list);
	}

	public static CodeList getActivityColumns()
	{
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				};
			
		return new CodeList(list);
	}

	public static CodeList getIndicatorColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
//				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
//				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
//				Task.PSEUDO_TAG_TASK_TOTAL, 
				};
	
		return new CodeList(list);
	}

	public static CodeList getMethodColumns()
	{
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				};
	
		return new CodeList(list);
	}

	public static CodeList getTaskColumns()
	{		
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				};
		
		return new CodeList(list);
	}
	
	public static CodeList getMeasurementColumns()
	{
		String[] list = {
				Measurement.TAG_DATE,
				};
	
		return new CodeList(list);		
	}
	
	public static CodeList getTargetColumns()
	{
		String[] list = {
				Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE,
				Target.TAG_TEXT,
				};
		return new CodeList(list);		
	}

	public static CodeList getDirectThreatsColumns()
	{
		String[] list = {
				Cause.PSEUDO_TAG_TAXONOMY_CODE_VALUE,
				Cause.TAG_TEXT,
				};
		return new CodeList(list);		
	}

	private static CodeList getAssignmentColumns()
	{
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				ResourceAssignment.PSEUDO_TAG_WHEN_TOTAL,
				Indicator.PSEUDO_TAG_FACTOR,
				};
		return new CodeList(list);
	}

	public static CodeList getProgressReportColumns()
	{
		return new ProgressReportRowColumnProvider().getColumnListToShow();
	}
	
	public static CodeList getStrategicPlanColumns()
	{
		return new StrategicRowColumnProvider().getColumnListToShow();
	}

	public static CodeList getMonitoringPlanColumns()
	{
		return new MonitoringRowColumnProvider().getColumnListToShow();
	}

	public static CodeList getWorkPlanColumns(Project project)
	{
		return new WorkPlanRowColumnProvider(project).getColumnListToShow();
	}

	public static CodeList getVisibleColumnsForSingleType(ViewData viewData)
	{
		String propertyName = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		if(propertyName.length() == 0)
			return ColumnManager.getGoalColumns();
		
		if (propertyName.equals(Goal.OBJECT_NAME))
			return ColumnManager.getGoalColumns();
	
		if (propertyName.equals(Objective.OBJECT_NAME))
			return ColumnManager.getObjectiveColumns();
		
		if (propertyName.equals(Strategy.OBJECT_NAME))
			return ColumnManager.getStrategyColumns();
		
		if (propertyName.equals(Task.ACTIVITY_NAME))
			return ColumnManager.getActivityColumns();
	
		if (propertyName.equals(Indicator.OBJECT_NAME))
			return ColumnManager.getIndicatorColumns();
	
		if (propertyName.equals(Task.METHOD_NAME))
			return ColumnManager.getMethodColumns();
	
		if (propertyName.equals(Task.OBJECT_NAME))
			return ColumnManager.getTaskColumns();
		
		if (propertyName.equals(Measurement.OBJECT_NAME))
			return ColumnManager.getMeasurementColumns();
		
		if (propertyName.equals(Target.OBJECT_NAME) || propertyName.equals(HumanWelfareTarget.OBJECT_NAME))
			return ColumnManager.getTargetColumns();
		
		if (propertyName.equals(Cause.OBJECT_NAME_THREAT) || propertyName.equals(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR))
			return ColumnManager.getDirectThreatsColumns();
		
		if (propertyName.equals(ResourceAssignment.OBJECT_NAME))
			return ColumnManager.getAssignmentColumns();
		
		EAM.logError("getVisibleColumnsForSingleType unknown choice: " + propertyName);
		return new CodeList();
	}

	public static CodeList getVisibleColumnsForCustomization(ViewData viewData)
	{
		try
		{
			ORef customizationRef = viewData.getORef(ViewData.TAG_TREE_CONFIGURATION_REF);
			if(customizationRef.isInvalid())
				return new CodeList();
			
			PlanningViewConfiguration customization = (PlanningViewConfiguration)viewData.getProject().findObject(customizationRef);
			CodeList columnCodes = customization.getColumnConfiguration();
			omitUnknownColumnTagsInPlace(viewData.getProject(), columnCodes);
			
			return columnCodes;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: Unable to read customized columns");
			return new CodeList();
		}
	}
	
	private static void omitUnknownColumnTagsInPlace(Project project, CodeList rawCodes)
	{
		ChoiceQuestion question = project.getQuestion(CustomPlanningColumnsQuestion.class);
		CodeList validColumnCodes = question.getAllCodes();
		CodeList originalCodeList = new CodeList(rawCodes);
		rawCodes.retainAll(validColumnCodes);
		
		boolean wereCodesRemoved = originalCodeList.size() != rawCodes.size();
		originalCodeList.subtract(validColumnCodes);
		if (wereCodesRemoved)
			EAM.logWarning(("Column codes list was filtered and had unknown codes removed from it. Codes removed:" + originalCodeList));
	}
}
