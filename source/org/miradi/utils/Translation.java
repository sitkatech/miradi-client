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
package org.miradi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Translation
{
	public static void initialize() throws Exception
	{
		restoreDefaultLocalization();
	}
	
	public static void restoreDefaultLocalization() throws IOException
	{
		textTranslations = null;
		fieldLabelTranslations = loadProperties(getTranslationFileURL("FieldLabels.properties"));
	}

	public static void setLocalization(URL urlOfLocalizationZip) throws Exception
	{
		ZipFile zip = new ZipFile(new File(urlOfLocalizationZip.toURI()));
		try
		{
			textTranslations = loadPropertiesFile(zip, "LocalizedText.properties");
			fieldLabelTranslations = loadPropertiesFile(zip, "FieldLabels.properties");
		}
		finally
		{
			zip.close();
		}
	}

	public static String text(String key)
	{
		String result = extractPartToDisplay(key);
	
		if(textTranslations != null)
			result = textTranslations.getProperty(key, "~" + key + "~");
		
		return extractPartToDisplay(result);
	}

	public static String fieldLabel(int objectType, String fieldTag)
	{
		String fullTag = Integer.toString(objectType) + "." + fieldTag;
		String label = fieldLabelTranslations.getProperty(fullTag);
		if(label == null)
			label = fieldTag;
		return label;
	}

	private static Properties loadPropertiesFile(ZipFile zip, String entryName) throws IOException
	{
		ZipEntry text = zip.getEntry(entryName);
		InputStream in = zip.getInputStream(text);
		try
		{
			return loadProperties(in);
		}
		finally
		{
			in.close();
		}
	}

	private static URL getTranslationFileURL(String filename) throws IOException
	{
		String resourceName = "/translations/en/" + filename;
		URL url = Translation.class.getResource(resourceName);
		if(url == null)
			throw new IOException("Translations not found: " + resourceName);
		
		return url;
	}

	public static Properties loadProperties(URL url) throws IOException
	{
		InputStream in = url.openStream();
		try
		{
			return loadProperties(in);
		}
		finally
		{
			in.close();
		}
	}

	private static Properties loadProperties(InputStream in) throws IOException
	{
		Properties properties = new Properties();
		properties.load(in);
		return properties;
	}
	
	public static String extractPartToDisplay(String result)
	{
		int lastBar = result.lastIndexOf('|');
		if(lastBar >= 0)
			result = result.substring(lastBar + 1);
	
		return result;
	}

	private static Properties textTranslations;
	private static Properties fieldLabelTranslations;
}
