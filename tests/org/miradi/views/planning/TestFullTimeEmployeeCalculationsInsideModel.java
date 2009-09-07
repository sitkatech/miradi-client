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

import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
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
		verifyCalculatedFullTimeEmployeeDays(year2009, 1.0, 240, 240);
		verifyCalculatedFullTimeEmployeeDays(q12009, 1.0, 240, 60);
		verifyCalculatedFullTimeEmployeeDays(jan2009, 1.0, 240, 20);
		
		verifyCalculatedFullTimeEmployeeDays(year2009, 0.5, 240, 120);
		verifyCalculatedFullTimeEmployeeDays(q12009, 0.5, 240, 30);
		verifyCalculatedFullTimeEmployeeDays(jan2009, 0.5, 240, 10);
		
		verifyCalculatedFullTimeEmployeeDays(year2009, 0.5, 120, 60);
		verifyCalculatedFullTimeEmployeeDays(q12009, 0.5, 120, 15);
		verifyCalculatedFullTimeEmployeeDays(jan2009, 0.5, 120, 5);
	}

	private void verifyCalculatedFullTimeEmployeeDays(DateUnit dateUnit, double fraction, double fullTimeEmployeeDaysPerYear, double expectedValue)
	{
		double calculatedValue = AssignmentDateUnitsTableModel.calculateFullTimeEmployeeDays(dateUnit, fraction, fullTimeEmployeeDaysPerYear);
		assertEquals("wrong calculated full time employee days value?", expectedValue, calculatedValue);
	}
	
	public void testCalculateFullTimeEmployeeFraction() throws Exception
	{
		verifyCalculatedFullTimeEmployeeFraction(year2009, 240, 240, 1.0);
		verifyCalculatedFullTimeEmployeeFraction(q12009, 60, 240, 1.0);
		verifyCalculatedFullTimeEmployeeFraction(jan2009, 20, 240, 1.0);
		
		verifyCalculatedFullTimeEmployeeFraction(year2009, 120, 240, 0.5);
		verifyCalculatedFullTimeEmployeeFraction(q12009, 30, 240, 0.5);
		verifyCalculatedFullTimeEmployeeFraction(jan2009, 10, 240, 0.5);
		
		verifyCalculatedFullTimeEmployeeFraction(year2009, 60, 120, 0.5);
		verifyCalculatedFullTimeEmployeeFraction(q12009, 15, 120, 0.5);
		verifyCalculatedFullTimeEmployeeFraction(jan2009, 5, 120, 0.5);
	}
	
	private void verifyCalculatedFullTimeEmployeeFraction(DateUnit dateUnit, double assignedDayCount, double fullTimeEmployeeDaysPerYear, double expectedValue) throws Exception
	{
		double fraction = AssignmentDateUnitsTableModel.calculateFullTimeEmployeeFraction(dateUnit, assignedDayCount, fullTimeEmployeeDaysPerYear);
		assertEquals("wrong calculated full time employee fraction?", expectedValue, fraction);
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
