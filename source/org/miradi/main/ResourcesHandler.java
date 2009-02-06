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
package org.miradi.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.martus.util.UnicodeReader;

public class ResourcesHandler
{
	public static void setLocalization(URL urlOfLocalizationZip) throws Exception
	{
		urlOfResourcesZip = urlOfLocalizationZip;
	}
	
	public static void restoreDefaultLocalization()
	{
		urlOfResourcesZip = null;
	}

	public static URL getResourceURL(String resourceFileName) throws RuntimeException
	{
		if(urlOfResourcesZip == null)
			return getEnglishResourceURL(resourceFileName);
		
		return getTranslatedResourceURL(resourceFileName);
	}
	
	public static URL getEnglishResourceURL(String resourceFileName) throws RuntimeException
	{
		if(!resourceFileName.startsWith("/"))
			resourceFileName = RESOURCES_PATH + resourceFileName;
		
		try
		{
			File fileInMagicDirectory = new File(EAM.getHomeDirectory(), EAM.EXTERNAL_RESOURCE_DIRECTORY_NAME + resourceFileName);
			if(fileInMagicDirectory.exists())
				return fileInMagicDirectory.toURI().toURL();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}

		Class thisClass = ResourcesHandler.class;
		URL url = thisClass.getResource(resourceFileName);
		return url;
	}
	
	private static URL getTranslatedResourceURL(String resourceFileName) throws RuntimeException
	{
		try
		{
			if(!resourceFileName.startsWith("/"))
				resourceFileName = RESOURCES_PATH + resourceFileName;
			return new URL("jar:" + urlOfResourcesZip.toExternalForm() + "!" + resourceFileName);
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
		
		return loadFile(url);
	}

	public static String loadFile(URL url) throws IOException
	{
		InputStream inputStream = url.openStream();
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			String result = reader.readAll();
			return result;
		}
		finally
		{
			reader.close();
		}
	}

	public static URL getResourceURLWithoutExceptions(String resourceFileName) 
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
	
	public static String RESOURCES_PATH = "/resources/";

	private static URL urlOfResourcesZip;

}
