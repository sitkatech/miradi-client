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

import java.text.ParseException;
import java.util.List;

public class CodeList extends StringList
{
	public CodeList()
	{
		super();
	}
	
	public CodeList(String[] codes)
	{
		super(codes);
	}
	
	public CodeList(CodeList copyFrom)
	{
		super(copyFrom);
	}
	
	public CodeList(EnhancedJsonObject json)
	{
		super(json);
	}
	
	public CodeList(String listAsJsonString) throws ParseException
	{
		super(listAsJsonString);
	}
	
	public CodeList(List<String> dataToUse)
	{
		super(dataToUse);
	}
								
	public CodeList withoutDuplicates()
	{
		CodeList withoutDuplicates = new CodeList();
		for(int index = 0; index < size(); ++index)
		{
			String code = get(index);
			if (!withoutDuplicates.contains(code))
				withoutDuplicates.add(code);
		}
		
		return withoutDuplicates;
	}
	
	@Override
	protected String getJsonTag()
	{
		return TAG_IDS;
	}
	
	public boolean containsInt(int codeAsInt)
	{
		return contains(Integer.toString(codeAsInt));
	}
		
	private static final String TAG_IDS = "Codes";
}

