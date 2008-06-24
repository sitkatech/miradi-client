/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.main;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.martus.util.UnicodeReader;

public class ResourcesHandler
{

	public static URL getResourceURL(String resourceFileName) throws RuntimeException
	{
		if(!resourceFileName.startsWith("/"))
			resourceFileName = "/resources/" + resourceFileName;
		
		Class thisClass = ResourcesHandler.class;
		URL url = thisClass.getResource(resourceFileName);
	
		if (!doesTestDirectoryExist())
			return url;

		try
		{
			final String relativePackagePath = EAM.convertToPath(thisClass.getPackage().getName());
			String relativePath = new File(relativePackagePath, resourceFileName).getPath();
			url = findAlternateResource(relativePath, url);
			return url;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String loadResourceFile(String resourceFileName) throws Exception
	{
		URL url = getResourceURL(resourceFileName);
		if(url == null)
			EAM.logError("Unable to find resource: " + ResourcesHandler.class.getPackage().getName() + ":" + resourceFileName);
		
		InputStream inputStream = url.openStream();
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			return reader.readAll();
		}
		finally
		{
			reader.close();
		}
	}

	public static URL loadResourceImageFile(String resourceFileName) 
	{
		try
		{
			URL url = getResourceURL(resourceFileName);
			
			if(url == null)
				EAM.logWarning("Missing resource: " + resourceFileName);
			return url;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}

	private static URL findAlternateResource(String relativePath, URL url) throws MalformedURLException
	{
		File newLoadPath = getAlternateDirectory(relativePath);
		if (newLoadPath.exists())
		{
			return newLoadPath.toURI().toURL();
		}
		return url;
	}

	private static File getAlternateDirectory(String relativePath)
	{
		File home = EAM.getHomeDirectory();
		File testDirectory = new File(home,EAM.EXTERNAL_RESOURCE_DIRECTORY_NAME);
		return new File(testDirectory,relativePath);
	}

	private static boolean doesTestDirectoryExist()
	{
		return new File(EAM.getHomeDirectory(),EAM.EXTERNAL_RESOURCE_DIRECTORY_NAME).exists();
	}

}
