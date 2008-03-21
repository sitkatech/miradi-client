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
package org.miradi.dialogs.objective;

import org.miradi.actions.ActionEditIndicatorRelevancyList;
import org.miradi.actions.ActionEditStrategyRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.ids.BaseId;
import org.miradi.ids.ObjectiveId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.objects.Objective;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

public class ObjectivePropertiesPanel extends ObjectDataInputPanel
{
	public ObjectivePropertiesPanel(Project projectToUse, Actions actionsToUse, ObjectPicker picker) throws Exception
	{
		this(projectToUse, new ObjectiveId(BaseId.INVALID.asInt()), actionsToUse, picker);
	}
		
	public ObjectivePropertiesPanel(Project projectToUse, ObjectiveId idToShow, Actions actionsToUse, ObjectPicker picker) throws Exception
	{
		super(projectToUse, ObjectType.OBJECTIVE, idToShow);
		
		ObjectDataInputField shortLabelField = createShortStringField(Objective.getObjectType(), Objective.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Objective.getObjectType(), Objective.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Objective"), new ObjectiveIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createMultilineField(Objective.getObjectType(), Desire.TAG_FULL_TEXT));

		addField(createReadonlyTextField(Objective.PSEUDO_TAG_FACTOR));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_STRATEGIES));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_TARGETS));
		
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(Objective.getObjectType(), Objective.PSEUDO_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditIndicatorRelevancyList.class), picker));
		addFieldWithEditButton(EAM.text("Strategies"), createReadOnlyObjectList(Objective.getObjectType(), Objective.PSEUDO_RELEVANT_STRATEGY_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditStrategyRelevancyList.class), picker));
		
		addField(createMultilineField(Goal.TAG_COMMENTS));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Objective Properties");
	}

}
