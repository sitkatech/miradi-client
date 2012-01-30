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

import java.text.ParseException;

import org.miradi.utils.EnhancedJsonObject;

public class StringStringMap extends AbstractStringKeyMap
{
	public StringStringMap()
	{
		super();
	}

	public StringStringMap(StringStringMap copyFrom)
	{
		super(copyFrom);
	}

	public StringStringMap(EnhancedJsonObject json)
	{
		super(json);
	}
	
	public StringStringMap(String mapAsJsonString) throws ParseException
	{
		super(mapAsJsonString);
	}
	
	@Override
	protected String getMapTag()
	{
		return TAG_STRING_MAP;
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringStringMap))
			return false;

		StringStringMap other = (StringStringMap) rawOther;
		return data.equals(other.data);
	}

	private static final String TAG_STRING_MAP = "StringMap";
}
