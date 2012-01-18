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

package org.miradi.utils;

public class StringUtilities
{
	public static String substringAfter(String value, String token)
	{
		final int firstIndexOf = value.indexOf(token);
		if (firstIndexOf >= 0)
			return value.substring(firstIndexOf + 1);
		
		return "";
	}
	
	public static String joinWithOr(String[] tagsToKeep)
	{
		final String string = "|";
		return join(tagsToKeep, string);
	}

	private static String join(String[] tagsToKeep, final String string)
	{
		String tagsSeperatedByOr = "";
		for (int index = 0; index < tagsToKeep.length; ++index)
		{
			if (index != 0)
				tagsSeperatedByOr += string;
			
			tagsSeperatedByOr += tagsToKeep[index];
		}
		
		return tagsSeperatedByOr;
	}
}
