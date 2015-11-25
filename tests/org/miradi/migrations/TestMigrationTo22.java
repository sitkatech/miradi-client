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

		ensureResourcePlansNotAdded(strategyRef);
	}

	public void testStrategyForwardMigrationEmptyResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, "");
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansNotAdded(strategy.getRef());
	}

	public void testStrategyForwardMigrationWithNonZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansNotAdded(strategy.getRef());
	}

	public void testStrategyForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2008, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithMixedEffortResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();

		ResourceAssignment resourceAssignment1 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList1 = new DateUnitEffortList();
		dateUnitEffortList1.add(getProject().createDateUnitEffort(2007, 2008, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment1, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList1.toJson().toString());

		ResourceAssignment resourceAssignment2 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList2 = new DateUnitEffortList();
		dateUnitEffortList2.add(getProject().createDateUnitEffort(2007, 2008, 1.0));
		getProject().fillObjectUsingCommand(resourceAssignment2, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList2.toJson().toString());

		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment1.getId(), resourceAssignment2.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansNotAdded(strategy.getRef());
	}

	public void testStrategyForwardMigrationWithMixedEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2008, 0.0));
		dateUnitEffortList.add(getProject().createDateUnitEffort(2008, 2009, 1.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansNotAdded(strategy.getRef());
	}

	public void testStrategyForwardMigrationWithZeroEffortResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();

		ResourceAssignment resourceAssignment1 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList1 = new DateUnitEffortList();
		DateUnitEffort dateUnitEffort1 = getProject().createDateUnitEffort(2007, 2008, 0.0);
		dateUnitEffortList1.add(dateUnitEffort1);
		getProject().fillObjectUsingCommand(resourceAssignment1, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList1.toJson().toString());

		ResourceAssignment resourceAssignment2 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList2 = new DateUnitEffortList();
		DateUnitEffort dateUnitEffort2 = getProject().createDateUnitEffort(2008, 2009, 0.0);
		dateUnitEffortList2.add(dateUnitEffort2);
		getProject().fillObjectUsingCommand(resourceAssignment2, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList2.toJson().toString());

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
		DateUnitEffortList resourcePlanDateUnitEffortList1 = new DateUnitEffortList(resourcePlan1.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		assertEquals("Only one date unit effort should have been added for the resource plan", resourcePlanDateUnitEffortList1.size(), 1);

		DateUnitEffort resourcePlanDateUnitEffort1 = resourcePlanDateUnitEffortList1.getDateUnitEffort(0);
		DateUnit resourcePlanDateUnit1 = resourcePlanDateUnitEffort1.getDateUnit();
		assertEquals("Date unit of resource plan effort should match that on assignment", resourcePlanDateUnit1, dateUnitEffort1.getDateUnit());
		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffort1.getQuantity(), 0.0);

		ORef resourcePlanRef2 = rawResourcePlanPool.getSortedReflist().get(1);
		RawObject resourcePlan2 = reverseMigratedProject.findObject(resourcePlanRef2);
		DateUnitEffortList resourcePlanDateUnitEffortList2 = new DateUnitEffortList(resourcePlan2.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		assertEquals("Only one date unit effort should have been added for the resource plan", resourcePlanDateUnitEffortList2.size(), 1);

		DateUnitEffort resourcePlanDateUnitEffort2 = resourcePlanDateUnitEffortList2.getDateUnitEffort(0);
		DateUnit resourcePlanDateUnit2 = resourcePlanDateUnitEffort2.getDateUnit();
		assertEquals("Date unit of resource plan effort should match that on assignment", resourcePlanDateUnit2, dateUnitEffort2.getDateUnit());
		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffort2.getQuantity(), 0.0);
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
		assertEquals("Only one date unit effort should have been added for the resource plan (since source date units for the assignment were for same month)", resourcePlanDateUnitEffortList.size(), 1);

		DateUnitEffort resourcePlanDateUnitEffort = resourcePlanDateUnitEffortList.getDateUnitEffort(0);
		DateUnit resourcePlanDateUnit = resourcePlanDateUnitEffort.getDateUnit();
		assertTrue("Date unit for resource plan should be month", resourcePlanDateUnit.isMonth());
		assertEquals("Month for resource plan date unit should match date on assignment date unit effort", resourcePlanDateUnit.getMonth(), cal1.getGregorianMonth());
		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffort.getQuantity(), 0.0);
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
		assertEquals("Two date unit efforts should have been added for the resource plan (since source date units for the assignment were for different month)", resourcePlanDateUnitEffortList.size(), 2);

		DateUnitEffort resourcePlanDateUnitEffort1 = resourcePlanDateUnitEffortList.getDateUnitEffort(0);
		DateUnit resourcePlanDateUnit1 = resourcePlanDateUnitEffort1.getDateUnit();
		assertTrue("Date unit for resource plan should be month", resourcePlanDateUnit1.isMonth());
		assertEquals("Month for resource plan date unit should match date on assignment date unit effort", resourcePlanDateUnit1.getMonth(), cal1.getGregorianMonth());
		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffort1.getQuantity(), 0.0);

		DateUnitEffort resourcePlanDateUnitEffort2 = resourcePlanDateUnitEffortList.getDateUnitEffort(1);
		DateUnit resourcePlanDateUnit2 = resourcePlanDateUnitEffort2.getDateUnit();
		assertTrue("Date unit for resource plan should be month", resourcePlanDateUnit2.isMonth());
		assertEquals("Month for resource plan date unit should match date on assignment date unit effort", resourcePlanDateUnit2.getMonth(), cal2.getGregorianMonth());
		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffort2.getQuantity(), 0.0);
	}

	public void testTaskForwardMigrationNoResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef taskRef = getProject().createTask(strategy).getRef();
		ensureResourcePlansNotAdded(taskRef);
	}

	public void testTaskForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task task = getProject().createTask(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2008, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(task, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansAdded(task.getRef(), resourceAssignment);
	}

	public void testIndicatorForwardMigrationNoResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef indicatorRef = getProject().createIndicator(strategy).getRef();
		ensureResourcePlansNotAdded(indicatorRef);
	}

	public void testIndicatorForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2008, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(indicator, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureResourcePlansAdded(indicator.getRef(), resourceAssignment);
	}

	private void ensureResourcePlansNotAdded(ORef objectRef) throws Exception
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

	private void ensureResourcePlansAdded(ORef objectRef, ResourceAssignment resourceAssignment) throws Exception
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
		DateUnitEffortList resourcePlanDateUnitEffortList = new DateUnitEffortList(resourcePlan.getData(ResourcePlan.TAG_DATEUNIT_EFFORTS));
		DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_EFFORTS));
		assertEquals("Resource plan date unit effort list should match that on resource assignment", resourceAssignmentDateUnitEffortList, resourcePlanDateUnitEffortList);

		DateUnitEffort resourcePlanDateUnitEffort = resourcePlanDateUnitEffortList.getDateUnitEffort(0);
		assertEquals("Quantity on resource plan date unit effort should be 0", resourcePlanDateUnitEffort.getQuantity(), 0.0);
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
