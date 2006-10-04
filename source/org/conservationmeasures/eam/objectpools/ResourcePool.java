/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectResource;

public class ResourcePool extends EAMNormalObjectPool
{
	public ResourcePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.PROJECT_RESOURCE);
	}
	
	public void put(ProjectResource resource)
	{
		put(resource.getId(), resource);
	}
	
	public ProjectResource find(BaseId id)
	{
		return (ProjectResource)getRawObject(id);
	}

	EAMObject createRawObject(BaseId actualId)
	{
		return new ProjectResource(actualId);
	}


}
