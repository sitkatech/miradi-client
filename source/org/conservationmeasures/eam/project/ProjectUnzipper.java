package org.conservationmeasures.eam.project;

import java.io.IOException;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ProjectUnzipper
{
	public static boolean IsZipFileImportable(ZipInputStream zipInput) throws IOException
	{
		HashSet topLevelDirectories = new HashSet();

		while(true)
		{
			ZipEntry entry = zipInput.getNextEntry();
			if(entry == null)
				break;
			String name = entry.getName();
			if(isTopLevelFile(name))
				return false;
			if(hasLeadingSlash(name))
				return false;
			
			String topLevelDirectory = name.substring(0, findSlash(name));
			topLevelDirectories.add(topLevelDirectory);

		}
		
		if(topLevelDirectories.size() != 1)
			return false;
		
		return true;
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
