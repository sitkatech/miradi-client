/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.InvalidNumberException;

public class NumberData extends ObjectData
{
	public NumberData()
	{
		value = Double.NaN;
	}
	
	public NumberData(String valueToUse)
	{
		this();
		try
		{
			set(valueToUse);
		}
		catch (Exception e)
		{
			EAM.logDebug("NumberData ignoring invalid: " + valueToUse);
		}
	}

	public void set(String newValue) throws Exception
	{
		try
		{
			value = Double.parseDouble(newValue);
		}
		catch (NumberFormatException e)
		{
			throw new InvalidNumberException(e);
		}
	}

	public String get()
	{
		if(new Double(value).isNaN())
			return "";
		return Double.toString(value);
	}

	double value;
}
