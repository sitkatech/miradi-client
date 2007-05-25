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
			new ChoiceItem("12", "4"),
			new ChoiceItem("24", "8"),
			new ChoiceItem("36", "12"),
			new ChoiceItem("64", "18"),
		};
	}
}
