/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.objective;

import org.conservationmeasures.eam.actions.ActionEditIndicatorRelevancyList;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

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
		addField(createReadonlyTextField(Objective.TAG_RELEVANT_INDICATOR_SET));
		add(createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditIndicatorRelevancyList.class), picker)); 
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Objective Properties");
	}

}
