/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;
import java.awt.Dimension;

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
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiScrollPane;

public class AssignmentEditorComponent extends DisposablePanel
{
	public AssignmentEditorComponent(Actions actions, Project projectToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(new BorderLayout());
		
		//FIXME budget code - remove, added just to display bigger columns
		Dimension dimension = new Dimension(950,400);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		
		project = projectToUse;
		objectPicker = objectPickerToUse;

		unitsModel = new BudgetTableUnitsModel(project, new IdList());
		budgetTable = new BudgetTable(project, unitsModel);
		
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
		
		unitsModel.setTask(task);
	}

	public void dataWasChanged()
	{
		unitsModel.dataWasChanged();
	}
	
	Box createButtonBar(Actions actions)
	{
		Box box = Box.createVerticalBox();
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), budgetTable));
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), objectPicker));
		return box;
	}

	Project project;
	BudgetTableUnitsModel unitsModel;
	BudgetTable budgetTable;
	ObjectPicker objectPicker;
}

