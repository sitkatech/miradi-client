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

public class CodeToStringMap extends AbstractStringToStringMap
{
	public CodeToStringMap()
	{
		super();
	}

	public CodeToStringMap(CodeToStringMap copyFrom)
	{
		super(copyFrom);
	}

	public CodeToStringMap(EnhancedJsonObject json)
	{
		super(json);
	}
	
	public CodeToStringMap(String mapAsJsonString) throws ParseException
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
		if(!(rawOther instanceof CodeToStringMap))
			return false;

		CodeToStringMap other = (CodeToStringMap) rawOther;
		return data.equals(other.data);
	}

	private static final String TAG_STRING_MAP = "StringMap";
}
