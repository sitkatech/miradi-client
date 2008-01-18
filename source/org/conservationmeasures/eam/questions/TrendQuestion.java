/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;


public class TrendQuestion extends StaticChoiceQuestion
{
	public TrendQuestion(String tagToUse)
	{
		super(tagToUse, "Trends", getTrends());
	}

	static ChoiceItem[] getTrends()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", new IndicatorIcon()),
				new ChoiceItem("Unknown", "Unknown", new MiradiResourceImageIcon("images/arrows/va_unknown16.png")),
				new ChoiceItem("StrongIncrease", "Strong Increase", new MiradiResourceImageIcon("images/arrows/va_strongup16.png")),
				new ChoiceItem("MildIncrease", "Mild Increase", new MiradiResourceImageIcon("images/arrows/va_mildup16.png")),
				new ChoiceItem("Flat", "Flat", new MiradiResourceImageIcon("images/arrows/va_flat16.png")),
				new ChoiceItem("MildDecrease", "Mild Decrease", new MiradiResourceImageIcon("images/arrows/va_milddown16.png")),
				new ChoiceItem("StrongDecrease", "Strong Decrease", new MiradiResourceImageIcon("images/arrows/va_strongdown16.png")),
		};
	}
}