/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class StatusConfidenceQuestion extends ChoiceQuestion
{
	public StatusConfidenceQuestion(String tag)
	{
		super(tag, "Status Confidence", getStatusConfidences());
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
