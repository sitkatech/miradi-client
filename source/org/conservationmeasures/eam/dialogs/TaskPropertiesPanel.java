/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTable;
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;

public class TaskPropertiesPanel extends ObjectDataInputPanel
{
	public TaskPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(actions, projectToUse, BaseId.INVALID);
	}
	public TaskPropertiesPanel(Actions actions, Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		setBorder(BorderFactory.createEtchedBorder());

		BudgetTableUnitsModel unitsModel = new BudgetTableUnitsModel(projectToUse, new IdList());
		BudgetTable budgetTable = new BudgetTable(projectToUse, unitsModel);
		JScrollPane scrollPane = new JScrollPane(budgetTable);
		add(scrollPane, BorderLayout.BEFORE_FIRST_LINE);
		
		addField(createReadonlyTextField(Task.PSEUDO_TAG_FACTOR_LABEL));
		addField(createStringField(Task.TAG_LABEL));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Properties");
	}
}
