/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.questions;

public class FontFamiliyQuestion extends StaticChoiceQuestion
{
	public FontFamiliyQuestion(String tag)
	{
		super(tag, getFamilyChoices());
	}
	
	static ChoiceItem[] getFamilyChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "sans-serif"),
			new ChoiceItem("serif", "serif"),
		};
	}
}
