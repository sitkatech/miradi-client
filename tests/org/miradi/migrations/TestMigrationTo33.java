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

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.migrations.forward.MigrationTo33;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.*;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TaskSchema;

public class TestMigrationTo33 extends AbstractTestMigration
{
	public TestMigrationTo33(String name)
	{
		super(name);
	}

	// TODO: MRD-6011 - need to revisit...
	public void testIndicatorWorkPlanDataMigratedByForwardMigration() throws Exception
	{
//		getProject().setProjectStartDate(2005);
//		getProject().setProjectEndDate(2007);
//
//		Strategy strategy = getProject().createAndPopulateStrategy();
//
//		Indicator indicator = getProject().createIndicator(strategy);
//		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_LABEL, indicatorName);
//		ResourceAssignment indicatorResourceAssignment = getProject().addResourceAssignment(indicator, 1.0, 2005, 2005);
//		ExpenseAssignment indicatorExpenseAssignment = getProject().addExpenseAssignment(indicator, dateUnit2005, 1.0);
//
//		Task method = getProject().createMethod(indicator);
//		getProject().fillObjectUsingCommand(method, Task.TAG_LABEL, methodName);
//		ResourceAssignment methodResourceAssignment = getProject().addResourceAssignment(method, 1.0, 2006, 2006);
//		ExpenseAssignment methodExpenseAssignment = getProject().addExpenseAssignment(method, dateUnit2006, 1.0);
//
//		int rcDiagramCountBefore = getProject().getAllRefsForType(ObjectType.RESULTS_CHAIN_DIAGRAM).size();
//		int strategyCountBefore = getProject().getAllRefsForType(ObjectType.STRATEGY).size();
//		ORefList indicatorRefsBefore = getProject().getAllRefsForType(ObjectType.INDICATOR);
//		int indicatorCountBefore = indicatorRefsBefore.size();
//
//		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo33.VERSION_TO));
//		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));
//
//		int strategyCountAfter = migratedProject.getAllRefsForType(ObjectType.STRATEGY).size();
//		int indicatorCountAfter = migratedProject.getAllRefsForType(ObjectType.INDICATOR).size();
//		int rcDiagramCountAfter = migratedProject.getAllRefsForType(ObjectType.RESULTS_CHAIN_DIAGRAM).size();
//
//		assertEquals(strategyCountBefore + 1, strategyCountAfter);
//		assertEquals(indicatorCountBefore, indicatorCountAfter);
//		assertEquals(rcDiagramCountBefore + 1, rcDiagramCountAfter);
//
//		for (ORef indicatorRef : migratedProject.getAllRefsForType(ObjectType.INDICATOR))
//		{
//			RawObject migratedIndicator = migratedProject.findObject(indicatorRef);
//			if (safeGetTag(migratedIndicator, BaseObject.TAG_LABEL).equals(indicatorName))
//			{
//				verifyNoAssignmentsOrExpenses(migratedIndicator);
//				verifyMonitoringActivityCreated(migratedProject, migratedIndicator, indicatorResourceAssignment, indicatorExpenseAssignment);
//
//				IdList methodIdList = new IdList(TaskSchema.getObjectType(), safeGetTag(migratedIndicator, Indicator.TAG_METHOD_IDS));
//				for (int i = 0; i < methodIdList.size(); i++)
//				{
//					BaseId methodId = methodIdList.get(i);
//					ORef methodRef = new ORef(ObjectType.TASK, methodId);
//					RawObject migratedMethod = migratedProject.findObject(methodRef);
//
//					if (safeGetTag(migratedMethod, BaseObject.TAG_LABEL).equals(methodName))
//					{
//						verifyNoAssignmentsOrExpenses(migratedMethod);
//						assertEquals(safeGetTag(migratedMethod, Task.TAG_SUBTASK_IDS), "");
//						verifyTaskCreated(migratedProject, migratedMethod, methodResourceAssignment, methodExpenseAssignment);
//					}
//				}
//			}
//		}
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

	private void verifyMonitoringActivityCreated(RawProject rawProject, RawObject migratedIndicator, ResourceAssignment indicatorResourceAssignment, ExpenseAssignment indicatorExpenseAssignment) throws Exception
	{
		ORef monitoringActivityRef = findTask(rawProject, safeGetTag(migratedIndicator, BaseObject.TAG_LABEL), migratedIndicator);
		assertTrue(monitoringActivityRef.isValid());
		RawObject monitoringActivity = rawProject.findObject(monitoringActivityRef);
		assertEquals(safeGetTag(monitoringActivity, Task.TAG_IS_MONITORING_ACTIVITY), BooleanData.BOOLEAN_TRUE);

		IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), safeGetTag(monitoringActivity, Task.TAG_RESOURCE_ASSIGNMENT_IDS));
		assertTrue(assignmentIdList.contains(indicatorResourceAssignment.getRef()));

		ORefList expenseRefList = new ORefList(safeGetTag(monitoringActivity, Task.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertTrue(expenseRefList.contains(indicatorExpenseAssignment.getRef()));

		String relevancySetAsJsonString = safeGetTag(migratedIndicator, Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
		RelevancyOverrideSet relevancySet = new RelevancyOverrideSet(relevancySetAsJsonString);
		assertTrue(relevancySet.contains(monitoringActivityRef));
	}

	private void verifyTaskCreated(RawProject rawProject, RawObject migratedMethod, ResourceAssignment methodResourceAssignment, ExpenseAssignment methodExpenseAssignment) throws Exception
	{
		ORef taskRef = findTask(rawProject, safeGetTag(migratedMethod, BaseObject.TAG_LABEL), migratedMethod);
		assertTrue(taskRef.isValid());
		RawObject task = rawProject.findObject(taskRef);

		IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), safeGetTag(task, Task.TAG_RESOURCE_ASSIGNMENT_IDS));
		assertTrue(assignmentIdList.contains(methodResourceAssignment.getRef()));

		ORefList expenseRefList = new ORefList(safeGetTag(task, Task.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertTrue(expenseRefList.contains(methodExpenseAssignment.getRef()));
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
		return MigrationTo33.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo33.VERSION_TO;
	}

	private final static DateUnit dateUnit2005 = new DateUnit("YEARFROM:2005-01");	
	private final static DateUnit dateUnit2006 = new DateUnit("YEARFROM:2006-01");
	private final static DateUnit dateUnit2007 = new DateUnit("YEARFROM:2007-01");
	private final static String indicatorName = "Indicator to Migrate";
	private final static String methodName = "Method to Migrate";
}
