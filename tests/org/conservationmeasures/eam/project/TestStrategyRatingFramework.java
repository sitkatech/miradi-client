/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestStrategyRatingFramework extends EAMTestCase
{
	public TestStrategyRatingFramework(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		framework = project.getStrategyRatingFramework();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testGetImpactValueOptions()
	{
		ValueOption[] valueOptions = framework.getImpactValueOptions();
		assertEquals(5, valueOptions.length);
		for(int i = 0; i < valueOptions.length; ++i)
			assertEquals("Wrong value " + i + "?", i, valueOptions[i].getNumericValue());
		
		ValueOption medium = framework.findImpactValueOptionByNumericValue(2);
		assertEquals("Wrong label?", "Likely to solve some for a while", medium.getLabel());
		assertEquals("Wrong color?", Color.ORANGE, medium.getColor());
	}
	
	ProjectForTesting project;
	StrategyRatingFramework framework;
}
