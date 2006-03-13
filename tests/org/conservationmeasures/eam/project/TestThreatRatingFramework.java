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
import org.json.JSONArray;
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
		JSONArray bundleKeys = json.getJSONArray(ThreatRatingFramework.TAG_BUNDLE_KEYS);
		assertEquals("didn't jsonize bundle keys?", framework.getBundleCount(), bundleKeys.length());
	}
	
	public void testWriteAndRead() throws Exception
	{
		File tempDir = createTempDirectory();
		try
		{
			Project realProject = new Project();
			realProject.createOrOpen(tempDir);
			int createdId = realProject.createObject(ObjectType.THREAT_RATING_CRITERION);
			
			int threatId = 283;
			int targetId = 983;
			ThreatRatingBundle bundle = realProject.getThreatRatingFramework().getBundle(threatId, targetId);
			bundle.setValueId(createdId, 838);
			realProject.getThreatRatingFramework().saveBundle(bundle);
			realProject.getDatabase().writeThreatRatingFramework(realProject.getThreatRatingFramework());
			realProject.close();

			Project loadedProject = new Project();
			loadedProject.createOrOpen(tempDir);
			ThreatRatingFramework loadedFramework = loadedProject.getThreatRatingFramework();
			assertEquals("didn't reload framework?", createdId, loadedFramework.getCriterion(createdId).getId());
			ThreatRatingBundle gotBundle = loadedProject.getThreatRatingFramework().getBundle(threatId, targetId);
			assertEquals("didn't load bundles?", bundle.getValueId(createdId), gotBundle.getValueId(createdId));
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
		assertEquals("wrong number of default criteria?", 3, criteria.length);
		assertEquals("Scope", criteria[0].getLabel());
		assertEquals("Severity", criteria[1].getLabel());
		assertEquals("Irreversibility", criteria[2].getLabel());
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
		assertNotNull("Didn't write bundle?", project.getDatabase().readThreatRatingBundle(threatId, targetId));
		
		
		bundle.setValueId(criterionId, valueId);
		ThreatRatingBundle reGot = framework.getBundle(threatId, targetId);
		assertEquals("did't get same bundle?", bundle.getValueId(criterionId), reGot.getValueId(criterionId));
		
	}
	
	public void testGetThreatRatingSummary() throws Exception
	{
		framework.createDefaultObjects();
		
		int threatId1 = 1;
		int threatId2 = 2;
		int targetId1 = 3;
		int targetId2 = 4;

		ThreatRatingValueOption none = framework.findValueOptionByNumericValue(0);
		ThreatRatingValueOption high = framework.findValueOptionByNumericValue(3);
		ThreatRatingValueOption veryHigh = framework.findValueOptionByNumericValue(4);
		
		assertEquals("threat1 not none?", none, framework.getThreatThreatRatingValue(threatId1));
		assertEquals("threat2 not none?", none, framework.getThreatThreatRatingValue(threatId2));
		assertEquals("target1 not none?", none, framework.getTargetThreatRatingValue(targetId1));
		assertEquals("target2 not none?", none, framework.getTargetThreatRatingValue(targetId2));

		populateBundle(threatId1, targetId1, veryHigh);
		populateBundle(threatId1, targetId2, veryHigh);
		assertEquals("target1 not high?", high, framework.getTargetThreatRatingValue(targetId1));
		populateBundle(threatId2, targetId1, veryHigh);
		assertEquals("threat2 not high?", high, framework.getThreatThreatRatingValue(threatId2));
		populateBundle(threatId2, targetId2, veryHigh);
		
		assertEquals("threat1 not very high?", veryHigh, framework.getThreatThreatRatingValue(threatId1));
		assertEquals("threat2 not very high?", veryHigh, framework.getThreatThreatRatingValue(threatId2));
		assertEquals("target1 not very high?", veryHigh, framework.getTargetThreatRatingValue(targetId1));
		assertEquals("target2 not very high?", veryHigh, framework.getTargetThreatRatingValue(targetId2));
	}
	
	void populateBundle(int threatId, int targetId, ThreatRatingValueOption value) throws Exception
	{
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		ThreatRatingCriterion criteria[] = framework.getCriteria();
		for(int i = 0; i < criteria.length; ++i)
			bundle.setValueId(criteria[i].getId(), value.getId());
	}
	
	ThreatRatingFramework framework;
	private ProjectForTesting project;
}
