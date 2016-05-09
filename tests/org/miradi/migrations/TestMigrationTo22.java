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

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.migrations.forward.MigrationTo22;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;


public class TestMigrationTo22 extends AbstractTestMigration
{
	public TestMigrationTo22(String name)
	{
		super(name);
	}

	public void testStrategyForwardMigrationNoResourceAssignments() throws Exception
	{
		ORef strategyRef = getProject().createStrategy().getRef();

		ensureForwardMigrationResourcePlansNotAdded(strategyRef);
	}

	public void testStrategyForwardMigrationEmptyResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, "");
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, "");
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansNotAdded(strategy.getRef());
	}

	public void testStrategyForwardMigrationResourceAssignmentWithOnlyResourceId() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, "");
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithNonZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithMixedEffortResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();

		ResourceAssignment resourceAssignment1 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList1 = new DateUnitEffortList();
		dateUnitEffortList1.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment1, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList1.toJson().toString());

		ResourceAssignment resourceAssignment2 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList2 = new DateUnitEffortList();
		dateUnitEffortList2.add(getProject().createDateUnitEffort(2007, 2007, 1.0));
		getProject().fillObjectUsingCommand(resourceAssignment2, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList2.toJson().toString());

		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment1.getId(), resourceAssignment2.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(StrategySchema.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = reverseMigratedProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertFalse("Resource plans should have been added during forward migration", rawResourcePlanPool.isEmpty());
		assertEquals("Two resource plans should have been added", rawResourcePlanPool.size(), 2);

		ORef resourcePlanRef1 = rawResourcePlanPool.getSortedReflist().get(0);
		RawObject resourcePlan1 = reverseMigratedProject.findObject(resourcePlanRef1);
		verifyResourcePlanMatchesResourceAssignment(resourcePlan1, resourceAssignment1);

		ORef resourcePlanRef2 = rawResourcePlanPool.getSortedReflist().get(1);
		RawObject resourcePlan2 = reverseMigratedProject.findObject(resourcePlanRef2);
		verifyResourcePlanMatchesResourceAssignment(resourcePlan2, resourceAssignment2);
	}

	public void testStrategyForwardMigrationWithMixedEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		dateUnitEffortList.add(getProject().createDateUnitEffort(2008, 2008, 1.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithZeroEffortResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();

		ResourceAssignment resourceAssignment1 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList1 = new DateUnitEffortList();
		DateUnitEffort dateUnitEffort1 = getProject().createDateUnitEffort(2007, 2007, 0.0);
		dateUnitEffortList1.add(dateUnitEffort1);
		getProject().fillObjectUsingCommand(resourceAssignment1, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList1.toJson().toString());

		ResourceAssignment resourceAssignment2 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList2 = new DateUnitEffortList();
		DateUnitEffort dateUnitEffort2 = getProject().createDateUnitEffort(2008, 2008, 0.0);
		dateUnitEffortList2.add(dateUnitEffort2);
		getProject().fillObjectUsingCommand(resourceAssignment2, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList2.toJson().toString());

		DateUnitEffortList combinedDateUnitEffortList = new DateUnitEffortList();
		combinedDateUnitEffortList.add(dateUnitEffort1);
		combinedDateUnitEffortList.add(dateUnitEffort2);

		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment1.getId(), resourceAssignment2.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(strategy.getRef().getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = reverseMigratedProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertFalse("Resource plans should have been added during forward migration", rawResourcePlanPool.isEmpty());
		assertEquals("Two resource plans should have been added", rawResourcePlanPool.size(), 2);

		ORef resourcePlanRef1 = rawResourcePlanPool.getSortedReflist().get(0);
		RawObject resourcePlan1 = reverseMigratedProject.findObject(resourcePlanRef1);

		assertEquals("Resource populated on resource plan should match that on resource assignment", resourcePlan1.getData(ResourcePlan.TAG_RESOURCE_ID), resourceAssignment1.getData(ResourcePlan.TAG_RESOURCE_ID));

		DateUnitEffortList resourcePlanDateUnitEffortList1 = new DateUnitEffortList(resourcePlan1.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		verifyResourcePlanDateUnitEffortListMatchesThatOfResourceAssignment(resourcePlanDateUnitEffortList1, combinedDateUnitEffortList);

		ORef resourcePlanRef2 = rawResourcePlanPool.getSortedReflist().get(1);
		RawObject resourcePlan2 = reverseMigratedProject.findObject(resourcePlanRef2);

		assertEquals("Resource populated on resource plan should match that on resource assignment", resourcePlan2.getData(ResourcePlan.TAG_RESOURCE_ID), resourceAssignment2.getData(ResourcePlan.TAG_RESOURCE_ID));

		DateUnitEffortList resourcePlanDateUnitEffortList2 = new DateUnitEffortList(resourcePlan2.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		verifyResourcePlanDateUnitEffortListMatchesThatOfResourceAssignment(resourcePlanDateUnitEffortList2, combinedDateUnitEffortList);
	}

	private void verifyResourcePlanDateUnitEffortListMatchesThatOfResourceAssignment(DateUnitEffortList resourcePlanDateUnitEffortList, DateUnitEffortList resourceAssignmentDateUnitEffortList) throws Exception
	{
		DateRange resourcePlanDateRange = getDateRange(resourcePlanDateUnitEffortList);
		DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
		assertTrue("Resource plan date unit should encompass that on resource assignment", resourcePlanDateRange.contains(resourceAssignmentDateRange));
	}

	public void testStrategyForwardMigrationWithResourceAssignmentMultipleDays() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		MultiCalendar cal1 = MultiCalendar.createFromGregorianYearMonthDay(2007, 12, 1);
		DateUnit dateUnit1 = new DateUnit(cal1.toIsoDateString());
		dateUnitEffortList.add(new DateUnitEffort(dateUnit1, 0.0));
		MultiCalendar cal2 = MultiCalendar.createFromGregorianYearMonthDay(2007, 12, 2);
		DateUnit dateUnit2 = new DateUnit(cal2.toIsoDateString());
		dateUnitEffortList.add(new DateUnitEffort(dateUnit2, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(strategy.getRef().getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = reverseMigratedProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertFalse("Resource plans should have been added during forward migration", rawResourcePlanPool.isEmpty());
		assertEquals("Only one resource plan should have been added", rawResourcePlanPool.size(), 1);

		ORef resourcePlanRef = rawResourcePlanPool.getSortedReflist().get(0);
		RawObject resourcePlan = reverseMigratedProject.findObject(resourcePlanRef);

		DateUnitEffortList resourcePlanDateUnitEffortList = new DateUnitEffortList(resourcePlan.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		DateRange resourcePlanDateRange = getDateRange(resourcePlanDateUnitEffortList);
		DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_EFFORTS));
		DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
		assertTrue("Resource plan date unit should encompass that on resource assignment", resourcePlanDateRange.contains(resourceAssignmentDateRange));
		assertEquals("Resource populated on resource plan should match that on resource assignment", resourcePlan.getData(ResourcePlan.TAG_RESOURCE_ID), resourceAssignment.getData(ResourcePlan.TAG_RESOURCE_ID));
	}

	public void testStrategyForwardMigrationWithResourceAssignmentMultipleMonths() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		MultiCalendar cal1 = MultiCalendar.createFromGregorianYearMonthDay(2007, 11, 1);
		DateUnit dateUnit1 = new DateUnit(cal1.toIsoDateString());
		dateUnitEffortList.add(new DateUnitEffort(dateUnit1, 0.0));
		MultiCalendar cal2 = MultiCalendar.createFromGregorianYearMonthDay(2007, 12, 1);
		DateUnit dateUnit2 = new DateUnit(cal2.toIsoDateString());
		dateUnitEffortList.add(new DateUnitEffort(dateUnit2, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(strategy.getRef().getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = reverseMigratedProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertFalse("Resource plans should have been added during forward migration", rawResourcePlanPool.isEmpty());
		assertEquals("Only one resource plan should have been added", rawResourcePlanPool.size(), 1);

		ORef resourcePlanRef = rawResourcePlanPool.getSortedReflist().get(0);
		RawObject resourcePlan = reverseMigratedProject.findObject(resourcePlanRef);

		DateUnitEffortList resourcePlanDateUnitEffortList = new DateUnitEffortList(resourcePlan.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		DateRange resourcePlanDateRange = getDateRange(resourcePlanDateUnitEffortList);
		DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_EFFORTS));
		DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
		assertTrue("Resource plan date unit should encompass that on resource assignment", resourcePlanDateRange.contains(resourceAssignmentDateRange));
	}

	public void testStrategyReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureReverseMigrationResourcePlansRemoved(strategy.getRef());
	}

	public void testTaskForwardMigrationNoResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef taskRef = getProject().createTask(strategy).getRef();
		ensureForwardMigrationResourcePlansNotAdded(taskRef);
	}

	public void testTaskForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task task = getProject().createTask(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(task, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansAdded(task.getRef(), resourceAssignment);
	}

	public void testTaskReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task task = getProject().createTask(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(task, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureReverseMigrationResourcePlansRemoved(task.getRef());
	}

	public void testIndicatorForwardMigrationNoResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef indicatorRef = getProject().createIndicator(strategy).getRef();
		ensureForwardMigrationResourcePlansNotAdded(indicatorRef);
	}

	public void testIndicatorForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(indicator, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationResourcePlansAdded(indicator.getRef(), resourceAssignment);
	}

	public void testIndicatorReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(indicator, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureReverseMigrationResourcePlansRemoved(indicator.getRef());
	}

	private void ensureForwardMigrationResourcePlansNotAdded(ORef objectRef) throws Exception
	{
		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(objectRef.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertFalse("Field should not have been added during forward migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = reverseMigratedProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertTrue("No resource plans should have been added during forward migration", rawResourcePlanPool == null || rawResourcePlanPool.isEmpty());
	}

	private void ensureForwardMigrationResourcePlansAdded(ORef objectRef, ResourceAssignment resourceAssignment) throws Exception
	{
		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(objectRef.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = reverseMigratedProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertFalse("Resource plans should have been added during forward migration", rawResourcePlanPool.isEmpty());
		assertEquals("Only one resource plan should have been added", rawResourcePlanPool.size(), 1);

		ORef resourcePlanRef = rawResourcePlanPool.getSortedReflist().get(0);
		RawObject resourcePlan = reverseMigratedProject.findObject(resourcePlanRef);
		verifyResourcePlanMatchesResourceAssignment(resourcePlan, resourceAssignment);
	}

	private void verifyResourcePlanMatchesResourceAssignment(RawObject resourcePlan, ResourceAssignment resourceAssignment) throws Exception
	{
		if (!resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_EFFORTS).isEmpty())
		{
			DateUnitEffortList resourcePlanDateUnitEffortList = new DateUnitEffortList(resourcePlan.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
			DateRange resourcePlanDateRange = getDateRange(resourcePlanDateUnitEffortList);

			DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_EFFORTS));
			DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

			assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
			assertTrue("Resource plan date unit should encompass that on resource assignment", resourcePlanDateRange.contains(resourceAssignmentDateRange));
		}

		if (!resourceAssignment.getData(ResourceAssignment.TAG_RESOURCE_ID).isEmpty())
		{
			assertEquals("Resource populated on resource plan should match that on resource assignment", resourcePlan.getData(ResourcePlan.TAG_RESOURCE_ID), resourceAssignment.getData(ResourcePlan.TAG_RESOURCE_ID));
		}
	}

	private void ensureReverseMigrationResourcePlansRemoved(ORef objectRef) throws Exception
	{
		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo22.VERSION_TO));

		RawPool rawPoolForType = rawProject.getRawPoolForType(objectRef.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo22.TAG_RESOURCE_PLAN_IDS));
		}

		RawPool rawResourcePlanPool = rawProject.getRawPoolForType(ResourcePlanSchema.getObjectType());
		assertTrue("Resource plans should have been removed during forward migration", rawResourcePlanPool == null || rawResourcePlanPool.isEmpty());
	}

	private DateRange getDateRange(DateUnitEffortList dateUnitEffortList) throws Exception
	{
		DateRange dateRange = null;

		for (int index = 0; index < dateUnitEffortList.size(); ++index)
		{
			DateUnit dateUnit = dateUnitEffortList.getDateUnitEffort(index).getDateUnit();
			DateRange candidateDateRange = getProject().getProjectCalendar().convertToDateRange(dateUnit);
			dateRange = DateRange.combine(dateRange, candidateDateRange);
		}

		return dateRange;
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo22.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo22.VERSION_TO;
	}
}
