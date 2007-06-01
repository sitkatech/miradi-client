/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.MiradiLogger;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
import org.conservationmeasures.eam.utils.Translation;
import org.martus.swing.UiNotifyDlg;
import org.martus.util.UnicodeReader;

public class EAM
{

	// MUST be positioned before main() so it gets initialized first
	private static MiradiLogger logger = new MiradiLogger();

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
			SwingUtilities.invokeAndWait(new MainWindowRunner(args));
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
			logger.setExceptionLoggingDestination(new PrintStream(new FileOutputStream(destination)));
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
		if(oldEamDirectory.exists() || !miradiDirectory.exists())
		{
			EAM.errorDialog("Import failed. Be sure no projects are open, and that you " +
					"have permission to create " + miradiDirectory.getAbsolutePath());
			return false;
		}
		
		String[] importCompleteText = {
			"Import complete.",
			"",
			"We strongly recommend that you uninstall e-Adaptive Management, ",
			"if you have not already done so. It is now obsolete, having been ",
			"replaced by Miradi.",
		};
		EAM.okDialog("Import Complete", importCompleteText);
		return true;
	}
	
	///////////////////////////////////////////////////////////////////
	// Logging
	public static void setLogToString()
	{
		logger.setLogToString();
	}
	
	public static void setLogToConsole()
	{
		logger.setLogToConsole();
	}
	
	public static String getLoggedString()
	{
		return logger.getLoggedString();
	}
	
	public static void setLogLevel(int level)
	{
		logger.setLogLevel(level);
	}
	
	public static void logException(Exception e)
	{
		logger.logException(e);
	}
	
	public static void logError(String text)
	{
		logger.logError(text);
	}
	
	public static void logWarning(String text)
	{
		logger.logWarning(text);
	}
	
	public static void logDebug(String text)
	{
		logger.logDebug(text);
	}
	
	public static void logVerbose(String text)
	{
		logger.logVerbose(text);
	}
	
	private static final class MainWindowRunner implements Runnable
	{
		MainWindowRunner(String[] argsToUse)
		{
			args = argsToUse;
		}
		
		public void run()
		{
			try
			{
				mainWindow = new MainWindow();
				mainWindow.start(args);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				errorDialog("Unexpected error: " + e.getMessage());
			}
		}
		
		String[] args;
	}
	
	public static final int LOG_QUIET = MiradiLogger.LOG_QUIET;
	public static final int LOG_NORMAL = MiradiLogger.LOG_NORMAL;
	public static final int LOG_DEBUG = MiradiLogger.LOG_DEBUG;
	public static final int LOG_VERBOSE = MiradiLogger.LOG_VERBOSE;
	

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



	public static String loadResourceFile(Class thisClass, String resourceFileName) throws Exception
	{
		URL url = getResourceURL(thisClass, resourceFileName);
		
		InputStream inputStream = url.openStream();
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			return reader.readAll();
		}
		finally
		{
			reader.close();
		}
	}

	public static String getResourcePath(Class thisClass, String resourceFileName) throws Exception
	{
		URL url = getResourceURL(thisClass, resourceFileName);
		return url.getPath();
	}


	public static URL getResourceURL(Class thisClass, String resourceFileName) throws MalformedURLException
	{
		URL url = thisClass.getResource(resourceFileName);

		if (doesTestDirectoryExist())
		{
			final String relativePackagePath = convertToPath(thisClass.getPackage().getName());
			String relativePath = new File(relativePackagePath, resourceFileName).getPath();
			url = findAlternateResource(relativePath, url);
		}
		return url;
	}

	public static String convertToPath(String path)
	{
		return path.replace('.', File.separatorChar);
	}
	
	public static URL loadResourceImageFile(String resourceFileName) 
	{
		try
		{
			// TODO: There should be a cleaner way to do this:
			URL url = MiradiResourceImageIcon.class.getClassLoader().getResource(resourceFileName);

			if (doesTestDirectoryExist())
			{
				url = findAlternateResource(resourceFileName, url);
			}
			
			return url;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}
	

	private static URL findAlternateResource(String relativePath, URL url) throws MalformedURLException
	{
		File newLoadPath = getAlternateDirectory(relativePath);
		if (newLoadPath.exists())
		{
			 return newLoadPath.toURL();
		}
		EAM.logVerbose("File not found in external resource directory directory:" + newLoadPath);
		return url;
	}

	private static File getAlternateDirectory(String relativePath)
	{
		File home = EAM.getHomeDirectory();
		File testDirectory = new File(home,EXTERNAL_RESOURCE_DIRECTORY_NAME);
		return new File(testDirectory,relativePath);
	}
	
	
	private static boolean doesTestDirectoryExist()
	{
		return new File(EAM.getHomeDirectory(),EXTERNAL_RESOURCE_DIRECTORY_NAME).exists();
	}

	///////////////////////////////////////////////////////////////////
	
	private final static String EXTERNAL_RESOURCE_DIRECTORY_NAME = "ExternalResourceDirectory";
	
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



