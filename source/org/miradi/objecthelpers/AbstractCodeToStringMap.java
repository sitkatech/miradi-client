package org.miradi.objecthelpers;

import java.text.ParseException;

import org.miradi.utils.EnhancedJsonObject;

/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

abstract public class AbstractCodeToStringMap extends AbstractStringToStringMap
{
	public AbstractCodeToStringMap()
	{
		super();
	}

	public AbstractCodeToStringMap(CodeToUserStringMap copyFrom)
	{
		super(copyFrom);
	}

	public AbstractCodeToStringMap(EnhancedJsonObject json)
	{
		super(json);
	}

	public AbstractCodeToStringMap(String mapAsJsonString) throws ParseException
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
		if(!(rawOther instanceof CodeToUserStringMap))
			return false;

		CodeToUserStringMap other = (CodeToUserStringMap) rawOther;
		return data.equals(other.data);
	}

	private static final String TAG_STRING_MAP = "StringMap";
}
