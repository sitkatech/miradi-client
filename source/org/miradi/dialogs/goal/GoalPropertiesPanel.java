/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.goal;

import org.miradi.actions.ActionEditGoalIndicatorRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.GoalIcon;
import org.miradi.ids.BaseId;
import org.miradi.ids.GoalId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

public class GoalPropertiesPanel extends ObjectDataInputPanel
{
	public GoalPropertiesPanel(Project projectToUse, Actions actionsToUse, ObjectPicker picker) throws Exception
	{
		this(projectToUse, new GoalId(BaseId.INVALID.asInt()), actionsToUse, picker);
	}
	
	public GoalPropertiesPanel(Project projectToUse, GoalId idToShow, Actions actionsToUse, ObjectPicker picker) throws Exception
	{
		super(projectToUse, ObjectType.GOAL, idToShow);
		
		ObjectDataInputField shortLabelField = createShortStringField(Goal.getObjectType(), Goal.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Goal.getObjectType(), Goal.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Goal"), new GoalIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createMultilineField(Goal.getObjectType(), Desire.TAG_FULL_TEXT));
		
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_FACTOR));
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_STRATEGIES));
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_DIRECT_THREATS));
		
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(Goal.getObjectType(), Goal.PSEUDO_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditGoalIndicatorRelevancyList.class), picker));
		addField(createMultilineField(Goal.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Goal Properties");
	}

}
