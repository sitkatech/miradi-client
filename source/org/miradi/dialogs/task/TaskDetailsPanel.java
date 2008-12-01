/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
import org.miradi.views.umbrella.ObjectPicker;

public class TaskDetailsPanel extends ObjectDataInputPanel
{
	public TaskDetailsPanel(Project projectToUse, Actions actionsToUse, BaseId idToEdit)
	{
		super(projectToUse, Task.getObjectType(), idToEdit);
		
		taskNameLabel = new PanelTitleLabel("x");
		ObjectDataInputField taskNameField = createExpandableField(ObjectType.TASK, Task.TAG_LABEL);
		ObjectDataInputField taskIdField = createShortStringField(ObjectType.TASK, Task.TAG_SHORT_LABEL);
		addFieldsOnOneLine(taskNameLabel, new ObjectDataInputField[] {taskIdField, taskNameField,} );
		addField(createMultilineField(Task.getObjectType(), Task.TAG_DETAILS));

		progressReportsLabel = new PanelTitleLabel(EAM.text("Progress Reports"));
		readOnlyProgressReportsList = createReadOnlyObjectList(Task.getObjectType(), Task.TAG_PROGRESS_REPORT_REFS);
		editProgressReportButton = createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditActivityProgressReports.class), null);
		addFieldWithEditButton(progressReportsLabel, readOnlyProgressReportsList, editProgressReportButton);
	}

	private void hideOrShowProgressSection()
	{
		boolean isActivity = false;
		Task task = getTask();
		if(task != null)
			isActivity = task.isActivity();
		
		ObjectPicker picker = null;
		if (isActivity)
			picker = super.getPicker();
				
		editProgressReportButton.setPicker(picker);
		
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
		if(task.isActivity())
			return EAM.text("Activity");
		if(task.isMethod())
			return EAM.text("Method");
		return EAM.text("Task");
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
		return EAM.text("Summary");
	}
	
	private PanelTitleLabel taskNameLabel;
	
	private PanelTitleLabel progressReportsLabel;
	private ObjectDataInputField readOnlyProgressReportsList;
	private ObjectsActionButton editProgressReportButton;
}
