package org.conservationmeasures.eam.questions;

public class FontStyleQuestion extends ChoiceQuestion
{
	public FontStyleQuestion(String tag)
	{
		super(tag, "Font Style", getStyleChoices());
	}
	
	static ChoiceItem[] getStyleChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "None"),
			new ChoiceItem("Italic", "Italic"),
		};
	}
}
