/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Logging
{
	private static void setLogDestination(PrintStream dest)
	{
		logDestination = dest;
	}
	
	public static void setLogToString()
	{
		logContents = new ByteArrayOutputStream();
		setLogDestination(new PrintStream(logContents));
	}
	
	public static void setLogToConsole()
	{
		setLogDestination(System.out);
	}
	
	public static String getLoggedString()
	{
		return logContents.toString();
	}
	
	public static void setLogLevel(int logLevel)
	{
		level = logLevel;
	}
	
	public static void logException(Exception e)
	{
		logDestination.println("ERROR: ");
		// TODO: actually print the stack trace to logDestination
		e.printStackTrace();
	}
	
	public static void logError(String text)
	{
		logDestination.println("ERROR: " + text);
	}
	
	public static void logWarning(String text)
	{
		if(level >= LOG_NORMAL)
			logDestination.println("WARNING: " + text);
	}
	
	public static void logDebug(String text)
	{
		if(level >= LOG_DEBUG)
			logDestination.println("DEBUG: " + text);
	}
	
	public static void logVerbose(String text)
	{
		if(level >= LOG_VERBOSE)
			logDestination.println("VERBOSE: " + text);
	}
	
	private static int level;
	private static PrintStream logDestination = System.out;
	private static ByteArrayOutputStream logContents;

	public static final int LOG_QUIET = -1;
	public static final int LOG_NORMAL = 0;
	public static final int LOG_DEBUG = 1;
	public static final int LOG_VERBOSE = 2;
}
