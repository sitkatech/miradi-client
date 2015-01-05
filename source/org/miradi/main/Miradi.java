/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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
package org.miradi.main;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.AllLanguagesQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.utils.LanguagePackFileFilter;
import org.miradi.utils.Translation;
import org.miradi.views.umbrella.AbstractProjectImporter;


public class Miradi
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			addThirdPartyJarsToClasspath();
			setBestLookAndFeel();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error initializing Miradi");
			System.exit(1);
		}
		
		if(!EAM.initializeHomeDirectory())
		{
			System.exit(1);
		}

		Translation.initialize();
		
		EAM.setLogLevel(EAM.LOG_VERBOSE);
		EAM.logDebug("Miradi version " + VersionConstants.getVersionAndTimestamp());
		EAM.logDebug("Java version = " + System.getProperty("java.version"));
		EAM.logDebug("Locale = " + Locale.getDefault().toString());
		
		EAM.setLogLevel(EAM.LOG_DEBUG);
		for(int i = 0; i < args.length; ++i)
		{
			String arg = args[i];
			
			if(arg.startsWith("--"))
			{
				processCommandLineSwitch(arg);
			}
			else if(arg.endsWith(".po"))
			{
				poFile = new File(arg);
				args[i] = "--";
				EAM.logDebug("Using PO file translations: " + poFile.getAbsolutePath());
			}
		}

		EAM.setExceptionLoggingDestination();
		EAM.possiblyLogTooLowInitialMemory();
		
		StaticQuestionManager.initialize();

		SpellCheckerManager.initializeSpellChecker();

		Miradi.start(args);
	}

	private static void processCommandLineSwitch(String arg)
	{
		if(arg.equals("--verbose"))
		{
			EAM.setLogLevel(EAM.LOG_VERBOSE);
		}
		else if (arg.equals(ALPHA_TESTER_MODE_ON_SWITCH))
		{
			isAlphaTesterMode = true;
			EAM.logDebug("Alpha tester mode enabled");
		}
		else if(arg.equals("--demo"))
		{
			demoMode = true;
			EAM.logDebug("Demo mode enabled");
		}
		else if(arg.equals("--developer"))
		{
			developerMode = true;
			EAM.logDebug("Developer mode enabled");
		}
		else if(arg.equals("--repair"))
		{
			EAM.logDebug("Repair mode enabled");
		}
		else
		{
			EAM.logDebug("Unrecognized command-line switch: " + arg);
		}
	}

	public static boolean isDemoMode()
	{
		return demoMode;
	}

	public static boolean isDeveloperMode()
	{
		return developerMode;
	}
	
	public static boolean isAlphaTesterMode()
	{
		return isAlphaTesterMode;
	}
	
	///////////////////////////////////////////////////////////////////
	// Translations
	
	public static File getPoFileIfAny()
	{
		return poFile;
	}

	public static void setLocalization(URL urlOfLocalizationZip, String languageCode) throws Exception
	{
		Translation.setLocalization(urlOfLocalizationZip, languageCode);
		ResourcesHandler.setLocalization(urlOfLocalizationZip);
	}

	public static void restoreDefaultLocalization() throws Exception
	{
		Translation.restoreDefaultLocalization();
		ResourcesHandler.restoreDefaultLocalization();
	}

	public static void switchToLanguage(String languageCode) throws Exception
	{
		if(languageCode == null)
		{
			Miradi.restoreDefaultLocalization();
			return;
		}
		
		String jarName = LANGUAGE_PACK_PREFIX + languageCode + ".jar";
		File jarFile = findLanguageJar(jarName);
		EAM.logDebug("Loading language pack: " + jarFile.getAbsolutePath());
		Miradi.setLocalization(jarFile.toURI().toURL(), languageCode);
	}
	
	public static void switchToLanguage(File poFileToLoad) throws Exception
	{
		Translation.setLocalization(poFileToLoad);
		ResourcesHandler.restoreDefaultLocalization();
	}

	public static HashSet<ChoiceItem> getAvailableLanguageChoices() throws Exception
	{
		HashSet<ChoiceItem> results = new HashSet<ChoiceItem>();
		results.addAll(getAvailableLanguageChoices(EAM.getHomeDirectory()));
		results.addAll(getAvailableLanguageChoices(getAppCodeDirectory()));
		return results;
	}
	
	private static Vector<ChoiceItem> getAvailableLanguageChoices(File directory) throws Exception
	{
		EAM.logDebug("Looking for content jars in: " + directory.getAbsolutePath());
		
		AllLanguagesQuestion languages = new AllLanguagesQuestion();
		Vector<ChoiceItem> results = new Vector<ChoiceItem>();
		String[] jarNames = directory.list(new LanguagePackFileFilter());
		if(jarNames == null)
			return results;
		
		for(int i = 0; i < jarNames.length; ++i)
		{
			String[] parts = jarNames[i].split("-");
			String languageCode = parts[2].split("\\.")[0];
			String languageName = languages.lookupLanguageCode(languageCode);
			results.add(new ChoiceItem(languageCode, languageName));
		}
		return results;
	}

	private static File findLanguageJar(String jarName) throws URISyntaxException
	{
		File jarFile = new File(EAM.getHomeDirectory(), jarName);
		if(jarFile.exists())
			return jarFile;

		jarFile = new File(getAppCodeDirectory(), jarName);
		if(jarFile.exists())
			return jarFile;

		EAM.logError("Unable to find content file: " + jarFile.getAbsolutePath());
		System.exit(2);
		return null;
	}
	
	public static void addThirdPartyJarsToClasspath() throws Exception
	{
		String jarSubdirectoryName = "ThirdParty";
		File miradiDirectory = getAppCodeDirectory();
		File thirdPartyDirectory = new File(miradiDirectory, jarSubdirectoryName);
		RuntimeJarLoader.addJarsInSubdirectoryToClasspath(thirdPartyDirectory);
		System.err.println("Miradi code running from: " + miradiDirectory.getAbsolutePath());
		System.err.println("Added jars to classpath: " + thirdPartyDirectory.getAbsolutePath());
	}
	
	public static File getAppCodeDirectory() throws URISyntaxException
	{
		final URL resourceUrl = Miradi.class.getResource("/resources");
		String imagesURIString = resourceUrl.toURI().getSchemeSpecificPart();
		String imagesPathString = stripPrefix(imagesURIString);
		
		int bangAt = imagesPathString.indexOf('!');
		if(bangAt < 0)
		{
			File imagesDirectory = new File(imagesPathString);
			final File directory = imagesDirectory.getParentFile();
			return directory;
		}
		
		String jarURIString = imagesPathString.substring(0, bangAt);
		File jarFile = new File(jarURIString);
		final File directory = jarFile.getParentFile();
		return directory;
	}

	private static String stripPrefix(String uri)
	{
		int startOfRealPath = uri.indexOf(':') + 1;
		return uri.substring(startOfRealPath);
	}

	public static void start(String[] args)
	{
		try
		{
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
		if(isLinux())
			return;
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

	public static boolean isLinux()
	{
		return System.getProperty("os.name").equals("Linux");
	}

	public static boolean isWindows()
	{
		return System.getProperty("os.name").startsWith("Windows");
	}
	
	private static final class MainWindowRunner implements Runnable
	{
		MainWindowRunner(String[] argsToUse)
		{
			commandLineArgs = argsToUse;
		}
		
		public void run()
		{
			try
			{
				EAM.setMainWindow(MainWindow.create());
				getMainWindow().start(commandLineArgs);
				
				CommandLineProjectFileImporterHelper.importIfRequested(getMainWindow(), commandLineArgs);
			}
			catch (UnsupportedNewVersionSchemaException e)
			{
				AbstractProjectImporter.logTooNewVersionException(e);
				System.exit(1);
			}
			catch(Exception e)
			{
				EAM.alertUserOfNonFatalException(e);
				System.exit(1);
			}
		}

		private MainWindow getMainWindow()
		{
			return EAM.getMainWindow();
		}
		
		private String[] commandLineArgs;
	}

    public static final String LANGUAGE_PACK_VERSION = "4.2";
    public static final String MAIN_VERSION = LANGUAGE_PACK_VERSION + ".0";

    public static final String LANGUAGE_PACK_PREFIX = "MiradiContent-" + LANGUAGE_PACK_VERSION + "-";
    private static final String ALPHA_TESTER_MODE_ON_SWITCH = "--tester";
    public static final String REPAIR_PROJECT_ON_SWITCH = "--repair";

    private static boolean demoMode;
    private static boolean developerMode;
    private static boolean isAlphaTesterMode;
    private static File poFile;
}
