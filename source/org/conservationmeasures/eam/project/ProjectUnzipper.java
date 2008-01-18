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
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.martus.util.DirectoryUtils;

public class ProjectUnzipper
{
	public static boolean isZipFileImportable(File zipFile) throws IOException
	{
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
		return isZipFileImportable(zipIn);
	}
	
	public static boolean isZipFileImportable(ZipInputStream zipInput) throws IOException
	{
		HashSet topLevelDirectories = new HashSet();

		try
		{
			while(true)
			{
				ZipEntry entry = zipInput.getNextEntry();
				if(entry == null)
					break;
				String name = entry.getName();
				if(isTopLevelFile(name))
				{
					EAM.logDebug("ProjectUnzipper found file at top level");
					return false;
				}
				if(hasLeadingSlash(name))
				{
					EAM.logDebug("ProjectUnzipper found leading slash");
					return false;
				}
				
				String topLevelDirectory = name.substring(0, findSlash(name));
				topLevelDirectories.add(topLevelDirectory);

			}
			
			if(topLevelDirectories.size() != 1)
			{
				EAM.logDebug("ProjectUnzipper didn't find exactly one top-level directory");
				return false;
			}
			
			return true;
		}
		finally
		{
			zipInput.close();
		}
	}
	

	public static void unzipToProjectDirectory(File zipFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		if(!Project.isValidProjectFilename(newProjectFilename))
			throw new Exception("Illegal project name: " + newProjectFilename);
		
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
		
		File tempHomeDir = createTempDirectory("$$$"+newProjectFilename);

		File tempProjectDirectory = new File(tempHomeDir, newProjectFilename);

		tempProjectDirectory.mkdir();
		
		try 
		{
			unzip(zipIn, tempProjectDirectory);
			validateAndCopyProject(homeDirectory, newProjectFilename, tempProjectDirectory);
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempHomeDir);
		}

	}

	private static void validateAndCopyProject(File homeDirectory, String newProjectFilename, File tempProjectDirectory) throws Exception
	{
		// TODO: Find a better test for whether or not the import failed? 
		if (ProjectServer.isExistingProject(tempProjectDirectory))
		{
			File destinationProjectDirectory = new File(homeDirectory,newProjectFilename);
			DirectoryUtils.copyDirectoryTree(tempProjectDirectory, destinationProjectDirectory);
			return;
		}
		
		throw new Exception("Illegal project: " + newProjectFilename);
	}
	
	
	private static File createTempDirectory(String name) throws IOException
	{
		File dir = File.createTempFile(name, null);
		dir.delete();
		dir.mkdirs();
		return dir;
	}
	
	public static void unzip(ZipInputStream zipInput, File destinationDirectory) throws IOException
	{
		try
		{
			while(true)
			{
				ZipEntry entry = zipInput.getNextEntry();
				if(entry == null)
					break;
				String relativeFilePath = entry.getName();
				int slashAt = findSlash(relativeFilePath);
				relativeFilePath = relativeFilePath.substring(slashAt + 1);
				File file = new File(destinationDirectory, relativeFilePath);
				extractOneFile(zipInput, file, entry);
				zipInput.closeEntry();
			}
		}
		finally
		{
			zipInput.close();
		}
	}

	private static void extractOneFile(ZipInputStream zipInput, File destinationFile, ZipEntry entry) throws FileNotFoundException, IOException
	{
		//TODO: This code skips the higher level (first level) directory(ies) ; there should be a better way to express this
		if(entry.isDirectory())
			return;
		
		destinationFile.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(destinationFile);
		byte[] buffer = new byte[512];
		int got = -1;
		while( (got = zipInput.read(buffer)) > 0)
		{
			// TODO: Optimize by reading entire file at once?
			out.write(buffer, 0, got);
		}
		out.close();
	}

	private static int findSlash(String name)
	{
		return name.indexOf('/');
	}

	private static boolean isTopLevelFile(String name)
	{
		return findSlash(name) < 0;
	}

	private static boolean hasLeadingSlash(String name)
	{
		return findSlash(name) == 0;
	}
	
}
