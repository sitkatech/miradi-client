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

package org.miradi.dialogfields.editors;

import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;
import org.miradi.utils.DateRange;

public class DateUnitRange
{
	public DateUnitRange(ProjectCalendar projectCalendarToUse, DateUnit dateUnit2, DateUnit dateUnit1) throws Exception
	{
		projectCalendar = projectCalendarToUse;
		
		startDateUnit = getEarliestDateUnit(projectCalendarToUse, dateUnit1, dateUnit2);
		endDateUnit = getLatestDateUnit(projectCalendarToUse, dateUnit2, dateUnit1);
	}

	private DateUnit getEarliestDateUnit(ProjectCalendar projectCalendarToUse, DateUnit dateUnit1, DateUnit dateUnit2) throws Exception
	{
		if (dateUnit1 == null || dateUnit2 == null)
			return null;
		
		DateRange firstDateRange = projectCalendarToUse.convertToDateRange(dateUnit1);
		DateRange secondDateRange = projectCalendarToUse.convertToDateRange(dateUnit2);
		if (firstDateRange.getStartDate().before(secondDateRange.getStartDate()))
			return dateUnit1;
		
		return dateUnit2;
	}
	
	private DateUnit getLatestDateUnit(ProjectCalendar projectCalendarToUse, DateUnit dateUnit1, DateUnit dateUnit2) throws Exception
	{
		if (dateUnit1 == null || dateUnit2 == null)
			return null;
		
		DateRange firstDateRange = projectCalendarToUse.convertToDateRange(dateUnit1);
		DateRange secondDateRange = projectCalendarToUse.convertToDateRange(dateUnit2);
		if (firstDateRange.getStartDate().after(secondDateRange.getStartDate()))
			return dateUnit1;
		
		return dateUnit2;
	}
	
	public int getFiscalYearStartMonth()
	{
		return getProjectCalendar().getFiscalYearFirstMonth(); 
	}
	
	public ProjectCalendar getProjectCalendar()
	{
		return projectCalendar;
	}
	
	public DateUnit getStartDateUnit()
	{
		return startDateUnit;
	}

	public DateUnit getEndDateUnit()
	{
		return endDateUnit;
	}
	
	private ProjectCalendar projectCalendar;
	private DateUnit startDateUnit;
	private DateUnit endDateUnit;
}
