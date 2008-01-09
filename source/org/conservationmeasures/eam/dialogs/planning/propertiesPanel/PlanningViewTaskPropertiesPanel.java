/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.dialogs.base.DisposablePanelWithDescription;
import org.conservationmeasures.eam.dialogs.task.TaskPropertiesInputPanel;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class PlanningViewTaskPropertiesPanel extends DisposablePanelWithDescription
{
	public PlanningViewTaskPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(new OneColumnGridLayout());
		
		taskPropertiesInputPanel = new TaskPropertiesInputPanel(mainWindowToUse.getProject());
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
