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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;


public class Translation
{
	public static Locale getTranslationLocale()
	{
		return currentTranslationLocale;
	}
	
	public static void restoreDefaultLocale()
	{
		currentTranslationLocale = Locale.US;
	}

	public static void setTranslationLocale(Locale locale) throws IOException
	{
		currentTranslationLocale = locale;
		textTranslations = loadTextTranslations(locale);
	}

	public static String text(String key)
	{
		if(isDefaultLocale())
			return extractPartToDisplay(key);
	
		return textTranslations.getProperty(key, "<" + key + ">");
	}

	public static void loadFieldLabels() throws IOException
	{
		String fileName = "FieldLabels.properties";
		InputStream in = Translation.class.getResourceAsStream(fileName);
		if(in == null)
			throw new IOException("Missing file: " + fileName + " in " + Translation.class.getName());
		try
		{
			Properties properties = fieldLabelTranslations;
			properties.load(in);
		}
		finally
		{
			in.close();
		}
		
	}
	
	private static Properties loadTextTranslations(Locale locale) throws IOException
	{
		if(isDefaultLocale())
			return null;
		
		String resourceName = "/translations/" + locale.getLanguage() + "/miradi.properties";
		URL url = Translation.class.getResource(resourceName);
		if(url == null)
			throw new IOException("Translations not found: " + resourceName);
		
		return loadProperties(url);
	}

	public static Properties loadProperties(URL url) throws IOException
	{
		InputStream in = url.openStream();
		try
		{
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		}
		finally
		{
			in.close();
		}
		
	}
	public static String fieldLabel(int objectType, String fieldTag)
	{
		String fullTag = Integer.toString(objectType) + "." + fieldTag;
		String label = fieldLabelTranslations.getProperty(fullTag);
		if(label == null)
			label = fieldTag;
		return label;
	}

	public static String extractPartToDisplay(String result)
	{
		int lastBar = result.lastIndexOf('|');
		if(lastBar >= 0)
			result = result.substring(lastBar + 1);
	
		return result;
	}

	private static boolean isDefaultLocale()
	{
		return currentTranslationLocale.equals(Locale.US);
	}
	
	private static Properties textTranslations;
	private static Properties fieldLabelTranslations = new Properties();
	private static Locale currentTranslationLocale = Locale.US;
}
