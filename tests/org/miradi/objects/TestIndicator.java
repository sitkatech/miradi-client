/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.objects;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.DateRange;


public class TestIndicator extends ObjectTestCase
{
	public TestIndicator(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public void testFields() throws Exception
	{
		verifyFields(ObjectType.INDICATOR);
	}
	
	public void testGetLatestMeasurementRef() throws Exception
	{
		ORef indicatorRef = project.createObject(Indicator.getObjectType());
		Indicator indicator = (Indicator) project.findObject(indicatorRef);
		assertEquals("Found date?", ORef.INVALID, indicator.getLatestMeasurementRef());
		
		ORef measurementRef1 = project.createObject(Measurement.getObjectType());
		ORef measurementRef2 = project.createObject(Measurement.getObjectType());
		ORefList measurementRefList = new ORefList();
		measurementRefList.add(measurementRef1);
		measurementRefList.add(measurementRef2);
		
		CommandSetObjectData setMeasurements = new CommandSetObjectData(indicatorRef, Indicator.TAG_MEASUREMENT_REFS, measurementRefList.toString());
		project.executeCommand(setMeasurements);
		
		MultiCalendar expectedBefore = MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1);
		CommandSetObjectData setMeasurement1Date = new CommandSetObjectData(measurementRef1, Measurement.TAG_DATE, expectedBefore.toIsoDateString());
		project.executeCommand(setMeasurement1Date);

		MultiCalendar expectedAfter = MultiCalendar.createFromGregorianYearMonthDay(2000, 1, 1);
		CommandSetObjectData setMeasurement2Date = new CommandSetObjectData(measurementRef2, Measurement.TAG_DATE, expectedAfter.toIsoDateString());
		project.executeCommand(setMeasurement2Date);
		
		String latestMeasurmentRefAsString2 = indicator.getPseudoData(indicator.PSEUDO_TAG_LATEST_MEASUREMENT_REF);
		ORef latestMeasurementRef2 = ORef.createFromString(latestMeasurmentRefAsString2);
		assertEquals("found latest measurement?", measurementRef2, latestMeasurementRef2);
		
		Measurement latestMeasurement = (Measurement) project.findObject(latestMeasurementRef2);
		MultiCalendar foundAfter = latestMeasurement.getDate();
		assertEquals("Found latest date?", expectedAfter, foundAfter);
	}
	
	public void testIsRefList() throws Exception
	{
		ORef indicatorRef = project.createObject(Indicator.getObjectType());
		Indicator indicator = (Indicator) project.findObject(indicatorRef);
		assertTrue("is not measurment ref tag?", indicator.isRefList(Indicator.TAG_MEASUREMENT_REFS));
	}

	public void testGetAnnotationType() throws Exception
	{
		ORef indicatorRef = project.createObject(Indicator.getObjectType());
		Indicator indicator = (Indicator) project.findObject(indicatorRef);
		assertEquals("is wrong annotation type?", Measurement.getObjectType(), indicator.getAnnotationType(Indicator.TAG_MEASUREMENT_REFS));
	}
	
	public void testGetWorkUnits() throws Exception
	{
		verifyGetWorkUnits(getProject(), Indicator.getObjectType(), Indicator.TAG_METHOD_IDS);
	}
	
	public static void verifyGetWorkUnits(ProjectForTesting project, int objectType, String taskTag) throws Exception
	{
		Task task = project.createTask();
		project.addAssignment(task, 14, 2006, 2009);
		project.addAssignment(task, 15, 2006, 2009);
		BaseObject baseObject = project.createBaseObject(objectType);
		IdList taskIds = new IdList(Task.getObjectType());
		taskIds.addRef(task.getRef());
		project.setObjectData(baseObject.getRef(), taskTag, taskIds.toString());
		
		IdList taskIdsFromObject = new IdList(Task.getObjectType(), baseObject.getData(taskTag));
		assertEquals("wrong method count?", 1, taskIdsFromObject.size());
		
		DateRange dateRange = new DateRange(project.createMultiCalendar(2006), project.createMultiCalendar(2009));
		assertEquals("wrong work units for methods", 29.0, baseObject.getWorkUnits(dateRange).getRawValue());
		
		BaseObject objectWithNoTasks = project.createBaseObject(objectType);
		project.addAssignment(objectWithNoTasks, 45, 2006, 2009);
		assertEquals("wrong work units for methods", 45.0, objectWithNoTasks.getWorkUnits(dateRange).getRawValue());
	}

	private ProjectForTesting project;
}
