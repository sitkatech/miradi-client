/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.File;

import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;
import org.martus.util.DirectoryUtils;

public class TestThreatRatingFramework extends EAMTestCase
{
	public TestThreatRatingFramework(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		framework = project.getThreatRatingFramework();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testJson()
	{
		JSONObject json = framework.toJson();
		IdList criterionIds = new IdList(json.getJSONObject(ThreatRatingFramework.TAG_CRITERION_IDS));
		assertEquals("didn't jsonize criteria?", framework.getCriteria().length, criterionIds.size());
		IdList valueOptionIds = new IdList(json.getJSONObject(ThreatRatingFramework.TAG_VALUE_OPTION_IDS));
		assertEquals("didn't jsonize value options?", framework.getValueOptions().length, valueOptionIds.size());
	}
	
	public void testWriteAndRead() throws Exception
	{
		File tempDir = createTempDirectory();
		try
		{
			Project realProject = new Project();
			realProject.createOrOpen(tempDir);
			int createdId = realProject.createObject(ObjectType.THREAT_RATING_CRITERION);
			realProject.close();

			Project loadedProject = new Project();
			loadedProject.createOrOpen(tempDir);
			ThreatRatingFramework loadedFramework = loadedProject.getThreatRatingFramework();
			assertEquals("didn't reload framework?", createdId, loadedFramework.getCriterion(createdId).getId());
			loadedProject.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
	}
	
	public void testGetBundleValue()
	{
		int noneId = framework.findValueOptionByNumericValue(0).getId();
		ThreatRatingBundle bundle = new ThreatRatingBundle(1, 2, noneId);
		ThreatRatingValueOption result = framework.getBundleValue(bundle);
		assertEquals("didn't default correctly? ", 0, result.getNumericValue());
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
	
	public void testBundles() throws Exception
	{
		int threatId = 77;
		int targetId = 292;
		int criterionId = 22;
		int valueId = 639;
		
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		bundle.setValueId(criterionId, valueId);
		ThreatRatingBundle reGot = framework.getBundle(threatId, targetId);
		assertEquals("did't get same bundle?", bundle.getValueId(criterionId), reGot.getValueId(criterionId));
		
	}
	
	ThreatRatingFramework framework;
	private ProjectForTesting project;
}
