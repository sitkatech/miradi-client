/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;


import javax.swing.BorderFactory;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

public class TaskPropertiesInputPanel extends ObjectDataInputPanel
{
	public TaskPropertiesInputPanel(Project projectToUse) throws Exception
	{
		this(projectToUse, BaseId.INVALID);
	}
	public TaskPropertiesInputPanel(Project projectToUse, BaseId idToEdit, AssignmentEditorComponent editorComponentToUse) throws Exception
	{
		this(projectToUse, idToEdit);
		editorComponent = editorComponentToUse;
	}
	
	public TaskPropertiesInputPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		
		hasBothSubTaskAssignmentsWarningLabel = new PanelTitleLabel(EAM.text("NOTE: The budget total for this task is the sum of the budget totals of its subtasks. The resource assignments below are not included in this value."));
		addCommonFields();
	}
	
	public void dispose()
	{
		super.dispose();
	}

	private void addCommonFields()
	{
		addField(createStringField(ObjectType.TASK, Task.TAG_LABEL));
		addField(createReadonlyTextField(Task.PSEUDO_TAG_TASK_BUDGET_TOTAL));
		addLabel(new UiLabel(""));
		addLabel(hasBothSubTaskAssignmentsWarningLabel);
		updateFieldsFromProject();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updatedWarningMessageVisiblity(orefsToUse);
		
		if (editorComponent == null)
			return;
		if (orefsToUse.length==0)
			editorComponent.setTaskId(BaseId.INVALID);
		else
			editorComponent.setTaskId(orefsToUse[0].getObjectId());
	}

	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void updateTable()
	{
		if (editorComponent == null)
			return;
		
		editorComponent.dataWasChanged();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			updateTable();
	}
	
	private void updatedWarningMessageVisiblity(ORef[] orefsToUse)
	{
		hasBothSubTaskAssignmentsWarningLabel.setVisible(isVisible(orefsToUse));
	}
			
	private boolean isVisible(ORef[] orefsToUse)
	{
		if (orefsToUse.length == 0)
			return false;
		
		ORef firstRef = orefsToUse[0];
		if(firstRef.isInvalid())
			return false;
		
		BaseObject foundObject = getProject().findObject(firstRef);
		if (foundObject.getType() != Task.getObjectType())
			return false;
		
		Task task = (Task) foundObject;
		if (task.getSubtaskCount() == 0 || task.getAssignmentRefs().size() == 0)
			return false;
		
		return true;
	}
	
	Project project;
	AssignmentEditorComponent editorComponent;
	PanelTitleLabel hasBothSubTaskAssignmentsWarningLabel;
}