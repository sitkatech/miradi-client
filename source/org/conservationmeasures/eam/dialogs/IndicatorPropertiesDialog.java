/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.ratings.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.ratings.PriorityRatingQuestion;

public class IndicatorPropertiesDialog extends ObjectPropertiesDialog
{
	public IndicatorPropertiesDialog(MainWindow parentToUse, EAMObject objectToEdit) throws Exception
	{
		super(parentToUse, objectToEdit);
		setTitle(EAM.text("Title|Indicator Properties"));
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
		};

}
