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

import org.miradi.utils.InvalidNumberException;

public class NumberData extends ObjectData
{
	public NumberData(String tagToUse)
	{
		super(tagToUse);
		value = Double.NaN;
	}
	
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			value = Double.NaN;
			return;
		}
		
		try
		{
			value = Double.parseDouble(newValue);
		}
		catch (NumberFormatException e)
		{
			throw new InvalidNumberException(e);
		}
	}

	public String get()
	{
		if(new Double(value).isNaN())
			return "";
		return Double.toString(value);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof NumberData))
			return false;
		
		NumberData other = (NumberData)rawOther;
		return new Double(value).equals(new Double(other.value));
	}

	public int hashCode()
	{
		return (int)value;
	}


	double value;
}
