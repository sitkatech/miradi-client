/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.awt.Color;


public class StrategyCostQuestion extends StaticChoiceQuestion
{
	public StrategyCostQuestion()
	{
		super(getCostChoices());
	}
	
	static ChoiceItem[] getCostChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified", Color.WHITE),
			new ChoiceItem("1", "Prohibitively Expensive", COLOR_ALERT),
			new ChoiceItem("2", "Expensive", COLOR_CAUTION),
			new ChoiceItem("3", "Moderate", COLOR_OK),
			new ChoiceItem("4", "Inexpensive", COLOR_GREAT),
		};
	}

}
