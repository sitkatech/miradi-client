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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.migrations.forward.MigrationTo28;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.util.Vector;

import static org.miradi.objects.BaseObject.TAG_LABEL;
import static org.miradi.objects.BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;
import static org.miradi.objects.Strategy.TAG_ACTIVITY_IDS;

public class TestMigrationTo28 extends AbstractTestMigration
{
	public TestMigrationTo28(String name)
	{
		super(name);
	}

	public void testSharedActivitiesSplitByForwardMigration() throws Exception
	{
		double numberOfUnits = 10.0;

		Strategy strategy1 = getProject().createAndPopulateStrategy();
		ORefList strategy1ActivityRefs = strategy1.getActivityRefs();
		Task activity1 = (Task) getProject().findObject(strategy1ActivityRefs.get(0));

		Strategy strategy2 = getProject().createAndPopulateStrategy();
		ORefList strategy2ActivityRefs = strategy2.getActivityRefs();
		Task activity2 = (Task) getProject().findObject(strategy2ActivityRefs.get(0));
		CommandSetObjectData cmdToShareActivity1 = CommandSetObjectData.createAppendIdCommand(strategy2, Strategy.TAG_ACTIVITY_IDS, activity1.getId());
		getProject().executeCommand(cmdToShareActivity1);

		assertEquals(strategy1.getActivityRefs().size(), 1);
		assertEquals(activity1.getResourceAssignmentRefs().size(), 1);

		ORef resourceAssignmentForActivity1Ref = activity1.getResourceAssignmentRefs().toArray()[0];
		ResourceAssignment resourceAssignmentForActivity1 = (ResourceAssignment) getProject().findObject(resourceAssignmentForActivity1Ref);
		assertEquals(resourceAssignmentForActivity1.getDateUnitEffortList().size(), 1);
		DateUnitEffort dateUnitEffortForResourceAssignment1 = resourceAssignmentForActivity1.getDateUnitEffortList().getDateUnitEffort(0);
		assertEquals(dateUnitEffortForResourceAssignment1.getQuantity(), numberOfUnits);

		assertEquals(strategy2.getActivityRefs().size(), 2);
		assertTrue(strategy2.getActivityRefs().contains(activity1.getRef()));
		assertTrue(strategy2.getActivityRefs().contains(activity2.getRef()));

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo28.VERSION_TO));
		migrateProject(rawProject, new VersionRange(MigrationTo28.VERSION_TO));

		RawPool strategyPool = rawProject.getRawPoolForType(ObjectType.STRATEGY);
		assertNotNull(strategyPool);
		assertEquals(strategyPool.keySet().size(), 2);

		RawObject rawStrategy1 = rawProject.findObject(strategy1.getRef());
		assertNotNull(rawStrategy1);
		assertTrue(rawStrategy1.containsKey(TAG_ACTIVITY_IDS));
		IdList activityIdListForRawStrategy1 = new IdList(TaskSchema.getObjectType(), rawStrategy1.get(TAG_ACTIVITY_IDS));
		assertEquals(activityIdListForRawStrategy1, new IdList(activity1));

		RawObject rawActivity1 = rawProject.findObject(activity1.getRef());
		assertTrue(rawActivity1.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS));
		IdList assignmentIdListForRawActivity1 = new IdList(ResourceAssignmentSchema.getObjectType(), rawActivity1.get(TAG_RESOURCE_ASSIGNMENT_IDS));
		assertEquals(assignmentIdListForRawActivity1, new IdList(resourceAssignmentForActivity1));

		RawObject rawAssignment1 = rawProject.findObject(resourceAssignmentForActivity1Ref);
		assertTrue(rawAssignment1.containsKey(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		DateUnitEffortList dateUnitEffortListForRawAssignment1 = new DateUnitEffortList(rawAssignment1.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		assertEquals(dateUnitEffortListForRawAssignment1.size(), 1);
		DateUnitEffort dateUnitEffortForRawAssignment1 = dateUnitEffortListForRawAssignment1.getDateUnitEffort(0);
		assertEquals(dateUnitEffortForRawAssignment1.getQuantity(), numberOfUnits / 2);

		RawObject rawStrategy2 = rawProject.findObject(strategy2.getRef());
		assertNotNull(rawStrategy2);
		assertTrue(rawStrategy2.containsKey(TAG_ACTIVITY_IDS));
		IdList activityIdListForRawStrategy2 = new IdList(TaskSchema.getObjectType(), rawStrategy2.get(TAG_ACTIVITY_IDS));
		assertEquals(activityIdListForRawStrategy2.size(), 2);
		assertFalse(activityIdListForRawStrategy2.contains(activity1.getId()));
		assertTrue(activityIdListForRawStrategy2.contains(activity2.getId()));

		activityIdListForRawStrategy2.removeId(activity2.getId());
		BaseId addedActivityIdForRawStrategy2 = activityIdListForRawStrategy2.get(0);
		ORef addedActivityForRawStrategy2Ref = new ORef(ObjectType.TASK, addedActivityIdForRawStrategy2);
		RawObject rawAddedActivity = rawProject.findObject(addedActivityForRawStrategy2Ref);
		assertTrue(rawAddedActivity.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS));
		IdList assignmentIdListForRawAddedActivity = new IdList(ResourceAssignmentSchema.getObjectType(), rawAddedActivity.get(TAG_RESOURCE_ASSIGNMENT_IDS));
		assertEquals(assignmentIdListForRawAddedActivity.size(), 1);

		BaseId addedAssignmentId = assignmentIdListForRawAddedActivity.get(0);
		ORef addedAssignmentRef = new ORef(ObjectType.RESOURCE_ASSIGNMENT, addedAssignmentId);
		RawObject rawAddedAssignment = rawProject.findObject(addedAssignmentRef);
		assertTrue(rawAddedAssignment.containsKey(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		DateUnitEffortList dateUnitEffortListForRawAddedAssignment1 = new DateUnitEffortList(rawAddedAssignment.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		assertEquals(dateUnitEffortListForRawAddedAssignment1.size(), 1);
		DateUnitEffort dateUnitEffortForRawAddedAssignment1 = dateUnitEffortListForRawAddedAssignment1.getDateUnitEffort(0);
		assertEquals(dateUnitEffortForRawAddedAssignment1.getQuantity(), numberOfUnits / 2);

		verifyRawObjectMatchesBaseObject(activity1, rawAddedActivity);
	}

	public static void verifyRawObjectMatchesBaseObject(BaseObject baseObject, RawObject rawObject) throws Exception
	{
		Vector<String> storedTags = baseObject.getStoredFieldTags();

		Vector<String> ignoredTags = new Vector<String>();
		ignoredTags.add(ResourceAssignment.TAG_TIMEFRAME_IDS);	// added by migration 22 and so not present on original object

		for (String tag : storedTags)
		{
			if (ignoredTags.contains(tag))
				continue;

			final boolean isOwnedField = baseObject.isOwnedField(tag);
			if (tag.equals(TAG_LABEL))
			{
				assertEquals("Copy of " + baseObject.getData(tag), rawObject.getData(tag));
			}
			else if (isOwnedField && baseObject.isRefList(tag))
			{
				assertNotEquals("Tag '" + tag + "' should not match", baseObject.getData(tag), rawObject.getData(tag));
				ORefList baseObjectRefList = baseObject.getSafeRefListData(tag);
				ORefList rawObjectRefList = safeGetORefList(rawObject, tag);
				assertEquals("Size of ORefList for Tag '" + tag + "' should match", baseObjectRefList.size(), rawObjectRefList.size());
			}
			else if (isOwnedField && baseObject.isIdListTag(tag))
			{
				assertNotEquals("Tag '" + tag + "' should not match", baseObject.getData(tag), rawObject.getData(tag));
				IdList baseObjectIdList = baseObject.getSafeIdListData(tag);
				IdList rawObjectIdList = safeGetIdList(baseObject, rawObject, tag);
				assertEquals("Size of IdList for Tag '" + tag + "' should match", baseObjectIdList.size(), rawObjectIdList.size());
			}
			else
			{
				assertEquals("Tag '" + tag + "' should match", baseObject.getData(tag), rawObject.getData(tag));
			}
		}
	}

	private static ORefList safeGetORefList(RawObject rawObject, String tag) throws Exception
	{
		ORefList refList = new ORefList(){};

		if (rawObject.containsKey(tag))
			refList = new ORefList(rawObject.getData(tag));

		return refList;
	}

	private static IdList safeGetIdList(BaseObject baseObject, RawObject rawObject, String tag) throws Exception
	{
		IdList idList = new IdList(baseObject.getType()){};

		if (rawObject.containsKey(tag))
			idList = new IdList(baseObject.getType(), rawObject.getData(tag));

		return idList;
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
}
