/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.questions;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class PriorityRatingQuestion extends ChoiceQuestion
{
	public PriorityRatingQuestion(String tag)
	{
		super(tag, "Priority", getPriorityChoices());
	}
	
	static ChoiceItem[] getPriorityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Low", COLOR_1_OF_4),
			new ChoiceItem("2", "Medium", COLOR_2_OF_4),
			new ChoiceItem("3", "High", COLOR_3_OF_4),
			new ChoiceItem("4", "Very High", COLOR_4_OF_4),
		};
	}
}
