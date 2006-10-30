/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class StrategyCostQuestion extends RatingQuestion
{
	public StrategyCostQuestion(String tag)
	{
		super(tag, "Cost", getCostChoices());
	}
	
	static RatingChoice[] getCostChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Prohibitively Expensive", COLOR_1_OF_4),
			new RatingChoice("2", "Expensive", COLOR_2_OF_4),
			new RatingChoice("3", "Moderate", COLOR_3_OF_4),
			new RatingChoice("4", "Inexpensive", COLOR_4_OF_4),
		};
	}

}
