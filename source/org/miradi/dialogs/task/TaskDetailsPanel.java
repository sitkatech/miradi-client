/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.activity.ActivityFactorVisibilityControlPanel;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.ActivityIcon;
import org.miradi.icons.EmptyIcon;
import org.miradi.icons.MonitoringActivityIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.FillerLabel;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.planning.ConfigurablePlanningManagementPanel;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.workplan.WorkPlanView;

import javax.swing.*;

public class TaskDetailsPanel extends ObjectDataInputPanel
{
	public TaskDetailsPanel(Project projectToUse, MainWindow mainWindowToUse, ActivityFactorVisibilityControlPanel activityVisibilityButtonPanel) throws Exception
	{
		super(projectToUse, TaskSchema.getObjectType());
		
		taskNameLabel = new PanelTitleLabel("x");
		ObjectDataInputField taskNameField = createExpandableField(ObjectType.TASK, Task.TAG_LABEL);
		ObjectDataInputField taskIdField = createShortStringField(ObjectType.TASK, Task.TAG_SHORT_LABEL);
		addFieldsOnOneLine(taskNameLabel, new ObjectDataInputField[] {taskIdField, taskNameField,} );

		if (canEnableMonitoringActivityField(projectToUse, mainWindowToUse))
			addField(createCheckBoxField(TaskSchema.getObjectType(), Task.TAG_IS_MONITORING_ACTIVITY, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
		else
			addField(createReadOnlyCheckBoxField(TaskSchema.getObjectType(), Task.TAG_IS_MONITORING_ACTIVITY, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));

		if (activityVisibilityButtonPanel != null)
		{
			add(new FillerLabel());
			addSubPanelField(activityVisibilityButtonPanel);
		}
		
		addField(createMultilineField(TaskSchema.getObjectType(), Task.TAG_DETAILS));
		addCustomFields(mainWindowToUse.getActions());
		addTaxonomyFields(TaskSchema.getObjectType());
		addField(createMultilineField(TaskSchema.getObjectType(), Task.TAG_COMMENTS));
	}

	protected void addCustomFields(Actions actionsToUse)
	{
	}

	private void updateTaskNameLabel()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				taskNameLabel.setIcon(getTaskTypeIcon());
				taskNameLabel.setText(getTaskTypeLabel());
			}
		});
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

	private boolean canEnableMonitoringActivityField(Project projectToUse, MainWindow mainWindowToUse)
	{
		String currentViewName = projectToUse.getCurrentView();

		if (currentViewName.equals(WorkPlanView.getViewName()))
			return false;

		if (currentViewName.equals(PlanningView.getViewName()))
		{
			UmbrellaView currentView = mainWindowToUse.getCurrentView();
			MiradiTabContentsPanelInterface currentTab = currentView.getCurrentTabPanel();
			DisposablePanelWithDescription currentTabTabContentsComponent = currentTab.getTabContentsComponent();
			return !(currentTabTabContentsComponent instanceof ConfigurablePlanningManagementPanel);
		}

		return true;
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
