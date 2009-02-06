/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.objectdata;

public class BooleanData extends IntegerData
{
	public BooleanData(String tagToUse)
	{
		super(tagToUse);
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
