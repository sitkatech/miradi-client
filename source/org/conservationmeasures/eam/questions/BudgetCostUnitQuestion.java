/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class BudgetCostUnitQuestion extends StaticChoiceQuestion
{
	public BudgetCostUnitQuestion(String tagToUse)
	{
		super(tagToUse, getCostUnitChoices());
	}

	static ChoiceItem[] getCostUnitChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified"),
				new ChoiceItem("Hours", "Hours"),
				new ChoiceItem(DAYS_CODE, DAYS_CODE),
				new ChoiceItem("Weeks", "Weeks"),
				new ChoiceItem("Months", "Months"),
				new ChoiceItem("Each", "Each"),
		};
	}

	public static final String DAYS_CODE = "Days";
}
