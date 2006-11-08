/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.ratings.PriorityRatingQuestion;

public class IndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public IndicatorPropertiesPanel(Project projectToUse, Actions actions, Indicator indicator) throws Exception
	{
		super(projectToUse, indicator.getType(), indicator.getId());
		
		addField(createStringField(indicator.TAG_SHORT_LABEL));
		addField(createStringField(indicator.TAG_LABEL));
		addField(createMultilineField(indicator.TAG_METHOD));
		addField(createRatingField(new PriorityRatingQuestion(indicator.TAG_PRIORITY)));
		addField(createRatingField(new IndicatorStatusRatingQuestion(indicator.TAG_STATUS)));			// Rating
		addField(createStringField(indicator.TAG_LOCATION));
		addField(createStringField(indicator.TAG_COST));
		addField(createStringField(indicator.TAG_FUNDING_SOURCE));
		addField(createStringField(indicator.TAG_WHEN));
		addField(createListField(actions, indicator.TAG_RESOURCE_IDS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}

}
