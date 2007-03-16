/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class RatingSourceQuestion extends ChoiceQuestion
{
	public RatingSourceQuestion(String tag)
	{
		super(tag, "Rating Source", getStatusChoices());
	}
	
	static ChoiceItem[] getStatusChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem("1", "Rough Estimate"),
			new ChoiceItem("2", "Expert Opinon"),
			new ChoiceItem("3", "Published Study"),
		};
	}

}