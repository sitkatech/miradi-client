/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.utils.Translation;

public class EAM
{
	public static void main(String[] args)
	{
		setLogLevel(LOG_DEBUG);
		try
		{
			setBestLookAndFeel();
			VersionConstants.setVersionString();
	
			JFrame mainWindow = new MainWindow();
			mainWindow.setVisible(true);
		}
		catch(Exception e)
		{
			logException(e);
		}
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
	
	public static void setLogLevel(int level)
	{
		Logging.setLogLevel(level);
	}
	
	public static void logException(Exception e)
	{
		Logging.logException(e);
	}
	
	public static void logError(String text)
	{
		Logging.logError(text);
	}
	
	public static void logWarning(String text)
	{
		Logging.logWarning(text);
	}
	
	public static void logDebug(String text)
	{
		Logging.logDebug(text);
	}
	
	public static void logVerbose(String text)
	{
		Logging.logVerbose(text);
	}
	
	public static final int LOG_QUIET = Logging.LOG_QUIET;
	public static final int LOG_NORMAL = Logging.LOG_NORMAL;
	public static final int LOG_DEBUG = Logging.LOG_DEBUG;
	public static final int LOG_VERBOSE = Logging.LOG_VERBOSE;
	

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
