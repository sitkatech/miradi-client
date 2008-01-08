/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverride;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSet;


public class TestObjective extends ObjectTestCase
{
	public TestObjective(String name)
	{
		super(name);
	}

	public void testCreateCommandsToClear() throws Exception
	{
		verifyFields(ObjectType.OBJECTIVE);
	}
	
	public void testGetRelevantRefList() throws Exception
	{		
		ORef causeRef = getProject().createFactorAndReturnRef(Cause.getObjectType());

		BaseId objectiveId = getProject().addItemToObjectiveList(causeRef, Cause.TAG_OBJECTIVE_IDS);
		BaseId indicatorId = getProject().addItemToIndicatorList(causeRef, Cause.TAG_INDICATOR_IDS);
		ORef indicatorRef = new ORef(Indicator.getObjectType(), indicatorId);
		
		Objective objective = Objective.find(getProject(), new ORef(Objective.getObjectType(), objectiveId));
		assertEquals("wrong indicator count?", 1, objective.getIndicatorsOnSameFactor().size());
		
		verifyRelevancy(indicatorRef, objective, true, 1);
		verifyRelevancy(indicatorRef, objective, false, 0);
	}

	private void verifyRelevancy(ORef indicatorRef, Objective objective, boolean overrideBoolean, int expectedValue) throws Exception
	{
		RelevancyOverrideSet relvancyOverrides = new RelevancyOverrideSet();
		relvancyOverrides.add(new RelevancyOverride(indicatorRef, overrideBoolean));
		objective.setData(Objective.TAG_RELEVANT_INDICATOR_SET, relvancyOverrides.toString());
		assertEquals("wrong indicator count?", expectedValue, objective.getRelevantRefList().size());
	}
}
