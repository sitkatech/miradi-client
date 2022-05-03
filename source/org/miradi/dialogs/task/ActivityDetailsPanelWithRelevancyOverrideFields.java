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

package org.miradi.dialogs.task;

import org.miradi.actions.*;
import org.miradi.dialogs.activity.ActivityFactorVisibilityControlPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.schemas.TaskSchema;

public class ActivityDetailsPanelWithRelevancyOverrideFields extends TaskDetailsPanel
{
	public ActivityDetailsPanelWithRelevancyOverrideFields(Project projectToUse, MainWindow mainWindowToUse, ActivityFactorVisibilityControlPanel activityVisibilityButtonPanel) throws Exception
	{
		super(projectToUse, mainWindowToUse, activityVisibilityButtonPanel);
	}

	@Override
	protected void addCustomFields(Actions actionsToUse)
	{
		ObjectsAction objectiveRelevancyEditAction = actionsToUse.getObjectsAction(ActionEditActivityObjectiveRelevancyList.class);
		addFieldWithEditButton(EAM.text("Objectives"), createReadOnlyObjectList(TaskSchema.getObjectType(), Task.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS), createObjectsActionButton(objectiveRelevancyEditAction, getPicker()));
		
		ObjectsAction goalRelevancyEditAction = actionsToUse.getObjectsAction(ActionEditActivityGoalRelevancyList.class);
		addFieldWithEditButton(EAM.text("Goals"), createReadOnlyObjectList(TaskSchema.getObjectType(), Task.PSEUDO_TAG_RELEVANT_GOAL_REFS), createObjectsActionButton(goalRelevancyEditAction, getPicker()));

		ObjectsAction indicatorRelevancyEditAction = actionsToUse.getObjectsAction(ActionEditActivityIndicatorRelevancyList.class);
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(TaskSchema.getObjectType(), Task.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(indicatorRelevancyEditAction, getPicker()));
	}
}
