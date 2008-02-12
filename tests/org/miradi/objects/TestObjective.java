/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Cause;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;


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
		assertEquals("wrong indicator count?", expectedValue, objective.getRelevantIndicatorRefList().size());
	}
}
