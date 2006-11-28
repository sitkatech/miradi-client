/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

public class BudgetCostUnitQuestion extends RatingQuestion
{
	public BudgetCostUnitQuestion(String tagToUse)
	{
		super(tagToUse, "Cost Units", getCostUnitChoices());
	}

	static RatingChoice[] getCostUnitChoices()
	{
		return new RatingChoice[] {
				new RatingChoice("", "Not Specified"),
				new RatingChoice("hours", "Hours"),
				new RatingChoice("days", "Days"),
				new RatingChoice("weeks", "Weeks"),
				new RatingChoice("months", "Months"),
				new RatingChoice("each", "Each"),
		};
	}
}
