/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;


public class StrategyFeasibilityQuestion extends StaticChoiceQuestion
{
	public StrategyFeasibilityQuestion(String tag)
	{
		super(tag, "Feasibility", getFeasibilityChoices());
	}
	
	static ChoiceItem[] getFeasibilityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Impossible", COLOR_1_OF_4),
			new ChoiceItem("2", "Difficult", COLOR_2_OF_4),
			new ChoiceItem("3", "Moderate", COLOR_3_OF_4),
			new ChoiceItem("4", "Easy", COLOR_4_OF_4),
		};
	}

}
