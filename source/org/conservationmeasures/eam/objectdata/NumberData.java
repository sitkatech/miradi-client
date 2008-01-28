/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.utils.InvalidNumberException;

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
