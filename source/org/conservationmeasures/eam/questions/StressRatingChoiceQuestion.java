/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class StressRatingChoiceQuestion extends StaticChoiceQuestion
{
	public StressRatingChoiceQuestion()
	{
		super(getStressRatingChoices());
	}
	
	public static ChoiceItem[] getStressRatingChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Low", COLOR_GREAT),
			new ChoiceItem("2", "Medium", COLOR_OK),
			new ChoiceItem("3", "High", COLOR_CAUTION),
			new ChoiceItem("4", "Very High", COLOR_ALERT),
		};
	}
}
