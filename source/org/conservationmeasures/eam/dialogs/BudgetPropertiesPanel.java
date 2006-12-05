/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.BudgetTableEditorComponent;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class BudgetPropertiesPanel extends ObjectDataInputPanel
{
	public BudgetPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID);
	}
	
	public BudgetPropertiesPanel(Project projectToUse, Actions actions, Task task) throws Exception
	{
		this(projectToUse, actions, task.getId());
	}
	
	public BudgetPropertiesPanel(Project projectToUse, Actions actions, BaseId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToShow);
		
		BudgetTableEditorComponent tableEditroComponent = new BudgetTableEditorComponent(projectToUse, actions);
		add(tableEditroComponent);
		//FIXME budget code - not working yet
		//updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Text|Budget propertiess");
	}
}
