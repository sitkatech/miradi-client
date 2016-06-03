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
import org.miradi.migrations.forward.MigrationTo32;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import static org.miradi.objects.BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;

public class TestMigrationTo32 extends AbstractTestMigration
{
	public TestMigrationTo32(String name)
	{
		super(name);
	}

	public void testSharedMethodsSplitByForwardMigration() throws Exception
	{
		double numberOfUnits = 10.0;

		Strategy strategy1 = getProject().createAndPopulateStrategy();
		Indicator indicator1 = getProject().createAndPopulateIndicator(strategy1);

		Task method1 = getProject().createMethod(indicator1);
		getProject().populateTask(method1, "Some Shared Method");

		Strategy strategy2 = getProject().createAndPopulateStrategy();
		Indicator indicator2 = getProject().createAndPopulateIndicator(strategy2);

		ORefList methodRefsForIndicator2 = indicator2.getMethodRefs();
		assertEquals(methodRefsForIndicator2.size(), 1);

		CommandSetObjectData cmdToShareMethod1 = CommandSetObjectData.createAppendIdCommand(indicator2, Indicator.TAG_METHOD_IDS, method1.getId());
		getProject().executeCommand(cmdToShareMethod1);

		assertEquals(indicator1.getMethodRefs().size(), 1);
		assertEquals(method1.getResourceAssignmentRefs().size(), 1);

		assertEquals(indicator2.getMethodRefs().size(), 2);

		int indicatorCountBeforeMigration = getProject().getAllRefsForType(ObjectType.INDICATOR).size();

		ORef resourceAssignmentForMethod1Ref = method1.getResourceAssignmentRefs().toArray()[0];
		ResourceAssignment resourceAssignmentForMethod1 = (ResourceAssignment) getProject().findObject(resourceAssignmentForMethod1Ref);
		assertEquals(resourceAssignmentForMethod1.getDateUnitEffortList().size(), 1);
		DateUnitEffort dateUnitEffortForResourceAssignment1 = resourceAssignmentForMethod1.getDateUnitEffortList().getDateUnitEffort(0);
		assertEquals(dateUnitEffortForResourceAssignment1.getQuantity(), numberOfUnits);

		assertEquals(indicator1.getMethodRefs().size(), 1);
		assertTrue(indicator1.getMethodRefs().contains(method1.getRef()));

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo32.VERSION_TO));
		migrateProject(rawProject, new VersionRange(MigrationTo32.VERSION_TO));

		RawPool indicatorPool = rawProject.getRawPoolForType(ObjectType.INDICATOR);
		assertNotNull(indicatorPool);
		assertEquals(indicatorPool.keySet().size(), indicatorCountBeforeMigration);

		RawObject rawIndicator1 = rawProject.findObject(indicator1.getRef());
		assertNotNull(rawIndicator1);
		assertTrue(rawIndicator1.containsKey(Indicator.TAG_METHOD_IDS));
		IdList methodIdListForRawIndicator1 = new IdList(TaskSchema.getObjectType(), rawIndicator1.get(Indicator.TAG_METHOD_IDS));
		assertEquals(methodIdListForRawIndicator1, new IdList(method1));

		RawObject rawMethod1 = rawProject.findObject(method1.getRef());
		assertTrue(rawMethod1.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS));
		IdList assignmentIdListForRawMethod1 = new IdList(ResourceAssignmentSchema.getObjectType(), rawMethod1.get(TAG_RESOURCE_ASSIGNMENT_IDS));
		assertEquals(assignmentIdListForRawMethod1, new IdList(resourceAssignmentForMethod1));

		RawObject rawAssignment1 = rawProject.findObject(resourceAssignmentForMethod1Ref);
		assertTrue(rawAssignment1.containsKey(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		DateUnitEffortList dateUnitEffortListForRawAssignment1 = new DateUnitEffortList(rawAssignment1.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		assertEquals(dateUnitEffortListForRawAssignment1.size(), 1);
		DateUnitEffort dateUnitEffortForRawAssignment1 = dateUnitEffortListForRawAssignment1.getDateUnitEffort(0);
		assertEquals(dateUnitEffortForRawAssignment1.getQuantity(), numberOfUnits / 2);

		RawObject rawIndicator2 = rawProject.findObject(indicator2.getRef());
		assertNotNull(rawIndicator2);
		assertTrue(rawIndicator2.containsKey(Indicator.TAG_METHOD_IDS));
		IdList methodIdListForRawIndicator2 = new IdList(TaskSchema.getObjectType(), rawIndicator2.get(Indicator.TAG_METHOD_IDS));
		assertEquals(methodIdListForRawIndicator2.size(), 2);
		BaseId idForMethod2 = methodRefsForIndicator2.convertToIdList(ObjectType.TASK).get(0);
		assertTrue(methodIdListForRawIndicator2.contains(idForMethod2));
		assertFalse(methodIdListForRawIndicator2.contains(method1.getId()));

		methodIdListForRawIndicator2.removeId(idForMethod2);
		BaseId addedMethodIdForRawIndicato2 = methodIdListForRawIndicator2.get(0);
		ORef addedMethodForRawIndicator2Ref = new ORef(ObjectType.TASK, addedMethodIdForRawIndicato2);
		RawObject rawAddedMethod = rawProject.findObject(addedMethodForRawIndicator2Ref);
		assertTrue(rawAddedMethod.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS));
		IdList assignmentIdListForRawAddedMethod = new IdList(ResourceAssignmentSchema.getObjectType(), rawAddedMethod.get(TAG_RESOURCE_ASSIGNMENT_IDS));
		assertEquals(assignmentIdListForRawAddedMethod.size(), 1);

		BaseId addedAssignmentId = assignmentIdListForRawAddedMethod.get(0);
		ORef addedAssignmentRef = new ORef(ObjectType.RESOURCE_ASSIGNMENT, addedAssignmentId);
		RawObject rawAddedAssignment = rawProject.findObject(addedAssignmentRef);
		assertTrue(rawAddedAssignment.containsKey(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		DateUnitEffortList dateUnitEffortListForRawAddedAssignment1 = new DateUnitEffortList(rawAddedAssignment.getData(ResourceAssignment.TAG_DATEUNIT_DETAILS));
		assertEquals(dateUnitEffortListForRawAddedAssignment1.size(), 1);
		DateUnitEffort dateUnitEffortForRawAddedAssignment1 = dateUnitEffortListForRawAddedAssignment1.getDateUnitEffort(0);
		assertEquals(dateUnitEffortForRawAddedAssignment1.getQuantity(), numberOfUnits / 2);

		TestMigrationTo31.verifyRawObjectMatchesBaseObject(method1, rawAddedMethod);
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
}
