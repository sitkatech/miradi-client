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
		super(tag, "Feasibility and Cost", getFeasibilityChoices());
	}
	
	static RatingChoice[] getFeasibilityChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Impossible or Hugely Expensive", Color.RED),
			new RatingChoice("2", "Difficult AND Expensive", Color.ORANGE),
			new RatingChoice("3", "Difficult OR Expensive", Color.YELLOW),
			new RatingChoice("4", "Easy and Inexpensive", Color.GREEN),
		};
	}

}
