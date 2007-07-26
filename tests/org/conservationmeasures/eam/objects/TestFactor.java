/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.HashMap;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestFactor extends TestCaseEnhanced
{
	public TestFactor(String name)
	{
		super(name);
	}
	
	protected void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	protected void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}

	public void testComments() throws Exception
	{
		FactorId id = new FactorId(35);
		Cause factor = new Cause(project.getObjectManager(), id);
		assertEquals("started with a comment?", "", factor.getComment());
		String sampleComment = "yowza";
		factor.setComment(sampleComment);
		assertEquals("couldn't getComment?", sampleComment, factor.getComment());
	}
	
	public void testSetGetData() throws Exception
	{
		FactorId id = new FactorId(72);
		Target target = new Target(id);
		String[] tags = {
			Factor.TAG_COMMENT,
			Factor.TAG_INDICATOR_IDS,
			Factor.TAG_GOAL_IDS,
			Factor.TAG_OBJECTIVE_IDS,
		};
		
		IdList sampleIdList = new IdList();
		sampleIdList.add(12);
		sampleIdList.add(275);
		
		String[] sampleData = {
			"Whatever comment",
			sampleIdList.toString(),
			sampleIdList.toString(),
			sampleIdList.toString(),
		};
		
		for(int i = 0; i < tags.length; ++i)
		{
			target.setData(tags[i], sampleData[i]);
			assertEquals("didn't set/get for " + tags[i], sampleData[i], target.getData(tags[i]));
		}
	}

	public void testJson() throws Exception
	{
		IdList goals = new IdList();
		goals.add(new BaseId(2));
		goals.add(new BaseId(5));
		
		IdList objectives = new IdList();
		objectives.add(new BaseId(7));
		objectives.add(new BaseId(9));
		
		IdList indicators = new IdList();
		indicators.add(new BaseId(23));
		indicators.add(new BaseId(422));

		FactorId factorId = new FactorId(2342);
		Cause factor = new Cause(project.getObjectManager(), factorId);
		factor.setLabel("JustAName");
		factor.setComment("This is a great comment");
		factor.setIndicators(indicators);
		factor.setGoals(goals);
		factor.setObjectives(objectives);
		Cause got = (Cause)Factor.createFromJson(project.getObjectManager(), factor.getType(), factor.toJson());
		assertEquals("wrong type?", factor.getNodeType(), got.getNodeType());
		assertEquals("wrong id?", factor.getId(), got.getId());
		assertEquals("wrong name?", factor.getLabel(), got.getLabel());
		assertEquals("wrong comment?", factor.getComment(), got.getComment());
		assertEquals("wrong indicator count?", factor.getIndicators().size(), got.getIndicators().size());
		assertEquals("wrong indicators?", factor.getIndicators(), got.getIndicators());
		assertEquals("wrong goals count?", factor.getGoals().size(), got.getGoals().size());
		assertEquals("wrong goals?", factor.getGoals(), got.getGoals());
		assertEquals("wrong objectives count?", factor.getObjectives().size(), got.getObjectives().size());
		assertEquals("wrong objectives?", factor.getObjectives(), got.getObjectives());
	}
	
	public void testFixupAllIndicatorRefs() throws Exception
	{
		fixupRefs(Cause.getObjectType(), Indicator.getObjectType(), Factor.TAG_INDICATOR_IDS);
		fixupRefs(Cause.getObjectType(), Objective.getObjectType(), Factor.TAG_OBJECTIVE_IDS);
		fixupRefs(Cause.getObjectType(), Goal.getObjectType(), Factor.TAG_GOAL_IDS);
		fixupRefs(Cause.getObjectType(), KeyEcologicalAttribute.getObjectType(), Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}
		
	public void fixupRefs(int factorType, int annotationType, String annotationFactorTag) throws Exception
	{
		ORef factorRef = project.createFactorAndReturnRef(factorType);
		BaseId oldId1 = project.createFactorAndReturnId(annotationType);
		BaseId oldId2 = project.createFactorAndReturnId(annotationType);
		
		BaseId newId1 = project.createFactorAndReturnId(annotationType);
		BaseId newId2 = project.createFactorAndReturnId(annotationType);
		
		IdList annotationIds = new IdList();
		annotationIds.add(oldId1);
		annotationIds.add(oldId2);
		
		CommandSetObjectData setFactorAnnotationIds = new CommandSetObjectData(factorRef, annotationFactorTag, annotationIds.toString());
		project.executeCommand(setFactorAnnotationIds);
		
		HashMap oldToNewRefMap = new HashMap();
		oldToNewRefMap.put(oldId1, newId1);
		oldToNewRefMap.put(oldId2, newId2);
		
		Factor factor = (Factor) project.findObject(factorRef);
		Command[] commandToFixRefs = factor.createCommandToFixupRefLists(oldToNewRefMap);
		project.executeCommands(commandToFixRefs);
		
		IdList newAnnotationIds = new IdList(factor.getData(annotationFactorTag));
		assertFalse("contains wrong old id?", newAnnotationIds.contains(oldId1));
		assertFalse("contains wrong old id?", newAnnotationIds.contains(oldId2));
		
		assertTrue("does not contain new id?", newAnnotationIds.contains(newId1));
		assertTrue("does not contain new id?", newAnnotationIds.contains(newId2));
	}
		
	ProjectForTesting project;
}
