/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.util.Locale;

import javax.swing.JFrame;

public class EAM
{
	public static void main(String[] args)
	{
		JFrame mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
	
	///////////////////////////////////////////////////////////////////
	// Logging
	public static void setLogToString()
	{
		Logging.setLogToString();
	}
	
	public static void setLogToConsole()
	{
		Logging.setLogToConsole();
	}
	
	public static String getLoggedString()
	{
		return Logging.getLoggedString();
	}
	
	public static void logWarning(String text)
	{
		Logging.logWarning(text);
	}
	

	///////////////////////////////////////////////////////////////////
	// Translations
	public static void setTranslationLocale(Locale locale)
	{
		Translation.setTranslationLocale(locale);
	}

	public static String text(String key)
	{
		return Translation.text(key);
	}

	
	///////////////////////////////////////////////////////////////////

	public static String NEWLINE = System.getProperty("line.separator");
}
