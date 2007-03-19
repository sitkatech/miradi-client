/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class TrendQuestion extends ChoiceQuestion
{
	public TrendQuestion(String tagToUse)
	{
		super(tagToUse, "Trends", getTrends());
	}

	static ChoiceItem[] getTrends()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Unknown Trend"),
				new ChoiceItem("StrongIncrease", "Strong Increase"),
				new ChoiceItem("MildIncrease", "Mild Increase"),
				new ChoiceItem("Flat", "Flat"),
				new ChoiceItem("MildDecrease", "Mild Decrease"),
				new ChoiceItem("StrongDecrease", "Strong Decrease"),
				new ChoiceItem("UnkownTrend", "Unkown Trend"),
		};
	}
}