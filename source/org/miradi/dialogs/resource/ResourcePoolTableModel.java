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
package org.miradi.dialogs.resource;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceTypeQuestion;

public class ResourcePoolTableModel extends ObjectPoolTableModel
{
	public ResourcePoolTableModel(Project project)
	{
		super(project, ObjectType.PROJECT_RESOURCE, COLUMN_TAGS);
	}

	public boolean isChoiceItemColumn(int column)
	{
		if(isResourceTypeColumn(column))
			return true;
		
		return super.isChoiceItemColumn(column);
	}

	private boolean isResourceTypeColumn(int column)
	{
		return COLUMN_TAGS[column].equals(ProjectResource.TAG_RESOURCE_TYPE);
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		if(isResourceTypeColumn(column))
		{
			return new ResourceTypeQuestion();
		}
		return super.getColumnQuestion(column);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		ProjectResource.TAG_RESOURCE_TYPE,
		ProjectResource.TAG_INITIALS,
		ProjectResource.TAG_GIVEN_NAME,
		ProjectResource.TAG_SUR_NAME,
		ProjectResource.TAG_ORGANIZATION,
		ProjectResource.TAG_POSITION,
		ProjectResource.TAG_PHONE_NUMBER,
		ProjectResource.TAG_EMAIL,
	};

}
