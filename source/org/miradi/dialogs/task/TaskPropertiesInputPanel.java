/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.task;


import javax.swing.BorderFactory;
import javax.swing.Icon;

import org.martus.swing.UiLabel;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.ForcastOverrideSubPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.ActivityIcon;
import org.miradi.icons.MethodIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class TaskPropertiesInputPanel extends ObjectDataInputPanel
{
	public TaskPropertiesInputPanel(Project projectToUse) throws Exception
	{
		this(projectToUse, BaseId.INVALID);
	}
	
	public TaskPropertiesInputPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
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
		taskNameLabel = new PanelTitleLabel("x");
		ObjectDataInputField taskNameField = createStringField(ObjectType.TASK, Task.TAG_LABEL);
		addFieldsOnOneLine(taskNameLabel, new ObjectDataInputField[] {taskNameField,} );

		ForcastOverrideSubPanel budgetSubPanel = new ForcastOverrideSubPanel(getProject(), new ORef(Task.getObjectType(), BaseId.INVALID));
		addSubPanel(budgetSubPanel);

		addLabel(EAM.text("Budget"));
		addFieldComponent(budgetSubPanel);
		
		
		addLabel(new UiLabel(""));
		addLabel(hasBothSubTaskAssignmentsWarningLabel);
		updateFieldsFromProject();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updatedWarningMessageVisiblity(orefsToUse);
		updateTaskNameLabel();
	}

	
	private void updateTaskNameLabel()
	{
		taskNameLabel.setIcon(getTaskTypeIcon());
		taskNameLabel.setText(getTaskTypeLabel());
	}
	
	private Icon getTaskTypeIcon()
	{
		ORef ref = getRefForType(Task.getObjectType());
		if(ref.isInvalid())
			return null;
		
		Task task = Task.find(getProject(), ref);
		if(task.isActivity())
			return new ActivityIcon();
		if(task.isMethod())
			return new MethodIcon();
		return new TaskIcon();
	}

	private String getTaskTypeLabel()
	{
		ORef ref = getRefForType(Task.getObjectType());
		if(ref.isInvalid())
			return "";
		
		Task task = Task.find(getProject(), ref);
		return task.getTypeName();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void updateTable()
	{
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
	
	private PanelTitleLabel hasBothSubTaskAssignmentsWarningLabel;
	private PanelTitleLabel taskNameLabel;
}