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
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.EnhancedJsonObject;

public class TestDateUnitEffort extends EAMTestCase
{
	public TestDateUnitEffort(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		verifyStoreAndRestore(createSampleData());
		verifyCreateFromJson(createSampleData());
	}
	
	private void verifyStoreAndRestore(DateUnitEffort dateUnitEffortToVerifyAgainst) throws Exception
	{
		EnhancedJsonObject json = dateUnitEffortToVerifyAgainst.toJson();
		DateUnitEffort dateUnitEffortFromJson = new DateUnitEffort(json);
		assertEquals("Json round trip failed?", dateUnitEffortToVerifyAgainst, dateUnitEffortFromJson);
	}

	private void verifyCreateFromJson(DateUnitEffort dateUnitEffortToVerifyAgainst) throws Exception
	{
		DateUnitEffort dateUnitEffortFromJson = new DateUnitEffort(dateUnitEffortToVerifyAgainst.toJson());
		assertEquals("Json round trip failed?", dateUnitEffortToVerifyAgainst, dateUnitEffortFromJson);			
	}
	
	private DateUnitEffort createSampleData()
	{
		return new DateUnitEffort(10.0, TestDateUnit.month12);
	}
}
