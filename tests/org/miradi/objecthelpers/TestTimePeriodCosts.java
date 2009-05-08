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
package org.miradi.objecthelpers;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.ProjectResource;
import org.miradi.utils.OptionalDouble;

public class TestTimePeriodCosts extends TestCaseWithProject
{
	public TestTimePeriodCosts(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		TimePeriodCosts timePeriodCosts = new TimePeriodCosts();
		OptionalDouble emptyDouble = timePeriodCosts.getUnits(ORef.INVALID);
		assertNull("should not have value?", emptyDouble);
		
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		timePeriodCosts.addResourceCost(projectResource.getRef(), new OptionalDouble(10.0));
		assertEquals("wrong units cost?", 10.0, timePeriodCosts.getUnits(projectResource.getRef()).getValue());
		
		timePeriodCosts.setExpense(new OptionalDouble(20.0));
		assertEquals("wrong expense?", 20.0, timePeriodCosts.getExpense().getValue());
		
		assertEquals("wrong project resources sum?", 100.0, timePeriodCosts.calculateProjectResources(getProject()).getValue());
		assertEquals("wrong total cost?", 120.0, timePeriodCosts.calculateTotal(getProject()).getValue());
	}
}
