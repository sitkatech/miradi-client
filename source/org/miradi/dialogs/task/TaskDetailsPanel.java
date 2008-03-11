/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.task;

import javax.swing.Icon;

import org.miradi.actions.ActionEditActivityProgressReports;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.ActivityIcon;
import org.miradi.icons.EmptyIcon;
import org.miradi.icons.MethodIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

public class TaskDetailsPanel extends ObjectDataInputPanel
{
	public TaskDetailsPanel(Project projectToUse, Actions actions, BaseId idToEdit)
	{
		super(projectToUse, Task.getObjectType(), idToEdit);
		
		taskNameLabel = new PanelTitleLabel("x");
		ObjectDataInputField taskNameField = createStringField(ObjectType.TASK, Task.TAG_LABEL);
		addFieldsOnOneLine(taskNameLabel, new ObjectDataInputField[] {taskNameField,} );
		addField(createMultilineField(Task.getObjectType(), Task.TAG_DETAILS));

		progressReportsLabel = new PanelTitleLabel(EAM.text("Progress Reports"));
		readOnlyProgressReportsList = createReadOnlyObjectList(Task.getObjectType(), Task.TAG_PROGRESS_REPORT_REFS);
		editProgressReportButton = createObjectsActionButton(actions.getObjectsAction(ActionEditActivityProgressReports.class), getPicker());
		addFieldWithEditButton(progressReportsLabel, readOnlyProgressReportsList, editProgressReportButton);
		
		
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
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updateTaskNameLabel();
		hideOrShowProgressSection();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Task Details Panel Title|");
	}

	private PanelTitleLabel taskNameLabel;
	
	PanelTitleLabel progressReportsLabel;
	ObjectDataInputField readOnlyProgressReportsList;
	ObjectsActionButton editProgressReportButton;

}
