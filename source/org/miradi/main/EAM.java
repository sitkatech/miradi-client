/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.martus.swing.UiNotifyDlg;
import org.martus.util.UnicodeReader;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.resources.ResourcesHandler;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.utils.MiradiLogger;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.utils.Translation;

public class EAM
{
	// NOTE: This MUST be the first thing in the class so it is initialized first!
	private static MiradiLogger logger = new MiradiLogger();

	public static boolean initializeHomeDirectory()
	{
		alertIfHomeIsNotOnC();
		
		File preferredHomeDir = getPreferredHomeDirectory();
		
		preferredHomeDir.mkdirs();
		if (!preferredHomeDir.exists() || !preferredHomeDir.isDirectory())
		{	
			displayHtmlDialog("NoHomeDirectoryFoundMessage.html","@DIRECTORY_NAME@", preferredHomeDir.getAbsolutePath());
			return true;
		}

		if(!EAM.handleEamToMiradiMigration())
			return false;
		
		if(!EAM.handleMigrationToDocumentsDirectory())
			return false;
		
		return true;
	}

	public static String getJavaVersion()
	{
		return System.getProperty("java.version");
	}
	
	public static File getHomeDirectory()
	{
		File preferredHomeDir = getPreferredHomeDirectory();
		if (preferredHomeDir != null)
			return preferredHomeDir;
		
		File defaultHomeDirectory = getDefaultHomeDirectory();
		Preferences.userNodeForPackage(Miradi.class).put(EAM.MIRADI_DATA_DIRECTORY_KEY, defaultHomeDirectory.getAbsolutePath());
		
		return defaultHomeDirectory;
	}

	private static void alertIfHomeIsNotOnC()
	{
		if (!Miradi.isWindows())
			return;
		
		String homeDir = getHomeDirectory().getAbsolutePath();
		if (homeDir.contains("C:\\"))
			return;
		
		displayHtmlDialog("NoWindowsDataLocalDataLocationMessage.html", "@DIRECTORY_NAME@", homeDir);
	}

	private static void displayHtmlDialog(String htmlFileName, String findToReplace,  String replacementForStr1)
	{
		try
		{
			String html = EAM.loadResourceFile(ResourcesHandler.class, htmlFileName);
			html = html.replace(findToReplace, replacementForStr1);
			HtmlViewPanel htmlViwer = new HtmlViewPanel(getMainWindow(), EAM.text("Warning"), html, null);
			htmlViwer.showAsOkDialog();
		}
		catch (Exception e)
		{
			logException(e);
		}
	}

	private static File getPreferredHomeDirectory()
	{
		String preferredHomeDirAsString = Preferences.userNodeForPackage(Miradi.class).get(MIRADI_DATA_DIRECTORY_KEY, "");
		if (preferredHomeDirAsString == null || preferredHomeDirAsString.length() == 0)
			return null;
		
		return new File(preferredHomeDirAsString);
	}

	private static File getDefaultHomeDirectory()
	{
		return new File(FileSystemView.getFileSystemView().getDefaultDirectory(), "Miradi");
	}
	
	public static File getOldMiradiHomeDirectory()
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
			File destination = getDefaultExceptionsLogFile();
			FileOutputStream outputStream = new FileOutputStream(destination);
			setExceptionLoggingDestination(outputStream);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Unable to create exception logging file: " + e.getLocalizedMessage());
		}

	}

	private static File getDefaultExceptionsLogFile()
	{
		return new File(getHomeDirectory(), "exceptions.log");
	}

	public static void setExceptionLoggingDestination(OutputStream outputStream)
	{
		PrintStream printStream = new PrintStream(outputStream);
		setExceptionLoggingDestination(printStream);
	}


	public static void setExceptionLoggingDestination(PrintStream printStream)
	{
		logger.setExceptionLoggingDestination(printStream);
	}
	
	public static PrintStream getExceptionLoggingDestination()
	{
		return logger.getExceptionLoggingDestination();
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
		Toolkit.getDefaultToolkit().beep();
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
	
	public static void logStackTrace()
	{
		try
		{
			throw new Exception();
		}
		catch(Exception e)
		{
			logger.logException(e);
		}
	}

	public static void logVerbose(String text)
	{
		logger.logVerbose(text);
	}
	
	public static void internalError(String notificationText)
	{
		try
		{
			logError(notificationText);
			throw new Exception(notificationText);
		}
		catch (Exception e)
		{
			logException(e);
			notifyDialog(convertExceptionToString(e));
		}
	}

	public static String convertExceptionToString(Exception exceptionToConvert)
	{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
		exceptionToConvert.printStackTrace(printWriter);
		printWriter.close();

		return byteArrayOutputStream.toString();
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

	public static void panic(Exception e)
	{
		logException(e);
		errorDialog(EAM.text("An unexpected error occurred: " + e.getMessage() +
							 "\n\nPlease report this to the Miradi support team, " +
							 "ideally including the contents of this file: " +
							 "\n\n   " + getDefaultExceptionsLogFile().getAbsolutePath() + 
							 "\n\nMiradi has attempted to save your latest changes, and will now exit."));
		System.exit(0);
	}
	
	public static void errorDialog(String errorMessage)
	{
		JOptionPane.showMessageDialog(getMainWindow(), errorMessage, EAM.text("Wintitle|Error"), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void notifyDialog(String text)
	{
		JOptionPane.showMessageDialog(getMainWindow(), text, EAM.text("Wintitle|Information"), JOptionPane.INFORMATION_MESSAGE);
	}

	public static int confirmDialog(String title, String text, String[] buttonLabels)
	{
		return JOptionPane.showOptionDialog(getMainWindow(), text, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonLabels, null);
	}

	
	public static void okDialog(String title, String[] body)
	{
		new UiNotifyDlg(getMainWindow(), title, body, new String[] {text("Button|OK")});
	}

	public static boolean confirmDialog(String title, String[] body)
	{
		String[] buttons = { text("Button|Overwrite"), getCancelButtonText() };
		return confirmDialog(title, body, buttons);
	}

	public static String getCancelButtonText()
	{
		return text("Button|Cancel");
	}

	public static boolean confirmDialog(String title, String[] body, String[] buttons)
	{
		UiNotifyDlg dlg = new UiNotifyDlg(getMainWindow(), title, body, buttons);
		if (wasWindowClosed(dlg))
			return false;
		
		return (dlg.getResult().equals(buttons[0]));
	}
	
	public static boolean confirmDeletRetainDialog(String[] body)
	{
		String[] buttons = {EAM.text("Delete"), EAM.text("Retain"), };
		return EAM.confirmDialog(EAM.text("Delete"), body, buttons);
	}
	
	public static String choiceDialog(String title, String[] body, String[] buttons)
	{
		UiNotifyDlg dlg = new UiNotifyDlg(getMainWindow(), title, body, buttons);
		if (wasWindowClosed(dlg))
			return "";
		
		return (dlg.getResult());		
	}

	private static boolean wasWindowClosed(UiNotifyDlg dlg)
	{
		return dlg.getResult() == null;
	}



	public static String loadResourceFile(Class thisClass, String resourceFileName) throws Exception
	{
		URL url = getResourceURL(thisClass, resourceFileName);
		if(url == null)
			EAM.logError("Unable to find resource: " + thisClass.getPackage().getName() + ":" + resourceFileName);
		
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
			return newLoadPath.toURI().toURL();
		}
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
	
	public static void setMainWindow(MainWindow mainWindow)
	{
		EAM.mainWindow = mainWindow;
	}


	public static MainWindow getMainWindow()
	{
		return mainWindow;
	}

	static boolean handleMigrationToDocumentsDirectory()
	{
		File oldDirectory = getOldMiradiHomeDirectory();
		File newDirectory = getHomeDirectory();
		if(oldDirectory.equals(newDirectory))
			return true;
		
		if(!oldDirectory.exists())
			return true;
		
		if(!oldDirectory.isDirectory())
			return true;
		
		if(newDirectory.exists())
		{
			if(newDirectory.isDirectory())
				return true;
			
			EAM.errorDialog("<html>" +
					"Miradi cannot run because there a file exists where " +
					"its data directory should be:" +
					"<br>" + newDirectory.getAbsolutePath());
			return false;
		}
		
		EAM.logWarning("Migrating " + oldDirectory.getAbsolutePath() + " to " + newDirectory.getAbsolutePath());
		try
		{
			boolean worked = oldDirectory.renameTo(newDirectory);
			if(worked)
				worked = !(oldDirectory.exists());
			if(worked)
				worked = (newDirectory.exists());
			
			if(worked)
				return true;
			
			EAM.errorDialog("<html>" +
					"Miradi was unable to move existing projects from " +
					"<br>" + oldDirectory.getAbsolutePath() + 
					"<br> to " +
					"<br>" + newDirectory.getAbsolutePath() + 
					"<br>Please contact Miradi support for assistance in resolving this problem.");
			return false;
		}
		catch(Exception e)
		{
			EAM.panic(e);
			return false;
		}
		
	}
	
	static boolean handleEamToMiradiMigration()
	{
		File miradiDirectory = getHomeDirectory();
		if(miradiDirectory.exists())
			return true;
		
		File oldEamDirectory = getOldEamHomeDirectory();
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
		if(!confirmDialog("e-Adaptive Management Data Import", miradiMigrationText, new String[] {"Import", "Exit"}))
			return false;
		
		oldEamDirectory.renameTo(miradiDirectory);
		if(oldEamDirectory.exists() || !miradiDirectory.exists())
		{
			errorDialog("Import failed. Be sure no projects are open, and that you " +
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
		okDialog("Import Complete", importCompleteText);
		return true;
	}
	
	public static void possiblyLogTooLowInitialMemory()
	{
		long maxMemory = Runtime.getRuntime().maxMemory();
		if (maxMemory < 100000000)
			logWarning(text("It appears that Miradi was launched without the -Xmx512m switch. As a result, certain operations like Reports may run out of memory."));    
	}

	public final static String EXTERNAL_RESOURCE_DIRECTORY_NAME = "ExternalResourceDirectory";
	
	public static int STANDARD_SCROLL_INCREMENT = 12;

	public static String NEWLINE = System.getProperty("line.separator");
	private static MainWindow mainWindow;
	public static String PROJECT_EXTENSION = ".eam";
	public static final Color READONLY_BACKGROUND_COLOR = new Color(217, 217, 217);
	public static final Color READONLY_FOREGROUND_COLOR = Color.black;
	public static final Color EDITABLE_BACKGROUND_COLOR = Color.WHITE;
	public static final Color EDITABLE_FOREGROUND_COLOR = Color.BLUE;
	
	public static final ORef WORKPLAN_STRATEGY_ROOT = new ORef(ObjectType.FAKE, new BaseId(1));
	public static final ORef WORKPLAN_MONITORING_ROOT = new ORef(ObjectType.FAKE, new BaseId(2));
	
	public static final String MIRADI_DATA_DIRECTORY_KEY = "MiradiDataDirectory";
}



