/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class BudgetCostUnitQuestion extends StaticChoiceQuestion
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
