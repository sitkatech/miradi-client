/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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


public class CurrencyTypeQuestion extends StaticChoiceQuestion
{
	public CurrencyTypeQuestion()
	{
		super(getCurrencyChoices());
	}

	static ChoiceItem[] getCurrencyChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Dollar (United States)"),
				new ChoiceItem("EUR", "Euro"),
				new ChoiceItem("GBP", "Pound (United Kingdom)"),
				new ChoiceItem("JPY", "Yen (Japan)"),
				new ChoiceItem("CAD", "Dollar (Canada)"),
				new ChoiceItem("AUD", "Dollar (Australia)"),
				new ChoiceItem("CHF", "Franc (Switzerland)"),
				new ChoiceItem("RUB", "Ruble (Russia)"),
				new ChoiceItem("CNY", "Chinese Yuan Renminbi"),
				new ChoiceItem("ZAR", "Rand (South Africa)"),
				new ChoiceItem("MXN", "Peso (Mexico)"),
				new ChoiceItem("???", "Other"),
		};
	}
}
