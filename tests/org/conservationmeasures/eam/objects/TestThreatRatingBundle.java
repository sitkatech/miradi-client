/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

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
}
