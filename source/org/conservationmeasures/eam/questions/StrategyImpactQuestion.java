/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;


public class StrategyImpactQuestion extends StaticChoiceQuestion
{
	public StrategyImpactQuestion(String tag)
	{
		super(tag, "Impact on Key Factors", getImpactChoices());
	}
	
	static ChoiceItem[] getImpactChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "None", COLOR_ALERT),
			new ChoiceItem("2", "Low", COLOR_CAUTION),
			new ChoiceItem("3", "Medium", COLOR_OK),
			new ChoiceItem("4", "High", COLOR_GREAT),
		};
	}

}
