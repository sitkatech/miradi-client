/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class WorkPlanTableEditorComponent extends AssignmentEditorComponent
{
	public WorkPlanTableEditorComponent(Actions actions, Project projectToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(actions, projectToUse, objectPickerToUse);
		
		project = projectToUse;
		objectPicker = objectPickerToUse;

		mainTableModel = new BudgetTableUnitsModel(project, new IdList());
		lockedModel = new WorkPlanTableModelLockedHeaderRows(mainTableModel);
		scrollModel = new WorkPlanTableModelScrollableHeaderRows(mainTableModel);
		
		lockedTable = new BudgetTable(projectToUse, lockedModel);
		scrollTable = new BudgetTable(projectToUse, scrollModel);
				
		JScrollPane scrollPane = createScrollPaneWithFixedHeader();
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.EAST);
	}
}
