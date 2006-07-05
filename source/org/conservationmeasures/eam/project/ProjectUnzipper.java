package org.conservationmeasures.eam.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.conservationmeasures.eam.main.EAM;

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
	
	public static File unzipToTempDirectory(File zipFile) throws IOException
	{
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
		File directory = File.createTempFile("eAM", null);
		directory.delete();
		directory.mkdir();
		unzip(zipIn, directory);
		return directory;
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
				extractOneFile(zipInput, destinationDirectory, entry);
				zipInput.closeEntry();
			}
		}
		finally
		{
			zipInput.close();
		}
	}

	private static void extractOneFile(ZipInputStream zipInput, File destinationDirectory, ZipEntry entry) throws FileNotFoundException, IOException
	{
		if(entry.isDirectory())
			return;
		
		long entryLength = entry.getSize();
		File file = new File(destinationDirectory, entry.getName());
		file.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[512];
		int got = -1;
		while( (got = zipInput.read(buffer)) > 0)
		{
			// TODO: Optimize by reading entire file at once?
			out.write(buffer, 0, got);
		}
		out.flush();	// shouldn't be needed but all the examples have it!
		out.close();
		long fileLength = file.length();
		if(entryLength >= 0 && entryLength != fileLength)
			throw new RuntimeException("Uncompressed wrong number of bytes for " + file + ", expected " + entryLength + " but was " + fileLength);
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
