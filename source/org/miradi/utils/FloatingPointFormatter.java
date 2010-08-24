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

import org.miradi.utils.OptionalDouble;

public class FloatingPointFormatter
{
	public FloatingPointFormatter()
	{
		decimalFormat = new DecimalFormat("#.#");
		decimalFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
	}
	
	public static String formatEditableValue(OptionalDouble valueToFormat)
	{
		if (valueToFormat.hasNoValue())
			return "";
		
		return formatEditableValue(valueToFormat.getValue());
	}
	
	public static String formatEditableValue(double valueToFormat)
	{
		FloatingPointFormatter formatter = new FloatingPointFormatter();
		formatter.setGroupingUsed(false);
		formatter.setMinimumFractionDigits(0);
		formatter.setMaximumFractionDigits(3);

		return formatter.format(valueToFormat);
	}
	
	public String format(double valueToFormat)
	{
		return decimalFormat.format(valueToFormat);
	}
	
	public void setGroupingUsed(boolean shouldGroup)
	{
		decimalFormat.setGroupingUsed(shouldGroup);
	}
	
	public void setMinimumFractionDigits(int minFractionDigits)
	{
		decimalFormat.setMinimumFractionDigits(minFractionDigits);
	}

	public void setMaximumFractionDigits(int maxFractionDigits)
	{
		decimalFormat.setMaximumFractionDigits(maxFractionDigits);
	}

	protected DecimalFormat decimalFormat;
}
