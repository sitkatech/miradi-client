/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class StrategyFeasibilityQuestion extends RatingQuestion
{
	public StrategyFeasibilityQuestion(String tag)
	{
		super(tag, "Feasibility", getFeasibilityChoices());
	}
	
	static RatingChoice[] getFeasibilityChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Impossible", COLOR_1_OF_4),
			new RatingChoice("2", "Difficult", COLOR_2_OF_4),
			new RatingChoice("3", "Moderate", COLOR_3_OF_4),
			new RatingChoice("4", "Easy", COLOR_4_OF_4),
		};
	}

}
