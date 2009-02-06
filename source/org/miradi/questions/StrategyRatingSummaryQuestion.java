/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.questions;

import java.awt.Color;

import org.miradi.main.EAM;


public class StrategyRatingSummaryQuestion extends StaticChoiceQuestion
{
	public StrategyRatingSummaryQuestion()
	{
		super(getStrategyRatingChoices());
	}
	
	static ChoiceItem[] getStrategyRatingChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", EAM.text("Unknown"), Color.WHITE),
			new ChoiceItem("1", EAM.text("Not Effective"), COLOR_ALERT),
			new ChoiceItem("2", EAM.text("Less Effective"), COLOR_CAUTION),
			new ChoiceItem("3", EAM.text("Effective"), COLOR_OK),
			new ChoiceItem("4", EAM.text("Very Effective"), COLOR_GREAT),
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
