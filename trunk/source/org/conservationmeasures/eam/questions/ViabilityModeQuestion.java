/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

public class ViabilityModeQuestion extends ChoiceQuestion
{
	public ViabilityModeQuestion(String tagToUse)
	{
		super(tagToUse, "Target Viability Methodology", getViabilityModeChoices());
	}

	static ChoiceItem[] getViabilityModeChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Basic"),
				new ChoiceItem("TNC", "Detailed"),
		};
	}
	public static String TNC_STYLE_CODE = "TNC";
}
