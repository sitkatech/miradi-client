/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;



public class PriorityRatingQuestion extends StaticChoiceQuestion
{
	public PriorityRatingQuestion()
	{
		super(getPriorityChoices());
	}
	
	static ChoiceItem[] getPriorityChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem("1", "Low"),
			new ChoiceItem("2", "Medium"),
			new ChoiceItem("3", "High"),
			new ChoiceItem("4", "Very High"),
		};
	}
}
