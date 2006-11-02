/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class ProjectResourcePropertiesPanel extends ObjectDataInputPanel
{
	public ProjectResourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, idToEdit);

		addField(createStringField(ProjectResource.TAG_INITIALS));
		addField(createStringField(ProjectResource.TAG_NAME));
		addField(createStringField(ProjectResource.TAG_POSITION));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Resource Properties");
	}
}
