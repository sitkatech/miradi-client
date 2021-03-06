/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.objectdata;

import org.miradi.objecthelpers.CodeToChoiceMap;

public class CodeToChoiceMapData extends ObjectData
{
	public CodeToChoiceMapData(String tagToUse)
	{
		super(tagToUse);
		
		data = new CodeToChoiceMap();
	}

	@Override
	public String get()
	{
		return getStringToChoiceMap().toJsonString();
	}
	
	public CodeToChoiceMap getStringToChoiceMap()
	{
		return data;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new CodeToChoiceMap(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeToChoiceMapData))
			return false;

		CodeToChoiceMapData other = (CodeToChoiceMapData) rawOther;
		return other.data.equals(data);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean isCodeToChoiceMapData()
	{
		return true;
	}

	private CodeToChoiceMap data;
}
