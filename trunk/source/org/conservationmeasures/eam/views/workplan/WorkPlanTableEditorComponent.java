/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class WorkPlanTableEditorComponent extends AssignmentEditorComponent
{
	public WorkPlanTableEditorComponent(Actions actions, Project project, ObjectPicker objectPicker,BudgetTableUnitsModel model) throws Exception
	{
		super(actions, project, objectPicker, 
				new WorkPlanTableModelLockedHeaderRows(model),
				new WorkPlanTableModelScrollableHeaderRows(model));

	}
}
