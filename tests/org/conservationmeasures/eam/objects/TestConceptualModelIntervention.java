/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.project.RatingValueSet;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class TestConceptualModelIntervention extends EAMTestCase
{
	public TestConceptualModelIntervention(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		BaseId interventionId = new BaseId(17);
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		assertEquals("already has activities?", 0, intervention.getActivityIds().size());
		
		BaseId activityId1 = new BaseId(77);
		intervention.insertActivityId(activityId1, 0);
		IdList afterAdd1 = intervention.getActivityIds();
		assertEquals("didn't add?", 1, afterAdd1.size());
		assertEquals("wrong task id?", activityId1, afterAdd1.get(0));
		
		BaseId activityId2 = new BaseId(92);
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
		BaseId interventionId = new BaseId(66);
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		IdList empty = new IdList(intervention.getData(ConceptualModelIntervention.TAG_ACTIVITY_IDS));
		assertEquals("not empty to start?", 0, empty.size());
		
		BaseId activityId = new BaseId(828);
		IdList oneItem = new IdList();
		oneItem.add(activityId);
		intervention.setData(ConceptualModelIntervention.TAG_ACTIVITY_IDS, oneItem.toString());
		
		IdList got = new IdList(intervention.getData(ConceptualModelIntervention.TAG_ACTIVITY_IDS));
		assertEquals("round trip failed?", oneItem, got);
	}
	
	public void testStatus() throws Exception
	{
		BaseId interventionId = new BaseId(91);
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
	
	public void testRatings() throws Exception
	{
		BaseId interventionId = new BaseId(17);
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		RatingValueSet empty = new RatingValueSet(new EnhancedJsonObject(""));
		intervention.setData(ConceptualModelIntervention.TAG_RATING_VALUE_SET, empty.toString());
		assertEquals("not empty?", defaultValueId, intervention.getRatingValueId(criterionId1, defaultValueId));
		RatingValueSet full = new RatingValueSet();
		full.setValueId(criterionId1, valueId1);
		full.setValueId(criterionId2, valueId2);
		intervention.setData(ConceptualModelIntervention.TAG_RATING_VALUE_SET, full.toString());
		assertEquals("set failed1?", valueId1, intervention.getRatingValueId(criterionId1, defaultValueId));
		assertEquals("set failed2?", valueId2, intervention.getRatingValueId(criterionId2, defaultValueId));
		assertEquals("get failed?", full.toString(), intervention.getData(ConceptualModelIntervention.TAG_RATING_VALUE_SET));
	}
	
	public void testJson() throws Exception
	{
		BaseId interventionId = new BaseId(17);
		ConceptualModelIntervention intervention = new ConceptualModelIntervention(interventionId);
		intervention.setData(ConceptualModelIntervention.TAG_STATUS, ConceptualModelIntervention.STATUS_DRAFT);
		intervention.insertActivityId(new BaseId(23), 0);
		intervention.insertActivityId(new BaseId(37), 1);
		
		intervention.setRatingValueId(criterionId1, valueId1);
		intervention.setRatingValueId(criterionId2, valueId2);
		
		ConceptualModelIntervention got = (ConceptualModelIntervention)EAMBaseObject.createFromJson(intervention.getType(), intervention.toJson());
		assertTrue("Didn't restore status?", got.isStatusDraft());
		assertEquals("Didn't read activities?", intervention.getActivityIds(), got.getActivityIds());
		assertEquals("Didn't read ratings1?", valueId1, got.getRatingValueId(criterionId1, defaultValueId));
		assertEquals("Didn't read ratings2?", valueId2, got.getRatingValueId(criterionId2, defaultValueId));
		assertEquals("Didn't default ratings?", defaultValueId, got.getRatingValueId(criterionId3, defaultValueId));
	}
	
	static final BaseId criterionId1 = new BaseId(17);
	static final BaseId criterionId2 = new BaseId(952);
	static final BaseId criterionId3 = new BaseId(2833);
	static final BaseId valueId1 = new BaseId(85);
	static final BaseId valueId2 = new BaseId(2398);
	static final BaseId defaultValueId = new BaseId(7272);

}
