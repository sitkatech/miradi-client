/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ProgressPercentSchema;
import org.miradi.utils.CommandVector;


public class TestObjective extends TestDesire
{
	public TestObjective(String name)
	{
		super(name);
	}

	public void testCreateCommandsToClear() throws Exception
	{
		verifyFields(ObjectType.OBJECTIVE);
	}
	
	public void testGetRelevantIndicatorRefList() throws Exception
	{		
		ORef causeRef = getProject().createObject(CauseSchema.getObjectType());

		BaseId objectiveId = getProject().addItemToObjectiveList(causeRef, Cause.TAG_OBJECTIVE_IDS);
		BaseId indicatorId = getProject().addItemToIndicatorList(causeRef, Cause.TAG_INDICATOR_IDS);
		ORef indicatorRef = new ORef(IndicatorSchema.getObjectType(), indicatorId);
		
		Objective objective = Objective.find(getProject(), new ORef(ObjectiveSchema.getObjectType(), objectiveId));
		assertEquals("wrong indicator count?", 1, objective.getIndicatorsOnSameFactor().size());
		
		verifyRelevancy(indicatorRef, objective, true, 1);
		verifyRelevancy(indicatorRef, objective, false, 0);
	}

	private void verifyRelevancy(ORef indicatorRef, Objective objective, boolean overrideBoolean, int expectedValue) throws Exception
	{
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(new RelevancyOverride(indicatorRef, overrideBoolean));
		objective.setData(Objective.TAG_RELEVANT_INDICATOR_SET, relevancyOverrides.toString());
		assertEquals("wrong indicator count?", expectedValue, objective.getRelevantIndicatorRefList().size());
	}
	
	public void testCreateCommandsToDeleteChildren() throws Exception
	{
		verifyProgressPercentIsDeleted(getProject());
	}

	public static void verifyProgressPercentIsDeleted(ProjectForTesting projectToUse) throws Exception
	{
		Strategy strategy = projectToUse.createStrategy();
		Objective objective = projectToUse.createObjective(strategy);
		
		verifyAnnotationIsDeletedFromParent(projectToUse, objective, Objective.TAG_PROGRESS_PERCENT_REFS, ProgressPercentSchema.getObjectType());
	}

	public static void verifyAnnotationIsDeletedFromParent(ProjectForTesting projectToUse, BaseObject parent, String annotationTag, int annotationType) throws Exception
	{
		ORef annotationRef = projectToUse.createObject(annotationType);
		ORefList singleAnnotationRefList = new ORefList(annotationRef);
		
		projectToUse.fillObjectUsingCommand(parent, annotationTag, singleAnnotationRefList);
		
		CommandVector commandsToDelete = parent.createCommandsToDeleteChildrenAndObject();
		projectToUse.executeCommands(commandsToDelete);
		
		ORefList shouldBeEmptyRefList = projectToUse.getPool(annotationType).getRefList();
		assertTrue("Annotation was not deleted?", shouldBeEmptyRefList.isEmpty());
	}	
}
