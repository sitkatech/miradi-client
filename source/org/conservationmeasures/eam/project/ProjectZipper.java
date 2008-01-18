/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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

	public static void createProjectZipFile(File destination,
			String zipTopLevelDirectory, File projectDirectory)
			throws FileNotFoundException, Exception, IOException
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				destination));
		addTreeToZip(out, zipTopLevelDirectory, projectDirectory);
		out.close();
	}

	public static void createProjectZipFile(File destination,
			File projectDirectory) throws FileNotFoundException, Exception,
			IOException
	{
		String projectName = projectDirectory.getName();
		createProjectZipFile(destination, projectName, projectDirectory);
	}

	static void addTreeToZip(ZipOutputStream out, String prefix,
			File parentDirectory) throws Exception
	{
		if(prefix.length() > 0)
			prefix = prefix + "/";

		File[] files = parentDirectory.listFiles();
		for(int i = 0; i < files.length; ++i)
		{
			File thisFile = files[i];
			String entryName = prefix + thisFile.getName();
			if(thisFile.isDirectory())
			{
				addTreeToZip(out, entryName, thisFile);
			}
			else
			{
				ZipEntry entry = new ZipEntry(entryName);
				int size = (int) thisFile.length();
				entry.setSize(size);
				out.putNextEntry(entry);
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
