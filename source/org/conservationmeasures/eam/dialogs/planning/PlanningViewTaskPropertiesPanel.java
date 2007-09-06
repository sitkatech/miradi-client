/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.util.Vector;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.TaskPropertiesInputPanel;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewTaskPropertiesPanel extends DisposablePanel
{
	public PlanningViewTaskPropertiesPanel(Project projectToUse) throws Exception
	{
		super(new BasicGridLayout(2, 1));
		
		taskEditor = new PlanningViewAssignmentEditorComponent();
		taskPropertiesInputPanel = new TaskPropertiesInputPanel(projectToUse);
	
		add(taskPropertiesInputPanel);
		add(taskEditor);
	}
	
	public void dispose()
	{
		taskPropertiesInputPanel.dispose();
		taskPropertiesInputPanel = null;
		
		taskEditor.dispose();
		taskEditor = null;
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Task Property");
	}
	
	public Vector getFields()
	{
		return taskPropertiesInputPanel.getFields();
	}

	private PlanningViewAssignmentEditorComponent taskEditor;
	private TaskPropertiesInputPanel taskPropertiesInputPanel;
}
