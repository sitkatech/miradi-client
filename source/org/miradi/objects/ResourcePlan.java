/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.ResourcePlanId;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.utils.OptionalDouble;

public class ResourcePlan extends AbstractPlanningObject
{
	public ResourcePlan(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, new ResourcePlanId(idToUse.asInt()), createSchema(objectManager));
	}

	public static ResourcePlanSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ResourcePlanSchema createSchema(ObjectManager objectManager)
	{
		return (ResourcePlanSchema) objectManager.getSchemas().get(ObjectType.RESOURCE_PLAN);
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_PROJECT_RESOURCE_LABEL))
			return getProjectResourceLabel();

		if (fieldTag.equals(PSEUDO_TAG_OWNING_FACTOR_NAME))
			return getOwningFactorName();

		return super.getPseudoData(fieldTag);
	}

	private String getOwningFactorName()
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor();
		if (owningFactor == null)
			return "";

		return owningFactor.toString();
	}

	private String getProjectResourceLabel()
	{
		ProjectResource projectResource = getProjectResource();
		if (projectResource == null)
			return "";

		return projectResource.getInitials();
	}

	private ProjectResource getProjectResource()
	{
		return ProjectResource.find(getProject(), getResourceRef());
	}

	@Override
	protected TimePeriodCosts createTimePeriodCosts(OptionalDouble quantity)
	{
		return new TimePeriodCosts(getResourceRef(), quantity);
	}

	@Override
	protected boolean shouldIncludeEffort(DateUnit dateUnit) throws Exception
	{
		return true;
	}

	@Override
	public String toString()
	{
		ProjectResource projectResource = getProjectResource();
		if (projectResource == null)
			return "";

		return projectResource.getFullName();
	}

	public ORef getResourceRef()
	{
		return getRefData(TAG_RESOURCE_ID);
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ResourcePlanSchema.getObjectType();
	}
	
	public static ResourcePlan find(ObjectManager objectManager, ORef resourcePlanRef)
	{
		return (ResourcePlan) objectManager.findObject(resourcePlanRef);
	}
	
	public static ResourcePlan find(Project project, ORef resourcePlanRef)
	{
		return find(project.getObjectManager(), resourcePlanRef);
	}

	public static final String TAG_RESOURCE_ID = "ResourceId";
	public static final String PSEUDO_TAG_PROJECT_RESOURCE_LABEL = "PseudoTagProjectResourceLabel";
	public static final String PSEUDO_TAG_OWNING_FACTOR_NAME = "PseudoTagOwningFactorName";
}
