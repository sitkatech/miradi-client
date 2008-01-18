/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
				new ChoiceItem("0.5", "Smallest"),
				new ChoiceItem("0.75", "Very Small"),
				new ChoiceItem("1.0", "Small"),
				new ChoiceItem("1.25", "Large"),
				new ChoiceItem("1.50", "Very Large"),
				new ChoiceItem("2.5", "Largest"),
		};
	}
}
