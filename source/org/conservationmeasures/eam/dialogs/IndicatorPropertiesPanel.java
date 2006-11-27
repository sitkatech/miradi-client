/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.ratings.PriorityRatingQuestion;

public class IndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public IndicatorPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new IndicatorId(BaseId.INVALID.asInt()));
	}
	
	public IndicatorPropertiesPanel(Project projectToUse, Actions actions, Indicator indicator) throws Exception
	{
		this(projectToUse, actions, (IndicatorId)indicator.getId());
	}
	
	public IndicatorPropertiesPanel(Project projectToUse, Actions actions, IndicatorId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.INDICATOR, idToShow);
		
		addField(createStringField(Indicator.PSEUDO_TAG_FACTOR));
		addField(createStringField(Indicator.TAG_SHORT_LABEL));
		addField(createStringField(Indicator.TAG_LABEL));
		addField(createMultilineField(Indicator.TAG_METHOD));
		addField(createRatingField(new PriorityRatingQuestion(Indicator.TAG_PRIORITY)));
		addField(createRatingField(new IndicatorStatusRatingQuestion(Indicator.TAG_STATUS)));			// Rating
		addField(createStringField(Indicator.TAG_LOCATION));
		addField(createStringField(Indicator.TAG_COST));
		addField(createStringField(Indicator.TAG_FUNDING_SOURCE));
		addField(createStringField(Indicator.TAG_WHEN));
		addField(createListField(actions, Indicator.TAG_RESOURCE_IDS));
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_STRATEGIES));
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_TARGETS));
				
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}

}
