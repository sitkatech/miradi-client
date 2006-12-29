/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.budget.BudgetTreeTablePanel;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

public class BudgetTableEditorComponent extends DisposablePanel
{
	public BudgetTableEditorComponent(Project projectToUse, Actions actions, BudgetTreeTablePanel treeTableComponentToUse) throws Exception
	{
		super(new BorderLayout());
		project = projectToUse;
		treeTableComponent = treeTableComponentToUse;
		
		budgetTableModel = new BudgetTableModel(project, new IdList());
		budgetTable = new BudgetTable(project, budgetTableModel);
		
		UiScrollPane scrollPane = new UiScrollPane(budgetTable);
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.LINE_END);
	}

	public void dispose()
	{
		super.dispose();
	}

	public void setTaskId(BaseId taskId)
	{ 
		budgetTable.setTask(taskId);
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		
		budgetTableModel.setTask(task);
	}

	public void dataWasChanged()
	{
		
		budgetTableModel.dataWasChanged();
		
		if (budgetTable.getCellEditor() != null)
			budgetTable.getCellEditor().cancelCellEditing();
	}
	
	JPanel createButtonBar(Actions actions)
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel box = new JPanel(layout);
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), budgetTable));
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), treeTableComponent.getTree()));
		return box;
	}

	Project project;
	BudgetTableModel budgetTableModel;
	BudgetTable budgetTable;
	BudgetTreeTablePanel treeTableComponent;
}
