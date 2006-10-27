/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.dialogfields.DialogField;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.ratings.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.ratings.PriorityRatingQuestion;

public class IndicatorPropertiesPanel extends ObjectPropertiesPanel
{
	public IndicatorPropertiesPanel(MainWindow parentToUse, EAMObject objectToEdit) throws Exception
	{
		super(parentToUse, objectToEdit);
		initializeFields(tags);
	}

	DialogField createDialogField(String tag, String existingValue) throws Exception
	{
		if(tag.equals(Indicator.TAG_PRIORITY))
			return createChoiceField(new PriorityRatingQuestion(tag), existingValue);
		
		if(tag.equals(Indicator.TAG_STATUS))
			return createChoiceField(new IndicatorStatusRatingQuestion(tag), existingValue);

		return super.createDialogField(tag, existingValue);
	}
	
	static final String[] tags = new String[] {
		Indicator.TAG_SHORT_LABEL, 
		Indicator.TAG_LABEL,
		Indicator.TAG_METHOD,
		Indicator.TAG_RESOURCE_IDS,
		Indicator.TAG_LOCATION,
		Indicator.TAG_PRIORITY,
		Indicator.TAG_STATUS,
		Indicator.TAG_COST,
		Indicator.TAG_FUNDING_SOURCE,
		Indicator.TAG_WHEN,
		};

	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}

}
