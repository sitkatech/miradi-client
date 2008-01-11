/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.resource;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
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
		ProjectResource.TAG_GIVEN_NAME,
		ProjectResource.TAG_SUR_NAME,
		ProjectResource.TAG_ORGANIZATION,
		ProjectResource.TAG_POSITION,
		ProjectResource.TAG_PHONE_NUMBER,
		ProjectResource.TAG_EMAIL,
	};

}
