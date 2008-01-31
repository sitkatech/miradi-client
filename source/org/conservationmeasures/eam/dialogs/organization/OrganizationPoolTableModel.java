/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.organization;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Organization;
import org.conservationmeasures.eam.project.Project;

public class OrganizationPoolTableModel extends ObjectPoolTableModel
{
	public OrganizationPoolTableModel(Project project)
	{
		super(project, ObjectType.ORGANIZATION, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Organization.TAG_LABEL,
	};
}
