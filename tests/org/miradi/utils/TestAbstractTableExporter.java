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

package org.miradi.utils;

import org.miradi.main.TestCaseWithProject;

public class TestAbstractTableExporter extends TestCaseWithProject
{
	public TestAbstractTableExporter(String name)
	{
		super(name);
	}
	
	public void testBuildModelColumnIndexArray()
	{		
		CodeList desiredCodeList = new CodeList(new String[] {"c", "a", "d", "b"});
		verifyColumnIndexes(desiredCodeList);
	}

	public void testEmptyDesiredColumnCodes()
	{
		CodeList emptyDesiredColumnCodes = new CodeList();
		verifyColumnIndexes(emptyDesiredColumnCodes);
	}
	
	private void verifyColumnIndexes(CodeList desiredCodeList)
	{
		CodeList modelColumnCodes = new CodeList(new String[]{"a", "b", "c", "d",});
		int[] createdExpectedColumnIndexes = new int[modelColumnCodes.size()];
		for (int index  = 0; index < modelColumnCodes.size(); ++index)
		{
			String expectedCode = modelColumnCodes.get(index);
			if (desiredCodeList.contains(expectedCode))
				createdExpectedColumnIndexes[index] = desiredCodeList.find(expectedCode);
			else
				createdExpectedColumnIndexes[index] = index;
		}
		
		int[] modelColumnArray = AbstractTableExporter.buildModelColumnIndexArray(desiredCodeList, modelColumnCodes);
		for (int column = 0; column < modelColumnArray.length; ++column)
		{
			assertEquals("wrong column index", createdExpectedColumnIndexes[column], modelColumnArray[column]);
		}
	}
}
