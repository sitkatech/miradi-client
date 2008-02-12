/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.organization;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Organization;
import org.miradi.project.Project;

public class OrganizationPoolTableModel extends ObjectPoolTableModel
{
	public OrganizationPoolTableModel(Project project)
	{
		super(project, ObjectType.ORGANIZATION, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Organization.TAG_SHORT_LABEL,
		Organization.TAG_LABEL,
		Organization.TAG_ROLES_DESCRIPTION,
		Organization.TAG_CONTACT_FIRST_NAME,
		Organization.TAG_CONTACT_LAST_NAME,
		Organization.TAG_EMAIL,
		Organization.TAG_PHONE_NUMBER,
	};
}
