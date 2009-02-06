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
package org.miradi.utils;

import java.text.ParseException;

import org.json.JSONObject;

public class DataMap extends EnhancedJsonObject
{
	public DataMap()
	{
	}
	
	public DataMap(JSONObject copyFrom) throws ParseException
	{
		super(copyFrom.toString());
	}
	
	public void putInt(String tag, int value)
	{
		put(tag, new Integer(value));
	}
	
	public void putString(String tag, String value)
	{
		put(tag, value);
	}
	
	public int getInt(String tag)
	{
		return ((Integer)get(tag)).intValue();
	}
	
	public String getString(String tag)
	{
		return (String)get(tag);
	}
}
