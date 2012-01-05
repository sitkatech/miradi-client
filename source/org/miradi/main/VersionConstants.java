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
package org.miradi.main;

import java.io.IOException;
import java.io.InputStream;

import org.martus.util.UnicodeReader;

public class VersionConstants
{
	public static String getVersionAndTimestamp() throws Exception
	{
		return getVersion() + " " + getTimestamp();
	}

	public static String getVersion() throws Exception
	{
		if (VERSION_STRING == null)
			VERSION_STRING = readFile(VERSION_FILENAME);
		
		return VERSION_STRING;
	}
	
	public static String getTimestamp() throws Exception
	{
		if (TIMESTAMP_STRING == null)
			TIMESTAMP_STRING = readFile(TIMESTAMP_FILENAME);

		return TIMESTAMP_STRING;
	}
	
	public static boolean hasValidVersion() throws Exception
	{
		return isValid(getVersion());
	}
	
	public static boolean hasValidTimestamp() throws Exception
	{
		return isValid(getTimestamp());
	}
	
	private static boolean isValid(String value)
	{
		if (value == null || value.length() == 0 || value.equals(UNKNOWN_VALUE))
			return false;
		
		return true;
	}
	
	private static String readFile(String filename) throws IOException
	{
		InputStream in = VersionConstants.class.getResourceAsStream(filename);
		if(in == null)
			return UNKNOWN_VALUE;
	
		UnicodeReader reader = new UnicodeReader(in);
		try
		{
			return reader.readLine();
		}
		finally
		{
			reader.close();
		}
	}
	
	private static final String VERSION_FILENAME = "/miradi.version.txt";
	private static final String TIMESTAMP_FILENAME = "/miradi.timestamp.txt";
	private static final String UNKNOWN_VALUE = "(unknown)";
	private static String VERSION_STRING;
	private static String TIMESTAMP_STRING;
}
