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
package org.miradi.dialogs.goal;

import org.miradi.actions.ActionEditGoalIndicatorRelevancyList;
import org.miradi.actions.ActionEditGoalProgressPercent;
import org.miradi.actions.ActionEditGoalStrategyActivityRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.GoalIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.objects.ProgressPercent;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class GoalPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public GoalPropertiesPanel(Project projectToUse, Actions actionsToUse, ObjectPicker picker) throws Exception
	{
		super(projectToUse, ObjectType.GOAL);
		createSingleSection(EAM.text("Goal"));
		
		ObjectDataInputField shortLabelField = createShortStringField(Goal.getObjectType(), Goal.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Goal.getObjectType(), Goal.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Goal"), new GoalIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createMultilineField(Goal.getObjectType(), Desire.TAG_FULL_TEXT));
		
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_FACTOR));
		addField(createReadonlyTextField(Goal.PSEUDO_TAG_DIRECT_THREATS));
		
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(Goal.getObjectType(), Goal.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditGoalIndicatorRelevancyList.class), picker));
		addFieldWithEditButton(EAM.text("Strategies And Activities"), createReadOnlyObjectList(Goal.getObjectType(), Goal.PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditGoalStrategyActivityRelevancyList.class), picker));
		
		String[] columnTags = new String[]{ProgressPercent.TAG_DATE, ProgressPercent.TAG_PERCENT_COMPLETE, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES};
		PanelTitleLabel progressPercentsLabel = new PanelTitleLabel(EAM.text("Progress Percents"));
		ObjectDataInputField readOnlyProgressPercentsList = createReadOnlyObjectListTableField(Goal.getObjectType(), Goal.TAG_PROGRESS_PERCENT_REFS, ProgressPercent.getObjectType(), columnTags);
		ObjectsActionButton editProgressPercentButton = createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditGoalProgressPercent.class), getPicker());
		addFieldWithEditButton(progressPercentsLabel, readOnlyProgressPercentsList, editProgressPercentButton);
		
		addField(createMultilineField(Goal.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Goal Properties");
	}

}
