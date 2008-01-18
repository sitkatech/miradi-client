/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class RatingSourceQuestion extends StaticChoiceQuestion
{
	public RatingSourceQuestion(String tag)
	{
		super(tag, "Rating Source", getStatusChoices());
	}
	
	static ChoiceItem[] getStatusChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem("RoughGuess", "Rough Guess"),
			new ChoiceItem("ExpertKnowlege", "Expert Knowlege"),
			new ChoiceItem("ExternalResearch", "External Research"),
			new ChoiceItem("OnsiteResearch", "Onsite Research"),
		};
	}

}