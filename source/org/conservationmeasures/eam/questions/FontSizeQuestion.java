/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.questions;

public class FontSizeQuestion extends StaticChoiceQuestion
{
	public FontSizeQuestion(String tag)
	{
		super(tag, getSizeChoices());
	}
	
	static ChoiceItem[] getSizeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("0", "System Default"),
			new ChoiceItem("6", "6"),
			new ChoiceItem("8", "8"),
			new ChoiceItem("10", "10"),
			new ChoiceItem("11", "11"),
			new ChoiceItem("12", "12"),
			new ChoiceItem("14", "14"),
			new ChoiceItem("18", "18"),
			new ChoiceItem("24", "24"),
		};
	}
}
