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
package org.miradi.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.miradi.main.EAM;
import org.miradi.utils.MpzFileFilterForChooserDialog;

public class ProjectZipper
{
	public static void createProjectZipFile(File destination, String zipTopLevelDirectory, File projectDirectory) throws FileNotFoundException, Exception, IOException
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destination));
		addTreeToZip(out, zipTopLevelDirectory, projectDirectory);
		out.close();
	}

	public static void createProjectZipFile(File destination, File projectDirectory) throws FileNotFoundException, Exception, IOException
	{
		String projectName = projectDirectory.getName();
		createProjectZipFile(destination, projectName, projectDirectory);
	}

	public static void addTreeToZip(ZipOutputStream out, String prefix, File parentDirectory) throws Exception
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
				if (thisFile.getAbsolutePath().endsWith(MpzFileFilterForChooserDialog.EXTENSION))
				{
					EAM.logVerbose("Ignoring zipping file = " + thisFile.getAbsolutePath());
					continue;
				}
				
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
