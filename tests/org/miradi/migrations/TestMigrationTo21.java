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
import org.miradi.migrations.forward.MigrationTo21;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;


public class TestMigrationTo21 extends AbstractTestMigration
{
	public TestMigrationTo21(String name)
	{
		super(name);
	}

	public void testStrategyForwardMigrationNoResourceAssignments() throws Exception
	{
		ORef strategyRef = getProject().createStrategy().getRef();

		ensureForwardMigrationTimeframesNotAdded(strategyRef);
	}

	public void testStrategyForwardMigrationEmptyResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, "");
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, "");
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationTimeframesNotAdded(strategy.getRef());
	}

	public void testStrategyForwardMigrationResourceAssignmentWithOnlyResourceId() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, "");
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationTimeframesAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithNonZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationTimeframesAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationTimeframesAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithMixedEffortResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();

		ResourceAssignment resourceAssignment1 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList1 = new DateUnitEffortList();
		dateUnitEffortList1.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment1, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList1.toJson().toString());

		ResourceAssignment resourceAssignment2 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList2 = new DateUnitEffortList();
		dateUnitEffortList2.add(getProject().createDateUnitEffort(2007, 2007, 1.0));
		getProject().fillObjectUsingCommand(resourceAssignment2, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList2.toJson().toString());

		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment1.getId(), resourceAssignment2.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(StrategySchema.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = reverseMigratedProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertFalse("Timeframes should have been added during forward migration", rawTimeframePool.isEmpty());
		assertEquals("Two timeframes should have been added", rawTimeframePool.size(), 2);

		ORef timeframeRef1 = rawTimeframePool.getSortedReflist().get(0);
		RawObject timeframe1 = reverseMigratedProject.findObject(timeframeRef1);
		verifyTimeframeMatchesResourceAssignment(timeframe1, resourceAssignment1);

		ORef timeframeRef2 = rawTimeframePool.getSortedReflist().get(1);
		RawObject timeframe2 = reverseMigratedProject.findObject(timeframeRef2);
		verifyTimeframeMatchesResourceAssignment(timeframe2, resourceAssignment2);
	}

	public void testStrategyForwardMigrationWithMixedEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		dateUnitEffortList.add(getProject().createDateUnitEffort(2008, 2008, 1.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationTimeframesAdded(strategy.getRef(), resourceAssignment);
	}

	public void testStrategyForwardMigrationWithZeroEffortResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();

		ResourceAssignment resourceAssignment1 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList1 = new DateUnitEffortList();
		DateUnitEffort dateUnitEffort1 = getProject().createDateUnitEffort(2007, 2007, 0.0);
		dateUnitEffortList1.add(dateUnitEffort1);
		getProject().fillObjectUsingCommand(resourceAssignment1, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList1.toJson().toString());

		ResourceAssignment resourceAssignment2 = getProject().createAndPopulateResourceAssignment();
		DateUnitEffortList dateUnitEffortList2 = new DateUnitEffortList();
		DateUnitEffort dateUnitEffort2 = getProject().createDateUnitEffort(2008, 2008, 0.0);
		dateUnitEffortList2.add(dateUnitEffort2);
		getProject().fillObjectUsingCommand(resourceAssignment2, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList2.toJson().toString());

		DateUnitEffortList combinedDateUnitEffortList = new DateUnitEffortList();
		combinedDateUnitEffortList.add(dateUnitEffort1);
		combinedDateUnitEffortList.add(dateUnitEffort2);

		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment1.getId(), resourceAssignment2.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(strategy.getRef().getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = reverseMigratedProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertFalse("Timeframes should have been added during forward migration", rawTimeframePool.isEmpty());
		assertEquals("Two timeframes should have been added", rawTimeframePool.size(), 2);

		ORef timeframeRef1 = rawTimeframePool.getSortedReflist().get(0);
		RawObject timeframe1 = reverseMigratedProject.findObject(timeframeRef1);

		DateUnitEffortList timeframeDateUnitEffortList1 = new DateUnitEffortList(timeframe1.getData(Timeframe.TAG_DATEUNIT_DETAILS));
		verifyTimeframeDateUnitEffortListMatchesThatOfResourceAssignment(timeframeDateUnitEffortList1, combinedDateUnitEffortList);

		ORef timeframeRef2 = rawTimeframePool.getSortedReflist().get(1);
		RawObject timeframe2 = reverseMigratedProject.findObject(timeframeRef2);

		DateUnitEffortList timeframeDateUnitEffortList2 = new DateUnitEffortList(timeframe2.getData(Timeframe.TAG_DATEUNIT_DETAILS));
		verifyTimeframeDateUnitEffortListMatchesThatOfResourceAssignment(timeframeDateUnitEffortList2, combinedDateUnitEffortList);
	}

	private void verifyTimeframeDateUnitEffortListMatchesThatOfResourceAssignment(DateUnitEffortList timeframeDateUnitEffortList, DateUnitEffortList resourceAssignmentDateUnitEffortList) throws Exception
	{
		DateRange timeframeDateRange = getDateRange(timeframeDateUnitEffortList);
		DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

		assertEquals("Quantity on timeframe date unit effort should be 0", timeframeDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
		assertTrue("Timeframe date unit should encompass that on resource assignment", timeframeDateRange.contains(resourceAssignmentDateRange));
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
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(strategy.getRef().getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = reverseMigratedProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertFalse("Timeframes should have been added during forward migration", rawTimeframePool.isEmpty());
		assertEquals("Only one timeframe should have been added", rawTimeframePool.size(), 1);

		ORef timeframeRef = rawTimeframePool.getSortedReflist().get(0);
		RawObject timeframe = reverseMigratedProject.findObject(timeframeRef);

		DateUnitEffortList timeframeDateUnitEffortList = new DateUnitEffortList(timeframe.getData(Timeframe.TAG_DATEUNIT_DETAILS));
		DateRange timeframeDateRange = getDateRange(timeframeDateUnitEffortList);
		DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

		assertEquals("Quantity on timeframe date unit effort should be 0", timeframeDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
		assertTrue("Timeframe date unit should encompass that on resource assignment", timeframeDateRange.contains(resourceAssignmentDateRange));
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
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(strategy.getRef().getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = reverseMigratedProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertFalse("Timeframes should have been added during forward migration", rawTimeframePool.isEmpty());
		assertEquals("Only one timeframe should have been added", rawTimeframePool.size(), 1);

		ORef timeframeRef = rawTimeframePool.getSortedReflist().get(0);
		RawObject timeframe = reverseMigratedProject.findObject(timeframeRef);

		DateUnitEffortList timeframeDateUnitEffortList = new DateUnitEffortList(timeframe.getData(Timeframe.TAG_DATEUNIT_DETAILS));
		DateRange timeframeDateRange = getDateRange(timeframeDateUnitEffortList);
		DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

		assertEquals("Quantity on timeframe date unit effort should be 0", timeframeDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
		assertTrue("Timeframe date unit should encompass that on resource assignment", timeframeDateRange.contains(resourceAssignmentDateRange));
	}

	public void testStrategyReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(strategy, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureReverseMigrationTimeframesRemoved(strategy.getRef());
	}

	public void testTaskForwardMigrationNoResourceAssignments() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		ORef taskRef = getProject().createTask(strategy).getRef();
		ensureForwardMigrationTimeframesNotAdded(taskRef);
	}

	public void testTaskForwardMigrationWithZeroEffortResourceAssignment() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task task = getProject().createTask(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(task, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureForwardMigrationTimeframesAdded(task.getRef(), resourceAssignment);
	}

	public void testTaskReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task task = getProject().createTask(strategy);
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();

		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
		getProject().fillObjectUsingCommand(task, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());

		ensureReverseMigrationTimeframesRemoved(task.getRef());
	}

	// TODO: MRD-6011 - need to revisit...

//	public void testIndicatorForwardMigrationNoResourceAssignments() throws Exception
//	{
//		Strategy strategy = getProject().createStrategy();
//		ORef indicatorRef = getProject().createIndicator(strategy).getRef();
//		ensureForwardMigrationTimeframesNotAdded(indicatorRef);
//	}
//
//	public void testIndicatorForwardMigrationWithZeroEffortResourceAssignment() throws Exception
//	{
//		Strategy strategy = getProject().createStrategy();
//		Indicator indicator = getProject().createIndicator(strategy);
//		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
//
//		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
//		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
//		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
//		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
//		getProject().fillObjectUsingCommand(indicator, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());
//
//		ensureForwardMigrationTimeframesAdded(indicator.getRef(), resourceAssignment);
//	}
//
//	public void testIndicatorReverseMigration() throws Exception
//	{
//		Strategy strategy = getProject().createStrategy();
//		Indicator indicator = getProject().createIndicator(strategy);
//		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
//
//		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
//		dateUnitEffortList.add(getProject().createDateUnitEffort(2007, 2007, 0.0));
//		getProject().fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toJson().toString());
//		IdList idList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[]{resourceAssignment.getId()});
//		getProject().fillObjectUsingCommand(indicator, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, idList.toJson().toString());
//
//		ensureReverseMigrationTimeframesRemoved(indicator.getRef());
//	}

	private void ensureForwardMigrationTimeframesNotAdded(ORef objectRef) throws Exception
	{
		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(objectRef.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertFalse("Field should not have been added during forward migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = reverseMigratedProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertTrue("No timeframes should have been added during forward migration", rawTimeframePool == null || rawTimeframePool.isEmpty());
	}

	private void ensureForwardMigrationTimeframesAdded(ORef objectRef, ResourceAssignment resourceAssignment) throws Exception
	{
		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawPool rawPoolForType = reverseMigratedProject.getRawPoolForType(objectRef.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = reverseMigratedProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertFalse("Timeframes should have been added during forward migration", rawTimeframePool.isEmpty());
		assertEquals("Only one timeframe should have been added", rawTimeframePool.size(), 1);

		ORef timeframeRef = rawTimeframePool.getSortedReflist().get(0);
		RawObject timeframe = reverseMigratedProject.findObject(timeframeRef);
		verifyTimeframeMatchesResourceAssignment(timeframe, resourceAssignment);
	}

	private void verifyTimeframeMatchesResourceAssignment(RawObject timeframe, ResourceAssignment resourceAssignment) throws Exception
	{
		if (!resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS).isEmpty())
		{
			DateUnitEffortList timeframeDateUnitEffortList = new DateUnitEffortList(timeframe.getData(Timeframe.TAG_DATEUNIT_DETAILS));
			DateRange timeframeDateRange = getDateRange(timeframeDateUnitEffortList);

			DateUnitEffortList resourceAssignmentDateUnitEffortList = new DateUnitEffortList(resourceAssignment.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
			DateRange resourceAssignmentDateRange = getDateRange(resourceAssignmentDateUnitEffortList);

			assertEquals("Quantity on timeframe date unit effort should be 0", timeframeDateUnitEffortList.getDateUnitEffort(0).getQuantity(), 0.0);
			assertTrue("Timeframe date unit should encompass that on resource assignment", timeframeDateRange.contains(resourceAssignmentDateRange));
		}
	}

	private void ensureReverseMigrationTimeframesRemoved(ORef objectRef) throws Exception
	{
		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));

		RawPool rawPoolForType = rawProject.getRawPoolForType(objectRef.getObjectType());
		for(ORef ref : rawPoolForType.keySet())
		{
			RawObject rawObject = rawPoolForType.get(ref);
			assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo21.TAG_TIMEFRAME_IDS));
		}

		RawPool rawTimeframePool = rawProject.getRawPoolForType(TimeframeSchema.getObjectType());
		assertTrue("Timeframes should have been removed during forward migration", rawTimeframePool == null || rawTimeframePool.isEmpty());
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
		return MigrationTo21.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo21.VERSION_TO;
	}
}
