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

public class MonthAbbreviationsQuestion extends StaticChoiceQuestion
{
	public MonthAbbreviationsQuestion()
	{
		super(getMonthChoices());
	}

	private static ChoiceItem[] getMonthChoices()
	{
		return new ChoiceItem[]{
				new ChoiceItem("1", "Jan"),
				new ChoiceItem("2", "Feb"),
				new ChoiceItem("3", "Mar"),
				new ChoiceItem("4", "Apr"),
				new ChoiceItem("5", "May"),
				new ChoiceItem("6", "Jun"),
				new ChoiceItem("7", "Jul"),
				new ChoiceItem("8", "Aug"),
				new ChoiceItem("9", "Sep"),
				new ChoiceItem("10", "Oct"),
				new ChoiceItem("11", "Nov"),
				new ChoiceItem("12", "Dec"),
		};
	}
}
