/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.activity.ActivityFactorVisibilityControlPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.*;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.FillerLabel;

public class TaskDetailsPanel extends ObjectDataInputPanel
{
	public TaskDetailsPanel(Project projectToUse, Actions actionsToUse, ActivityFactorVisibilityControlPanel activityVisibilityButtonPanel, boolean shouldHaveIsMonitoringActivityField) throws Exception
	{
		super(projectToUse, TaskSchema.getObjectType());
		
		taskNameLabel = new PanelTitleLabel("x");
		ObjectDataInputField taskNameField = createExpandableField(ObjectType.TASK, Task.TAG_LABEL);
		ObjectDataInputField taskIdField = createShortStringField(ObjectType.TASK, Task.TAG_SHORT_LABEL);
		addFieldsOnOneLine(taskNameLabel, new ObjectDataInputField[] {taskIdField, taskNameField,} );

		if (shouldHaveIsMonitoringActivityField)
			addField(createCheckBoxField(TaskSchema.getObjectType(), Task.TAG_IS_MONITORING_ACTIVITY, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));

		if (activityVisibilityButtonPanel != null)
		{
			add(new FillerLabel());
			addSubPanelField(activityVisibilityButtonPanel);
		}
		
		addField(createMultilineField(TaskSchema.getObjectType(), Task.TAG_DETAILS));
		addCustomFields(actionsToUse);
		addTaxonomyFields(TaskSchema.getObjectType());
		addField(createMultilineField(TaskSchema.getObjectType(), Task.TAG_COMMENTS));
	}

	protected void addCustomFields(Actions actionsToUse)
	{
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

		if(task.isMonitoringActivity())
			return new MonitoringActivityIcon();

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

		if(task.isMonitoringActivity())
			return EAM.text("Monitoring Activity");

		if(task.isActivity())
			return EAM.text("Activity");

		if(task.isMethod())
			return EAM.text("Method");
		
		return EAM.text("Task");
	}
	
	private Task getTask()
	{
		ORef ref = getRefForType(TaskSchema.getObjectType());
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
	}

	@Override
	public void updateFieldsFromProject()
	{
		super.updateFieldsFromProject();
		updateTaskNameLabel();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}
	
	private PanelTitleLabel taskNameLabel;
}
