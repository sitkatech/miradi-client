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
		super(tag, "Potential Impact", getImpactChoices());
	}
	
	static RatingChoice[] getImpactChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Useless", Color.RED),
			new RatingChoice("2", "Partial AND Temporary Solution", Color.ORANGE),
			new RatingChoice("3", "Partial OR Temporary Solution", Color.YELLOW),
			new RatingChoice("4", "Complete and Permanent Solution", Color.GREEN),
		};
	}

}
