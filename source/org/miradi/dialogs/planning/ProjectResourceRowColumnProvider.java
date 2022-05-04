/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.planning;

import org.miradi.main.EAM;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.questions.WorkPlanProjectResourceConfigurationQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.utils.CodeList;

public class ProjectResourceRowColumnProvider extends AbstractBudgetCategoryRowColumnProvider
{
	public ProjectResourceRowColumnProvider(Project projectToUse)
	{
		this(projectToUse, "");
	}

	public ProjectResourceRowColumnProvider(Project projectToUse, String uniqueTreeTableIdentifierToUse)
	{
		super(projectToUse);

		uniqueTreeTableIdentifier = uniqueTreeTableIdentifierToUse;
	}

	@Override
	public CodeList getColumnCodesToShow()
	{
		return new CodeList(new String[] {
				ProjectResource.TAG_RESOURCE_TYPE,
				WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE,
				WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_BUDGET_DETAILS_COLUMN_CODE,
		});
		
	}

	@Override
	public boolean shouldIncludeEmptyRows()
	{
		try
		{
			if (!uniqueTreeTableIdentifier.isEmpty())
			{
				TableSettings tableSettings = TableSettings.findOrCreate(getProject(), uniqueTreeTableIdentifier);
				String projectResourceConfiguration = tableSettings.getData(TableSettings.TAG_WORK_PLAN_PROJECT_RESOURCE_CONFIGURATION);
				return projectResourceConfiguration.equals(WorkPlanProjectResourceConfigurationQuestion.ALL_PROJECT_RESOURCES_CODE);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		return true;
	}

	@Override
	public int getObjectType()
	{
		return ProjectResourceSchema.getObjectType();
	}

	@Override
	public String getObjectTypeName()
	{
		return ProjectResourceSchema.OBJECT_NAME;
	}

	private final String uniqueTreeTableIdentifier;
}
