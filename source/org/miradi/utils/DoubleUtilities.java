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

package org.miradi.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DoubleUtilities
{
	public static double toDoubleFromDataFormat(String doubleAsString) throws Exception
	{
		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.parse(doubleAsString).doubleValue();
	}

	public static double toDoubleForHumans(String doubleAsString) throws Exception
	{
		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.parse(doubleAsString).doubleValue();
	}

	public static String toStringForData(double doubleToConvert)
	{
		Locale usLocale = Locale.US;
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getNumberInstance(usLocale);
		applyPatternToAvoidScientificNotation(formatter);

		return formatter.format(doubleToConvert).toString();
	}

	private static void applyPatternToAvoidScientificNotation(DecimalFormat formatter)
	{
		formatter.applyPattern("#.#");
	}

	public static String toStringForHumans(double doubleToConvert)
	{
		NumberFormat formatter = NumberFormat.getInstance();
		return formatter.format(doubleToConvert);
	}
}
