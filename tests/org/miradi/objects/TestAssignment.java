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
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;

public class TestAssignment extends ObjectTestCase
{
	public TestAssignment(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		dateRange1 = createDateRange(createMultiCalendar(2008, 1, 1), createMultiCalendar(2008, 2, 1));
		dateRange2 = createDateRange(createMultiCalendar(2009, 1, 1), createMultiCalendar(2009, 2, 1));
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.ASSIGNMENT);
	}
	
	public void testGetWorkUnits() throws Exception
	{
		Assignment assignment = getProject().createAssignment();
		assertFalse("Empty assignment has work unit values?", assignment.getWorkUnits(dateRange1).hasValue());

		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		dateRangeEffortList.add(createDateRangeEffort(2, dateRange1));
		dateRangeEffortList.add(createDateRangeEffort(5, dateRange2));

		getProject().fillObjectUsingCommand(assignment, Assignment.TAG_DATERANGE_EFFORTS, dateRangeEffortList.toString());

		assertEquals("wrong assignment work units?", 2.0, assignment.getWorkUnits(dateRange1).getValue());
		assertEquals("wrong assignment work units?", 5.0, assignment.getWorkUnits(dateRange2).getValue());
		
		DateRange totalProjectDateRange = DateRange.combine(dateRange1, dateRange2);
		assertEquals("wrong totals work units", 7.0, assignment.getWorkUnits(totalProjectDateRange).getValue());
	}
	
	public DateRangeEffort createDateRangeEffort(int unitQuantatiy, DateRange dateRange) throws Exception
	{
		return new DateRangeEffort("", unitQuantatiy, dateRange);
	}

	private DateRange createDateRange(MultiCalendar startDate, MultiCalendar endDate) throws Exception
	{
		return new DateRange(startDate, endDate);
	}

	private MultiCalendar createMultiCalendar(int year, int month, int day)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(year, month, day);
	}
	
	private DateRange dateRange1;
	private DateRange dateRange2;
}
