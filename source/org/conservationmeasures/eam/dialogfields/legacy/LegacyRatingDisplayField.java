/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields.legacy;

import java.awt.Component;

import org.conservationmeasures.eam.dialogfields.RatingLabel;
import org.conservationmeasures.eam.ratings.RatingQuestion;

public class LegacyRatingDisplayField extends LegacyDialogField
{
	public LegacyRatingDisplayField(RatingQuestion questionToUse)
	{
		super("", "");
		labelComponent = new RatingLabel(questionToUse);
	}
	
	public Component getComponent()
	{
		return labelComponent;
	}

	public String getText()
	{
		return null;
	}

	RatingLabel labelComponent;
}
