/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.awt.Color;

public class StatusQuestion extends StaticChoiceQuestion
{
	public StatusQuestion()
	{
		super(getStatuses());
	}

	static ChoiceItem[] getStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", Color.WHITE),
				new ChoiceItem("1", "Poor", COLOR_ALERT),
				new ChoiceItem("2", "Fair", COLOR_CAUTION),
				new ChoiceItem("3", "Good", COLOR_OK),
				new ChoiceItem("4", "Very Good", COLOR_GREAT),
		};
	}
	
	public static final String UNSPECIFIED = "";
	public static final String POOR = "1";
	public static final String FAIR = "2";
	public static final String GOOD = "3";
	public static final String VERY_GOOD = "4";
}
