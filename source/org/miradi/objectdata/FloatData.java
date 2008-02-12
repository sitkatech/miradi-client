/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectdata;


public class FloatData extends ObjectData
{
	public FloatData(String tagToUse)
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
		
		value = Float.parseFloat(newValue);
	}
	
	public String get()
	{
		return Float.toString(value);
	}
	
	public float asFloat()
	{
		return value;
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof FloatData))
			return false;
		
		FloatData other = (FloatData)rawOther;
		return new Float(value).equals(new Float(other.value));
	}

	public int hashCode()
	{
		return new Float(value).hashCode();
	}


	float value;
}
