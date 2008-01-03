/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;

public class CurrencyDecimalQuestion extends StaticChoiceQuestion
{
	public CurrencyDecimalQuestion(String tagToUse)
	{
		super(tagToUse, EAM.text("Label|Currency Decimal Question"), getStaticChoices());
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
