/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
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
		framework = new ThreatRatingFramework(new IdAssigner());
		framework.createDefaultObjects();
	}
	
	public void testGetBundleValue()
	{
		ThreatRatingBundle bundle = new ThreatRatingBundle(1, 2);
		ThreatRatingValueOption result = framework.getBundleValue(bundle);
		assertEquals("didn't default to none?", 0, result.getNumericValue());
	}

	public void testRatingValueOptions() throws Exception
	{
		ThreatRatingValueOption[] options = framework.getValueOptions();
		assertEquals("wrong number of default options?", 5, options.length);
		assertEquals("wrong order or label?", "Very High", options[1].getLabel());
		assertEquals("wrong numeric value? ", 3, options[2].getNumericValue());
		assertEquals("bad color?", Color.YELLOW, options[3].getColor());
	}
	
	public void testFindValueOptionByNumericValue()
	{
		ThreatRatingValueOption optionNone = framework.findValueOptionByNumericValue(0);
		assertEquals(Color.WHITE, optionNone.getColor());
	}
	
	public void testThreatRatingCriteria() throws Exception
	{
		ThreatRatingCriterion[] criteria = framework.getCriteria();
		assertEquals("wrong number of default criteria?", 4, criteria.length);
		assertEquals("Scope", criteria[0].getLabel());
		assertEquals("Severity", criteria[1].getLabel());
		assertEquals("Custom1", criteria[2].getLabel());
		assertEquals("Custom2", criteria[3].getLabel());
	}
	
	public void testIdAssignment() throws Exception
	{
		assertNotEquals("reused ids?", framework.getCriteria()[0].getId(), framework.getValueOptions()[0].getId());
	}
	
	ThreatRatingFramework framework;
}
