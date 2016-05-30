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

import org.miradi.migrations.forward.MigrationTo28;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.project.Project;

public class TestMigrationTo28 extends AbstractTestMigration
{
	public TestMigrationTo28(String name)
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

		assertFalse(strategyResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(activityResourceAssignment.isAssignmentDataSuperseded(dateUnit2006));
		assertFalse(subTaskResourceAssignment.isAssignmentDataSuperseded(dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
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

		assertTrue(strategyResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertTrue(activityResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(subTaskResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
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

		assertFalse(strategyExpenseAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(activityExpenseAssignment.isAssignmentDataSuperseded(dateUnit2006));
		assertFalse(subTaskExpenseAssignment.isAssignmentDataSuperseded(dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
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

		assertTrue(strategyExpenseAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertTrue(activityExpenseAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(subTaskExpenseAssignment.isAssignmentDataSuperseded(dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
	}

	public void testForwardMigrationIndicatorNoResourceAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ResourceAssignment indicatorResourceAssignment = getProject().addResourceAssignment(indicator, 1.0, 2005, 2005);

		Task method = getProject().createMethod(indicator);
		ResourceAssignment methodResourceAssignment = getProject().addResourceAssignment(method, 2.0, 2006, 2006);

		Task subTask = getProject().createTask(method);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2007, 2007);

		assertFalse(indicatorResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(methodResourceAssignment.isAssignmentDataSuperseded(dateUnit2006));
		assertFalse(subTaskResourceAssignment.isAssignmentDataSuperseded(dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);
	}

	public void testForwardMigrationIndicatorResourceAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ResourceAssignment indicatorResourceAssignment = getProject().addResourceAssignment(indicator, 1.0, 2005, 2005);

		Task method = getProject().createMethod(indicator);
		ResourceAssignment methodResourceAssignment = getProject().addResourceAssignment(method, 2.0, 2005, 2005);

		Task subTask = getProject().createTask(method);
		ResourceAssignment subTaskResourceAssignment = getProject().addResourceAssignment(subTask, 3.0, 2005, 2005);

		assertTrue(indicatorResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertTrue(methodResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(subTaskResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.RESOURCE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
	}

	public void testForwardMigrationIndicatorNoExpenseAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ExpenseAssignment indicatorResourceAssignment = getProject().addExpenseAssignment(indicator, dateUnit2005, 1.0);

		Task method = getProject().createMethod(indicator);
		ExpenseAssignment methodResourceAssignment = getProject().addExpenseAssignment(method, dateUnit2006, 2.0);

		Task subTask = getProject().createTask(method);
		ExpenseAssignment subTaskExpenseAssignment = getProject().addExpenseAssignment(subTask, dateUnit2007, 3.0);

		assertFalse(indicatorResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(methodResourceAssignment.isAssignmentDataSuperseded(dateUnit2006));
		assertFalse(subTaskExpenseAssignment.isAssignmentDataSuperseded(dateUnit2007));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 3);
	}

	public void testForwardMigrationIndicatorExpenseAssignmentsRemoved() throws Exception
	{
		getProject().setProjectStartDate(2005);
		getProject().setProjectEndDate(2007);

		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ExpenseAssignment indicatorResourceAssignment = getProject().addExpenseAssignment(indicator, dateUnit2005, 1.0);

		Task method = getProject().createMethod(indicator);
		ExpenseAssignment methodResourceAssignment = getProject().addExpenseAssignment(method, dateUnit2005, 2.0);

		Task subTask = getProject().createTask(method);
		ExpenseAssignment subTaskExpenseAssignment = getProject().addExpenseAssignment(subTask, dateUnit2005, 3.0);

		assertTrue(indicatorResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertTrue(methodResourceAssignment.isAssignmentDataSuperseded(dateUnit2005));
		assertFalse(subTaskExpenseAssignment.isAssignmentDataSuperseded(dateUnit2005));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = migratedProject.getRawPoolForType(ObjectType.EXPENSE_ASSIGNMENT);
		assertEquals(rawPoolForType.size(), 1);
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo28.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo28.VERSION_TO;
	}

	private final static DateUnit dateUnit2007 = new DateUnit("YEARFROM:2007-01");
	private final static DateUnit dateUnit2006 = new DateUnit("YEARFROM:2006-01");
	private final static DateUnit dateUnit2005 = new DateUnit("YEARFROM:2005-01");
}
