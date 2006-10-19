/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

public class StringData extends ObjectData
{
	public StringData()
	{
		value = "";
	}
	
	public StringData(String valueToUse)
	{
		value = valueToUse;
	}

	public void set(String newValue)
	{
		value = newValue;
	}

	public String get()
	{
		return value;
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringData))
			return false;
		
		StringData other = (StringData)rawOther;
		return value.equals(other.value);
	}

	public int hashCode()
	{
		return value.hashCode();
	}


	String value;
}
