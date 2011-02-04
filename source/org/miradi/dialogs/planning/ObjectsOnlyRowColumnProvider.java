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
package org.miradi.dialogs.planning;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;

public class ObjectsOnlyRowColumnProvider extends PlanningViewRowColumnProvider implements PlanningTreeRowColumnProvider
{
	public ObjectsOnlyRowColumnProvider(Project project)
	{
		super(project);
	}
	
	public CodeList getColumnCodesToShow() throws Exception
	{
		return new CodeList(getVisibleColumnsForSingleType(getCurrentViewData()));
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return getVisibleRowsForSingleType(getCurrentViewData());
	}

	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}

	private CodeList getVisibleRowsForSingleType(ViewData viewData)
	{
		String singleType = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		if(singleType.length() == 0)
			singleType = Goal.OBJECT_NAME;
		
		CodeList codes = new CodeList();
		codes.add(singleType);
		return codes;
	}

	private CodeList getVisibleColumnsForSingleType(ViewData viewData)
	{
		String propertyName = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		if(propertyName.length() == 0)
			return ObjectsOnlyRowColumnProvider.getGoalColumns();
		
		if (propertyName.equals(Goal.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getGoalColumns();
	
		if (propertyName.equals(Objective.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getObjectiveColumns();
		
		if (propertyName.equals(Strategy.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getStrategyColumns();
		
		if (propertyName.equals(Task.ACTIVITY_NAME))
			return ObjectsOnlyRowColumnProvider.getActivityColumns();
	
		if (propertyName.equals(Indicator.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getIndicatorColumns();
	
		if (propertyName.equals(Task.METHOD_NAME))
			return ObjectsOnlyRowColumnProvider.getMethodColumns();
	
		if (propertyName.equals(Task.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getTaskColumns();
		
		if (propertyName.equals(Measurement.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getMeasurementColumns();
		
		if (propertyName.equals(Target.OBJECT_NAME) || propertyName.equals(HumanWelfareTarget.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getTargetColumns();
		
		if (propertyName.equals(Cause.OBJECT_NAME_THREAT) || propertyName.equals(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR))
			return ObjectsOnlyRowColumnProvider.getDirectThreatsColumns();
		
		if (propertyName.equals(ResourceAssignment.OBJECT_NAME))
			return ObjectsOnlyRowColumnProvider.getAssignmentColumns();
		
		EAM.logError("getVisibleColumnsForSingleType unknown choice: " + propertyName);
		return new CodeList();
	}

	private static CodeList getGoalColumns()
	{
		String[] list = {
			Goal.PSEUDO_TAG_FACTOR,
			Goal.TAG_FULL_TEXT,
			// % complete,
			// Budget total,
		};
		return new CodeList(list);
	}

	private static CodeList getObjectiveColumns()
	{
		String[] list = {
				Objective.PSEUDO_TAG_FACTOR,
				Objective.TAG_FULL_TEXT,
				// % complete,
				// Budget total,
			};
			return new CodeList(list);
	}

	private static CodeList getStrategyColumns()
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

	private static CodeList getActivityColumns()
	{
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				};
			
		return new CodeList(list);
	}

	private static CodeList getIndicatorColumns()
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

	private static CodeList getMethodColumns()
	{
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				};
	
		return new CodeList(list);
	}

	private static CodeList getTaskColumns()
	{		
		String[] list = {
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				};
		
		return new CodeList(list);
	}

	private static CodeList getMeasurementColumns()
	{
		String[] list = {
				Measurement.TAG_DATE,
				};
	
		return new CodeList(list);		
	}

	private static CodeList getTargetColumns()
	{
		String[] list = {
				Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE,
				Target.TAG_TEXT,
				};
		return new CodeList(list);		
	}

	private static CodeList getDirectThreatsColumns()
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
}
