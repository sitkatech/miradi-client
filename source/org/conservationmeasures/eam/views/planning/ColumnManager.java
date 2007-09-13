package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.dialogs.planning.MonitoringRowColumnProvider;
import org.conservationmeasures.eam.dialogs.planning.StrategicRowColumnProvider;
import org.conservationmeasures.eam.dialogs.planning.WorkPlanRowColumnProvider;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.CodeList;

public class ColumnManager
{

	static public CodeList getMasterColumnList()
	{
		CodeList masterColumnList = new CodeList();
		masterColumnList.add(Indicator.TAG_PRIORITY);
		masterColumnList.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		masterColumnList.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
		masterColumnList.add(Task.PSEUDO_TAG_TASK_TOTAL);
		
		masterColumnList.add(Indicator.PSEUDO_TAG_FACTOR);
		masterColumnList.add(Indicator.TAG_MEASUREMENT_SUMMARY);
		masterColumnList.add(Indicator.PSEUDO_TAG_METHODS); 
		masterColumnList.add(Indicator.PSEUDO_TAG_STATUS_VALUE);
		
		return masterColumnList;
	}

	public static CodeList getGoalColumns()
	{
		
		return new CodeList();
	}

	public static CodeList getObjectiveColumns()
	{
		return new CodeList();
	}

	public static CodeList getStrategyColumns()
	{
		String[] list = {
		Indicator.TAG_PRIORITY,
		Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
		Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
		Task.PSEUDO_TAG_TASK_TOTAL, };
			
		return new CodeList(list);
	}

	public static CodeList getActivityColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_TOTAL, };
			
		return new CodeList(list);
	}

	public static CodeList getIndicatorColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_TOTAL, };
	
		return new CodeList(list);
	}

	public static CodeList getMethodColumns()
	{
		String[] list = {
				Indicator.TAG_PRIORITY,
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_TOTAL, };
	
		return new CodeList(list);
	}

	public static CodeList getTaskColumns()
	{		
		String[] list = {
				Indicator.TAG_PRIORITY,
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_TOTAL, };
		
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

	private static CodeList getStrategicPlanColumns()
	{
		return new StrategicRowColumnProvider().getColumnListToShow();
	}

	static CodeList getMonitoringPlanColumns()
	{
		return new MonitoringRowColumnProvider().getColumnListToShow();
	}

	private static CodeList getWorkPlanColumns()
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
