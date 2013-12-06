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


public class CurrencyTypeQuestion extends StaticChoiceQuestion
{
	public CurrencyTypeQuestion()
	{
		super(getCurrencyChoices());
	}

	static ChoiceItem[] getCurrencyChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Dollar (United States)")),
				new ChoiceItem("EUR", EAM.text("Euro")),
				new ChoiceItem("GBP", EAM.text("Pound (United Kingdom)")),
				new ChoiceItem("JPY", EAM.text("Yen (Japan)")),
				new ChoiceItem("CAD", EAM.text("Dollar (Canada)")),
				new ChoiceItem("AUD", EAM.text("Dollar (Australia)")),
				new ChoiceItem("CHF", EAM.text("Franc (Switzerland)")),
				new ChoiceItem("RUB", EAM.text("Ruble (Russia)")),
				new ChoiceItem("CNY", EAM.text("Chinese Yuan Renminbi")),
				new ChoiceItem("ZAR", EAM.text("Rand (South Africa)")),
				new ChoiceItem("MXN", EAM.text("Peso (Mexico)")),
				new ChoiceItem("???", EAM.text("Currency|Other")),
		};
	}
}
