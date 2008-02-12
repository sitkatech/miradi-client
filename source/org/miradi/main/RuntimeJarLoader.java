/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main;

import java.io.File;

public class RuntimeJarLoader
{
	public static void addJarsInSubdirectoryToClasspath(File thirdPartyDirectory) throws Exception
	{
		if(!thirdPartyDirectory.exists())
			return;

		addJarsToClasspath(thirdPartyDirectory);
	}

	private static void addJarsToClasspath(File jarDirectory) throws Exception
	{
		File[] jars = jarDirectory.listFiles();
		for(int i = 0; i < jars.length; ++i)
		{
			File jarFile = jars[i];
			if(!jarFile.exists())
				throw new RuntimeException("Cannot find: " + jarFile);
			ClassPathHacker.addFile(jarFile);
		}
	}

}
