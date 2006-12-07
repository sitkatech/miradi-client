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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
import org.martus.swing.UiScrollPane;

public class BudgetTableEditorComponent extends DisposablePanel
{
	public BudgetTableEditorComponent(Project projectToUse, Actions actions, WorkPlanPanel treeTableComponentToUse) throws Exception
	{
		super(new BorderLayout());
		project = projectToUse;
		treeTableComponent = treeTableComponentToUse;

		budgetTableModel = new BudgetTableModel(project, new IdList());
		budgetTable = new BudgetTable(project, budgetTableModel);
		
		UiScrollPane scrollPane = new UiScrollPane(budgetTable);
		add(scrollPane, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.EAST);
	}

	public void dispose()
	{
		super.dispose();
	}

	public void setTaskId(BaseId taskId)
	{ 
		//FIXME budget code - rebuild table after new task has been selected in tree
		budgetTable.setTask(taskId);
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		
		if (task == null)
			return;
		
		budgetTableModel.setTask(task);
	}

	public void dataWasChanged()
	{
		budgetTableModel.dataWasChanged();
	}
	
	Box createButtonBar(Actions actions)
	{
		Box box = Box.createVerticalBox();
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), budgetTable));
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), treeTableComponent.getTree()));
		return box;
	}

	Project project;
	BudgetTableModel budgetTableModel;
	BudgetTable budgetTable;
	WorkPlanPanel treeTableComponent;
}
