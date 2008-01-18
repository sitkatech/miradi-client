/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class StrategicRowColumnProvider implements RowColumnProvider
{	
	public CodeList getColumnListToShow()
	{
		return new CodeList(new String[] {
							Strategy.PSEUDO_TAG_RATING_SUMMARY,
							Desire.PSEUDO_TAG_FACTOR,
							BaseObject.PSEUDO_TAG_BUDGET_TOTAL,
							Strategy.PSEUDO_TAG_TAXONOMY_CODE_VALUE,
//							Strategy.PSEUDO_TAG_PROGRESS,
//							Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
//							Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
//							Task.PSEUDO_TAG_TASK_TOTAL, 
							});
	}

	public CodeList getRowListToShow()
	{
		return new CodeList(new String[] {
							ConceptualModelDiagram.OBJECT_NAME,
							ResultsChainDiagram.OBJECT_NAME,
							Goal.OBJECT_NAME,
							Objective.OBJECT_NAME,
							Strategy.OBJECT_NAME,});
	}
	
	public String getPropertyName()
	{
		return PlanningView.STRATEGIC_PLAN_RADIO_CHOICE;
	}
}
