/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.awt.Color;

public class ThreatRatingQuestion extends StaticChoiceQuestion
{
	public ThreatRatingQuestion()
	{
		super(getChoiceItems());
	}

	static ChoiceItem[] getChoiceItems()
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
