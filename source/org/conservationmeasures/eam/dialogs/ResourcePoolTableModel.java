/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class ResourcePoolTableModel extends ObjectPoolTableModel
{
	public ResourcePoolTableModel(Project project)
	{
		super(project, ObjectType.PROJECT_RESOURCE, ProjectResource.TAG_NAME);
	}
}
