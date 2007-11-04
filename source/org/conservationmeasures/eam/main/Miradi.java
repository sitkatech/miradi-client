/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.io.File;
import java.net.URISyntaxException;


public class Miradi
{
	public static void main(String[] args)
	{
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
		EAM.main(args);
	}

	private static void addThirdPartyJarsToClasspath() throws Exception
	{
		String jarSubdirectoryName = "ThirdParty";
		File miradiDirectory = getAppCodeDirectory();
		System.out.println("Miradi code running from: " + miradiDirectory.getAbsolutePath());
		File thirdPartyDirectory = new File(miradiDirectory, jarSubdirectoryName);
		System.out.println("Adding jars to classpath: " + thirdPartyDirectory.getAbsolutePath());
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

}
