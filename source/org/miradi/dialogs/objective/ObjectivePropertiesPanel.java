/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.objective;

import org.miradi.actions.ActionEditIndicatorRelevancyList;
import org.miradi.actions.ActionEditStrategyRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.ObjectiveId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
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
		
		addField(createShortStringField(Objective.TAG_SHORT_LABEL));
		addField(createStringField(Objective.TAG_LABEL));
		addField(createMultilineField(Goal.TAG_FULL_TEXT));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_FACTOR));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_STRATEGIES));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Objective.PSEUDO_TAG_TARGETS));
		addField(createMultilineField(Goal.TAG_COMMENTS));
		
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(Objective.getObjectType(), Objective.PSEUDO_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditIndicatorRelevancyList.class), picker));
		addFieldWithEditButton(EAM.text("Strategies"), createReadOnlyObjectList(Objective.getObjectType(), Objective.PSEUDO_RELEVANT_STRATEGY_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditStrategyRelevancyList.class), picker));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Objective Properties");
	}

}
