/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class DiagramFactorFontStyleQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontStyleQuestion(String tag)
	{
		super(tag, "Diagram Factor Style", getStyleChoices());
	}
	
	static ChoiceItem[] getStyleChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Plain (Default)"),
			new ChoiceItem("<B>", "Bold"),
		};
	}
}
