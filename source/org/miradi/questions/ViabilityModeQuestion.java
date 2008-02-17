/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.icons.IndicatorIcon;
import org.miradi.icons.KeyEcologicalAttributeIcon;

public class ViabilityModeQuestion extends StaticChoiceQuestion
{
	public ViabilityModeQuestion()
	{
		super(getViabilityModeChoices());
	}

	static ChoiceItem[] getViabilityModeChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Simple", new IndicatorIcon()),
				new ChoiceItem("TNC", "Key Attribute", new KeyEcologicalAttributeIcon()),
		};
	}
	public static String TNC_STYLE_CODE = "TNC";
}
