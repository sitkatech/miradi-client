/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objects;

import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Target;

public class TestFactor extends TestCaseWithProject
{
	public TestFactor(String name)
	{
		super(name);
	}
	
	public void testComments() throws Exception
	{
		FactorId id = new FactorId(35);
		Cause factor = new Cause(getObjectManager(), id);
		assertEquals("started with a comment?", "", factor.getComment());
		String sampleComment = "yowza";
		factor.setComment(sampleComment);
		assertEquals("couldn't getComment?", sampleComment, factor.getComment());
	}
	
	public void testSetGetData() throws Exception
	{
		FactorId id = new FactorId(72);
		Target target = new Target(getObjectManager(), id);
		String[] tags = {
			Factor.TAG_COMMENT,
			Factor.TAG_INDICATOR_IDS,
			Factor.TAG_GOAL_IDS,
			Factor.TAG_OBJECTIVE_IDS,
		};

		Vector sampleIds = new Vector();
		sampleIds.add(new BaseId(12));
		sampleIds.add(new BaseId(275));
		
		String[] sampleData = {
			"Whatever comment",
			new IdList(Indicator.getObjectType(), sampleIds).toString(),
			new IdList(Goal.getObjectType(), sampleIds).toString(),
			new IdList(Objective.getObjectType(), sampleIds).toString(),
		};
		
		for(int i = 0; i < tags.length; ++i)
		{
			target.setData(tags[i], sampleData[i]);
			assertEquals("didn't set/get for " + tags[i], sampleData[i], target.getData(tags[i]));
		}
	}

	public void testJson() throws Exception
	{
		IdList goals = new IdList(Goal.getObjectType());
		goals.add(new BaseId(2));
		goals.add(new BaseId(5));
		
		IdList objectives = new IdList(Objective.getObjectType());
		objectives.add(new BaseId(7));
		objectives.add(new BaseId(9));
		
		IdList indicators = new IdList(Indicator.getObjectType());
		indicators.add(new BaseId(23));
		indicators.add(new BaseId(422));

		FactorId factorId = new FactorId(2342);
		Cause factor = new Cause(getObjectManager(), factorId);
		factor.setLabel("JustAName");
		factor.setComment("This is a great comment");
		factor.setIndicators(indicators);
		factor.setGoals(goals);
		factor.setObjectives(objectives);
		Cause got = (Cause)Factor.createFromJson(getObjectManager(), factor.getType(), factor.toJson());
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
}
