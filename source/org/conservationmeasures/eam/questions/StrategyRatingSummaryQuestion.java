/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;


public class StrategyRatingSummaryQuestion extends StaticChoiceQuestion
{
	public StrategyRatingSummaryQuestion(String tag)
	{
		super(tag, "Priority", getStrategyRatingChoices());
	}
	
	static ChoiceItem[] getStrategyRatingChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Unknown", Color.WHITE),
			new ChoiceItem("1", "Not Effective", COLOR_ALERT),
			new ChoiceItem("2", "Less Effective", COLOR_CAUTION),
			new ChoiceItem("3", "Effective", COLOR_OK),
			new ChoiceItem("4", "Very Effective", COLOR_GREAT),
		};
	}

	public ChoiceItem getResult(ChoiceItem impact, ChoiceItem feasibility)
	{
		String impactCode = impact.getCode();
		String feasibilityCode = feasibility.getCode();
		
		int impactValue = codeToInt(impactCode);
		int feasibilityValue = codeToInt(feasibilityCode);
		
		int min = Math.min(impactValue, feasibilityValue);
		if(min == 0)
			return findChoiceByCode("");
		
		return findChoiceByCode(Integer.toString(min));
	}
	
	private int codeToInt(String code)
	{
		if(code.length() == 0)
			return 0;
		return Integer.parseInt(code);
	}
}
