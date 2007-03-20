/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

public class ViabilitySelectionQuestion extends ChoiceQuestion
{
	public ViabilitySelectionQuestion(String tagToUse)
	{
		super(tagToUse, "Viability Selection", getViabilitySelectionChoices());
	}

	static ChoiceItem[] getViabilitySelectionChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("Basic", "Basic"),
				new ChoiceItem("TNC", "TNC"),
		};
	}
}
