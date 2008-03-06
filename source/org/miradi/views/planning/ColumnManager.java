/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.planning;

import org.miradi.dialogs.planning.MonitoringRowColumnProvider;
import org.miradi.dialogs.planning.StrategicRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ViewData;
import org.miradi.utils.CodeList;

public class ColumnManager
{

	static public CodeList getMasterColumnList()
	{
		CodeList masterColumnList = new CodeList();
		
		masterColumnList.add(Desire.TAG_FULL_TEXT);
		masterColumnList.add(Indicator.PSEUDO_TAG_METHODS); 
		masterColumnList.add(Indicator.PSEUDO_TAG_FACTOR);
		masterColumnList.add(Indicator.TAG_PRIORITY);
		masterColumnList.add(Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE);
		masterColumnList.add(Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS);
		masterColumnList.add(Strategy.PSEUDO_TAG_TAXONOMY_CODE_VALUE);
		
		masterColumnList.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		masterColumnList.add(BaseObject.PSEUDO_TAG_COMBINED_EFFORT_DATES);
		masterColumnList.add(BaseObject.PSEUDO_TAG_BUDGET_TOTAL);
		masterColumnList.add(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL);
		masterColumnList.add(Measurement.META_COLUMN_TAG);
		masterColumnList.add(Indicator.META_COLUMN_TAG);
			
		return masterColumnList;
	}

	public static CodeList getGoalColumns()
	{
		String[] list = {
			Goal.TAG_FULL_TEXT,
			Goal.PSEUDO_TAG_FACTOR,
			// % complete,
			// Budget total,
		};
		return new CodeList(list);
	}

	public static CodeList getObjectiveColumns()
	{
		String[] list = {
				Objective.TAG_FULL_TEXT,
				Objective.PSEUDO_TAG_FACTOR,
				// % complete,
				// Budget total,
			};
			return new CodeList(list);
	}

	public static CodeList getStrategyColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				Strategy.TAG_TAXONOMY_CODE,
//				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
//				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
//				Task.PSEUDO_TAG_TASK_TOTAL, 
		};
			
		return new CodeList(list);
	}

	public static CodeList getActivityColumns()
	{
		String[] list = {
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				BaseObject.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_BUDGET_DETAIL, 
				};
			
		return new CodeList(list);
	}

	public static CodeList getIndicatorColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
//				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
//				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
//				Task.PSEUDO_TAG_TASK_TOTAL, 
				};
	
		return new CodeList(list);
	}

	public static CodeList getMethodColumns()
	{
		String[] list = {
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				BaseObject.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_BUDGET_DETAIL, 
				};
	
		return new CodeList(list);
	}

	public static CodeList getTaskColumns()
	{		
		String[] list = {
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				BaseObject.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_BUDGET_DETAIL, 
				};
		
		return new CodeList(list);
	}
	
	public static CodeList getMeasurementColumns()
	{
		//FIXME add the right tags here.  All measurement fields?
		String[] list = {
				Measurement.TAG_DATE,
				};
	
		return new CodeList(list);		
	}
	
	private static CodeList getTargetColumns()
	{
		String[] list = {
				Target.TAG_TARGET_STATUS,
				Target.PSEUDO_TAG_BUDGET_TOTAL,
				};
		return new CodeList(list);		
	}

	private static CodeList getDirectThreatsColumns()
	{
		String[] list = {
				Cause.TAG_TAXONOMY_CODE,
				Cause.PSEUDO_TAG_BUDGET_TOTAL
				};
		return new CodeList(list);		
	}

	private static CodeList getThreatReductionResultColumns()
	{
		String[] list = {
				ThreatReductionResult.PSEUDO_TAG_BUDGET_TOTAL,
				};
		return new CodeList(list);		
	}

	public static CodeList getVisibleColumnCodes(ViewData viewData)
	{
		String style = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		if(style.equals(PlanningView.STRATEGIC_PLAN_RADIO_CHOICE))
			return getStrategicPlanColumns();
		else if(style.equals(PlanningView.MONITORING_PLAN_RADIO_CHOICE))
			return getMonitoringPlanColumns();
		else if(style.equals(PlanningView.WORKPLAN_PLAN_RADIO_CHOICE))
			return getWorkPlanColumns();
		else if(style.equals(PlanningView.SINGLE_LEVEL_RADIO_CHOICE))
			return getVisibleColumnsForSingleType(viewData);
		else if(style.equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE))
			return getVisibleColumnsForCustomization(viewData);
		else if(style.equals(""))
			return getStrategicPlanColumns();
		else
		{
			EAM.logError("getVisibleColumnCodes unknown style: " + style);
			return new CodeList();
		}
	}

	public static CodeList getStrategicPlanColumns()
	{
		return new StrategicRowColumnProvider().getColumnListToShow();
	}

	public static CodeList getMonitoringPlanColumns()
	{
		return new MonitoringRowColumnProvider().getColumnListToShow();
	}

	public static CodeList getWorkPlanColumns()
	{
		return new WorkPlanRowColumnProvider().getColumnListToShow();
	}

	private static CodeList getVisibleColumnsForSingleType(ViewData viewData)
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
		
		if (propertyName.equals(Target.OBJECT_NAME))
			return ColumnManager.getTargetColumns();
		
		if (propertyName.equals(Cause.OBJECT_NAME_THREAT))
			return ColumnManager.getDirectThreatsColumns();
		
		if (propertyName.equals(ThreatReductionResult.OBJECT_NAME))
			return ColumnManager.getThreatReductionResultColumns();
		
		EAM.logError("getVisibleColumnsForSingleType unknown choice: " + propertyName);
		return new CodeList();
	}

	private static CodeList getVisibleColumnsForCustomization(ViewData viewData)
	{
		try
		{
			ORef customizationRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
			if(customizationRef.isInvalid())
				return new CodeList();
			PlanningViewConfiguration customization = (PlanningViewConfiguration)viewData.getProject().findObject(customizationRef);
			return customization.getColumnConfiguration();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: Unable to read customized columns");
			return new CodeList();
		}
	}

}
