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

import java.io.File;

public class StringUtilities
{
	public static String removeLastChar(String elementName)
	{
		return elementName.substring(0, elementName.length() - 1);
	}

	public static String substringAfter(String value, String token)
	{
		final int firstIndexOf = value.indexOf(token);
		if (firstIndexOf >= 0)
			return value.substring(firstIndexOf + 1);
		
		return "";
	}
	
	public static String substringBetween(String line, final String delimeterBefore, final String delimeterAfter)
	{
		final int indexAfterFirstFromChar = line.indexOf(delimeterBefore) + 1;
		final int firstIndexOfToChar = line.indexOf(delimeterAfter, indexAfterFirstFromChar);
		
		return line.substring(indexAfterFirstFromChar, firstIndexOfToChar);
	}
	
	public static String joinWithOr(String[] tagsToKeep)
	{
		return joinListItems(tagsToKeep, "", "|", "");
	}

	public static String joinListItems(final String[] list, final String prefix, final String delimiter, final String suffix)
	{
		String joinedItems = "";
		for (int index = 0; index < list.length; ++index)
		{
			if (index > 0)
				joinedItems += delimiter;
			
			joinedItems += prefix + list[index] + suffix;
		}
		
		return joinedItems;
	}
	
	public static byte[] getUtf8EncodedBytes(String actualMpf) throws Exception
	{
		return actualMpf.getBytes("UTF-8");
	}
	
	public static String subStringBeforeLastDot(String name)
	{
		String fileName = new File(name).getName();
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		
		return fileName.substring(0, lastDotAt);
	}
	
	public static final String EMPTY_SPACE= " ";
}
