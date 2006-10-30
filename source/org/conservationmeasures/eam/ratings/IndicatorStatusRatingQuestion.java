/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class IndicatorStatusRatingQuestion extends RatingQuestion
{
	public IndicatorStatusRatingQuestion(String tag)
	{
		super(tag, "Status", getStatusChoices());
	}
	
	static RatingChoice[] getStatusChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Not Specified", Color.WHITE),
			new RatingChoice("1", "Not Started", Color.RED),
			new RatingChoice("2", "Problems Implementing", Color.ORANGE),
			new RatingChoice("3", "Going Well", Color.YELLOW),
			new RatingChoice("4", "Fully on Schedule", Color.GREEN),
		};
	}

}
