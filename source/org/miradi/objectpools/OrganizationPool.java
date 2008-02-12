/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Organization;
import org.miradi.project.ObjectManager;

public class OrganizationPool extends EAMNormalObjectPool
{
	public OrganizationPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.ORGANIZATION);
	}
	
	public void put(Organization organization)
	{
		put(organization.getId(), organization);
	}
	
	public Organization find(BaseId id)
	{
		return (Organization) getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new Organization(objectManager, actualId);
	}
}
