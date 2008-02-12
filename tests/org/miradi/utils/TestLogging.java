/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.utils;

import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;

public class TestLogging extends EAMTestCase
{
	public TestLogging(String name)
	{
		super(name);
	}
	
	public void tearDown() throws Exception
	{
		EAM.setLogLevel(EAM.LOG_NORMAL);
		EAM.setLogToConsole();
		super.tearDown();
	}
	
	public void testLogToString()
	{
		String someText= "log this!";

		EAM.setLogLevel(EAM.LOG_VERBOSE);
		EAM.setLogToString();
		EAM.logError(someText);
		assertEquals("SEVERE: " + someText + EAM.NEWLINE, EAM.getLoggedString());

		EAM.setLogToString();
		EAM.logWarning(someText);
		assertEquals("WARNING: " + someText + EAM.NEWLINE, EAM.getLoggedString());

		EAM.setLogToString();
		EAM.logDebug(someText);
		assertEquals("INFO: " + someText + EAM.NEWLINE, EAM.getLoggedString());
	}
	
	public void testSetLogLevel()
	{
		EAM.setLogToString();
		EAM.setLogLevel(EAM.LOG_QUIET);
		EAM.logError("quiet/error");
		EAM.logWarning("quiet x");
		EAM.logDebug("quiet x");
		EAM.logVerbose("quiet x");
		EAM.setLogLevel(EAM.LOG_NORMAL);
		EAM.logError("normal/error");
		EAM.logWarning("normal/warning");
		EAM.logDebug("quiet x");
		EAM.logVerbose("quiet x");
		EAM.setLogLevel(EAM.LOG_DEBUG);
		EAM.logError("debug/error");
		EAM.logWarning("debug/warning");
		EAM.logDebug("debug/debug");
		EAM.logVerbose("quiet x");
		EAM.setLogLevel(EAM.LOG_VERBOSE);
		EAM.logError("verbose/error");
		EAM.logWarning("verbose/warning");
		EAM.logDebug("verbose/debug");
		EAM.logVerbose("verbose/verbose");
		String logged = EAM.getLoggedString();
		assertNotContains("logged when shouldn't?", "x", logged);
		assertContains("didn't log when should?", "quiet/error", logged);
		assertContains("didn't log when should?", "normal/error", logged);
		assertContains("didn't log when should?", "normal/warning", logged);
		assertContains("didn't log when should?", "debug/error", logged);
		assertContains("didn't log when should?", "debug/warning", logged);
		assertContains("didn't log when should?", "debug/debug", logged);
		assertContains("didn't log when should?", "verbose/error", logged);
		assertContains("didn't log when should?", "verbose/warning", logged);
		assertContains("didn't log when should?", "verbose/debug", logged);
		assertContains("didn't log when should?", "verbose/verbose", logged);
	}
	
	public void testLogException()
	{
		EAM.setLogToString();
		try
		{
			throw new RuntimeException();
		}
		catch(RuntimeException expected)
		{
			EAM.logException(expected);
		}
		assertContains("Exception", EAM.getLoggedString());
	}
}
