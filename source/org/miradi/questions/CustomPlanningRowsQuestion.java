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

import java.util.Vector;

import org.miradi.icons.ActivityIcon;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.MethodIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.objects.Cause;
import org.miradi.project.Project;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.TaskSchema;

public class CustomPlanningRowsQuestion extends AbstractCustomPlanningRowsQuestion
{
	public CustomPlanningRowsQuestion(Project projectToUse)
	{
		super();

		project = projectToUse;
	}

	@Override
	protected boolean shouldIncludeHumanWellbeingTargetRow()
	{
		return getProject().getMetadata().isHumanWelfareTargetMode();
	}

	@Override
	protected boolean shouldIncludeBiophysicalFactorRow()
	{
		return getProject().getMetadata().isBiophysicalFactorMode();
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;

	@Override
	protected Vector<ChoiceItem> createCauseChoiceItems()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		choiceItems.add(createChoiceItem(CauseSchema.getObjectType(), Cause.OBJECT_NAME_THREAT, new DirectThreatIcon()));
		choiceItems.add(createChoiceItem(CauseSchema.getObjectType(), Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, new ContributingFactorIcon())); 

		return choiceItems;
	}
	
	@Override
	protected Vector<ChoiceItem> createTaskChoiceItems()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		choiceItems.add(createChoiceItem(TaskSchema.getObjectType(), TaskSchema.ACTIVITY_NAME, new ActivityIcon()));
		choiceItems.add(createChoiceItem(TaskSchema.getObjectType(), TaskSchema.METHOD_NAME, new MethodIcon()));
		choiceItems.add(createChoiceItem(TaskSchema.getObjectType(), TaskSchema.OBJECT_NAME, new TaskIcon()));

		return choiceItems;
	}
}
