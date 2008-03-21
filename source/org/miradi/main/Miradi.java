/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.util.Arrays;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.miradi.utils.Translation;


public class Miradi
{
	public static void main(String[] args)
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
		
		EAM.setLogLevel(EAM.LOG_DEBUG);
		if(Arrays.asList(args).contains("--verbose"))
			EAM.setLogLevel(EAM.LOG_VERBOSE);
		EAM.setExceptionLoggingDestination();
		EAM.possiblyLogTooLowInitialMemory();
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
		String imagesURIString = Miradi.class.getResource("/images").toURI().toString();
		String imagesPathString = stripPrefix(imagesURIString);
	
		int bangAt = imagesPathString.indexOf('!');
		if(bangAt < 0)
		{
			File imagesDirectory = new File(stripPrefix(imagesURIString));
			return imagesDirectory.getParentFile();
		}
		
		String jarURIString = imagesPathString.substring(0, bangAt);
		File jarFile = new File(stripPrefix(jarURIString));
		return jarFile.getParentFile();
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
