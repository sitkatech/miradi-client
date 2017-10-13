/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.utils.DoubleUtilities;

abstract public class AbstractIntegerData extends ObjectData
{
	public AbstractIntegerData(String tagToUse)
	{
		super(tagToUse);

		value = 0;
	}
	
	@Override
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
			double valueAsDouble = DoubleUtilities.toDoubleFromDataFormat(newValue);
			value = new Integer((int)valueAsDouble);
			if(Math.abs(valueAsDouble-value) >= .1)
				EAM.logWarning("TRUNCATING floating portion of: " + getTag());
		}
	}
	
	@Override
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
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof AbstractIntegerData))
			return false;
		
		AbstractIntegerData other = (AbstractIntegerData)rawOther;
		return new Integer(value).equals(new Integer(other.value));
	}

	@Override
	public int hashCode()
	{
		return value;
	}

	private int value;
}
