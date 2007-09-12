/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class MonitoringRowColumnProvider implements RowColumnProvider
{
	public CodeList getColumnListToShow()
	{
		return new CodeList(new String[] {Indicator.PSEUDO_TAG_STATUS_VALUE,			
				 			 			  Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
				 			 			  Task.PSEUDO_TAG_COMBINED_EFFORT_DATES,
				 			 			  Task.PSEUDO_TAG_TASK_TOTAL,
				 			 			  Indicator.PSEUDO_TAG_FACTOR,
				 			 			  Indicator.PSEUDO_TAG_METHODS, });
	}

	public CodeList getRowListToShow()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagram.OBJECT_NAME,
				ResultsChainDiagram.OBJECT_NAME,
				Goal.OBJECT_NAME,
				Objective.OBJECT_NAME,
				Indicator.OBJECT_NAME, });
	}
	
	public String getPropertyName()
	{
		return PlanningView.MONITORING_PLAN_RADIO_CHOICE;
	}
}
