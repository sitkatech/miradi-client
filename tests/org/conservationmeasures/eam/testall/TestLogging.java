/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.testall;

import org.conservationmeasures.eam.main.EAM;

public class TestLogging extends EAMTestCase
{
	public TestLogging(String name)
	{
		super(name);
	}
	
	public void testLogToString()
	{
		EAM.setLogToString();
		String someText= "log this!";
		EAM.logWarning(someText);
		assertEquals("WARNING: " + someText + EAM.NEWLINE, EAM.getLoggedString());
	}
}
