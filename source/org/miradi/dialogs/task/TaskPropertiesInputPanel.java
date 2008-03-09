/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.task;


import javax.swing.BorderFactory;
import javax.swing.Icon;

import org.martus.swing.UiLabel;
import org.miradi.actions.ActionEditActivityProgressReports;
import org.miradi.actions.Actions;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.ForecastOverrideSubPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.ActivityIcon;
import org.miradi.icons.EmptyIcon;
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
import org.miradi.utils.ObjectsActionButton;

public class TaskPropertiesInputPanel extends ObjectDataInputPanel
{
	public TaskPropertiesInputPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID);
	}
	
	public TaskPropertiesInputPanel(Project projectToUse, Actions actions, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		setBorder(BorderFactory.createEtchedBorder());
		
		hasBothSubTaskAssignmentsWarningLabel = new PanelTitleLabel(EAM.text("NOTE: The budget total for this task is the sum of the budget totals of its subtasks. The resource assignments below are not included in this value."));
		addCommonFields(actions);
	}
	
	public void dispose()
	{
		super.dispose();
	}

	private void addCommonFields(Actions actions)
	{
		taskNameLabel = new PanelTitleLabel("x");
		ObjectDataInputField taskNameField = createStringField(ObjectType.TASK, Task.TAG_LABEL);
		addFieldsOnOneLine(taskNameLabel, new ObjectDataInputField[] {taskNameField,} );

		ForecastOverrideSubPanel budgetSubPanel = new ForecastOverrideSubPanel(getProject(), actions, new ORef(Task.getObjectType(), BaseId.INVALID));
		addSubPanel(budgetSubPanel);

		addLabel(EAM.text("Budget"));
		addFieldComponent(budgetSubPanel);
		
		progressReportsLabel = new PanelTitleLabel(EAM.text("Progress Reports"));
		readOnlyProgressReportsList = createReadOnlyObjectList(Task.getObjectType(), Task.TAG_PROGRESS_REPORT_REFS);
		editProgressReportButton = createObjectsActionButton(actions.getObjectsAction(ActionEditActivityProgressReports.class), getPicker());
		addFieldWithEditButton(progressReportsLabel, readOnlyProgressReportsList, editProgressReportButton);
		
		addLabel(new UiLabel(""));
		addLabel(hasBothSubTaskAssignmentsWarningLabel);
		updateFieldsFromProject();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updatedWarningMessageVisiblity(orefsToUse);
		updateTaskNameLabel();
		hideOrShowProgressSection();
	}

	
	private void updateTaskNameLabel()
	{
		taskNameLabel.setIcon(getTaskTypeIcon());
		taskNameLabel.setText(getTaskTypeLabel());
	}
	
	private Icon getTaskTypeIcon()
	{
		Task task = getTask();
		if(task == null)
			return new EmptyIcon();
		
		if(task.isActivity())
			return new ActivityIcon();
		if(task.isMethod())
			return new MethodIcon();
		return new TaskIcon();
	}

	private String getTaskTypeLabel()
	{
		Task task = getTask();
		if(task == null)
			return "";
		return task.getTypeName();
	}
	
	private Task getTask()
	{
		ORef ref = getRefForType(Task.getObjectType());
		if(ref.isInvalid())
			return null;
		
		Task task = Task.find(getProject(), ref);
		return task;
	}
	
	private void hideOrShowProgressSection()
	{
		boolean isActivity = false;
		Task task = getTask();
		if(task != null)
			isActivity = task.isActivity();
		
		progressReportsLabel.setVisible(isActivity);
		readOnlyProgressReportsList.setVisible(isActivity);
		editProgressReportButton.setVisible(isActivity);
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
	
	PanelTitleLabel progressReportsLabel;
	ObjectDataInputField readOnlyProgressReportsList;
	ObjectsActionButton editProgressReportButton;

}