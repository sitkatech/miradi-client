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

package org.miradi.views.planning;

import org.miradi.dialogs.planning.AssignmentDateUnitsTableModel;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;

public class TestFullTimeEmployeeCalculationsInsideModel extends TestCaseWithProject
{
	public TestFullTimeEmployeeCalculationsInsideModel(String name)
	{
		super(name);
	}

	public void testCalculateFullTimeEmployeeDays()
	{
		verifyCalculatedFullTimeEmployeeDays(240, year2009, 1.0, 240);
		verifyCalculatedFullTimeEmployeeDays(240, q12009, 1.0, 60);
		verifyCalculatedFullTimeEmployeeDays(240, jan2009, 1.0, 20);
		
		verifyCalculatedFullTimeEmployeeDays(240, year2009, 0.5, 120);
		verifyCalculatedFullTimeEmployeeDays(240, q12009, 0.5, 30);
		verifyCalculatedFullTimeEmployeeDays(240, jan2009, 0.5, 10);
		
		verifyCalculatedFullTimeEmployeeDays(120, year2009, 0.5, 60);
		verifyCalculatedFullTimeEmployeeDays(120, q12009, 0.5, 15);
		verifyCalculatedFullTimeEmployeeDays(120, jan2009, 0.5, 5);
	}

	private void verifyCalculatedFullTimeEmployeeDays(double fullTimeEmployeeDaysPerYear, DateUnit dateUnit, double fraction, double assignedDayCount)
	{
		final double TOLERANCE = 0.00001;
		double calculatedValue = AssignmentDateUnitsTableModel.calculateFullTimeEmployeeDays(dateUnit, fraction, fullTimeEmployeeDaysPerYear);
		assertEquals("wrong calculated full time employee days value?", assignedDayCount, calculatedValue, TOLERANCE);
		
		double calculatedFraction = AssignmentDateUnitsTableModel.calculateFullTimeEmployeeFraction(dateUnit, assignedDayCount, fullTimeEmployeeDaysPerYear);
		assertEquals("wrong calculated full time employee fraction?", fraction, calculatedFraction, TOLERANCE);
	}
	
	public void testGetNumberOfMonthsIn()
	{
		verifyNumberFoMonthsInDateUnit(year2009, 1);
		verifyNumberFoMonthsInDateUnit(q12009, 4);
		verifyNumberFoMonthsInDateUnit(jan2009, 12);

		verifyThrowsException(new DateUnit());
		verifyThrowsException(new DateUnit("2009-01-02"));
	}
	
	private void verifyNumberFoMonthsInDateUnit(DateUnit dateUnit, int expectedNumberOfMonths)
	{
		int retrievedNumberOfMonths = AssignmentDateUnitsTableModel.getNumberOfDateUnitsInYear(dateUnit);
		assertEquals("wrong number of months in date unit?", expectedNumberOfMonths, retrievedNumberOfMonths);
	}
	
	private void verifyThrowsException(DateUnit dateUnit)
	{
		try
		{
			AssignmentDateUnitsTableModel.getNumberOfDateUnitsInYear(dateUnit);
			fail("DateUnit should not have any months?");
		}
		catch (Exception expectedExceptionToIgnore)
		{
		}
	}
	
	private static final DateUnit year2009 = new DateUnit("YEARFROM:2009-01");
	private static final DateUnit q12009 = new DateUnit("2009Q1");
	private static final DateUnit jan2009 = new DateUnit("2009-01");
}
