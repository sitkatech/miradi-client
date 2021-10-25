/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.utils.CodeList;

abstract public class AbstractStringListData extends ObjectData
{
	public AbstractStringListData(String tagToUse)
	{
		super(tagToUse);
		
		data = new CodeList();
	}
	
	@Override
	public void set(String newValue) throws Exception
	{
		set(new CodeList(newValue));
	}
	
	@Override
	public String get()
	{
		return data.toString();
	}
	
	public void set(CodeList newCodes)
	{
		data = newCodes;
	}
	
	public CodeList getCodeList()
	{
		return new CodeList(data);
	}
	
	public int size()
	{
		return data.size();
	}
	
	public String get(int index)
	{
		return data.get(index);
	}
	
	public void add(String code)
	{
		data.add(code);
	}
	
	public void removeCode(String code)
	{
		data.removeCode(code);
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof AbstractStringListData))
			return false;
		
		AbstractStringListData other = (AbstractStringListData)rawOther;
		return data.equals(other.data);
	}

	@Override
	public int hashCode()
	{
		return data.hashCode();
	}

	private CodeList data;
}
