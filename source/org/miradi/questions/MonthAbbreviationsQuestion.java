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

import org.miradi.main.EAM;

public class MonthAbbreviationsQuestion extends StaticChoiceQuestion
{
	public MonthAbbreviationsQuestion()
	{
		super(getMonthChoices());
	}

	private static ChoiceItem[] getMonthChoices()
	{
		return new ChoiceItem[]{
				new ChoiceItem("1", EAM.text("Jan")),
				new ChoiceItem("2", EAM.text("Feb")),
				new ChoiceItem("3", EAM.text("Mar")),
				new ChoiceItem("4", EAM.text("Apr")),
				new ChoiceItem("5", EAM.text("May")),
				new ChoiceItem("6", EAM.text("Jun")),
				new ChoiceItem("7", EAM.text("Jul")),
				new ChoiceItem("8", EAM.text("Aug")),
				new ChoiceItem("9", EAM.text("Sep")),
				new ChoiceItem("10", EAM.text("Oct")),
				new ChoiceItem("11", EAM.text("Nov")),
				new ChoiceItem("12", EAM.text("Dec")),
		};
	}
}
