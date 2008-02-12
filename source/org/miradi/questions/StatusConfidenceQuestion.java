/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;


public class StatusConfidenceQuestion extends StaticChoiceQuestion
{
	public StatusConfidenceQuestion()
	{
		super(getStatusConfidences());
	}
	
	static ChoiceItem[] getStatusConfidences()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Not Specified"),
			new ChoiceItem("RoughGuess", "Rough Guess"),
			new ChoiceItem("ExpertKnowledge", "Expert Knowledge"),
			new ChoiceItem("RapidAssessment", "Rapid Assessment"),
			new ChoiceItem("IntensiveAssessment", "Intensive Assessment")
		};
	}
}
