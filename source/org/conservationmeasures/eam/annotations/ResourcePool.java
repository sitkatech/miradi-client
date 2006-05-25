/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ObjectPool;

public class ResourcePool extends ObjectPool
{
	public void put(ProjectResource resource)
	{
		put(resource.getId(), resource);
	}
	
	public ProjectResource find(int id)
	{
		return (ProjectResource)getRawObject(id);
	}


}
