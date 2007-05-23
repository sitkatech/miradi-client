package org.conservationmeasures.eam.questions;

public class FontWeightQuestion extends ChoiceQuestion
{
	public FontWeightQuestion(String tag)
	{
		super(tag, "Font Weight", getWeightChoices());
	}
	
	static ChoiceItem[] getWeightChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "None"),
			new ChoiceItem("Bold", "Bold"),
		};
	}
}
