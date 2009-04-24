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
package org.miradi.project;

import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.utils.DateRange;

public class TestDateUnit extends TestCaseWithProject
{
	public TestDateUnit(String name)
	{
		super(name);
	}	

	public void testExtractYears() throws Exception
	{
		verifyYears(getProject().parseIsoDate("2006-01-02"), getProject().parseIsoDate("2006-02-02"), 1);
		verifyYears(getProject().parseIsoDate("2006-01-02"), getProject().parseIsoDate("2007-02-02"), 2);
	}

	private void verifyYears(MultiCalendar startDate, MultiCalendar endDate, int expectedYearCount) throws Exception
	{
		DateRange dateRange = new DateRange(startDate, endDate);
		
		Vector<Integer> years = new DateUnit().extractYears(dateRange);
		assertEquals("wrong years count?", expectedYearCount, years.size());
	}
}
