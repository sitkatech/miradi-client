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
package org.miradi.project;

import java.text.DecimalFormat;

/*
 * CurrencyFormat
 * 
 * The main reason this class exists is for compatibility with Java 5,
 * whose DecimalFormat class does not support rounding.
 * 
 */
public class CurrencyFormat
{
	public CurrencyFormat()
	{
		decimalFormat = new DecimalFormat();
	}

	public String format(double valueToFormat)
	{
		int decimals = decimalFormat.getMaximumFractionDigits();
		double shiftBy = Math.pow(10, decimals);
		double readyForRounding = valueToFormat * shiftBy;
		long roundedLong = Math.round(readyForRounding);
		double rounded = roundedLong / shiftBy;
		String result = decimalFormat.format(rounded);
		return result;
	}

	public void setGroupingUsed(boolean b)
	{
		decimalFormat.setGroupingUsed(b);
	}

	public void setMinimumFractionDigits(int currencyDecimalPlaces)
	{
		decimalFormat.setMinimumFractionDigits(currencyDecimalPlaces);
	}

	public void setMaximumFractionDigits(int currencyDecimalPlaces)
	{
		decimalFormat.setMaximumFractionDigits(currencyDecimalPlaces);
	}

	private DecimalFormat decimalFormat;

}
