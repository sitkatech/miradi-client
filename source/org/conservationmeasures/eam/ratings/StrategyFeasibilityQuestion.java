/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class StrategyFeasibilityQuestion extends ChoiceQuestion
{
	public StrategyFeasibilityQuestion(String tag)
	{
		super(tag, "Feasibility", getFeasibilityChoices());
	}
	
	static ChoiceItem[] getFeasibilityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Impossible", COLOR_1_OF_4),
			new ChoiceItem("2", "Difficult", COLOR_2_OF_4),
			new ChoiceItem("3", "Moderate", COLOR_3_OF_4),
			new ChoiceItem("4", "Easy", COLOR_4_OF_4),
		};
	}

}
