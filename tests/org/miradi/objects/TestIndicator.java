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

import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.CommandVector;


public class TestIndicator extends AbstractObjectWithBudgetDataToDeleteTestCase
{
	public TestIndicator(String name)
	{
		super(name);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	@Override
	protected int getType()
	{
		return Indicator.getObjectType();
	}

	public void testFields() throws Exception
	{
		verifyFields(getType());
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
		project.setProjectDates(1999, 2012);

		BaseObject baseObject = project.createBaseObject(objectType);
		Task task = project.createTask(baseObject);
		project.addResourceAssignment(task, 14, 2007, 2007);
		project.addResourceAssignment(task, 15, 2006, 2006);
		
		IdList taskIdsFromObject = new IdList(Task.getObjectType(), baseObject.getData(taskTag));
		assertEquals("wrong method count?", 1, taskIdsFromObject.size());
		
		DateUnit dateUnit = project.createDateUnit(2006, 2006);
		assertEquals("wrong work units for methods", 15.0, ProjectForTesting.calculateTimePeriodCosts(baseObject, dateUnit));
		
		BaseObject objectWithNoTasks = project.createBaseObject(objectType);
		project.addResourceAssignment(objectWithNoTasks, 45, 2006, 2006);
		assertEquals("wrong work units for methods", 45.0, ProjectForTesting.calculateTimePeriodCosts(objectWithNoTasks, dateUnit));
	}
	
	public void testIsAssignmentDataSuperseded() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		TestTask.verifyIsAssignmentDataSuperseded(getProject(), indicator, Indicator.TAG_METHOD_IDS);
	}
	
	public void testIndicatorIsRemovedFromRelevancyListWhenDeleted() throws Exception
	{
		Cause indicatorOwner = getProject().createCause();
		Indicator indicator = getProject().createIndicator(indicatorOwner);
		Strategy objectiveOwner = getProject().createStrategy();
		Objective objective = getProject().createObjective(objectiveOwner);
		RelevancyOverrideSet relevantIndicators = new RelevancyOverrideSet();
		relevantIndicators.add(new RelevancyOverride(indicator.getRef(), true));
		getProject().fillObjectUsingCommand(objective, Objective.TAG_RELEVANT_INDICATOR_SET, relevantIndicators.toString());
		assertEquals("Object's indicator relevancy list was not updated?", 1, objective.getAllIndicatorRefsFromRelevancyOverrides().size());
		
		CommandVector commandsToDeleteIndicator = indicator.createCommandsToDeleteChildrenAndObject();
		getProject().executeCommandsAsTransaction(commandsToDeleteIndicator);
		
		assertEquals("Indicator was not removed from objective relevancy list?", 0, objective.getAllIndicatorRefsFromRelevancyOverrides().size());
	}
	
	public void testCreateCommandsToClone() throws Exception
	{
		Cause indicatorOwner = getProject().createCause();
		Indicator indicator = getProject().createIndicator(indicatorOwner);
		ResourceAssignment assignment = getProject().createResourceAssignment();
		IdList assignmentIds = new IdList(ResourceAssignment.getObjectType(), new BaseId[] { assignment.getId() });
		indicator.setData(Indicator.TAG_RESOURCE_ASSIGNMENT_IDS, assignmentIds.toString());
		Command[] commandsToClone = indicator.createCommandsToClone(indicator.getId());
		Vector<String> modifiedTags = extractSetDataCommands(commandsToClone, indicator.getRef());
		assertNotContains(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, modifiedTags);
	}

	private Vector<String> extractSetDataCommands(Command[] commandsToClone, ORef ref)
	{
		Vector<String> tags = new Vector<String>();
		for(int i = 0; i < commandsToClone.length; ++i)
		{
			if(commandsToClone[i].getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			{
				CommandSetObjectData command = (CommandSetObjectData) commandsToClone[i];
				if(command.getObjectORef().equals(ref))
					tags.add(command.getFieldTag());
			}
		}
		return tags;
	}

	private ProjectForTesting project;
}
