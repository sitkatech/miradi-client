/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class DiagramFactorFontSizeQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontSizeQuestion(String tag)
	{
		super(tag, "Diagram Factor Font", getFontChoices());
	}
	
	static ChoiceItem[] getFontChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Medium (Default)"),
				new ChoiceItem("6", "Smallest"),
				new ChoiceItem("10", "Very Small"),
				new ChoiceItem("14", "Small"),
				new ChoiceItem("18", "Large"),
				new ChoiceItem("22", "Very Large"),
				new ChoiceItem("26", "Largest"),
		};
	}
}
