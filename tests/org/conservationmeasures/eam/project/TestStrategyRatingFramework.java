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
	
	public void testGetFeasibilityValueOptions()
	{
		ValueOption[] valueOptions = framework.getFeasibilityValueOptions();
		assertEquals(5, valueOptions.length);
		for(int i = 0; i < valueOptions.length; ++i)
			assertEquals("Wrong value " + i + "?", i, valueOptions[i].getNumericValue());
		
		ValueOption medium = framework.findFeasibilityValueOptionByNumericValue(2);
		assertEquals("Wrong label?", "Relatively inexpensive or easy, not both", medium.getLabel());
		assertEquals("Wrong color?", Color.ORANGE, medium.getColor());
	}
	
	public void testJson()
	{
		StrategyRatingFramework loaded = new StrategyRatingFramework(project, framework.toJson());
		assertEquals("Didn't reload impact options?", framework.getImpactValueOptions().length, loaded.getImpactValueOptions().length);
		assertEquals("Didn't reload feasibility options?", framework.getFeasibilityValueOptions().length, loaded.getFeasibilityValueOptions().length);
	}
	
	ProjectForTesting project;
	StrategyRatingFramework framework;
}
