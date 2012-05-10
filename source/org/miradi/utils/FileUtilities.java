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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtilities
{

	public static void copyStream(InputStream inputStream, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int got = -1;
		while( (got = inputStream.read(buffer)) > 0)
		{
			out.write(buffer, 0, got);
		}
	}

	public static void copyStreamToFile(InputStream inputStream, File destinationFile) throws IOException
	{
		FileOutputStream out = new FileOutputStream(destinationFile);
		try
		{
			copyStream(inputStream, out);
		}
		finally
		{
			out.close();
		}
	}

	public static File createTempDirectory(String nameHint) throws IOException
	{
		File tempDirectory = File.createTempFile("$$$" + nameHint, null);
		tempDirectory.deleteOnExit();
		tempDirectory.delete();
		tempDirectory.mkdir();
		return tempDirectory;
	}
	
	public static void deleteIfExists(File file) throws IOException
	{
		if(!file.exists())
			return;
		
		if(!file.delete())
			throw new IOException("Delete failed: " + file.getAbsolutePath());
	}
}
