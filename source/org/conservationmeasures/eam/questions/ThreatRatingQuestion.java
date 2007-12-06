/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

public class ThreatRatingQuestion extends StaticChoiceQuestion
{
	public ThreatRatingQuestion(String tagToUse)
	{
		super(tagToUse, "Threat Rating", getChoiceItems());
	}

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", Color.WHITE),
				new ChoiceItem("1", "Low", COLOR_ALERT),
				new ChoiceItem("2", "Medium", COLOR_CAUTION),
				new ChoiceItem("3", "High", OK),
				new ChoiceItem("4", "Very High", GREAT),
		};
	}
}
