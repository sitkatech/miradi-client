package org.conservationmeasures.eam.questions;

public class FontFamiliyQuestion extends StaticChoiceQuestion
{
	public FontFamiliyQuestion(String tag)
	{
		super(tag, "Font Family", getFamilyChoices());
	}
	
	static ChoiceItem[] getFamilyChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "sans-serif"),
			new ChoiceItem("serif", "serif"),
		};
	}
}
