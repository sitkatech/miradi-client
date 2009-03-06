/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.task.TaskPropertiesInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.ObjectPicker;

public class PlanningViewTaskPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public PlanningViewTaskPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), Task.getObjectType());

		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		taskPropertiesInputPanel = new TaskPropertiesInputPanel(mainWindowToUse, null, BaseId.INVALID);
		assignmentEditor = new PlanningViewAssignmentEditorComponent(mainWindowToUse, objectPickerToUse);
	
		addSubPanelWithTitledBorder(taskPropertiesInputPanel);
		
		createSingleSection(EAM.text("Assignments"));
		add(assignmentEditor);
	}
	
	public void dispose()
	{
		assignmentEditor.dispose();
		assignmentEditor = null;

		super.dispose();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		taskPropertiesInputPanel.setObjectRefs(hierarchyToSelectedRef);
		assignmentEditor.setObjectRefs(hierarchyToSelectedRef);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Task Properties");
	}
	
	public void dataWasChanged() throws Exception
	{
		assignmentEditor.dataWasChanged();
	}
	
	private TaskPropertiesInputPanel taskPropertiesInputPanel;
	private PlanningViewAssignmentEditorComponent assignmentEditor;
}
