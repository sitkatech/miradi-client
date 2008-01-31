/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Organization;
import org.conservationmeasures.eam.project.ObjectManager;

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
