/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

	public void set(String newValue) throws Exception
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
