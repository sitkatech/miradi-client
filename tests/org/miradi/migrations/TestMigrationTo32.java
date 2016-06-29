/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import org.miradi.ids.IdList;
import org.miradi.migrations.forward.MigrationTo32;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.*;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.EnhancedJsonObject;

public class TestMigrationTo32 extends AbstractTestMigration
{
	public TestMigrationTo32(String name)
	{
		super(name);
	}

	public void testIndicatorWorkPlanDataMigratedByForwardMigration() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		ProjectResource leader = getProject().createAndPopulateProjectResource();

		Strategy strategy = getProject().createAndPopulateStrategy();

		Indicator indicator = getProject().createIndicator(strategy);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_LABEL, indicatorName);
		getProject().fillObjectUsingCommand(indicator, BaseObject.TAG_ASSIGNED_LEADER_RESOURCE, leader.getRef());
		ResourceAssignment indicatorResourceAssignment = getProject().addResourceAssignment(indicator, 1.0, 2005, 2005);
		ExpenseAssignment indicatorExpenseAssignment = getProject().addExpenseAssignment(indicator, dateUnit2005, 1.0);

		int rcDiagramCountBefore = getProject().getAllRefsForType(ObjectType.RESULTS_CHAIN_DIAGRAM).size();
		int strategyCountBefore = getProject().getAllRefsForType(ObjectType.STRATEGY).size();
		ORefList indicatorRefsBefore = getProject().getAllRefsForType(ObjectType.INDICATOR);
		int indicatorCountBefore = indicatorRefsBefore.size();

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo32.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		int strategyCountAfter = migratedProject.getAllRefsForType(ObjectType.STRATEGY).size();
		int indicatorCountAfter = migratedProject.getAllRefsForType(ObjectType.INDICATOR).size();
		int rcDiagramCountAfter = migratedProject.getAllRefsForType(ObjectType.RESULTS_CHAIN_DIAGRAM).size();

		assertEquals(strategyCountBefore + 1, strategyCountAfter);
		assertEquals(indicatorCountBefore, indicatorCountAfter);
		assertEquals(rcDiagramCountBefore + 1, rcDiagramCountAfter);

		for (ORef indicatorRef : migratedProject.getAllRefsForType(ObjectType.INDICATOR))
		{
			RawObject migratedIndicator = migratedProject.findObject(indicatorRef);
			if (safeGetTag(migratedIndicator, BaseObject.TAG_LABEL).equals(indicatorName))
			{
				verifyNoAssignmentsOrExpenses(migratedIndicator);
				verifyMonitoringActivityCreated(migratedProject, migratedIndicator, indicatorResourceAssignment, indicatorExpenseAssignment, leader);
			}
		}
	}

	private String safeGetTag(RawObject rawObject, String tag)
	{
		if (rawObject.hasValue(tag))
			return rawObject.getData(tag);

		return "";
	}

	private void verifyNoAssignmentsOrExpenses(RawObject rawObject) throws Exception
	{
		IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), safeGetTag(rawObject, Indicator.TAG_RESOURCE_ASSIGNMENT_IDS));
		assertEquals(assignmentIdList.size(), 0);

		ORefList expenseRefList = new ORefList(safeGetTag(rawObject, Indicator.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertTrue(expenseRefList.isEmpty());
	}

	private void verifyMonitoringActivityCreated(RawProject rawProject, RawObject migratedIndicator, ResourceAssignment indicatorResourceAssignment, ExpenseAssignment indicatorExpenseAssignment, ProjectResource leader) throws Exception
	{
		ORef monitoringActivityRef = findTask(rawProject, safeGetTag(migratedIndicator, BaseObject.TAG_LABEL), migratedIndicator);
		assertTrue(monitoringActivityRef.isValid());
		RawObject monitoringActivity = rawProject.findObject(monitoringActivityRef);
		assertEquals(safeGetTag(monitoringActivity, Task.TAG_IS_MONITORING_ACTIVITY), BooleanData.BOOLEAN_TRUE);

		String leaderRefAsString = safeGetTag(monitoringActivity, BaseObject.TAG_ASSIGNED_LEADER_RESOURCE);
		ORef leaderRef = new ORef(new EnhancedJsonObject(leaderRefAsString));
		assertEquals(leaderRef, leader.getRef());

		IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), safeGetTag(monitoringActivity, Task.TAG_RESOURCE_ASSIGNMENT_IDS));
		assertTrue(assignmentIdList.contains(indicatorResourceAssignment.getRef()));

		ORefList expenseRefList = new ORefList(safeGetTag(monitoringActivity, Task.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertTrue(expenseRefList.contains(indicatorExpenseAssignment.getRef()));

		String relevancySetAsJsonString = safeGetTag(migratedIndicator, Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
		RelevancyOverrideSet relevancySet = new RelevancyOverrideSet(relevancySetAsJsonString);
		assertTrue(relevancySet.contains(monitoringActivityRef));
	}

	private ORef findTask(RawProject rawProject, String taskName, RawObject objectToIgnore)
	{
		ORefList taskRefList = rawProject.getAllRefsForType(ObjectType.TASK);
		for (ORef taskRef : taskRefList)
		{
			RawObject task = rawProject.findObject(taskRef);
			String name = safeGetTag(task, BaseObject.TAG_LABEL);
			if (name.equals(taskName) && !task.equals(objectToIgnore))
				return taskRef;
		}

		return ORef.createInvalidWithType(ObjectType.TASK);
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo32.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo32.VERSION_TO;
	}

	private final static DateUnit dateUnit2005 = new DateUnit("YEARFROM:2005-01");	
	private final static String indicatorName = "Indicator to Migrate";
}
