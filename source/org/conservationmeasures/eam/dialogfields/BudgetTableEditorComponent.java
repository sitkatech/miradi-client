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
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.budget.RowHeaderTable;
import org.conservationmeasures.eam.views.budget.RowHeaderTableModel;
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
		//FIXME budget code - add model to table
		//budgetTableModel = new BudgetTableModel(project);
		budgetTable = new BudgetTable(project);
		
		rowHeaderTableModel = new RowHeaderTableModel(project);
		rowHeaderTable = new RowHeaderTable(project);
		
		//FIXME budget code - only one table, no fixed row header
		//UiScrollPane fixedRowHeaderScrollPane = new UiScrollPane(rowHeaderTable);
		//add(fixedRowHeaderScrollPane, BorderLayout.WEST);
		
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
		budgetTable.setTask(taskId);
		rebuild();
	}

	public void rebuild()
	{
		System.out.println("budgetTableEditor rebuild");
		//FIXME budget code - table needs to fire 
		//budgetTableModel.fireTableDataChanged();
		//treeTableComponent.getTree().getModel()
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
	RowHeaderTable rowHeaderTable;
	RowHeaderTableModel rowHeaderTableModel;
	WorkPlanPanel treeTableComponent;
}
