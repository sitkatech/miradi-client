/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.awt.Color;


public class StrategyFeasibilityQuestion extends StaticChoiceQuestion
{
	public StrategyFeasibilityQuestion()
	{
		super(getFeasibilityChoices());
	}
	
	static ChoiceItem[] getFeasibilityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Low", COLOR_ALERT),
			new ChoiceItem("2", "Medium", COLOR_CAUTION),
			new ChoiceItem("3", "High", COLOR_OK),
			new ChoiceItem("4", "Very High", COLOR_GREAT),
		};
	}

}
