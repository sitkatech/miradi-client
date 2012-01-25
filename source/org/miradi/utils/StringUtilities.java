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
	public static String getToken(String text, int caretPosition)
	{
		if (caretPosition < 0)
			return "";

		if (!text.contains(EMPTY_SPACE))
			return text;
		
		int wordStartIndex = getStartIndexOfToken(text, caretPosition);
		int wordEndIndex = getEndIndex(text, caretPosition);
		
		if (wordStartIndex > wordEndIndex)
			return "";
		
		final String substring = text.substring(wordStartIndex, wordEndIndex);
		
		return substring;
	}
	
	private static int getStartIndexOfToken(final String text, final int caretPosition)
	{
		for (int index = caretPosition; index >= 0; --index)
		{
			final char charAt = text.charAt(index);
			String stringAt = Character.toString(charAt);
			if (stringAt.equals(EMPTY_SPACE))
				return index + 1;
		}
		
		return 0;
	}
	
	private static int getEndIndex(final String text, final int caretPosition)
	{
		for (int index = caretPosition; index < text.length(); ++index)
		{
			final char charAt = text.charAt(index);
			String stringAt = Character.toString(charAt);
			if (stringAt.equals(EMPTY_SPACE))
				return index;
		}
		
		return text.length();
	}

	public static String substringAfter(String value, String token)
	{
		final int firstIndexOf = value.indexOf(token);
		if (firstIndexOf >= 0)
			return value.substring(firstIndexOf + 1);
		
		return "";
	}
	
	public static String joinWithOr(String[] tagsToKeep)
	{
		return join(tagsToKeep, "|");
	}

	private static String join(String[] tagsToKeep, final String stringToJoinWith)
	{
		String tagsSeperatedByOr = "";
		for (int index = 0; index < tagsToKeep.length; ++index)
		{
			if (index != 0)
				tagsSeperatedByOr += stringToJoinWith;
			
			tagsSeperatedByOr += tagsToKeep[index];
		}
		
		return tagsSeperatedByOr;
	}
	
	public static final String EMPTY_SPACE= " ";
}
