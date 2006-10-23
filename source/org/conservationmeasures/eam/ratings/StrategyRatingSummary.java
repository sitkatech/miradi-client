/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

public class StrategyRatingSummary extends RatingQuestion
{
	public StrategyRatingSummary(String tag)
	{
		super(tag, "Priority", getStrategyRatingChoices());
	}
	
	static RatingChoice[] getStrategyRatingChoices()
	{
		return new RatingChoice[] {
			new RatingChoice("", "Unknown", Color.WHITE),
			new RatingChoice("1", "Ineffective", Color.RED),
			new RatingChoice("2", "Moderate", Color.ORANGE),
			new RatingChoice("3", "Effective", Color.YELLOW),
			new RatingChoice("4", "Very Effective", Color.GREEN),
		};
	}

	public RatingChoice getResult(RatingChoice impact, RatingChoice feasibility)
	{
		String impactCode = impact.getCode();
		String feasibilityCode = feasibility.getCode();
		if(impactCode.length() == 0 || feasibilityCode.length() == 0)
			return findChoiceByCode("");
		
		int total = Integer.parseInt(impactCode) + Integer.parseInt(feasibilityCode);
		int average = total / 2;
		if(average == 0)
			return findChoiceByCode("");
		return findChoiceByCode(Integer.toString(average));
	}
}
