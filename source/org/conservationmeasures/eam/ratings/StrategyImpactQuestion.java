/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class StrategyImpactQuestion extends RatingQuestion
{
	public StrategyImpactQuestion(String tag)
	{
		super(tag, "Impact on Key Factors", getImpactChoices());
	}
	
	static RatingChoice[] getImpactChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "None", COLOR_1_OF_4),
			new RatingChoice("2", "Low", COLOR_2_OF_4),
			new RatingChoice("3", "Medium", COLOR_3_OF_4),
			new RatingChoice("4", "High", COLOR_4_OF_4),
		};
	}

}
