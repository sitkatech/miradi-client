/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

public class ViabilityModeQuestion extends StaticChoiceQuestion
{
	public ViabilityModeQuestion(String tagToUse)
	{
		super(tagToUse, getViabilityModeChoices());
	}

	static ChoiceItem[] getViabilityModeChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Simple"),
				new ChoiceItem("TNC", "Key Attribute"),
		};
	}
	public static String TNC_STYLE_CODE = "TNC";
}
