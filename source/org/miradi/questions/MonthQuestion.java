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

public class MonthQuestion extends StaticChoiceQuestion
{
	@Override
	protected ChoiceItem[] createChoices()
	{
		return new ChoiceItem[]{
				new ChoiceItem("1", EAM.text("January")),
				new ChoiceItem("2", EAM.text("February")),
				new ChoiceItem("3", EAM.text("March")),
				new ChoiceItem("4", EAM.text("April")),
				new ChoiceItem("5", EAM.text("May")),
				new ChoiceItem("6", EAM.text("June")),
				new ChoiceItem("7", EAM.text("July")),
				new ChoiceItem("8", EAM.text("August")),
				new ChoiceItem("9", EAM.text("September")),
				new ChoiceItem("10", EAM.text("October")),
				new ChoiceItem("11", EAM.text("November")),
				new ChoiceItem("12", EAM.text("December")),
		};
	}
}
