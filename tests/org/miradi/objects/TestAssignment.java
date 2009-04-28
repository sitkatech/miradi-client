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
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TestDateRangeEffortList;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

public class TestAssignment extends ObjectTestCase
{
	public TestAssignment(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.ASSIGNMENT);
	}
	
	public void testGetWorkUnits() throws Exception
	{
		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		DateRangeEffort dateRangeEffort = TestDateRangeEffortList.createDateRangeEffort();
		dateRangeEffortList.add(dateRangeEffort);

		Assignment assignment = getProject().createAssignment();
		getProject().fillObjectUsingCommand(assignment, Assignment.TAG_DATERANGE_EFFORTS, dateRangeEffortList.toString());

		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(2000, 1, 1);
		DateRange dateRange = new DateRange(startDate, endDate);
		assertEquals("wrong assignment work units?", 1, assignment.getWorkUnits(dateRange));
	}
}
