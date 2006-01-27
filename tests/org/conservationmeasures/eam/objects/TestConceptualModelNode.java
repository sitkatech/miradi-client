/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.ObjectiveIds;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.martus.util.TestCaseEnhanced;

public class TestConceptualModelNode extends TestCaseEnhanced
{
	public TestConceptualModelNode(String name)
	{
		super(name);
	}
	
	public void testPriority()
	{
		ConceptualModelFactor factor = new ConceptualModelFactor(DiagramNode.TYPE_DIRECT_THREAT);
		assertEquals("didn't default to priority none?", ThreatPriority.createPriorityNone(), factor.getThreatPriority());
	}

	public void testJson()
	{
		GoalIds goals = new GoalIds();
		goals.addId(2);
		goals.addId(5);
		
		ObjectiveIds objectives = new ObjectiveIds();
		objectives.addId(7);
		objectives.addId(9);

		ConceptualModelFactor factor = new ConceptualModelFactor(DiagramNode.TYPE_DIRECT_THREAT);
		factor.setName("JustAName");
		factor.setNodePriority(ThreatPriority.createPriorityHigh());
		factor.setIndicatorId(new IndicatorId(99));
		factor.setGoals(goals);
		factor.setObjectives(objectives);
		ConceptualModelFactor got = (ConceptualModelFactor)ConceptualModelNode.createFrom(factor.toJson());
		assertEquals("wrong type?", factor.getType(), got.getType());
		assertEquals("wrong id?", factor.getId(), got.getId());
		assertEquals("wrong name?", factor.getName(), got.getName());
		assertEquals("wrong priority?", factor.getThreatPriority(), got.getThreatPriority());
		assertEquals("wrong indicator?", factor.getIndicatorId(), got.getIndicatorId());
		assertEquals("wrong goals count?", factor.getGoals().size(), got.getGoals().size());
		assertEquals("wrong goals?", factor.getGoals(), got.getGoals());
		assertEquals("wrong objectives count?", factor.getObjectives().size(), got.getObjectives().size());
		assertEquals("wrong objectives?", factor.getObjectives(), got.getObjectives());
	}
}
