/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.utils;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
			return value.substring(firstIndexOf + token.length());
		
		return "";
	}
	
	public static String substringBetween(String line, final String delimiterBefore, final String delimiterAfter)
	{
		final int indexAfterFirstFromChar = line.indexOf(delimiterBefore) + 1;
		final int firstIndexOfToChar = line.indexOf(delimiterAfter, indexAfterFirstFromChar);
		
		return line.substring(indexAfterFirstFromChar, firstIndexOfToChar);
	}
	
	public static String joinWithOr(Vector<String> list, String prefix, String suffix)
	{
		return joinListItems(list.toArray(new String[0]), prefix, "|", suffix);
	}
	
	public static String joinList(Vector<String> list, String delimiter)
	{
		return joinListItems(list.toArray(new String[0]), "", delimiter, "");
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

	public static String safelyStripTrailingString(String name, String suffix) throws Exception
	{
		if (!name.endsWith(suffix))
			return name;

		return stripTrailingString(name, suffix);
	}

	public static String stripTrailingString(String name, final String suffix) throws Exception
	{
		int lastIndexOfSuffix = name.lastIndexOf(suffix);
		if(lastIndexOfSuffix < 0)
			throw new Exception("Suffix " + suffix + " was not found in " + name);
		
		return name.substring(0, lastIndexOfSuffix);
	}
	
	public static String escapeQuotesWithBackslash(final String value)
	{
		final String replaceAll = value.replaceAll("\\\"", "\\\\\"");
		return replaceAll;
	}
	
	public static String unescapeQuotesWithBackslash(final String value)
	{
		return value.replaceAll("\\\\\"", "\\\"");
	}
	
	public static String removeIllegalCharacters(String value)
	{
		String regexp = "[^" + REGEXP_TAB + REGEXP_CR + REGEXP_LF + REGEXP_NON_CONTROL_CHARACTERS + "]";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) 
		   return matcher.replaceAll("");

		return value;
	}

	public static boolean isNullOrEmpty(final String value)
	{
		return value == null || value.isEmpty();
	}

	public static final String EMPTY_SPACE= " ";
	public static final String EMPTY_LINE= "";
	public static final String NEW_LINE = "\n";
	public static final String EMPTY_STRING = "";
	private static final String REGEXP_TAB = "\\u0009";
	private static final String REGEXP_CR = "\\u000A";
	private static final String REGEXP_LF = "\\u000D";
	private static final String REGEXP_NON_CONTROL_CHARACTERS = "\\u0020-\\uFFFF";
}
