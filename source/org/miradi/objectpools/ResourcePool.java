/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
	
	public void put(ProjectResource resource)
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
