/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

import org.conservationmeasures.eam.objects.RatingValueOption;
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

	public void testRatingValueOptions() throws Exception
	{
		RatingValueOption[] options = framework.getRatingValueOptions();
		assertEquals("wrong number of default options?", 5, options.length);
		assertEquals("wrong order?", ThreatRatingFramework.PRIORITY_NONE, options[options.length-1].getId());
		assertEquals("bad color?", Color.YELLOW, options[2].getColor());
		assertEquals("bad label?", "Low", options[3].getLabel());
	}
	
	ThreatRatingFramework framework;
}
