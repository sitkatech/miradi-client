/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning;

import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.PlanningView;

public class WorkPlanRowColumnProvider implements RowColumnProvider
{
	public CodeList getColumnListToShow()
	{
		return new CodeList(new String[] {
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				BaseObject.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				Task.PSEUDO_TAG_TASK_BUDGET_DETAIL,
				});
	}

	public CodeList getRowListToShow()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagram.OBJECT_NAME,
				ResultsChainDiagram.OBJECT_NAME,
				Strategy.OBJECT_NAME,
				Task.ACTIVITY_NAME,
				Indicator.OBJECT_NAME,
				Task.METHOD_NAME,
				Task.OBJECT_NAME, });
	}
	
	public String getPropertyName()
	{
		return PlanningView.WORKPLAN_PLAN_RADIO_CHOICE;
	}
}
