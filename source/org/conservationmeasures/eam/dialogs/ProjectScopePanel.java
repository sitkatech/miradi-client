/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;

public class ProjectScopePanel extends ObjectDataInputPanel
{
	public ProjectScopePanel(Project projectToUse, ProjectMetadata metadata)
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		addField(createMultilineField(metadata.TAG_PROJECT_SCOPE));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(metadata.TAG_PROJECT_VISION));
		addField(createStringField(metadata.TAG_SHORT_PROJECT_VISION));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Project Scope Properties");
	}

}
