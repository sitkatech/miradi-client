/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.UIManager;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.utils.Translation;
import org.martus.swing.UiNotifyDlg;
import org.martus.util.UnicodeReader;

public class EAM
{
	public static void main(String[] args)
	{
		if(!handleEamToMiradiMigration())
			return;
		
		setLogLevel(LOG_DEBUG);
		if(Arrays.asList(args).contains("--verbose"))
			setLogLevel(LOG_VERBOSE);
		setExceptionLoggingDestination();
		
		try
		{
			setBestLookAndFeel();
			VersionConstants.setVersionString();
			Translation.loadFieldLabels();
	
			mainWindow = new MainWindow();
			mainWindow.start(args);
		}
		catch(Exception e)
		{
			logException(e);
			errorDialog(e.getMessage());
		}
	}
	
	public static String getJavaVersion()
	{
		return System.getProperty("java.version");
	}
	
	static void setBestLookAndFeel() throws Exception
	{
		if(System.getProperty("os.name").equals("Linux"))
			return;
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

	public static File getHomeDirectory()
	{
		File home = new File(System.getProperty("user.home"), "Miradi");
		return home;
	}
	
	public static File getOldEamHomeDirectory()
	{
		File home = new File(System.getProperty("user.home"), "eAM");
		return home;
	}
	
	public static void setExceptionLoggingDestination()
	{
		try
		{
			File destination = new File(getHomeDirectory(), "exceptions.log");
			Logging.setExceptionLoggingDestination(new PrintStream(new FileOutputStream(destination)));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Unable to create exception logging file: " + e.getLocalizedMessage());
		}

	}
	
	private static boolean handleEamToMiradiMigration()
	{
		File miradiDirectory = EAM.getHomeDirectory();
		if(miradiDirectory.exists())
			return true;
		
		File oldEamDirectory = EAM.getOldEamHomeDirectory();
		if(!oldEamDirectory.exists())
			return true;
		
		String[] miradiMigrationText = {
			"Miradi has detected some e-Adaptive Management ",
			"projects and settings on this computer, which can ",
			"automatically be imported into Miradi.",
			"",
			"If you want to run Miradi without performing this migration, ",
			"delete the e-Adaptive Management project directory ",
			"(" + oldEamDirectory + "), or rename it to something else",
			"",
			"Do you want to Import the old data, or Exit Miradi?",
			"",
		};
		if(!EAM.confirmDialog("e-Adaptive Management Data Import", miradiMigrationText, new String[] {"Import", "Exit"}))
			return false;
		
		oldEamDirectory.renameTo(miradiDirectory);
		return true;
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

	public static String fieldLabel(int objectType, String fieldTag)
	{
		return Translation.fieldLabel(objectType, fieldTag);
	}
	
	///////////////////////////////////////////////////////////////////
	// Dialogs

	public static void errorDialog(String errorMessage)
	{
		okDialog("Error", new String[] {errorMessage});
	}
	
	public static void notifyDialog(String text)
	{
		okDialog("Information", new String[] {text});
	}

	public static void okDialog(String title, String[] body)
	{
		new UiNotifyDlg(mainWindow, title, body, new String[] {text("Button|OK")});
	}

	public static boolean confirmDialog(String title, String[] body)
	{
		String[] buttons = { text("Button|Overwrite"), text("Button|Cancel") };
		return confirmDialog(title, body, buttons);
	}

	public static boolean confirmDialog(String title, String[] body, String[] buttons)
	{
		UiNotifyDlg dlg = new UiNotifyDlg(mainWindow, title, body, buttons);
		if (wasWindowClosed(dlg))
			return false;
		
		return (dlg.getResult().equals(buttons[0]));
	}

	private static boolean wasWindowClosed(UiNotifyDlg dlg)
	{
		return dlg.getResult() == null;
	}


	public static String loadResourceFile(Class thisClass, String resourceFileName) throws IOException
	{
		URL htmlFile = thisClass.getResource(resourceFileName);
		UnicodeReader reader = new UnicodeReader(htmlFile.openStream());
		try
		{
			return reader.readAll();
		}
		finally
		{
			reader.close();
		}
	}
	///////////////////////////////////////////////////////////////////
	
	public static int STANDARD_SCROLL_INCREMENT = 12;

	public static String NEWLINE = System.getProperty("line.separator");
	public static MainWindow mainWindow;
	public static String PROJECT_EXTENSION = ".eam";
	public static final Color READONLY_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static final Color READONLY_FOREGROUND_COLOR = Color.black;
	public static final Color EDITABLE_BACKGROUND_COLOR = Color.WHITE;
	public static final Color EDITABLE_FOREGROUND_COLOR = Color.BLUE;
	
	public static final ORef WORKPLAN_STRATEGY_ROOT = new ORef(ObjectType.FAKE, new BaseId(1));
	public static final ORef WORKPLAN_MONITORING_ROOT = new ORef(ObjectType.FAKE, new BaseId(2));


	
}
