/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class PriorityRatingQuestion extends RatingQuestion
{
	public PriorityRatingQuestion(String tag)
	{
		super(tag, "Priority", getPriorityChoices());
	}
	
	static RatingChoice[] getPriorityChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Low", Color.GREEN),
			new RatingChoice("2", "Medium", Color.YELLOW),
			new RatingChoice("3", "High", Color.ORANGE),
			new RatingChoice("4", "Very High", Color.RED),
		};
	}
}
