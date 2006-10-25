/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;

public class IntegerData extends ObjectData
{
	public IntegerData()
	{
		value = 0;
	}
	
	public IntegerData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch(Exception e)
		{
			EAM.logDebug("IntegerData ignoring invalid: " + valueToUse);
		}
	}
	
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			value = 0;
			return;
		}
		
		value = Integer.parseInt(newValue);
	}
	
	public String get()
	{
		return Integer.toString(value);
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
