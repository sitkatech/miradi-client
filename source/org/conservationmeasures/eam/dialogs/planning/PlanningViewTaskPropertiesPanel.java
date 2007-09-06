/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.dialogs.DisposablePanelWithDescription;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.TaskPropertiesInputPanel;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewTaskPropertiesPanel extends DisposablePanelWithDescription
{
	public PlanningViewTaskPropertiesPanel(Project projectToUse) throws Exception
	{
		super(new BasicGridLayout(2, 1));
		
		assignmentEditor = new PlanningViewAssignmentEditorComponent(projectToUse);
		taskPropertiesInputPanel = new TaskPropertiesInputPanel(projectToUse);
	
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
		assignmentEditor.setObjectRefs(hierarchyToSelectedRef);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Task Properties");
	}
	
	private PlanningViewAssignmentEditorComponent assignmentEditor;
	private TaskPropertiesInputPanel taskPropertiesInputPanel;
}
