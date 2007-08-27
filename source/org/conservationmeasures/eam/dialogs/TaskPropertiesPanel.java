/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTableUnitsModel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.conservationmeasures.eam.views.workplan.AssignmentEditorComponent;
import org.conservationmeasures.eam.views.workplan.TaskPropertiesInputPanel;
import org.conservationmeasures.eam.views.workplan.WorkPlanTableEditorComponent;

public class TaskPropertiesPanel extends ObjectDataInputPanel
{
	public TaskPropertiesPanel(Project projectToUse) throws Exception
	{
		this(projectToUse, BaseId.INVALID);
	}
	
	public TaskPropertiesPanel(Project projectToUse, Actions actions, ObjectPicker objectPicker) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID, objectPicker);
	}
	
	public TaskPropertiesPanel(Project projectToUse, Actions actions, BaseId idToEdit, ObjectPicker objectPicker) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		BudgetTableUnitsModel budgetTableUnitsModel = new BudgetTableUnitsModel(project);
		editorComponent = new WorkPlanTableEditorComponent(actions, project, objectPicker, budgetTableUnitsModel);
		inputPanel = new TaskPropertiesInputPanel(project, idToEdit, editorComponent);
		
		setLayout(new BorderLayout());
		add(inputPanel, BorderLayout.PAGE_START);
		add(editorComponent, BorderLayout.CENTER);
	}

	public TaskPropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		inputPanel = new TaskPropertiesInputPanel(project, idToEdit);
		add(inputPanel);
	}
	
	public void dispose()
	{
		if(editorComponent != null)
			editorComponent.dispose();
		
		if(inputPanel != null)
			inputPanel.dispose();
		
		super.dispose();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		if (orefsToUse.length==0)
			inputPanel.setObjectRef(new ORef(ObjectType.FAKE,BaseId.INVALID));
		else
			inputPanel.setObjectRef(orefsToUse[0]);
	}
	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		inputPanel.commandExecuted(event);
	}

	Project project;
	AssignmentEditorComponent editorComponent;
	TaskPropertiesInputPanel inputPanel;	
}
