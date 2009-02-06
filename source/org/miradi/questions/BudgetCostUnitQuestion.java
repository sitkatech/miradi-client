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


public class BudgetCostUnitQuestion extends StaticChoiceQuestion
{
	public BudgetCostUnitQuestion()
	{
		super(getCostUnitChoices());
	}

	static ChoiceItem[] getCostUnitChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Not Specified")),
				new ChoiceItem("Hours", EAM.text("Hours")),
				new ChoiceItem(DAYS_CODE, EAM.text("Days")),
				new ChoiceItem("Weeks", EAM.text("Weeks")),
				new ChoiceItem("Months", EAM.text("Months")),
				new ChoiceItem("Each", EAM.text("Each")),
		};
	}

	public static final String DAYS_CODE = "Days";
}
