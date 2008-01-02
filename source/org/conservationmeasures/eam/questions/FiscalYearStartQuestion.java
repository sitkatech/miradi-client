/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import org.conservationmeasures.eam.main.EAM;

public class FiscalYearStartQuestion extends StaticChoiceQuestion
{
	public FiscalYearStartQuestion(String tagToUse)
	{
		super(tagToUse, EAM.text("Label|Fiscal Year Start"), getStaticChoices());
	}

	static ChoiceItem[] getStaticChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("January to December")),	
			new ChoiceItem("10", EAM.text("October to September")),	
			new ChoiceItem("7", EAM.text("July to June")),	
			new ChoiceItem("4", EAM.text("April to March")),	
		};
	}
}
