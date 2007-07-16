/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.budget.BudgetTableModelLockedHeaderRows;
import org.conservationmeasures.eam.views.budget.BudgetTableModelScrollableHeaderRows;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.conservationmeasures.eam.views.workplan.AssignmentEditorComponent;

public class BudgetTableEditorComponent extends AssignmentEditorComponent
{
	public BudgetTableEditorComponent(Project project, Actions actions, ObjectPicker picker, BudgetTableModel model) throws Exception
	{
		super(actions, project, picker,
				new BudgetTableModelLockedHeaderRows(model),
				new BudgetTableModelScrollableHeaderRows(model));
	}
}
