/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;


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
