/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.BorderFactory;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class ActivitiesPropertiesPanel extends ObjectDataInputPanel
{
	public ActivitiesPropertiesPanel(Actions actions, Project projectToUse, BaseId idToEdit, MainWindow mainWindow) throws Exception
	{
		super(projectToUse, ObjectType.TASK, idToEdit);
		setBorder(BorderFactory.createEtchedBorder());
		
		addField(createStringField(Task.TAG_LABEL));
		addField(createListField(actions, Task.TAG_RESOURCE_IDS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Properties");
	}
}
