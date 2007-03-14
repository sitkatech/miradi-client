/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class TrendStatusQuestion extends ChoiceQuestion
{
	public TrendStatusQuestion(String tagToUse)
	{
		super(tagToUse, "Trend Status", getTrendStatuses());
	}

	static ChoiceItem[] getTrendStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified"),
				new ChoiceItem("1", "Poor"),
				new ChoiceItem("2", "Fair"),
				new ChoiceItem("3", "Good"),
				new ChoiceItem("4", "Very Good"),
		};
	}
}
