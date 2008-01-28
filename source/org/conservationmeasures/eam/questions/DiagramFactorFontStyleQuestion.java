/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


public class DiagramFactorFontStyleQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontStyleQuestion()
	{
		super(getStyleChoices());
	}
	
	static ChoiceItem[] getStyleChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Plain (Default)"),
			new ChoiceItem("<B>", "Bold"),
			new ChoiceItem("<U>", "Underline"),
			new ChoiceItem("<S>", "Strike through"),
		};
	}
}
