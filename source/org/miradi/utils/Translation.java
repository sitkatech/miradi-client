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
package org.miradi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.martus.util.UnicodeReader;
import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;
import org.miradi.questions.QuestionManager;

public class Translation
{
	public static void initialize() throws Exception
	{
		restoreDefaultLocalization();
	}
	
	public static boolean isDefaultLocalization()
	{
		return textTranslations == null;
	}
	
	public static String getCurrentLanguageCode()
	{
		return currentLanguageCode;
	}
	
	public static void restoreDefaultLocalization() throws IOException
	{
		currentLanguageCode = "en";
		textTranslations = null;
		fieldLabelTranslations = loadProperties(getEnglishTranslationFileURL("FieldLabels.properties"));
	}

	public static void setLocalization(URL urlOfLocalizationZip, String languageCode) throws Exception
	{
		ZipFile zip = new ZipFile(new File(urlOfLocalizationZip.toURI()));
		try
		{
			textTranslations = loadPOFile(zip, "miradi_" + languageCode + ".po");
			QuestionManager.initialize();
			currentLanguageCode = languageCode;
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
		{
			String defaultValue = "~(" + result + ")";
			result = textTranslations.get(key);
			if(result == null)
				result = defaultValue;
		}
		
		return extractPartToDisplay(result);
	}

	public static String fieldLabel(int objectType, String fieldTag)
	{
		String fullTag = Integer.toString(objectType) + "." + fieldTag;
		String label = fieldLabelTranslations.getProperty(fullTag);
		if(label == null)
		{
			EAM.logStackTrace("Could not find tag in fieldLabels file: " + objectType + "." + fieldTag + " = " + fieldTag);
			label = fieldTag;
		}
		if(textTranslations == null || label.length() == 0)
			return label;

		return text("FieldLabel|" + fullTag + "|" + label);
	}
	
	public static String getHtmlContent(String resourceFileName) throws Exception
	{
		URL englishHtmlURL = ResourcesHandler.getEnglishResourceURL(resourceFileName);
		if(englishHtmlURL == null)
			throw new RuntimeException("Missing HTML content: " + resourceFileName);
		
		String englishHtml = ResourcesHandler.loadFile(englishHtmlURL);
		if(textTranslations == null)
			return englishHtml;
		
		String fixedNewLines = englishHtml.replaceAll("\r", "");
		String allOnOneLine = fixedNewLines.replaceAll("\\n", "");
		String withoutComments = allOnOneLine.replaceAll("<!--.*?-->", "");
		
		String key = "html|/resources/" + resourceFileName + "|" + withoutComments;
		String result = text(key);
		if(result.startsWith("~") && result.length() > 100)
			logAnyNearMisses(key);

		return result;
	}

	private static void logAnyNearMisses(String key)
	{
		for(String thisKey : textTranslations.keySet())
		{
			String prefix = extractPrefix(thisKey);
			if(prefix.length() > 0 && key.startsWith(prefix))
			{
				EAM.logDebug("FOUND NEAR MISS: " + prefix);
				EAM.logDebug(key);
				EAM.logDebug(thisKey);
			}
		}
	}

	private static String extractPrefix(String thisKey)
	{
		int stillPartOfPrefix = 0;
		while(true)
		{
			int pipe = thisKey.indexOf('|', stillPartOfPrefix);
			if(pipe < 0)
				break;
			stillPartOfPrefix = pipe + 1;
		}
		String prefix = thisKey.substring(0, stillPartOfPrefix);
		return prefix;
	}

	public static String translateTabDelimited(String prefix, String thisLine)
	{
		if(thisLine.indexOf(TAB_SUBSTITUTE) >= 0)
			throw new RuntimeException("Unexpected " + TAB_SUBSTITUTE + " in: " + thisLine);
		
		if(textTranslations == null)
			return thisLine;
		
		// strip code element
		int firstTabAt = thisLine.indexOf('\t');
		String code = thisLine.substring(0, firstTabAt);
		thisLine = thisLine.substring(firstTabAt + 1);
		
		thisLine = thisLine.replaceAll("\\t", TAB_SUBSTITUTE);
		String translated = textTranslations.get(prefix + thisLine);
		if(translated == null)
			translated = thisLine.replaceAll(TAB_SUBSTITUTE, TAB_SUBSTITUTE + "~");
		thisLine = code + TAB_SUBSTITUTE + translated;
		thisLine = thisLine.replaceAll(TAB_SUBSTITUTE, "\\\t");
		return thisLine;
	}

	private static HashMap<String, String> loadPOFile(ZipFile zip, String entryName) throws IOException
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
		String resourceName = "/resources/" + filename;
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

	private static HashMap<String, String> loadPO(UnicodeReader reader) throws IOException
	{
		HashMap<String, String> properties = new HashMap<String, String>();
		StringBuffer id = new StringBuffer();
		StringBuffer str = new StringBuffer();
		StringBuffer filling = null;
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			
			if(line.startsWith("#"))
				continue;
			
			if(line.startsWith("\"PO-Revision-Date: "))
			{
				int dateStart = line.indexOf(':') + 2;
				String date = line.substring(dateStart);
				properties.put(TRANSLATION_VERSION_KEY, date);
			}
			
			if(line.startsWith("msgid"))
			{
				addPoEntryToHash(properties, id, str);
				
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
		
		addPoEntryToHash(properties, id, str);

		return properties;
	}

	private static void addPoEntryToHash(HashMap<String, String> properties, StringBuffer id, StringBuffer str)
	{
		if(id.length() == 0 || str.length() == 0)
			return;
		
		properties.put(matchPOEscapedCharacters(id), matchPOEscapedCharacters(str));
	}

	private static String matchPOEscapedCharacters(StringBuffer str)
	{
		String value = str.toString();
		value = value.replaceAll("\\\\t", "\t");
		value = value.replaceAll("\\\\\"", "\"");
		value = value.replaceAll("\\\\\\\\", "\\\\");
		return value;
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

	public final static String TAB_SUBSTITUTE = "___";
	public final static String TRANSLATION_VERSION_KEY = "TranslationVersion";

	private static String currentLanguageCode;
	private static HashMap<String, String> textTranslations;
	private static Properties fieldLabelTranslations;
}
