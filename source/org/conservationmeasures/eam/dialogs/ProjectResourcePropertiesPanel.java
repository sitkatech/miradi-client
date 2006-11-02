/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class ProjectResourcePropertiesPanel extends ObjectDataInputPanel
{
	public ProjectResourcePropertiesPanel(Project projectToUse, ProjectResource objectToEdit) throws Exception
	{
		super(projectToUse, objectToEdit.getType(), objectToEdit.getId());

		addField(createStringField(objectToEdit.TAG_INITIALS));
		addField(createStringField(objectToEdit.TAG_NAME));
		addField(createStringField(objectToEdit.TAG_POSITION));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Resource Properties");
	}
}
