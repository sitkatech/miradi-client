/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.planning;

import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMainTableModel;
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
	
	public void testIsWhenEditable() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		assertTrue("can't edit new strategy?", isWhenEditable(strategy));
		
		getProject().addActivityToStrateyList(strategy.getRef(), Strategy.TAG_ACTIVITY_IDS);
		assertFalse("can edit strategy with activity?", isWhenEditable(strategy));
		
		Indicator indicatorWithTwoAssignments = getProject().createIndicatorWithCauseParent();
		getProject().addResourceAssignment(indicatorWithTwoAssignments);
		getProject().addResourceAssignment(indicatorWithTwoAssignments);
		assertFalse("can edit indicator with multiple assignments?", isWhenEditable(indicatorWithTwoAssignments));

		verifyWithThreeDateUnitEfforts();
		verifyWithTwoDateUnitEfforts();
		
//FIXME see the FIXME in the PVMTM.isWhenEditable.		
//		Task activityWithAssignmentWithValue = getProject().createActivity();
//		getProject().addResourceAssignment(activityWithAssignmentWithValue, 10.0, new DateUnit());
//		assertFalse("can edit activity with assignment that has values", isWhenEditable(activityWithAssignmentWithValue));
//
//		Task activityWithBlankAssignment = getProject().createActivity();
//		getProject().addResourceAssignment(activityWithBlankAssignment, 0.0, new DateUnit());
//		assertTrue("can't edit activity with assignment with no values?", isWhenEditable(activityWithBlankAssignment));	
	}

	private void verifyWithThreeDateUnitEfforts() throws Exception
	{
		DateUnitEffortList list = new DateUnitEffortList();
		list.add(getProject().createDateUnitEffort(2002, 2002, 20.0));
		list.add(getProject().createDateUnitEffort(2003, 2003, 30.0));
		list.add(getProject().createDateUnitEffort(2004, 2004, 40.0));
		
		Task activityWithAssignmentWithTwoDateUnitEfforts = getProject().createActivity();
		getProject().addResourceAssignment(activityWithAssignmentWithTwoDateUnitEfforts, list);
		assertFalse("can edit activity with assignment with three dateUnitEfforts?", isWhenEditable(activityWithAssignmentWithTwoDateUnitEfforts));		
	}

	private void verifyWithTwoDateUnitEfforts() throws Exception
	{
		DateUnitEffortList list = new DateUnitEffortList();
		list.add(getProject().createDateUnitEffort(2002, 2002, 20.0));
		list.add(getProject().createDateUnitEffort(2003, 2003, 30.0));
		
		Task activityWithAssignmentWithTwoDateUnitEfforts = getProject().createActivity();
		getProject().addResourceAssignment(activityWithAssignmentWithTwoDateUnitEfforts, list);
		assertTrue("cannot edit activity with assignment with two dateUnitEfforts?", isWhenEditable(activityWithAssignmentWithTwoDateUnitEfforts));
	}

	private boolean isWhenEditable(BaseObject baseObject)
	{
		return PlanningViewMainTableModel.isWhenEditable(baseObject);
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

	private boolean canOwnAssignments(int objectType)
	{
		return AssignmentDateUnitsTableModel.canOwnAssignments(ORef.createInvalidWithType(objectType));
	}
}
