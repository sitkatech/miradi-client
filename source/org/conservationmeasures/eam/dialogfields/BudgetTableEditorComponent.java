/* 
 * Copyright 2005-2007, Wildlife Conservation Society, 
 * Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
 * Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
 */ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.budget.BudgetTableModelLockedHeaderRows;
import org.conservationmeasures.eam.views.budget.BudgetTableModelScrollableHeaderRows;
import org.conservationmeasures.eam.views.budget.BudgetTreeTablePanel;
import org.conservationmeasures.eam.views.workplan.AssignmentEditorComponent;

public class BudgetTableEditorComponent extends AssignmentEditorComponent
{
	public BudgetTableEditorComponent(Project projectToUse, Actions actions, BudgetTreeTablePanel treeTableComponentToUse) throws Exception
	{
		super(actions, projectToUse, treeTableComponentToUse.getTree());
		objectPicker = treeTableComponentToUse.getTree();

		mainTableModel = new BudgetTableModel(projectToUse, new IdList());
		lockedModel = new BudgetTableModelLockedHeaderRows(mainTableModel);
		scrollModel = new BudgetTableModelScrollableHeaderRows(mainTableModel);
		
		lockedTable = new BudgetTable(projectToUse, lockedModel);
		scrollTable = new BudgetTable(projectToUse, scrollModel);
		
		JScrollPane scrollPane = createScrollPaneWithFixedHeader();
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.LINE_END);
	}
}
