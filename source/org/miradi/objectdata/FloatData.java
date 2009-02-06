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
