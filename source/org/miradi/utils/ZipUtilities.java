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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtilities
{

	public static void extractAll(ZipFile zipFile, File tempDirectory) throws IOException
	{
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while(entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			File destination = new File(tempDirectory, entry.getName());
			if(entry.isDirectory())
			{
				destination.mkdirs();
				continue;
			}
			
			destination.getParentFile().mkdirs();
			InputStream in = zipFile.getInputStream(entry);
			try
			{
				FileUtilities.copyStreamToFile(in, destination);
			}
			finally
			{
				in.close();
			}
		}
	}

	public static File createZipFromDirectory(File directoryToZip) throws IOException
	{
		File newZipFile = File.createTempFile("$$$CreatedFromDirectory", ".zip");
		OutputStream out = new FileOutputStream(newZipFile);
		try
		{
			ZipOutputStream zipOut = new ZipOutputStream(out);
			addToZip(zipOut, directoryToZip, directoryToZip.getAbsolutePath());
			zipOut.close();
			return newZipFile;
		}
		finally
		{
			out.close();
		}
	}

	private static void addToZip(ZipOutputStream zipOut, File directoryOrFileToZip, String basePath) throws IOException
	{
		if(directoryOrFileToZip.isDirectory())
			addDirectoryToZip(zipOut, directoryOrFileToZip, basePath);
		else
			addFileToZip(zipOut, directoryOrFileToZip, basePath);
	}
	
	private static void addDirectoryToZip(ZipOutputStream zipOut, File directoryToZip, String basePath) throws IOException
	{
		File[] files = directoryToZip.listFiles();
		if(files == null)
			return;
		
		for(File file : files)
			addToZip(zipOut, file, basePath);
	}

	private static void addFileToZip(ZipOutputStream zipOut, File fileToZip, String basePath) throws IOException
	{
		String fullName = fileToZip.getAbsolutePath();
		if(!fullName.startsWith(basePath))
			throw new RuntimeException("Tried to zip " + fullName + " with basePath of " + basePath);
		
		String relativeName = fullName.substring(basePath.length());
		ZipEntry fileEntry = new ZipEntry(relativeName);
		zipOut.putNextEntry(fileEntry);
		FileInputStream in = new FileInputStream(fileToZip);
		try
		{
			FileUtilities.copyStream(in, zipOut);
		}
		finally
		{
			in.close();
		}
		zipOut.closeEntry();
	}
}
