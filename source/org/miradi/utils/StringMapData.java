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

import org.martus.util.UnicodeWriter;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.StringMap;

public class StringMapData extends ObjectData
{
	public StringMapData(String tagToUse)
	{
		super(tagToUse);
		data = new StringMap();
	}

	public String get()
	{
		return getStringMap().toString();
	}
	
	public StringMap getStringMap()
	{
		return data;
	}

	@Override
	public void toXml(UnicodeWriter out) throws Exception
	{
		startTagToXml(out);
		data.toXml(out);
		endTagToXml(out);
	}

	public void set(String newValue) throws Exception
	{
		data = new StringMap(newValue);
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringMapData))
			return false;

		StringMapData other = (StringMapData) rawOther;
		return other.data.equals(data);
	}

	public int hashCode()
	{
		return toString().hashCode();
	}
	
	public boolean isStringMapData()
	{
		return true;
	}
	
	private StringMap data;
}
