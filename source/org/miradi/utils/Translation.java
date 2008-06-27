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

import org.martus.util.UnicodeReader;
import org.miradi.main.EAM;

public class Translation
{
	public static void initialize() throws Exception
	{
		restoreDefaultLocalization();
	}
	
	public static void restoreDefaultLocalization() throws IOException
	{
		textTranslations = null;
		fieldLabelTranslations = loadProperties(getEnglishTranslationFileURL("FieldLabels.properties"));
	}

	public static void setLocalization(URL urlOfLocalizationZip, String languageCode) throws Exception
	{
		ZipFile zip = new ZipFile(new File(urlOfLocalizationZip.toURI()));
		try
		{
			textTranslations = loadPOFile(zip, "miradi_" + languageCode + ".po");
		}
		catch(IOException e)
		{
			EAM.logException(e);
			throw (e);
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
			result = textTranslations.getProperty(key, "~(" + result + ")");
		
		return extractPartToDisplay(result);
	}

	public static String fieldLabel(int objectType, String fieldTag)
	{
		String fullTag = Integer.toString(objectType) + "." + fieldTag;
		String label = fieldLabelTranslations.getProperty(fullTag);
		if(label == null)
		{
			EAM.logError("Could not find tag in fieldLabels file: type=" + objectType + "  tag=" + fieldTag);
			label = fieldTag;
		}
		if(textTranslations == null)
			return label;

		return text("FieldLabel|" + fullTag + "|" + label);
	}

	private static Properties loadPOFile(ZipFile zip, String entryName) throws IOException
	{
		ZipEntry name = zip.getEntry(entryName);
		if(name == null)
			throw new IOException("Can't find " + entryName + " in " + zip.getName());
		
		UnicodeReader reader = new UnicodeReader(zip.getInputStream(name));
		try
		{
			return loadPO(reader);
		}
		finally
		{
			reader.close();
		}
	}

	private static URL getEnglishTranslationFileURL(String filename) throws IOException
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

	private static Properties loadPO(UnicodeReader reader) throws IOException
	{
		Properties properties = new Properties();
		StringBuffer id = new StringBuffer();
		StringBuffer str = new StringBuffer();
		StringBuffer filling = null;
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			
			if(line.startsWith("msgid"))
			{
				if(id.length() > 0 && str.length() > 0)
					properties.put(id.toString(), str.toString());
				
				id.setLength(0);
				str.setLength(0);
				filling = id;
			}
			else if(line.startsWith("msgstr"))
			{
				filling = str;
			}

			int start = line.indexOf('"');
			int end = line.lastIndexOf('"');
			if(start >= 0 && end >= 0)
			{
				String text = line.substring(start+1, end);
				filling.append(text);
			}
		}
		
		if(id.length() > 0 && str.length() > 0)
			properties.put(id.toString(), str.toString());

		return properties;
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
