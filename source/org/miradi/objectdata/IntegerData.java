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

import org.miradi.main.EAM;


public class IntegerData extends ObjectData
{
	public IntegerData(String tagToUse)
	{
		super(tagToUse);
		value = 0;
	}
	
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			value = 0;
			return;
		}
		
		try
		{
			value = Integer.parseInt(newValue);
		}
		catch(NumberFormatException e)
		{
			EAM.logDebug("Field " + getTag() + " expected integer but got: " + newValue);
			double valueAsDouble = Double.parseDouble(newValue);
			value = new Integer((int)valueAsDouble);
			if(Math.abs(valueAsDouble-value) >= .1)
				EAM.logWarning("TRUNCATING floating portion of: " + getTag());
		}
	}
	
	public String get()
	{
		if(value == 0)
			return "";
		return Integer.toString(value);
	}
	
	public int asInt()
	{
		return value;
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof IntegerData))
			return false;
		
		IntegerData other = (IntegerData)rawOther;
		return new Integer(value).equals(new Integer(other.value));
	}

	public int hashCode()
	{
		return value;
	}


	int value;
}
