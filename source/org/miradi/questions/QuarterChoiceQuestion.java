/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.EAM;

public class QuarterChoiceQuestion extends StaticChoiceQuestion
{
	public QuarterChoiceQuestion(int fiscalYearStartMonth)
	{
		super(createQuarterChoices(fiscalYearStartMonth));
	}
	
	private static ChoiceItem[] createQuarterChoices(int fiscalYearStartMonth)
	{
		return new ChoiceItem[]{
				new ChoiceItem("1", createQuarterLabel(fiscalYearStartMonth, EAM.text("Quarter 1"))),
				new ChoiceItem("2", createQuarterLabel(fiscalYearStartMonth, EAM.text("Quarter 2"))),
				new ChoiceItem("3", createQuarterLabel(fiscalYearStartMonth, EAM.text("Quarter 3"))),
				new ChoiceItem("4", createQuarterLabel(fiscalYearStartMonth, EAM.text("Quarter 4"))),
		};
	}

	private static String createQuarterLabel(int fiscalYearStartMonth, String quarterLabel)
	{
		return getFiscalYearLabel(fiscalYearStartMonth) + quarterLabel;
	}

	public static String getFiscalYearLabel(int fiscalYearStartMonth)
	{
		if (fiscalYearStartMonth == 1)
			return "";
			
		final String SINGLE_SPACE = " ";
		return EAM.text("FY") + SINGLE_SPACE;
	}
}
