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

import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.CodeStringMap;

public class CodeStringMapData extends ObjectData
{
	public CodeStringMapData(String tagToUse)
	{
		super(tagToUse);
		
		data = new CodeStringMap();
	}

	@Override
	public String get()
	{
		return getStringMap().toString();
	}
	
	public CodeStringMap getStringMap()
	{
		return data;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new CodeStringMap(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeStringMapData))
			return false;

		CodeStringMapData other = (CodeStringMapData) rawOther;
		return other.data.equals(data);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean isStringMapData()
	{
		return true;
	}
	
	private CodeStringMap data;
}
