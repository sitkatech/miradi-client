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
		verifyColumnIndexes("abcd", "abcd", "");
	}
	
	public void testDifferentDesiredSizeThanModelColumnCodes()
	{		
		verifyColumnIndexes("abc", "abcd", "abc");
	}
	
	public void testBuildModelColumnIndexArray()
	{		
		verifyColumnIndexes("cadb", "abcd", "cadb");
	}
	
	public void testNonExistingCodesFromModelColumnCodes()
	{
		verifyColumnIndexes("efgh", "abcd", "efgh");
	}
	
	private void verifyColumnIndexes(String expectedExpectedColumnCodesAsString, String modelColumnCodesAsString, String desiredColumnCodesAsString)
	{
		CodeList expectedColumnCodes = createCodeList(expectedExpectedColumnCodesAsString);
		CodeList modelColumnCodes = createCodeList(modelColumnCodesAsString);
		CodeList desiredColumnCodes = createCodeList(desiredColumnCodesAsString);
		HashMap<Integer, Integer> modelColumnMap = AbstractTableExporter.buildModelColumnIndexMap(desiredColumnCodes, modelColumnCodes);
		
		assertEquals("wrong map size", expectedColumnCodes.size(), modelColumnMap.size());
		Set<Integer> keys = modelColumnMap.keySet();
		for(Integer keyModelColumnIndex : keys)
		{
			int tableColumn = modelColumnMap.get(keyModelColumnIndex).intValue();
			String code = expectedColumnCodes.get(tableColumn);
			assertEquals("wrong table column index", tableColumn, expectedColumnCodes.find(code));
		}
	}

	private CodeList createCodeList(String model)
	{
		CodeList codeList = new CodeList();
		char[] codes = model.toCharArray();
		for (int index = 0; index < codes.length; ++index)
		{
			codeList.add(Character.toString(codes[index]));
		}
		
		return codeList;
	}	
}
