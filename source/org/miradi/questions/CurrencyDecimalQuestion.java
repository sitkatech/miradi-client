/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.main.EAM;

public class CurrencyDecimalQuestion extends StaticChoiceQuestion
{
	public CurrencyDecimalQuestion()
	{
		super(getStaticChoices());
	}

	static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("0")),	
			new ChoiceItem("1", EAM.text("1")),	
			new ChoiceItem("2", EAM.text("2")),	
			new ChoiceItem("3", EAM.text("3")),	
		};
	}
}
