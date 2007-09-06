/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.BorderLayout;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.TaskPropertiesInputPanel;

public class PlanningViewTaskPropertiesPanel extends DisposablePanel
{
	public PlanningViewTaskPropertiesPanel(Project projectToUse) throws Exception
	{
		super(new BorderLayout());
		
		taskEditor = new PlanningViewTaskEditorComponent();
		taskPropertiesInputPanel = new TaskPropertiesInputPanel(projectToUse);
	
		add(taskPropertiesInputPanel, BorderLayout.CENTER);
		add(taskEditor, BorderLayout.LINE_END);
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

	private PlanningViewTaskEditorComponent taskEditor;
	private TaskPropertiesInputPanel taskPropertiesInputPanel;
}
