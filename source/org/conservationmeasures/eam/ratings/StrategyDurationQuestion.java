/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class StrategyDurationQuestion extends RatingQuestion
{
	public StrategyDurationQuestion(String tag)
	{
		super(tag, "Duration of Impact", getDurationChoices());
	}
	
	static RatingChoice[] getDurationChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Useless", COLOR_1_OF_4),
			new RatingChoice("2", "Short-Term", COLOR_2_OF_4),
			new RatingChoice("3", "Long-Term", COLOR_3_OF_4),
			new RatingChoice("4", "Permanent", COLOR_4_OF_4),
		};
	}

}
