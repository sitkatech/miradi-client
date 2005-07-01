/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class EAM
{
	public static void main(String[] args) throws Exception
	{
		setBestLookAndFeel();

		JFrame mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
	
	static void setBestLookAndFeel() throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
