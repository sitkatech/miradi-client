/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.Translation;
import org.martus.util.TestCaseEnhanced;

public class EAMTestCase extends TestCaseEnhanced
{
	public EAMTestCase(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		verifySetUpAndTearDownCalledEqually(setUpLastCalledBy);
		++setUpTearDownCount;
		setUpLastCalledBy = getCurrentTestName();
	}

	public void tearDown() throws Exception
	{
		--setUpTearDownCount;
		EAM.setLogToConsole();
		verifySetUpAndTearDownCalledEqually(getCurrentTestName());
		Translation.restoreDefaultLocale();
	}
	
	private String getCurrentTestName()
	{
		return getClass().toString() + "." + getName();
	}

	public static void verifySetUpAndTearDownCalledEqually(String currentTestName) 
	{
		int countWas = setUpTearDownCount;
		setUpTearDownCount = 0;
		if(countWas < 0)
			fail("EAMTestCase.setUp not called by subclass " + currentTestName);
		
		if(countWas > 0)
			fail("EAMTestCase.tearDown not called by subclass " + setUpLastCalledBy);
	}
	
	public static int setUpTearDownCount;
	public static String setUpLastCalledBy;
}
