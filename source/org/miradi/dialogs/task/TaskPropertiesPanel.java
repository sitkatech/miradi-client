/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.task;

import javax.swing.BorderFactory;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class TaskPropertiesPanel extends ObjectDataInputPanel
{
	public TaskPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, BaseId.INVALID);
	}
	
	public TaskPropertiesPanel(Project projectToUse, Actions actions, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		setLayout(new OneColumnGridLayout());
		project = projectToUse;
		setBorder(BorderFactory.createEtchedBorder());
		inputPanel = new TaskPropertiesInputPanel(project, actions, idToEdit);
		
		String hintAboutPlanningView = EAM.text("<html><em>HINT: " +
				"To manage the details about who will do the work and when, " +
				"go to the Planning View and choose Work Plan</em>");
		
		add(inputPanel);
		add(new PanelTitleLabel(hintAboutPlanningView));
	}
	
	public void dispose()
	{
		if(inputPanel != null)
			inputPanel.dispose();
		
		super.dispose();
	}

	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		if (orefsToUse.length==0)
			inputPanel.setObjectRef(new ORef(ObjectType.FAKE,BaseId.INVALID));
		else
			inputPanel.setObjectRef(orefsToUse[0]);
	}
	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Task Properties");
	}
		
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		inputPanel.commandExecuted(event);
	}

	Project project;
	TaskPropertiesInputPanel inputPanel;	
}
