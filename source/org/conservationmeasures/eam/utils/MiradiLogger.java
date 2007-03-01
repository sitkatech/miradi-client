/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MiradiLogger
{
	public MiradiLogger()
	{
		logDestination = System.out;
	}
	
	private void setLogDestination(PrintStream dest)
	{
		logDestination = dest;
	}
	
	public void setExceptionLoggingDestination(PrintStream destination)
	{
		exceptionDestination = destination;
	}
	
	public void setLogToString()
	{
		logContents = new ByteArrayOutputStream();
		setLogDestination(new PrintStream(logContents));
	}
	
	public void setLogToConsole()
	{
		setLogDestination(System.out);
	}
	
	public String getLoggedString()
	{
		return logContents.toString();
	}
	
	public void setLogLevel(int logLevel)
	{
		level = logLevel;
	}
	
	public void logException(Exception e)
	{
		logDestination.println("ERROR: ");
		e.printStackTrace(logDestination);
		logDestination.flush();
		if(exceptionDestination != null)
		{
			e.printStackTrace(exceptionDestination);
			exceptionDestination.flush();
		}
	}
	
	public void logError(String text)
	{
		log("ERROR: ", text);
	}
	
	public void logWarning(String text)
	{
		if(level >= LOG_NORMAL)
			log("WARNING: ", text);
	}
	
	public void logDebug(String text)
	{
		if(level >= LOG_DEBUG)
			log("DEBUG: ", text);
	}
	
	public void logVerbose(String text)
	{
		if(level >= LOG_VERBOSE)
			log("VERBOSE: ", text);
	}
	
	private void log(String type, String text)
	{
		logDestination.println(type + text);
		logDestination.flush();
	}
	
	private int level;
	private PrintStream logDestination;
	private ByteArrayOutputStream logContents;
	private PrintStream exceptionDestination;

	public static final int LOG_QUIET = -1;
	public static final int LOG_NORMAL = 0;
	public static final int LOG_DEBUG = 1;
	public static final int LOG_VERBOSE = 2;
}
