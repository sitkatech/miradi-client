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

import org.miradi.main.EAMTestCase;
import org.miradi.project.TestDateUnit;
import org.miradi.questions.BudgetCostUnitQuestion;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;

public class TestDateUnitEffortList extends EAMTestCase
{
	public TestDateUnitEffortList(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		verifyStoreAndRestore(createSampleData());
		verifyCreateFromJson(createSampleData());
	}
	
	private void verifyStoreAndRestore(DateUnitEffortList dateUnitEffortListToVerifyAgainst) throws Exception
	{
		EnhancedJsonObject json = dateUnitEffortListToVerifyAgainst.toJson();
		DateUnitEffortList dateUnitEffortListFromJson = new DateUnitEffortList(json);
		assertEquals("dateUnitEffort is same?", dateUnitEffortListToVerifyAgainst, dateUnitEffortListFromJson);
	}

	private void verifyCreateFromJson(DateUnitEffortList dateUnitEffortListToVerifyAgainst) throws Exception
	{
		DateUnitEffortList dateUnitEffortFromJson = new DateUnitEffortList(dateUnitEffortListToVerifyAgainst.toJson());
		assertEquals("not same date unit effort list?", dateUnitEffortListToVerifyAgainst, dateUnitEffortFromJson);
			
		assertNull("not null?", DateRange.createFromJson(null));
		assertNull("not null?", DateRange.createFromJson(new EnhancedJsonObject()));
		assertNull("not null?", DateRange.createFromJson(new EnhancedJsonObject("{bogusTag:\"\"}")));
	}
	
	private DateUnitEffortList createSampleData()
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(BudgetCostUnitQuestion.DAYS_CODE, 10.0, TestDateUnit.month);
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		
		return dateUnitEffortList;
	}
}
