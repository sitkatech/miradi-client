/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestThreatRatingFramework extends EAMTestCase
{
	public TestThreatRatingFramework(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		framework = new ThreatRatingFramework();
	}

	public void testBasics() throws Exception
	{
		// TODO
	}
	
	ThreatRatingFramework framework;
}
