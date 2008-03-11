/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
