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

import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.TestDateUnit;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

public class TestAssignment extends ObjectTestCase
{
	public TestAssignment(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.RESOURCE_ASSIGNMENT);
	}
	
	public void testGetWorkUnits() throws Exception
	{
		getProject().setSingleYearProjectDate(2008);
		ResourceAssignment assignment = getProject().createResourceAssignment();
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		DateUnit dateUnit = getProject().createDateUnit(2008);
		assertFalse("Empty assignment has work unit values?", assignment.getWorkUnits(dateUnit).hasValue());

		DateUnit dateUnit1 = TestDateUnit.month12;
		DateUnit dateUnit2 = TestDateUnit.month01;
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(2, dateUnit1));
		dateUnitEffortList.add(createDateUnitEffort(5, dateUnit2));

		getProject().fillObjectUsingCommand(assignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());

		assertEquals("wrong assignment work units?", 20.0, assignment.getWorkUnits(dateUnit1).getValue());
		assertEquals("wrong assignment work units?", 50.0, assignment.getWorkUnits(dateUnit2).getValue());
		
		DateUnit totalProjectDateUnit = new DateUnit();
		assertEquals("wrong totals work units", 70.0, assignment.getWorkUnits(totalProjectDateUnit).getValue());
	}
	
	public DateUnitEffort createDateUnitEffort(int unitQuantatiy, DateUnit dateUnit) throws Exception
	{
		return new DateUnitEffort(unitQuantatiy, dateUnit);
	}
}
