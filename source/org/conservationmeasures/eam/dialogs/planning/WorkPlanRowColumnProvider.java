/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class WorkPlanRowColumnProvider implements RowColumnProvider
{
	public CodeList getColumnListToShow()
	{
		return new CodeList(new String[] {
				Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
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
