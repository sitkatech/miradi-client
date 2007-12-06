/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class StressScopeChoiceQuestion extends StaticChoiceQuestion
{
	public StressScopeChoiceQuestion(String tag)
	{
		super(tag, "Scope", getScopeChoices());
	}
	
	public static ChoiceItem[] getScopeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Low", GREAT),
			new ChoiceItem("2", "Medium", OK),
			new ChoiceItem("3", "High", COLOR_CAUTION),
			new ChoiceItem("4", "Very High", COLOR_ALERT),
		};
	}
}
