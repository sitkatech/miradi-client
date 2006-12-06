/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.BorderLayout;

import javax.swing.Box;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

public class BudgetTableEditorComponent extends DisposablePanel
{
	public BudgetTableEditorComponent(Project projectToUse, Actions actions, WorkPlanPanel treeTableComponentToUse)
	{
		super(new BorderLayout());
		project = projectToUse;
		treeTableComponent = treeTableComponentToUse;

		budgetTableModel = new BudgetTableModel(project, new DateRange[0], new Assignment[0]);
		budgetTable = new BudgetTable(project, budgetTableModel);
		
		UiScrollPane scrollPane = new UiScrollPane(budgetTable);
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.AFTER_LINE_ENDS);
	}

	public void dispose()
	{
		super.dispose();
	}

	public void setTaskId(BaseId taskId)
	{ 
		//FIXME budget code - rebuild table after new task has been selected in tree
		//budgetTable.setTask(taskId);
		//rebuild();
	}

	public void rebuild()
	{
		//FIXME budget code - table needs to fire??? 
		//budgetTableModel.fireTableDataChanged();
	}

	Box createButtonBar(Actions actions)
	{
		Box box = Box.createVerticalBox();
		box.add(new UiButton(actions.get(ActionRemoveAssignment.class)));
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), treeTableComponent.getTree()));
		return box;
	}

	Project project;
	BudgetTableModel budgetTableModel;
	BudgetTable budgetTable;
	WorkPlanPanel treeTableComponent;
}
