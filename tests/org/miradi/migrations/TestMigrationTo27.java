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

import org.miradi.migrations.forward.MigrationTo27;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.util.ArrayList;

public class TestMigrationTo27 extends AbstractTestMigration
{
	public TestMigrationTo27(String name)
	{
		super(name);
	}

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

		assertFalse(isAssignmentDataSuperseded(strategyResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataSuperseded(activityResourceAssignment, dateUnit2006));
		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);
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

		assertTrue(isAssignmentDataSuperseded(strategyResourceAssignment, dateUnit2005));
		assertTrue(isAssignmentDataSuperseded(activityResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
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

		assertTrue(isAssignmentDataSuperseded(strategyResourceAssignment, dateUnit2005));
		assertTrue(isAssignmentDataSuperseded(activityResourceAssignment, dateUnit2005));
		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
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

		assertFalse(isAssignmentDataSuperseded(strategyResourceAssignment, dateUnit2005Q1));
		assertTrue(isAssignmentDataSuperseded(strategyResourceAssignment, dateUnit2005Q2));
		assertFalse(isAssignmentDataSuperseded(activityResourceAssignment, dateUnit2005Q2));
		assertTrue(isAssignmentDataSuperseded(activityResourceAssignment, dateUnit2005Q3));
		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2005Q3));
		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2005Q4));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);
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

		assertFalse(isAssignmentDataSuperseded(strategyExpenseAssignment, dateUnit2005));
		assertFalse(isAssignmentDataSuperseded(activityExpenseAssignment, dateUnit2006));
		assertFalse(isAssignmentDataSuperseded(subTaskExpenseAssignment, dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);
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

		assertTrue(isAssignmentDataSuperseded(strategyExpenseAssignment, dateUnit2005));
		assertTrue(isAssignmentDataSuperseded(activityExpenseAssignment, dateUnit2005));
		assertFalse(isAssignmentDataSuperseded(subTaskExpenseAssignment, dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
	}

	// TODO: MRD-6011 - need to revisit...

	public void testForwardMigrationIndicatorNoResourceAssignmentsRemoved() throws Exception
	{
//		getProject().setProjectStartDate(2005);
//		getProject().setProjectEndDate(2007);
//
//		Strategy strategy = getProject().createStrategy();
//		Indicator indicator = getProject().createIndicator(strategy);
//		ResourceAssignment indicatorResourceAssignment = getProject().addResourceAssignment(indicator, 1.0, 2005, 2005);
//
//		Task method = getProject().createMethod(indicator);
//		ResourceAssignment methodResourceAssignment = getProject().addResourceAssignment(method, 2.0, 2006, 2006);
//
//		Task subTask = getProject().createTask(method);
//		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2007, 2007);
//
//		assertFalse(isAssignmentDataSuperseded(indicatorResourceAssignment, dateUnit2005));
//		assertFalse(isAssignmentDataSuperseded(methodResourceAssignment, dateUnit2006));
//		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2007));
//
//		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
//		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));
//
//		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
//		assertEquals(rawPoolForType.size(), 3);
	}

	public void testForwardMigrationIndicatorResourceAssignmentsRemoved() throws Exception
	{
//		getProject().setProjectStartDate(2005);
//		getProject().setProjectEndDate(2007);
//
//		Strategy strategy = getProject().createStrategy();
//		Indicator indicator = getProject().createIndicator(strategy);
//		ResourceAssignment indicatorResourceAssignment = getProject().addResourceAssignment(indicator, 1.0, 2005, 2005);
//
//		Task method = getProject().createMethod(indicator);
//		ResourceAssignment methodResourceAssignment = getProject().addResourceAssignment(method, 2.0, 2005, 2005);
//
//		Task subTask = getProject().createTask(method);
//		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2005, 2005);
//
//		assertTrue(isAssignmentDataSuperseded(indicatorResourceAssignment, dateUnit2005));
//		assertTrue(isAssignmentDataSuperseded(methodResourceAssignment, dateUnit2005));
//		assertFalse(isAssignmentDataSuperseded(subTaskResourceAssignment, dateUnit2005));
//
//		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
//		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));
//
//		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
//		assertEquals(rawPoolForType.size(), 1);
	}

	public void testForwardMigrationIndicatorNoExpenseAssignmentsRemoved() throws Exception
	{
//		getProject().setProjectStartDate(2005);
//		getProject().setProjectEndDate(2007);
//
//		Strategy strategy = getProject().createStrategy();
//		Indicator indicator = getProject().createIndicator(strategy);
//		ExpenseAssignment indicatorResourceAssignment = getProject().addExpenseAssignment(indicator, dateUnit2005, 1.0);
//
//		Task method = getProject().createMethod(indicator);
//		ExpenseAssignment methodResourceAssignment = getProject().addExpenseAssignment(method, dateUnit2006, 2.0);
//
//		Task subTask = getProject().createTask(method);
//		ExpenseAssignment subTaskExpenseAssignment = getProject().addExpenseAssignment(subTask, dateUnit2007, 3.0);
//
//		assertFalse(isAssignmentDataSuperseded(indicatorResourceAssignment, dateUnit2005));
//		assertFalse(isAssignmentDataSuperseded(methodResourceAssignment, dateUnit2006));
//		assertFalse(isAssignmentDataSuperseded(subTaskExpenseAssignment, dateUnit2007));
//
//		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
//		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));
//
//		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
//		assertEquals(rawPoolForType.size(), 3);
	}

	public void testForwardMigrationIndicatorExpenseAssignmentsRemoved() throws Exception
	{
//		getProject().setProjectStartDate(2005);
//		getProject().setProjectEndDate(2007);
//
//		Strategy strategy = getProject().createStrategy();
//		Indicator indicator = getProject().createIndicator(strategy);
//		ExpenseAssignment indicatorResourceAssignment = getProject().addExpenseAssignment(indicator, dateUnit2005, 1.0);
//
//		Task method = getProject().createMethod(indicator);
//		ExpenseAssignment methodResourceAssignment = getProject().addExpenseAssignment(method, dateUnit2005, 2.0);
//
//		Task subTask = getProject().createTask(method);
//		ExpenseAssignment subTaskExpenseAssignment = getProject().addExpenseAssignment(subTask, dateUnit2005, 3.0);
//
//		assertTrue(isAssignmentDataSuperseded(indicatorResourceAssignment, dateUnit2005));
//		assertTrue(isAssignmentDataSuperseded(methodResourceAssignment, dateUnit2005));
//		assertFalse(isAssignmentDataSuperseded(subTaskExpenseAssignment, dateUnit2005));
//
//		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo27.VERSION_TO));
//		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));
//
//		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
//		assertEquals(rawPoolForType.size(), 1);
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

	private static boolean isAssignmentDataSuperseded(ResourceAssignment assignment, DateUnit dateUnit) throws Exception
	{
		return assignment.getOwner().hasAnyChildTaskResourceData(dateUnit);
	}

	private static boolean isAssignmentDataSuperseded(ExpenseAssignment assignment, DateUnit dateUnit) throws Exception
	{
		return assignment.getOwner().hasAnyChildTaskExpenseData(dateUnit);
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo27.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo27.VERSION_TO;
	}

	private final static DateUnit dateUnit2007 = new DateUnit("YEARFROM:2007-01");
	private final static DateUnit dateUnit2006 = new DateUnit("YEARFROM:2006-01");
	private final static DateUnit dateUnit2005 = new DateUnit("YEARFROM:2005-01");

	private final static DateUnit dateUnit2005Q1 = new DateUnit("2005Q1");
	private final static DateUnit dateUnit2005Q2 = new DateUnit("2005Q2");
	private final static DateUnit dateUnit2005Q3 = new DateUnit("2005Q3");
	private final static DateUnit dateUnit2005Q4 = new DateUnit("2005Q4");
}
