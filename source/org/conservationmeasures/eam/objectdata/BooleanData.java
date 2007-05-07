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
	
	public void set(String newValue) throws Exception
	{
		super.set(newValue);
		if (asInt()<0 || asInt()>1)
			throw new RuntimeException("Invalid boolean value");
	}
	
	static public final String BOOLEAN_FALSE = "0";
	static public final String BOOLEAN_TRUE = "1";
}
