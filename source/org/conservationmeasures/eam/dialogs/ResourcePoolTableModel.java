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
		super(project, ObjectType.PROJECT_RESOURCE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		ProjectResource.TAG_INITIALS,
		ProjectResource.TAG_NAME,
		ProjectResource.TAG_POSITION,
		ProjectResource.TAG_PHONE_NUMBER,
		ProjectResource.TAG_EMAIL,
		ProjectResource.TAG_COST_PER_UNIT,
		ProjectResource.TAG_COST_UNIT,
		ProjectResource.TAG_ORGANIZATION,
	};

}
