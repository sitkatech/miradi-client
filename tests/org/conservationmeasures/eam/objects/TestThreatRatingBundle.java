/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;

public class TestThreatRatingBundle extends EAMTestCase
{
	public TestThreatRatingBundle(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		int threatId = 5;
		int targetId = 9;
		int defaultValueId = 29;
		
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		assertEquals("can't get threat id?", threatId, bundle.getThreatId());
		assertEquals("can't get target it?", targetId, bundle.getTargetId());
		
		int criterionId = 4;
		assertEquals("non-existant value isn't INVALID?", defaultValueId, bundle.getValueId(criterionId));
		
		int valueId = 12;
		bundle.setValueId(criterionId, valueId);
		assertEquals("didn't remember value?", valueId, bundle.getValueId(criterionId));
	}
	
	public void testPullDataFrom() throws Exception
	{
		int threatId = 5;
		int targetId = 9;
		int defaultValueId = 29;
		int threatId2 = 7;
		int targetId2 = 8;
		int defaultValueId2 = 59;
		
		int criterionId = 34;
		int valueId = 10;

		ThreatRatingBundle originalBundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		originalBundle.setValueId(criterionId, valueId);
		
		ThreatRatingBundle copiedBundle = new ThreatRatingBundle(threatId2, targetId2, defaultValueId2);
		copiedBundle.pullDataFrom(originalBundle);
		
		verifyBundlesEqual(criterionId, originalBundle, copiedBundle);
		
		ThreatRatingBundle copyConstructorBundle = new ThreatRatingBundle(originalBundle);
		verifyBundlesEqual(criterionId, originalBundle, copyConstructorBundle);
	}

	private void verifyBundlesEqual(int criterionId, ThreatRatingBundle originalBundle, ThreatRatingBundle copiedBundle)
	{
		assertEquals("threatId wrong?", originalBundle.getThreatId(), copiedBundle.getThreatId());
		assertEquals("targetId wrong?", originalBundle.getTargetId(), copiedBundle.getTargetId());
		assertEquals("defaultValueId wrong?", originalBundle.getDefaultValueId(), copiedBundle.getDefaultValueId());
		assertEquals("valueId wrong?", originalBundle.getValueId(criterionId), copiedBundle.getValueId(criterionId));
	}
	
	public void testJson() throws Exception
	{
		int threatId = 5;
		int targetId = 9;
		int defaultValueId = 29;
		int criterion1 = 32;
		int rating1 = 1;
		int criterion2 = 57;
		int rating2 = 2;
		
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		bundle.setValueId(criterion1, rating1);
		bundle.setValueId(criterion2, rating2);
		JSONObject json = bundle.toJson();
		ThreatRatingBundle loaded = new ThreatRatingBundle(json);
		assertEquals("Didn't load threat id?", bundle.getThreatId(), loaded.getThreatId());
		assertEquals("Didn't load target id?", bundle.getTargetId(), loaded.getTargetId());
		assertEquals("Didn't load default?", bundle.getValueId(-999), loaded.getValueId(-999));
		assertEquals("Didn't load rating 1?", bundle.getValueId(criterion1), loaded.getValueId(criterion1));
		assertEquals("Didn't load rating 2?", bundle.getValueId(criterion2), loaded.getValueId(criterion2));
	}
}
