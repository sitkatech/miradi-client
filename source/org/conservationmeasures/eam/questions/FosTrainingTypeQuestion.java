/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class FosTrainingTypeQuestion extends StaticChoiceQuestion
{
	public FosTrainingTypeQuestion()
	{
		super(getTrainingTypeChoices());
	}

	static ChoiceItem[] getTrainingTypeChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified"),
				new ChoiceItem("1", "Workshop"),
				new ChoiceItem("2", "University"),
				new ChoiceItem("3", "Online"),
				new ChoiceItem("4", "Other"),
		};
	}
}
