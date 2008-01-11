/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;

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
