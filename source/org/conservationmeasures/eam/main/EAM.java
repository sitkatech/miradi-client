/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;

public class EAM
{
	public static void main(String[] args)
	{
		JFrame mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
	
	public static Locale getTranslationLocale()
	{
		return currentTranslationLocale;
	}

	public static void setTranslationLocale(Locale locale)
	{
		currentTranslationLocale = locale;
		currentResourceBundle = getResourceBundle(locale);
		Locale actualLocaleUsed = currentResourceBundle.getLocale();
		if(!locale.equals(actualLocaleUsed))
		{
			logWarning("Requested " + locale + " but fell back to: " + actualLocaleUsed);
		}
	}

	private static ResourceBundle getResourceBundle(Locale locale)
	{
		return ResourceBundle.getBundle("EAM", locale);
	}

	public static String text(String key)
	{
		if(currentTranslationLocale.equals(Locale.US))
			return key;
		
		ResourceBundle resources = getResourceBundle(currentTranslationLocale);
		try
		{
			String result = resources.getString(key);
			if(result.equals(key))
				result = extractPartToDisplay(result);
			
			return result;
		}
		catch(MissingResourceException e)
		{
			//e.printStackTrace();
			logWarning("Unknown translation key: " + key);
			return "<" + key + ">";
		}
	}

	public static String extractPartToDisplay(String result)
	{
		int lastBar = result.lastIndexOf('|');
		if(lastBar >= 0)
			result = result.substring(lastBar + 1);

		return result;
	}
	
	private static void setLogDestination(PrintStream dest)
	{
		logDestination = dest;
	}
	
	public static void setLogToString()
	{
		logContents = new ByteArrayOutputStream();
		setLogDestination(new PrintStream(logContents));
	}
	
	public static void setLogToConsole()
	{
		setLogDestination(System.out);
	}
	
	public static String getLoggedString()
	{
		return logContents.toString();
	}
	
	public static void logWarning(String text)
	{
		logDestination.println("WARNING: " + text);
	}
	
	public static String NEWLINE = System.getProperty("line.separator");

	private static Locale currentTranslationLocale = new Locale("en", "US");
	private static ResourceBundle currentResourceBundle;

	private static PrintStream logDestination = System.out;
	private static ByteArrayOutputStream logContents;
}
