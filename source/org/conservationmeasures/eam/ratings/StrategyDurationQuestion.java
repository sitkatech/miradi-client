/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class StrategyDurationQuestion extends ChoiceQuestion
{
	public StrategyDurationQuestion(String tag)
	{
		super(tag, "Duration of Impact", getDurationChoices());
	}
	
	static ChoiceItem[] getDurationChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Useless", COLOR_1_OF_4),
			new ChoiceItem("2", "Short-Term", COLOR_2_OF_4),
			new ChoiceItem("3", "Long-Term", COLOR_3_OF_4),
			new ChoiceItem("4", "Permanent", COLOR_4_OF_4),
		};
	}

}
