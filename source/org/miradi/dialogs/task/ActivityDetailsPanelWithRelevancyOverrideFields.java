/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.task;

import org.miradi.actions.ActionEditActivityGoalRelevancyList;
import org.miradi.actions.ActionEditActivityObjectiveRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.actions.ObjectsAction;
import org.miradi.main.EAM;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class ActivityDetailsPanelWithRelevancyOverrideFields extends TaskDetailsPanel
{
	public ActivityDetailsPanelWithRelevancyOverrideFields(Project projectToUse, Actions actionsToUse) throws Exception
	{
		super(projectToUse, actionsToUse);
	}

	@Override
	protected void addCustomFields(Actions actionsToUse)
	{
		ObjectsAction objectiveRelevancyEditAction = actionsToUse.getObjectsAction(ActionEditActivityObjectiveRelevancyList.class);
		addFieldWithEditButton(EAM.text("Objectives"), createReadOnlyObjectList(Task.getObjectType(), Task.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS), createObjectsActionButton(objectiveRelevancyEditAction, getPicker()));
		
		ObjectsAction goalRelevancyEditAction = actionsToUse.getObjectsAction(ActionEditActivityGoalRelevancyList.class);
		addFieldWithEditButton(EAM.text("Goals"), createReadOnlyObjectList(Task.getObjectType(), Task.PSEUDO_TAG_RELEVANT_GOAL_REFS), createObjectsActionButton(goalRelevancyEditAction, getPicker()));
	}
}
