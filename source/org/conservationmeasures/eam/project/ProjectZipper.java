/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ProjectZipper
{

	public static void createProjectZipFile(File destination, File projectDirectory) throws FileNotFoundException, Exception, IOException
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destination));
		addTreeToZip(out, "", projectDirectory);
		out.close();
	}

	static void addTreeToZip(ZipOutputStream out, String prefix, File parentDirectory) throws Exception
	{
		File[] files = parentDirectory.listFiles();
		for(int i = 0; i < files.length; ++i)
		{
			File thisFile = files[i];
			if(prefix.length() > 0)
				prefix = prefix + "/";
			String entryName = prefix + thisFile.getName();
			if(thisFile.isDirectory())
			{
				addTreeToZip(out, entryName, thisFile);
			}
			else
			{
				ZipEntry entry = new ZipEntry(entryName);
				out.putNextEntry(entry);
				int size = (int)thisFile.length();
				byte[] contents = new byte[size];
				FileInputStream in = new FileInputStream(thisFile);
				in.read(contents);
				in.close();
				out.write(contents);
				out.closeEntry();
			}
		}
	}

}
