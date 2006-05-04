/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestConceptualModelIntervention extends EAMTestCase
{
	public TestConceptualModelIntervention(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		ConceptualModelIntervention intervention = new ConceptualModelIntervention();
		assertEquals("already has activities?", 0, intervention.getActivityIds().size());
		
		int activityId1 = 77;
		intervention.insertActivityId(activityId1, 0);
		IdList afterAdd1 = intervention.getActivityIds();
		assertEquals("didn't add?", 1, afterAdd1.size());
		assertEquals("wrong task id?", activityId1, afterAdd1.get(0));
		
		int activityId2 = 92;
		intervention.insertActivityId(activityId2, 0);
		IdList afterAdd2 = intervention.getActivityIds();
		assertEquals("didn't add 2?", 2, afterAdd2.size());
		assertEquals("didn't insert at front?", activityId2, afterAdd2.get(0));
		assertEquals("original id lost?", activityId1, afterAdd2.get(1));
		
		intervention.removeActivityId(activityId2);
		IdList afterRemove = intervention.getActivityIds();
		assertEquals("didn't remove?", 1, afterRemove.size());
		assertEquals("removed wrong id?", activityId1, afterRemove.get(0));
		
	}
	
	public void testJson() throws Exception
	{
		ConceptualModelIntervention intervention = new ConceptualModelIntervention();
		intervention.insertActivityId(23, 0);
		intervention.insertActivityId(37, 1);
		
		ConceptualModelIntervention got = new ConceptualModelIntervention(intervention.toJson());
		assertEquals("Didn't read activities?", intervention.getActivityIds(), got.getActivityIds());
	}
}
