/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import org.miradi.migrations.forward.MigrationTo25;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.util.ArrayList;

public class TestMigrationTo25 extends AbstractTestMigration
{
	public TestMigrationTo25(String name)
	{
		super(name);
	}

	// note: since work plan data is subsequently removed from indicators / methods, we won't explicitly test here
	// logic is exactly the same as what is exercised for strategies below

	public void testForwardMigrationStrategyNoResourceAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, 1.0, 2005, 2005);

		Task activity = getProject().createActivity(strategy);
		ResourceAssignment activityResourceAssignment = getProject().addResourceAssignment(activity, 2.0, 2006, 2006);

		Task subTask = getProject().createTask(activity);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2007, 2007);

		assertFalse(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2006));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertEquals(strategyResourceAssignmentIds, new IdList(strategyResourceAssignment));

		RawObject rawStrategyResourceAssignment = migratedProject.findObject(strategyResourceAssignment.getRef());
		String rawStrategyResourceAssignmentDateUnitEffortListAsString = rawStrategyResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawStrategyResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawStrategyResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawStrategyResourceAssignmentDateUnitEffortList, strategyResourceAssignment.getDateUnitEffortList());

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		String activityResourceAssignmentIdsAsString = rawActivity.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activityResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activityResourceAssignmentIdsAsString);
		assertEquals(activityResourceAssignmentIds, new IdList(activityResourceAssignment));

		RawObject rawActivityResourceAssignment = migratedProject.findObject(activityResourceAssignment.getRef());
		String rawActivityResourceAssignmentDateUnitEffortListAsString = rawActivityResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawActivityResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawActivityResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawActivityResourceAssignmentDateUnitEffortList, activityResourceAssignment.getDateUnitEffortList());

		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		String subTaskResourceAssignmentIdsAsString = rawSubTask.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTaskResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTaskResourceAssignmentIdsAsString);
		assertEquals(subTaskResourceAssignmentIds, new IdList(subTaskResourceAssignment));

		RawObject rawSubTaskResourceAssignment = migratedProject.findObject(subTaskResourceAssignment.getRef());
		String rawSubTaskResourceAssignmentDateUnitEffortListAsString = rawSubTaskResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList, subTaskResourceAssignment.getDateUnitEffortList());
	}

	public void testForwardMigrationStrategyResourceAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, 1.0, 2005, 2005);

		Task activity = getProject().createActivity(strategy);
		ResourceAssignment activityResourceAssignment = getProject().addResourceAssignment(activity, 2.0, 2005, 2005);

		Task subTask = getProject().createTask(activity);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2005, 2005);

		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005));
		assertTrue(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertTrue(strategyResourceAssignmentIds.isEmpty());

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		String activityResourceAssignmentIdsAsString = rawActivity.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activityResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activityResourceAssignmentIdsAsString);
		assertTrue(activityResourceAssignmentIds.isEmpty());

		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		String subTaskResourceAssignmentIdsAsString = rawSubTask.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTaskResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTaskResourceAssignmentIdsAsString);
		assertEquals(subTaskResourceAssignmentIds, new IdList(subTaskResourceAssignment));

		RawObject rawSubTaskResourceAssignment = migratedProject.findObject(subTaskResourceAssignment.getRef());
		String rawSubTaskResourceAssignmentDateUnitEffortListAsString = rawSubTaskResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList, subTaskResourceAssignment.getDateUnitEffortList());
	}

	public void testForwardMigrationStrategyResourceAssignmentsNotRemovedZeroQuantities() throws Exception
	{
		// 4.3.1 had an inconsistency where 0 unit assignments would cause those further up the hierarchy to be struck out...but still counted into totals

		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, 1.0, 2005, 2005);

		Task activity = getProject().createActivity(strategy);
		ResourceAssignment activityResourceAssignment = getProject().addResourceAssignment(activity, 0.0, 2005, 2005);

		Task subTask = getProject().createTask(activity);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 0.0, 2005, 2005);

		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005));
		assertTrue(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertEquals(strategyResourceAssignmentIds, new IdList(strategyResourceAssignment));

		RawObject rawStrategyResourceAssignment = migratedProject.findObject(strategyResourceAssignment.getRef());
		String rawStrategyResourceAssignmentDateUnitEffortListAsString = rawStrategyResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawStrategyResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawStrategyResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawStrategyResourceAssignmentDateUnitEffortList, strategyResourceAssignment.getDateUnitEffortList());

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		String activityResourceAssignmentIdsAsString = rawActivity.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activityResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activityResourceAssignmentIdsAsString);
		assertEquals(activityResourceAssignmentIds, new IdList(activityResourceAssignment));

		RawObject rawActivityResourceAssignment = migratedProject.findObject(activityResourceAssignment.getRef());
		String rawActivityResourceAssignmentDateUnitEffortListAsString = rawActivityResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawActivityResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawActivityResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawActivityResourceAssignmentDateUnitEffortList, activityResourceAssignment.getDateUnitEffortList());

		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		String subTaskResourceAssignmentIdsAsString = rawSubTask.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTaskResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTaskResourceAssignmentIdsAsString);
		assertEquals(subTaskResourceAssignmentIds, new IdList(subTaskResourceAssignment));

		RawObject rawSubTaskResourceAssignment = migratedProject.findObject(subTaskResourceAssignment.getRef());
		String rawSubTaskResourceAssignmentDateUnitEffortListAsString = rawSubTaskResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList, subTaskResourceAssignment.getDateUnitEffortList());
	}

	public void testForwardMigrationStrategyResourceAssignmentsRemovedMultipleActivities() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ArrayList<DateUnit> strategyDateUnits = new ArrayList<DateUnit>();
		strategyDateUnits.add(dateUnit2005Q1);
		strategyDateUnits.add(dateUnit2005Q4);
		DateUnitEffortList strategyDateUnitEffortList = createDateUnitEffortList(strategyDateUnits, 1.0);
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, strategyDateUnitEffortList);

		Task activity1 = getProject().createActivity(strategy);
		ArrayList<DateUnit> activity1DateUnits = new ArrayList<DateUnit>();
		activity1DateUnits.add(dateUnit2005Q3);
		DateUnitEffortList activity1DateUnitEffortList = createDateUnitEffortList(activity1DateUnits, 2.0);
		ResourceAssignment activity1ResourceAssignment = getProject().addResourceAssignment(activity1, activity1DateUnitEffortList);

		Task subTask1 = getProject().createTask(activity1);
		ArrayList<DateUnit> subTask1DateUnits = new ArrayList<DateUnit>();
		subTask1DateUnits.add(dateUnit2005Q4);
		DateUnitEffortList subTask1DateUnitEffortList = createDateUnitEffortList(subTask1DateUnits, 3.0);
		ResourceAssignment subTask1ResourceAssignment = getProject().addResourceAssignment(subTask1, subTask1DateUnitEffortList);

		Task activity2 = getProject().createActivity(strategy);
		ArrayList<DateUnit> activity2DateUnits = new ArrayList<DateUnit>();
		activity2DateUnits.add(dateUnit2005Q1);
		DateUnitEffortList activity2DateUnitEffortList = createDateUnitEffortList(activity2DateUnits, 2.0);
		ResourceAssignment activity2ResourceAssignment = getProject().addResourceAssignment(activity2, activity2DateUnitEffortList);

		Task subTask2 = getProject().createTask(activity2);
		ArrayList<DateUnit> subTask2DateUnits = new ArrayList<DateUnit>();
		subTask2DateUnits.add(dateUnit2005Q2);
		DateUnitEffortList subTask2DateUnitEffortList = createDateUnitEffortList(subTask2DateUnits, 3.0);
		ResourceAssignment subTask2ResourceAssignment = getProject().addResourceAssignment(subTask2, subTask2DateUnitEffortList);

		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005Q1));
		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005Q4));
		assertFalse(isAssignmentDataStruckOut(activity1ResourceAssignment, dateUnit2005Q3));
		assertFalse(isAssignmentDataStruckOut(subTask1ResourceAssignment, dateUnit2005Q4));
		assertFalse(isAssignmentDataStruckOut(activity2ResourceAssignment, dateUnit2005Q1));
		assertFalse(isAssignmentDataStruckOut(subTask2ResourceAssignment, dateUnit2005Q2));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 4);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertTrue(strategyResourceAssignmentIds.isEmpty());

		RawObject rawActivity1 = migratedProject.findObject(activity1.getRef());
		String activity1ResourceAssignmentIdsAsString = rawActivity1.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activity1ResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activity1ResourceAssignmentIdsAsString);
		assertEquals(activity1ResourceAssignmentIds, new IdList(activity1ResourceAssignment));

		RawObject rawActivity1ResourceAssignment = migratedProject.findObject(activity1ResourceAssignment.getRef());
		String rawActivity1ResourceAssignmentDateUnitEffortListAsString = rawActivity1ResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawActivity1ResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawActivity1ResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawActivity1ResourceAssignmentDateUnitEffortList, activity1ResourceAssignment.getDateUnitEffortList());

		RawObject rawSubTask1 = migratedProject.findObject(subTask1.getRef());
		String subTask1ResourceAssignmentIdsAsString = rawSubTask1.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTask1ResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTask1ResourceAssignmentIdsAsString);
		assertEquals(subTask1ResourceAssignmentIds, new IdList(subTask1ResourceAssignment));

		RawObject rawSubTask1ResourceAssignment = migratedProject.findObject(subTask1ResourceAssignment.getRef());
		String rawSubTask1ResourceAssignmentDateUnitEffortListAsString = rawSubTask1ResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTask1ResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTask1ResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTask1ResourceAssignmentDateUnitEffortList, subTask1ResourceAssignment.getDateUnitEffortList());

		RawObject rawActivity2 = migratedProject.findObject(activity2.getRef());
		String activity2ResourceAssignmentIdsAsString = rawActivity2.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activity2ResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activity2ResourceAssignmentIdsAsString);
		assertEquals(activity2ResourceAssignmentIds, new IdList(activity2ResourceAssignment));

		RawObject rawActivity2ResourceAssignment = migratedProject.findObject(activity2ResourceAssignment.getRef());
		String rawActivity2ResourceAssignmentDateUnitEffortListAsString = rawActivity2ResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawActivity2ResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawActivity2ResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawActivity2ResourceAssignmentDateUnitEffortList, activity2ResourceAssignment.getDateUnitEffortList());

		RawObject rawSubTask2 = migratedProject.findObject(subTask2.getRef());
		String subTask2ResourceAssignmentIdsAsString = rawSubTask2.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTask2ResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTask2ResourceAssignmentIdsAsString);
		assertEquals(subTask2ResourceAssignmentIds, new IdList(subTask2ResourceAssignment));

		RawObject rawSubTask2ResourceAssignment = migratedProject.findObject(subTask2ResourceAssignment.getRef());
		String rawSubTask2ResourceAssignmentDateUnitEffortListAsString = rawSubTask2ResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTask2ResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTask2ResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTask2ResourceAssignmentDateUnitEffortList, subTask2ResourceAssignment.getDateUnitEffortList());
	}

	public void testForwardMigrationStrategyAllResourceAssignmentsRemovedExceptLowestLevel() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, 1.0, dateUnit2005);

		Task activity = getProject().createActivity(strategy);
		ResourceAssignment activityResourceAssignment = getProject().addResourceAssignment(activity, 2.0, dateUnit2005Q4);

		Task subTask = getProject().createTask(activity);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, dateUnit200512);

		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005));
		assertTrue(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertTrue(strategyResourceAssignmentIds.isEmpty());

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		String activityResourceAssignmentIdsAsString = rawActivity.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activityResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activityResourceAssignmentIdsAsString);
		assertTrue(activityResourceAssignmentIds.isEmpty());

		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		String subTaskResourceAssignmentIdsAsString = rawSubTask.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTaskResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTaskResourceAssignmentIdsAsString);
		assertEquals(subTaskResourceAssignmentIds, new IdList(subTaskResourceAssignment));

		RawObject rawSubTaskResourceAssignment = migratedProject.findObject(subTaskResourceAssignment.getRef());
		String rawSubTaskResourceAssignmentDateUnitEffortListAsString = rawSubTaskResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList, subTaskResourceAssignment.getDateUnitEffortList());
	}

	public void testForwardMigrationStrategyResourceAssignmentsRemovedSkipLevel() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, 1.0, 2005, 2005);

		Task activity = getProject().createActivity(strategy);
		ResourceAssignment activityResourceAssignment = getProject().addResourceAssignment(activity, 2.0, 2005, 2005);

		Task task = getProject().createTask(activity);

		Task subTask = getProject().createTask(task);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2005, 2005);

		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005));
		assertTrue(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertTrue(strategyResourceAssignmentIds.isEmpty());

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		String activityResourceAssignmentIdsAsString = rawActivity.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activityResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activityResourceAssignmentIdsAsString);
		assertTrue(activityResourceAssignmentIds.isEmpty());

		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		String subTaskResourceAssignmentIdsAsString = rawSubTask.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList subTaskResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), subTaskResourceAssignmentIdsAsString);
		assertEquals(subTaskResourceAssignmentIds, new IdList(subTaskResourceAssignment));
	}

	public void testForwardMigrationStrategyResourceAssignmentsUpdated() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ArrayList<DateUnit> strategyDateUnits = new ArrayList<DateUnit>();
		strategyDateUnits.add(dateUnit2005Q1);
		strategyDateUnits.add(dateUnit2005Q2);
		DateUnitEffortList strategyDateUnitEffortList = createDateUnitEffortList(strategyDateUnits, 1.0);
		ResourceAssignment strategyResourceAssignment = getProject().addResourceAssignment(strategy, strategyDateUnitEffortList);

		Task activity = getProject().createActivity(strategy);
		ArrayList<DateUnit> activityDateUnits = new ArrayList<DateUnit>();
		activityDateUnits.add(dateUnit2005Q2);
		activityDateUnits.add(dateUnit2005Q3);
		DateUnitEffortList activityDateUnitEffortList = createDateUnitEffortList(activityDateUnits, 2.0);
		ResourceAssignment activityResourceAssignment = getProject().addResourceAssignment(activity, activityDateUnitEffortList);

		Task subTask = getProject().createTask(activity);
		ArrayList<DateUnit> subTaskDateUnits = new ArrayList<DateUnit>();
		subTaskDateUnits.add(dateUnit2005Q3);
		subTaskDateUnits.add(dateUnit2005Q4);
		DateUnitEffortList subTaskDateUnitEffortList = createDateUnitEffortList(subTaskDateUnits, 3.0);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, subTaskDateUnitEffortList);

		assertFalse(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005Q1));
		assertTrue(isAssignmentDataStruckOut(strategyResourceAssignment, dateUnit2005Q2));
		assertFalse(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2005Q2));
		assertTrue(isAssignmentDataStruckOut(activityResourceAssignment, dateUnit2005Q3));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2005Q3));
		assertFalse(isAssignmentDataStruckOut(subTaskResourceAssignment, dateUnit2005Q4));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		String strategyResourceAssignmentIdsAsString = rawStrategy.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList strategyResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), strategyResourceAssignmentIdsAsString);
		assertEquals(strategyResourceAssignmentIds, new IdList(strategyResourceAssignment));

		RawObject rawStrategyResourceAssignment = migratedProject.findObject(strategyResourceAssignment.getRef());
		String rawStrategyResourceAssignmentDateUnitEffortListAsString = rawStrategyResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawStrategyResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawStrategyResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawStrategyResourceAssignmentDateUnitEffortList.size(), 1);
		assertEquals(rawStrategyResourceAssignmentDateUnitEffortList.getDateUnitEffort(0), new DateUnitEffort(dateUnit2005Q1, 1.0));

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		String activityResourceAssignmentIdsAsString = rawActivity.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList activityResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), activityResourceAssignmentIdsAsString);
		assertEquals(activityResourceAssignmentIds, new IdList(activityResourceAssignment));

		RawObject rawActivityResourceAssignment = migratedProject.findObject(activityResourceAssignment.getRef());
		String rawActivityResourceAssignmentDateUnitEffortListAsString = rawActivityResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawActivityResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawActivityResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawActivityResourceAssignmentDateUnitEffortList.size(), 1);
		assertEquals(rawActivityResourceAssignmentDateUnitEffortList.getDateUnitEffort(0), new DateUnitEffort(dateUnit2005Q2, 2.0));
		
		RawObject rawTask = migratedProject.findObject(subTask.getRef());
		String taskResourceAssignmentIdsAsString = rawTask.getData(MigrationTo25.TAG_RESOURCE_ASSIGNMENT_IDS);
		IdList taskResourceAssignmentIds = new IdList(ResourceAssignmentSchema.getObjectType(), taskResourceAssignmentIdsAsString);
		assertEquals(taskResourceAssignmentIds, new IdList(subTaskResourceAssignment));

		RawObject rawSubTaskResourceAssignment = migratedProject.findObject(subTaskResourceAssignment.getRef());
		String rawSubTaskResourceAssignmentDateUnitEffortListAsString = rawSubTaskResourceAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskResourceAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskResourceAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList.size(), 2);
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList.getDateUnitEffortForSpecificDateUnit(dateUnit2005Q3), new DateUnitEffort(dateUnit2005Q3, 3.0));
		assertEquals(rawSubTaskResourceAssignmentDateUnitEffortList.getDateUnitEffortForSpecificDateUnit(dateUnit2005Q4), new DateUnitEffort(dateUnit2005Q4, 3.0));
	}

	public void testForwardMigrationStrategyNoExpenseAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ExpenseAssignment strategyExpenseAssignment = getProject().addExpenseAssignment(strategy, dateUnit2005, 1.0);

		Task activity = getProject().createActivity(strategy);
		ExpenseAssignment activityExpenseAssignment = getProject().addExpenseAssignment(activity, dateUnit2006, 2.0);

		Task subTask = getProject().createTask(activity);
		ExpenseAssignment subTaskExpenseAssignment = getProject().addExpenseAssignment(subTask, dateUnit2007, 3.0);

		assertFalse(isAssignmentDataStruckOut(strategyExpenseAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(activityExpenseAssignment, dateUnit2006));
		assertFalse(isAssignmentDataStruckOut(subTaskExpenseAssignment, dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		ORefList rawStrategyExpenseRefs = new ORefList(rawStrategy.getData(MigrationTo25.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertEquals(rawStrategyExpenseRefs, new ORefList(strategyExpenseAssignment));

		RawObject rawStrategyExpenseAssignment = migratedProject.findObject(strategyExpenseAssignment.getRef());
		String rawStrategyExpenseAssignmentDateUnitEffortListAsString = rawStrategyExpenseAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawStrategyExpenseAssignmentDateUnitEffortList = new DateUnitEffortList(rawStrategyExpenseAssignmentDateUnitEffortListAsString);
		assertEquals(rawStrategyExpenseAssignmentDateUnitEffortList, strategyExpenseAssignment.getDateUnitEffortList());
		
		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		ORefList rawActivityExpenseRefs = new ORefList(rawActivity.getData(MigrationTo25.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertEquals(rawActivityExpenseRefs, new ORefList(activityExpenseAssignment));

		RawObject rawActivityExpenseAssignment = migratedProject.findObject(activityExpenseAssignment.getRef());
		String rawActivityExpenseAssignmentDateUnitEffortListAsString = rawActivityExpenseAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawActivityExpenseAssignmentDateUnitEffortList = new DateUnitEffortList(rawActivityExpenseAssignmentDateUnitEffortListAsString);
		assertEquals(rawActivityExpenseAssignmentDateUnitEffortList, activityExpenseAssignment.getDateUnitEffortList());
		
		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		ORefList rawSubTaskExpenseRefs = new ORefList(rawSubTask.getData(MigrationTo25.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertEquals(rawSubTaskExpenseRefs, new ORefList(subTaskExpenseAssignment));

		RawObject rawSubTaskExpenseAssignment = migratedProject.findObject(subTaskExpenseAssignment.getRef());
		String rawSubTaskExpenseAssignmentDateUnitEffortListAsString = rawSubTaskExpenseAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskExpenseAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskExpenseAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskExpenseAssignmentDateUnitEffortList, subTaskExpenseAssignment.getDateUnitEffortList());
	}

	public void testForwardMigrationStrategyExpenseAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		ExpenseAssignment strategyExpenseAssignment = getProject().addExpenseAssignment(strategy, dateUnit2005, 1.0);

		Task activity = getProject().createActivity(strategy);
		ExpenseAssignment activityExpenseAssignment = getProject().addExpenseAssignment(activity, dateUnit2005, 2.0);

		Task subTask = getProject().createTask(activity);
		ExpenseAssignment subTaskExpenseAssignment = getProject().addExpenseAssignment(subTask, dateUnit2005, 3.0);

		assertTrue(isAssignmentDataStruckOut(strategyExpenseAssignment, dateUnit2005));
		assertTrue(isAssignmentDataStruckOut(activityExpenseAssignment, dateUnit2005));
		assertFalse(isAssignmentDataStruckOut(subTaskExpenseAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo25.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);

		RawObject rawStrategy = migratedProject.findObject(strategy.getRef());
		ORefList rawStrategyExpenseRefs = new ORefList(rawStrategy.getData(MigrationTo25.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertTrue(rawStrategyExpenseRefs.isEmpty());

		RawObject rawActivity = migratedProject.findObject(activity.getRef());
		ORefList rawActivityExpenseRefs = new ORefList(rawActivity.getData(MigrationTo25.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertTrue(rawActivityExpenseRefs.isEmpty());

		RawObject rawSubTask = migratedProject.findObject(subTask.getRef());
		ORefList rawSubTaskExpenseRefs = new ORefList(rawSubTask.getData(MigrationTo25.TAG_EXPENSE_ASSIGNMENT_REFS));
		assertEquals(rawSubTaskExpenseRefs, new ORefList(subTaskExpenseAssignment));

		RawObject rawSubTaskExpenseAssignment = migratedProject.findObject(subTaskExpenseAssignment.getRef());
		String rawSubTaskExpenseAssignmentDateUnitEffortListAsString = rawSubTaskExpenseAssignment.getData(MigrationTo25.TAG_DATEUNIT_EFFORTS);
		DateUnitEffortList rawSubTaskExpenseAssignmentDateUnitEffortList = new DateUnitEffortList(rawSubTaskExpenseAssignmentDateUnitEffortListAsString);
		assertEquals(rawSubTaskExpenseAssignmentDateUnitEffortList, subTaskExpenseAssignment.getDateUnitEffortList());
	}

	private DateUnitEffortList createDateUnitEffortList(ArrayList<DateUnit> dateUnitList, double units) throws Exception
	{
		DateUnitEffortList list = new DateUnitEffortList();

		for (DateUnit dateUnit : dateUnitList)
		{
			list.add(new DateUnitEffort(dateUnit, units));
		}

		return list;
	}

	private static boolean isAssignmentDataStruckOut(ResourceAssignment assignment, DateUnit dateUnit) throws Exception
	{
		return assignment.getOwner().hasAnyChildTaskResourceData(dateUnit);
	}

	private static boolean isAssignmentDataStruckOut(ExpenseAssignment assignment, DateUnit dateUnit) throws Exception
	{
		return assignment.getOwner().hasAnyChildTaskExpenseData(dateUnit);
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo25.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo25.VERSION_TO;
	}

	private final static DateUnit dateUnit2007 = new DateUnit("YEARFROM:2007-01");
	private final static DateUnit dateUnit2006 = new DateUnit("YEARFROM:2006-01");
	private final static DateUnit dateUnit2005 = new DateUnit("YEARFROM:2005-01");

	private final static DateUnit dateUnit2005Q1 = new DateUnit("2005Q1");
	private final static DateUnit dateUnit2005Q2 = new DateUnit("2005Q2");
	private final static DateUnit dateUnit2005Q3 = new DateUnit("2005Q3");
	private final static DateUnit dateUnit2005Q4 = new DateUnit("2005Q4");

	private final static DateUnit dateUnit200512 = new DateUnit("2005-12");
}
