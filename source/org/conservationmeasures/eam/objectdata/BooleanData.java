/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.objectdata;

public class BooleanData extends IntegerData
{
	public BooleanData()
	{
		super();
	}
	
	public BooleanData(String valueToUse)
	{
		super(valueToUse);
	}
	
	public boolean asBoolean()
	{
		if (get().length() == 0)
			return false;
		
		if (get().equals(BOOLEAN_FALSE))
			return false;
		
		if (get().equals(BOOLEAN_TRUE))
			return true;
		
		throw new RuntimeException("Invalid boolean value :" + get());
	}
	
	public Boolean asBooleanObject()
	{
		return new Boolean(asBoolean());
	}
	
	public static String toString(boolean booleanToConvert)
	{
		if (booleanToConvert == true)
			return BOOLEAN_TRUE;
		
		return "";
	}
	
	public void set(String newValue) throws Exception
	{
		super.set(newValue);
		if (asInt()<0 || asInt()>1)
			throw new RuntimeException("Invalid boolean value :" + newValue);
	}
	
	static public final String BOOLEAN_FALSE = "";
	static public final String BOOLEAN_TRUE = "1";
}
