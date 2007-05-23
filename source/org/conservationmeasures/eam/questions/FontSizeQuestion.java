package org.conservationmeasures.eam.questions;

public class FontSizeQuestion extends ChoiceQuestion
{
	public FontSizeQuestion(String tag)
	{
		super(tag, "Font Size", getSizeChoices());
	}
	
	static ChoiceItem[] getSizeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "System Default"),
			new ChoiceItem("4", "4"),
			new ChoiceItem("8", "8"),
			new ChoiceItem("12", "12"),
			new ChoiceItem("18", "18"),
		};
	}
}
