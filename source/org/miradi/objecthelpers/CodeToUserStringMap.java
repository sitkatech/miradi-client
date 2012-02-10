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

public class CodeToUserStringMap extends AbstractCodeToStringMap
{
	public CodeToUserStringMap()
	{
		super();
	}

	public CodeToUserStringMap(CodeToUserStringMap copyFrom)
	{
		super(copyFrom);
	}

	public CodeToUserStringMap(EnhancedJsonObject json)
	{
		super(json);
	}
	
	public CodeToUserStringMap(String mapAsJsonString) throws ParseException
	{
		super(mapAsJsonString);
	}
	
	public void putUserString(String code, String newValue)
	{
		rawPut(code, newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeToUserStringMap))
			return false;

		CodeToUserStringMap other = (CodeToUserStringMap) rawOther;
		return data.equals(other.data);
	}

}