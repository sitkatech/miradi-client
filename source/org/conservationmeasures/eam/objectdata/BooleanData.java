package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;

public class BooleanData extends ObjectData
{
	public BooleanData()
	{
		value = false;
	}
	
	public BooleanData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch(Exception e)
		{
			EAM.logDebug("BooleanData ignoring invalid: " + valueToUse);
		}
	}
	
	public void set(String newValue) throws Exception
	{
		if(newValue.length() == 0)
		{
			value = false;
			return;
		}
		
		value = Boolean.parseBoolean(newValue);
	}
	
	public String get()
	{
		return Boolean.toString(value);
	}
	

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof BooleanData))
			return false;
		
		BooleanData other = (BooleanData)rawOther;
		return new Boolean(value).equals(new Boolean(other.value));
	}

	public int hashCode()
	{
		return new Boolean(value).hashCode();
	}


	boolean value;
}
