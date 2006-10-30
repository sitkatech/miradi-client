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
			new RatingChoice("1", "Completely Ineffective", COLOR_1_OF_4),
			new RatingChoice("2", "Ineffective", COLOR_2_OF_4),
			new RatingChoice("3", "Effective", COLOR_3_OF_4),
			new RatingChoice("4", "Very Effective", COLOR_4_OF_4),
		};
	}

	public RatingChoice getResult(RatingChoice impact, RatingChoice duration, RatingChoice feasibility, RatingChoice cost)
	{
		String impactCode = impact.getCode();
		String durationCode = duration.getCode();
		String feasibilityCode = feasibility.getCode();
		String costCode = cost.getCode();
		
		int impactValue = codeToInt(impactCode);
		int durationValue = codeToInt(durationCode);
		int feasibilityValue = codeToInt(feasibilityCode);
		int costValue = codeToInt(costCode);
		
		int average = 0;
		if(impactValue * durationValue * feasibilityValue * costValue == 0)
			average = 0;
		else if(impactValue == 1 || durationValue == 1 || feasibilityValue == 1 || costValue == 1)
			average = 1;
		else
		{
			int total = impactValue + durationValue + feasibilityValue + costValue;
			average = total / 4;
		}
		
		if(average == 0)
			return findChoiceByCode("");
		return findChoiceByCode(Integer.toString(average));
	}

	private int codeToInt(String code)
	{
		if(code.length() == 0)
			return 0;
		return Integer.parseInt(code);
	}
}
