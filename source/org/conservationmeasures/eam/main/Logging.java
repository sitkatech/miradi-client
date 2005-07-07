/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

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
	
	public static void logWarning(String text)
	{
		logDestination.println("WARNING: " + text);
	}
	
	public static void logDebug(String text)
	{
		logDestination.println("DEBUG: " + text);
	}
	
	private static PrintStream logDestination = System.out;
	private static ByteArrayOutputStream logContents;
}
