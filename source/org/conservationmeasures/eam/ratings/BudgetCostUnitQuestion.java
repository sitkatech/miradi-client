/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;

public class BudgetCostUnitQuestion extends ChoiceQuestion
{
	public BudgetCostUnitQuestion(String tagToUse)
	{
		super(tagToUse, "Cost Units", getCostUnitChoices());
	}

	static ChoiceItem[] getCostUnitChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified"),
				new ChoiceItem("Hours", "Hours"),
				new ChoiceItem("Days", "Days"),
				new ChoiceItem("Weeks", "Weeks"),
				new ChoiceItem("Months", "Months"),
				new ChoiceItem("Each", "Each"),
		};
	}
}
