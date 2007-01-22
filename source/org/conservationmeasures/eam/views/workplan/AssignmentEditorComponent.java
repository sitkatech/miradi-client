/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

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
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

public class AssignmentEditorComponent extends DisposablePanel
{
	public AssignmentEditorComponent(Actions actions, Project projectToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(new BorderLayout());
		
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
		budgetTable.setTask(taskId);
		Task task = (Task)project.findObject(ObjectType.TASK, taskId);
		
		unitsModel.setTask(task);
	}

	public void dataWasChanged()
	{
		unitsModel.dataWasChanged();
		budgetTable.cancelCellEditing();
	}
	
	JPanel createButtonBar(Actions actions)
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel box = new JPanel(layout);
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), budgetTable));
		box.add(createObjectsActionButton(actions.getObjectsAction(ActionAddAssignment.class), objectPicker));
		return box;
	}

	Project project;
	BudgetTableUnitsModel unitsModel;
	BudgetTable budgetTable;
	ObjectPicker objectPicker;
}

