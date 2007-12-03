/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class ThreatStressRatingChoiceQuestion extends StaticChoiceQuestion
{
	public ThreatStressRatingChoiceQuestion(String tagToUse)
	{
		super(tagToUse, "Threat Stress Rating", getChoiceItems());
	}

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", Color.WHITE),
				new ChoiceItem("1", "Low", COLOR_1_OF_4),
				new ChoiceItem("2", "Medium", COLOR_2_OF_4),
				new ChoiceItem("3", "High", COLOR_3_OF_4),
				new ChoiceItem("4", "Very High", COLOR_4_OF_4),
		};
	}
}
