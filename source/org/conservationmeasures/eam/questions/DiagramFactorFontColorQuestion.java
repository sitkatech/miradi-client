/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class DiagramFactorFontColorQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontColorQuestion(String tag)
	{
		super(tag, "Diagram Factor Color", getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Black (Default)"),
			new ChoiceItem("#4E4848", "Dark Gray"),
			new ChoiceItem("#6D7B8D", "Light Gray"),
			new ChoiceItem("#C85A17", "Brown"),
			new ChoiceItem("#EDE275", "Tan"),
			new ChoiceItem("#FFFFFF", "White"),
			new ChoiceItem("#FF0000", "Red"),
			new ChoiceItem("#FF00FF", "Pink"),
			new ChoiceItem("#FF8040", "Orange"),
			new ChoiceItem("#FFFF00", "Yellow"),
			new ChoiceItem("#254117", "Dark Green"),
			new ChoiceItem("#5FFB17", "Light Green"),
			new ChoiceItem("#0000CC", "Dark Blue"),
			new ChoiceItem("#00CCFF", "Light Blue"),
		};
	}
}
