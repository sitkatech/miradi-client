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

package org.miradi.views.planning;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.DateUnitEffortList;

public class TestPlanningViewMainTableModel extends TestCaseWithProject
{
	public TestPlanningViewMainTableModel(String name)
	{
		super(name);
	}
	
	public void testIsAssignedWhenEditable() throws Exception
	{
		getProject().setProjectStartDate(2002);
		getProject().setProjectEndDate(2002);
		Strategy strategy = getProject().createStrategy();
		assertTrue("can't edit new strategy?", isAssignedWhenEditable(strategy));
		
		ORef activityRef = getProject().addActivityToStrategy(strategy.getRef(), Strategy.TAG_ACTIVITY_IDS);
		assertTrue("can't edit strategy with empty activity?", isAssignedWhenEditable(strategy));
		
		Task activity = Task.find(getProject(), activityRef);
		getProject().addResourceAssignment(activity);
		assertFalse("can edit strategy with filled activity?", isAssignedWhenEditable(strategy));
		
		Indicator indicatorWithTwoAssignments = getProject().createIndicatorWithCauseParent();
		getProject().addResourceAssignment(indicatorWithTwoAssignments);
		getProject().addResourceAssignment(indicatorWithTwoAssignments);
		assertFalse("can edit indicator with multiple assignments?", isAssignedWhenEditable(indicatorWithTwoAssignments));
	}
	
	public void testIsAssignedWhenEditableWithMultipleResourceAssignmentsWithIdenticalDateUnitEffortLists() throws Exception
	{
		Task activity = getProject().createActivity();
		DateUnitEffortList list = createSampleDateUnitEffortList(2, 2002, 0.0);
		getProject().addResourceAssignment(activity, list);
		getProject().addResourceAssignment(activity, list);
		assertTrue("cannot edit activity with multiple assignments with identical DateUnitEffortLists?", isAssignedWhenEditable(activity));
	}
	
	public void testIsAssignedWhenEditableWithMultipleResourceAssignmentsWithDifferentDateUnitEffortLists() throws Exception
	{
		Task activity = getProject().createActivity();
		DateUnitEffortList list1 = createSampleDateUnitEffortList(3, 2002, 0.0);
		getProject().addResourceAssignment(activity, list1);

		DateUnitEffortList list2 = createSampleDateUnitEffortList(3, 2002, 0.0);
		getProject().addResourceAssignment(activity, list2);
		assertFalse("can edit activity with multiple assignments with different DateUnitEffortLists?", isAssignedWhenEditable(activity));
	}

	public void testIsAssignedWhenEditableWithThreeDateUnitEfforts() throws Exception
	{
		DateUnitEffortList list = createSampleDateUnitEffortList(3, 2002, 0.0);
		Task activityWithAssignmentWithTwoDateUnitEfforts = createActivityWithResourceAssignment(list);
		assertFalse("can edit activity with assignment with three dateUnitEfforts?", isAssignedWhenEditable(activityWithAssignmentWithTwoDateUnitEfforts));
	}

	public void testIsAssignedWhenEditableWithTwoDateUnitEfforts() throws Exception
	{
		DateUnitEffortList list = createSampleDateUnitEffortList(2, 2002, 0.0);
		Task activityWithAssignmentWithTwoDateUnitEfforts = createActivityWithResourceAssignment(list);
		assertTrue("cannot edit activity with assignment with two dateUnitEfforts?", isAssignedWhenEditable(activityWithAssignmentWithTwoDateUnitEfforts));
	}
	
	public void testIsAssignedWhenEditableWithNonBlankDateUnitEfforts() throws Exception
	{
		getProject().setProjectStartDate(2002);
		getProject().setProjectEndDate(2002);
		DateUnitEffortList list = createSampleDateUnitEffortList(1, 2002, 10.0);
		Task activityWithAssignmentWithNonBlankDateUnitEfforts = createActivityWithResourceAssignment(list);
		assertFalse("cannot edit object with dateUnitEfforts that have data?", isAssignedWhenEditable(activityWithAssignmentWithNonBlankDateUnitEfforts));
	}

	public void testCanOwnAssignments()
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			if (Indicator.is(type) || Strategy.is(type) || Task.is(type))
				assertTrue("type " + type + "cannot refer to assignments?", canOwnAssignments(type));
			else
				assertFalse("Type" + type + " can refer to assignments?", canOwnAssignments(type));
		}
	}
	
	private Task createActivityWithResourceAssignment(DateUnitEffortList list) throws Exception
	{
		Task activityWithAssignmentWithTwoDateUnitEfforts = getProject().createActivity();
		getProject().addResourceAssignment(activityWithAssignmentWithTwoDateUnitEfforts, list);
		
		return activityWithAssignmentWithTwoDateUnitEfforts;
	}

	private boolean isAssignedWhenEditable(BaseObject baseObject)
	{
		return baseObject.isAssignedWhenEditable();
	}
	
	private DateUnitEffortList createSampleDateUnitEffortList(int listSize, int startYear, double effort) throws Exception
	{
		DateUnitEffortList list = new DateUnitEffortList();
		for (int index = 0; index < listSize; ++index)
		{
			int incrementedYear = startYear + index;
			list.add(getProject().createDateUnitEffort(incrementedYear, incrementedYear, effort));
		}
		
		return list;
	}

	private boolean canOwnAssignments(int objectType)
	{
		return BaseObject.canOwnPlanningObjects(ORef.createInvalidWithType(objectType));
	}
}
