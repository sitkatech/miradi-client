/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.task.TaskPropertiesInputPanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.umbrella.ObjectPicker;

public class PlanningViewTaskPropertiesPanel extends DisposablePanelWithDescription
{
	public PlanningViewTaskPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(new OneColumnGridLayout());
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		taskPropertiesInputPanel = new TaskPropertiesInputPanel(mainWindowToUse.getProject(), mainWindowToUse.getActions());
		assignmentEditor = new PlanningViewAssignmentEditorComponent(mainWindowToUse, objectPickerToUse);
	
		add(taskPropertiesInputPanel);
		add(assignmentEditor);
	}
	
	public void dispose()
	{
		taskPropertiesInputPanel.dispose();
		taskPropertiesInputPanel = null;
		
		assignmentEditor.dispose();
		assignmentEditor = null;
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
	
	public void dataWasChanged()
	{
		assignmentEditor.dataWasChanged();
	}
	
	private TaskPropertiesInputPanel taskPropertiesInputPanel;
	private PlanningViewAssignmentEditorComponent assignmentEditor;
}
