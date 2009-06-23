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
		int[] expectedColumnIndexes = new int[]{1, 3, 0, 2,};
		verifyColumnIndexes(expectedColumnIndexes, desiredCodeList);
	}

	public void testEmptyDesiredColumnCodes()
	{
		CodeList emptyDesiredColumnCodes = new CodeList();
		int[] expectedColumnIndexes = new int[]{0, 1, 2, 3, };
		verifyColumnIndexes(expectedColumnIndexes, emptyDesiredColumnCodes);
	}
	
	private void verifyColumnIndexes(int[] expectedColumnIndexes, CodeList desiredCodeList)
	{
		CodeList modelColumnCodes = new CodeList(new String[]{"a", "b", "c", "d",});
		int[] modelColumnArray = AbstractTableExporter.buildModelColumnIndexArray(desiredCodeList, modelColumnCodes);
		for (int column = 0; column < modelColumnArray.length; ++column)
		{
			assertEquals("wrong column index", expectedColumnIndexes[column], modelColumnArray[column]);
		}
	}
}
