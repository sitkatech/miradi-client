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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableModel;
import org.conservationmeasures.eam.views.budget.RowHeaderTable;
import org.conservationmeasures.eam.views.budget.RowHeaderTableModel;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

public class BudgetTableEditorComponent extends DisposablePanel
{
	public BudgetTableEditorComponent(Project projectToUse, Actions actions)
	{
		super(new BorderLayout());
		project = projectToUse;

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
		//FIXME budget code - add and remove buttons in bar
		add(createButtonBar(actions), BorderLayout.AFTER_LINE_ENDS);
	}

	public void dispose()
	{
		super.dispose();
	}

	public void setList(IdList idList)
	{
		//FIXME budget code - table needs to be updated from here. 
		//budgetTable.setTable(idList);
		rebuild();
	}

	public void rebuild()
	{
		budgetTableModel.fireTableDataChanged();
	}

	Box createButtonBar(Actions actions)
	{
		Box box = Box.createVerticalBox();
		box.add(new UiButton(actions.get(ActionRemoveAssignment.class)));
		box.add(new UiButton(actions.get(ActionAddAssignment.class)));
		//FIXME table needs to be picker for remove
		//box.add(createObjectsActionButton(actions.getObjectsAction(ActionRemoveAssignment.class), budgetTable));
		return box;
	}

	Project project;
	BudgetTableModel budgetTableModel;
	BudgetTable budgetTable;
	RowHeaderTable rowHeaderTable;
	RowHeaderTableModel rowHeaderTableModel; 
}
