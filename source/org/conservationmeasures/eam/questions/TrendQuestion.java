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
				new ChoiceItem("", "Not Specified"),
				new ChoiceItem("1", "Strong Increase"),
				new ChoiceItem("2", "Mild Increase"),
				new ChoiceItem("3", "Flat"),
				new ChoiceItem("4", "Mild decrease"),
				new ChoiceItem("5", "Strong decrease"),
				new ChoiceItem("6", "Unkown trend"),
		};
	}
}