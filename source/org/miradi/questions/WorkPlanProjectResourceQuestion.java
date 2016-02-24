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

package org.miradi.questions;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;

public class WorkPlanProjectResourceQuestion extends ObjectQuestion
{
	public WorkPlanProjectResourceQuestion(Project projectToUse)
	{
		super(getAllObjects(projectToUse));

		project = projectToUse;
	}

	private static BaseObject[] getAllObjects(Project project)
	{
		ORefList resourceAssignmentRefList = project.getPool(ResourceAssignmentSchema.getObjectType()).getRefList();
		ORefSet resourceRefSet = new ORefSet();

		for (int i = 0; i < resourceAssignmentRefList.size(); ++i)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(project, resourceAssignmentRefList.get(i));
			if (resourceAssignment.getResourceRef().isValid())
				resourceRefSet.add(resourceAssignment.getResourceRef());
		}

		ORefList resourceRefList = resourceRefSet.toRefList();
		BaseObject[] objectList = new BaseObject[resourceRefList.size()];
		for (int i = 0; i < resourceRefList.size(); ++i)
		{
			ProjectResource resource = ProjectResource.find(project, resourceRefList.get(i));
			objectList[i] = resource;
		}

		return objectList;
	}

	@Override
	public void reloadQuestion()
	{
		super.reloadQuestion();

		setObjects(getAllObjects(project));
	}

	protected Project getProject()
	{
		return project;
	}

	private Project project;
}
