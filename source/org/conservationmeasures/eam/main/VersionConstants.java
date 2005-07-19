/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.IOException;
import java.io.InputStream;

import org.martus.util.UnicodeReader;

public class VersionConstants
{
	public static void setVersionString() throws IOException
	{
		InputStream in = VersionConstants.class.getResourceAsStream(VERSION_FILENAME);
		if(in == null)
		{
			EAM.logWarning(VERSION_FILENAME + " not found");
			VERSION_STRING = "(unknown)";
			return;
		}
		UnicodeReader reader = new UnicodeReader(in);
		try
		{
			VERSION_STRING = reader.readLine();
		}
		finally
		{
			reader.close();
		}
	}
	
	private static final String VERSION_FILENAME = "/version.txt";
	public static String VERSION_STRING;
}
