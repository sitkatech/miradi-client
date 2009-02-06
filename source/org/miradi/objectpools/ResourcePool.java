/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.project.ObjectManager;
import org.miradi.questions.ResourceRoleQuestion;

public class ResourcePool extends EAMNormalObjectPool
{
	public ResourcePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.PROJECT_RESOURCE);
	}
	
	public void put(ProjectResource resource) throws Exception
	{
		put(resource.getId(), resource);
	}
	
	public ProjectResource find(BaseId id)
	{
		return (ProjectResource)getRawObject(id);
	}

	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new ProjectResource(objectManager, actualId);
	}

	public ProjectResource[] getAllProjectResources()
	{
		BaseId[] allIds = getIds();
		ProjectResource[] allProjectResources = new ProjectResource[allIds.length];
		for (int i = 0; i < allProjectResources.length; ++i)
		{
			allProjectResources[i] = find(allIds[i]);
		}
		
		return allProjectResources;
	}
	
	public ORefList getTeamMemberRefs()
	{
		ORefList teamMembers = new ORefList();
		try
		{
			ProjectResource[] projectResources = getAllProjectResources();
			for (int i = 0; i < projectResources.length; ++i)
			{
				if (projectResources[i].hasRole(ResourceRoleQuestion.TeamMemberRoleCode))
					teamMembers.add(projectResources[i].getRef());
			}
			
			return teamMembers;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}
}
