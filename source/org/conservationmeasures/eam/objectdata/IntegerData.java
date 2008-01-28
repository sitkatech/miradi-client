/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;

public class IntegerData extends ObjectData
{
	public IntegerData()
	{
		value = 0;
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
