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
package org.miradi.views.umbrella;

import org.miradi.main.EAM;
import org.miradi.objects.TableSettings;
import org.miradi.questions.WorkPlanProjectResourceConfigurationQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.views.planning.doers.CreatePoolObjectDoer;
import org.miradi.views.workplan.ProjectResourceManagementConfiguration;

public class CreateResourceDoer extends CreatePoolObjectDoer
{
	@Override
	public boolean isAvailable()
	{
		if (isWorkPlanView() && resourceListIsFiltered())
			return false;

		return super.isAvailable();
	}

	private boolean resourceListIsFiltered()
	{
		try
		{
			ProjectResourceManagementConfiguration projectResourceManagementConfiguration = new ProjectResourceManagementConfiguration(getProject());
			TableSettings tableSettings = TableSettings.findOrCreate(getProject(), projectResourceManagementConfiguration.getUniqueTreeTableIdentifier());
			String projectResourceConfiguration = tableSettings.getData(TableSettings.TAG_WORK_PLAN_PROJECT_RESOURCE_CONFIGURATION);
			return projectResourceConfiguration.equals(WorkPlanProjectResourceConfigurationQuestion.WORK_PLAN_ASSIGNMENT_RESOURCES_ONLY_CODE);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		return false;
	}

	@Override
	protected int getTypeToCreate()
	{
		return ProjectResourceSchema.getObjectType();
	}
}
