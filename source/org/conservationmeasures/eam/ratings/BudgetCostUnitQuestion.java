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
				new RatingChoice("1", "Hour"),
				new RatingChoice("2", "Day"),
				new RatingChoice("3", "Week"),
				new RatingChoice("4", "Month"),
				new RatingChoice("5", "Each"),
		};
	}
}
