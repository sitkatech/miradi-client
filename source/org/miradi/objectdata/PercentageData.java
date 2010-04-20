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
package org.miradi.objectdata;

import org.miradi.project.CurrencyFormat;


public class PercentageData extends NumberData
{
	public PercentageData(String tagToUse)
	{
		super(tagToUse);
	}

	@Override
	public void set(String newValue) throws Exception
	{
		newValue = newValue.replace("%", "");
		super.set(newValue);
	}

	@Override
	public String get()
	{
		if(new Double(value).isNaN())
			return "";
		
		String doubleAsString = super.get();
		double rawDouble = Double.parseDouble(doubleAsString);
		CurrencyFormat formatter = new CurrencyFormat();
		formatter.setGroupingUsed(false);

		return formatter.format(rawDouble);
	}
}
