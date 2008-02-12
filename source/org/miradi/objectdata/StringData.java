/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;

public class StringData extends ObjectData
{
	public StringData(String tagToUse)
	{
		super(tagToUse);
		value = "";
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


	private String value;
}
