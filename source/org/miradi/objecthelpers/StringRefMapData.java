/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objecthelpers;

import java.util.Set;

import org.miradi.objectdata.ObjectData;

public class StringRefMapData extends ObjectData
{
	public StringRefMapData(String tagToUse)
	{
		super(tagToUse);
		data = new StringRefMap();
	}

	public String get()
	{
		return getStringRefMap().toString();
	}
	
	public StringRefMap getStringRefMap()
	{
		return data;
	}

	public ORefList getRefList()
	{
		ORefList allValues = new ORefList();
		Set keys = data.getKeys();
		for(Object key : keys)
		{
			ORef ref = data.getValue((String) key);
			allValues.add(ref);
		}
		
		//FIXME this should fail a test first 
		return new ORefList();
	}

	public void set(String newValue) throws Exception
	{
		data = new StringRefMap(newValue);
	}

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringRefMapData))
			return false;

		StringRefMapData other = (StringRefMapData) rawOther;
		return other.data.equals(data);
	}

	public int hashCode()
	{
		return toString().hashCode();
	}

	private StringRefMap data;
}
