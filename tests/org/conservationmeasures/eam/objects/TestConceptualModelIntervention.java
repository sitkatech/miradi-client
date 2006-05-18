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
		int interventionId = 17;
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
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
	
	public void testActivityIds() throws Exception
	{
		int interventionId = 66;
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		IdList empty = new IdList(intervention.getData(ConceptualModelIntervention.TAG_ACTIVITY_IDS));
		assertEquals("not empty to start?", 0, empty.size());
		
		int activityId = 828;
		IdList oneItem = new IdList();
		oneItem.add(activityId);
		intervention.setData(ConceptualModelIntervention.TAG_ACTIVITY_IDS, oneItem.toString());
		
		IdList got = new IdList(intervention.getData(ConceptualModelIntervention.TAG_ACTIVITY_IDS));
		assertEquals("round trip failed?", oneItem, got);
	}
	
	public void testStatus() throws Exception
	{
		int interventionId = 91;
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		assertTrue("didn't default to real status?", intervention.isStatusReal());
		assertFalse("defaulted to draft status?", intervention.isStatusDraft());
		assertEquals("Didn't default to valid status?", ConceptualModelIntervention.STATUS_REAL, intervention.getData(ConceptualModelIntervention.TAG_STATUS));
		intervention.setData(ConceptualModelIntervention.TAG_STATUS, ConceptualModelIntervention.STATUS_DRAFT);
		assertEquals("set/get didn't work?", ConceptualModelIntervention.STATUS_DRAFT, intervention.getData(ConceptualModelIntervention.TAG_STATUS));
		assertFalse("didn't unset real status?", intervention.isStatusReal());
		assertTrue("didn't set to draft status?", intervention.isStatusDraft());
		intervention.setData(ConceptualModelIntervention.TAG_STATUS, ConceptualModelIntervention.STATUS_REAL);
		assertTrue("didn't restore to real status?", intervention.isStatusReal());
		assertFalse("didn't unset draft status?", intervention.isStatusDraft());
		intervention.setData(ConceptualModelIntervention.TAG_STATUS, "OIJFW*FJJF");
		assertTrue("didn't treat unknown as real?", intervention.isStatusReal());
		assertFalse("treated unknown as draft?", intervention.isStatusDraft());
		
	}
	
	public void testJson() throws Exception
	{
		int interventionId = 17;
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		intervention.setData(ConceptualModelIntervention.TAG_STATUS, ConceptualModelIntervention.STATUS_DRAFT);
		intervention.insertActivityId(23, 0);
		intervention.insertActivityId(37, 1);
		
		ConceptualModelIntervention got = new ConceptualModelIntervention(intervention.toJson());
		assertTrue("Didn't restore status?", got.isStatusDraft());
		assertEquals("Didn't read activities?", intervention.getActivityIds(), got.getActivityIds());
	}
}
