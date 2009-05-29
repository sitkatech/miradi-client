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
package org.miradi.objecthelpers;

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
		ORef[] allRefs = data.getValues().toArray(new ORef[0]);
		ORefList allValues = new ORefList(allRefs);
		
		//FIXME urgent: this should fail a test first with returning a new ORefList() 
		return allValues;
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
