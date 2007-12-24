/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class BudgetCostModeQuestion extends StaticChoiceQuestion
{
	public BudgetCostModeQuestion(String tagToUse)
	{
		super(tagToUse, "Budget Cost Mode", getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Rollup"),
				new ChoiceItem(OVERRIDE_MODE_CODE, "Override"),
		};
	}
	
	public static final String OVERRIDE_MODE_CODE = "BudgetOverrideMode";
}
