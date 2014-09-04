/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import java.util.LinkedHashMap;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;

public class RawObject extends LinkedHashMap<String, String>
{
	public RawObject(ORef ref)
	{
		this(ref.getObjectType());
	}

	public RawObject(int objectTypeToUse)
	{
		objectType = objectTypeToUse;
	}
	
	public void put(String tag, ORefList refList)
	{
		put(tag, refList.toString());
	}
	
	public String getData(String tag)
	{
		return get(tag);
	}
	
	public int getObjectType()
	{
		return objectType;
	}

	public String setData(String tag, String data)
	{
		return put(tag, data);
	}
	
	public boolean hasValue(String tag)
	{
		if (!containsKey(tag))
			return false;
		
		return getData(tag).length() > 0;
	}

	private int objectType;
}