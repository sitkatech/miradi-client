/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.conservationmeasures.eam.utils.Translation;


public class Miradi
{
	public static void main(String[] args)
	{
		EAM.initialize();
		try
		{
			addThirdPartyJarsToClasspath();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error initializing Miradi");
			System.exit(1);
		}

		if(!Miradi.handleEamToMiradiMigration())
			return;
		
		Miradi.start(args);
	}

	private static void addThirdPartyJarsToClasspath() throws Exception
	{
		String jarSubdirectoryName = "ThirdParty";
		File miradiDirectory = getAppCodeDirectory();
		EAM.logVerbose("Miradi code running from: " + miradiDirectory.getAbsolutePath());
		File thirdPartyDirectory = new File(miradiDirectory, jarSubdirectoryName);
		EAM.logVerbose("Adding jars to classpath: " + thirdPartyDirectory.getAbsolutePath());
		RuntimeJarLoader.addJarsInSubdirectoryToClasspath(thirdPartyDirectory);
	}
	
	private static File getAppCodeDirectory() throws URISyntaxException
	{
		String imagesURIString = Miradi.class.getResource("/").toURI().toString();
		String imagesPathString = stripPrefix(imagesURIString);
	
		int bangAt = imagesPathString.indexOf('!');
		if(bangAt < 0)
			return new File(stripPrefix(imagesURIString));
		
		String jarURIString = imagesPathString.substring(0, bangAt);
		return new File(stripPrefix(jarURIString));
	}

	private static String stripPrefix(String uri)
	{
		int startOfRealPath = uri.indexOf(':') + 1;
		return uri.substring(startOfRealPath);
	}

	static boolean handleEamToMiradiMigration()
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

	public static void start(String[] args)
	{
		EAM.setLogLevel(EAM.LOG_DEBUG);
		if(Arrays.asList(args).contains("--verbose"))
			EAM.setLogLevel(EAM.LOG_VERBOSE);
		EAM.setExceptionLoggingDestination();
		
		try
		{
			setBestLookAndFeel();
			VersionConstants.setVersionString();
			Translation.loadFieldLabels();
			SwingUtilities.invokeAndWait(new MainWindowRunner(args));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(e.getMessage());
		}
	}

	static void setBestLookAndFeel() throws Exception
	{
		if(System.getProperty("os.name").equals("Linux"))
			return;
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
				EAM.setMainWindow(new MainWindow());
				EAM.getMainWindow().start(args);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Unexpected error: " + e.getMessage());
				System.exit(1);
			}
		}
		
		String[] args;
	}

}
