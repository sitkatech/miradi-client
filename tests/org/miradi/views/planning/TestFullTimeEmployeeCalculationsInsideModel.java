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

	public void testCalculateFullTimeEmployeeFraction()
	{
		//FIXME test FTE calcs here. Method has been verified to fail already.
	}
	
	public void testGetNumberOfMonthsIn()
	{
		verifyNumberFoMonthsInDateUnit(new DateUnit("YEARFROM:2009-01"), 1);
		verifyNumberFoMonthsInDateUnit(new DateUnit("2009Q1"), 4);
		verifyNumberFoMonthsInDateUnit(new DateUnit("2009-01"), 12);
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
}
