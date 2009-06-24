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

import java.util.HashMap;
import java.util.Set;

import org.miradi.main.TestCaseWithProject;

public class TestAbstractTableExporter extends TestCaseWithProject
{
	public TestAbstractTableExporter(String name)
	{
		super(name);
	}

	public void testEmptyDesiredColumnCodes()
	{
		CodeList emptyDesiredColumnCodes = new CodeList();
		verifyColumnIndexes(emptyDesiredColumnCodes);
	}
	
	public void testDifferentDesiredSizeThanModelColumnCodes()
	{		
		CodeList desiredCodeList = new CodeList(new String[] {"a", "b", "c"});
		verifyColumnIndexes(desiredCodeList);
	}
	
	public void testBuildModelColumnIndexArray()
	{		
		CodeList desiredCodeList = new CodeList(new String[] {"c", "a", "d", "b"});
		verifyColumnIndexes(desiredCodeList);
	}
	
	public void testNonExistingCodesFromModelColumnCodes()
	{
		CodeList desiredCodeList = new CodeList(new String[] {"e", "f", "g", "h"});
		verifyColumnIndexes(desiredCodeList);
	}
	
	private void verifyColumnIndexes(CodeList desiredCodeList)
	{
		CodeList modelColumnCodes = new CodeList(new String[]{"a", "b", "c", "d",});
		HashMap<Integer, Integer> tableColumnToModelColumnMap = new HashMap();
		for (int modelColumnIndex  = 0; modelColumnIndex < modelColumnCodes.size(); ++modelColumnIndex)
		{
			String expectedCode = modelColumnCodes.get(modelColumnIndex);
			int tableColumnIndex = modelColumnIndex;
			if (desiredCodeList.contains(expectedCode))
				tableColumnIndex = desiredCodeList.find(expectedCode);
			
			tableColumnToModelColumnMap.put(tableColumnIndex, modelColumnIndex);
		}
		
		HashMap<Integer, Integer> modelColumnArray = AbstractTableExporter.buildModelColumnIndexArray(desiredCodeList, modelColumnCodes);
		Set<Integer> tableColumnKeys = tableColumnToModelColumnMap.keySet();
		for(Integer tableColumn : tableColumnKeys)
		{
			int expectedModelColumnIndex = tableColumnToModelColumnMap.get(tableColumn).intValue();
			if (modelColumnArray.size() <= expectedModelColumnIndex)
				continue;
			
			int columnIndex = modelColumnArray.get(tableColumn).intValue();
			assertEquals("wrong column index", expectedModelColumnIndex, columnIndex);
			assertTrue("column index cannot be less than 0?", columnIndex >= 0);
		}
	}
}
