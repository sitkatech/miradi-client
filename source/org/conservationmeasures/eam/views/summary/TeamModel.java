/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class TeamModel extends ObjectListTableModel
{
	public TeamModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.PROJECT_METADATA, projectToUse.getMetadata().getId(), 
				ProjectMetadata.TAG_TEAM_RESOURCE_IDS, ObjectType.PROJECT_RESOURCE, ProjectResource.TAG_NAME);
	}
}