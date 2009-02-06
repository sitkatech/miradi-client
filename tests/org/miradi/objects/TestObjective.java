/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
