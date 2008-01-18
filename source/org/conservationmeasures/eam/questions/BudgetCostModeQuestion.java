/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;


public class BudgetCostModeQuestion extends StaticChoiceQuestion
{

	public BudgetCostModeQuestion(String tagToUse)
	{
		super(tagToUse, "Budget Cost Mode", getChoiceItems());
	}

	public static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem(ROLLUP_MODE_CODE, EAM.text("Rollup")),
				new ChoiceItem(OVERRIDE_MODE_CODE, EAM.text("High Level Est.")),
		};
	}
	
	public static final String ROLLUP_MODE_CODE = "";
	public static final String OVERRIDE_MODE_CODE = "BudgetOverrideMode";
}
