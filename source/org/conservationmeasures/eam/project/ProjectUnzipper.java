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
			int slashAt = name.indexOf('/');
			if(slashAt == 0 )
				return false;
			if(slashAt > 0)
			{
				String directory = name.substring(0, slashAt);
				topLevelDirectories.add(directory);
			}
		}
		
		if(topLevelDirectories.size() != 1)
			return false;
		
		return true;
	}
	
}
