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

	public void testColumnIndexMap()
	{
		verifyColumnIndexes("abcd", "abcd", "");
		verifyColumnIndexes("abcd", "abcd", "abc");
		verifyColumnIndexes("acbd", "abcd", "acb");
		verifyColumnIndexes("cadb", "abcd", "cadb");
		verifyColumnIndexes("abcd", "abcd", "efgh");
		//FIXME temporarly disabled
		//verifyColumnIndexes("cabb", "abbc", "cab");
	}
	
	private void verifyColumnIndexes(String expectedExpectedColumnCodesAsString, String modelColumnCodesAsString, String desiredColumnCodesAsString)
	{
		CodeList expectedColumnCodes = createCodeList(expectedExpectedColumnCodesAsString);
		CodeList modelColumnCodes = createCodeList(modelColumnCodesAsString);
		CodeList desiredColumnCodes = createCodeList(desiredColumnCodesAsString);
		HashMap<Integer, Integer> tableColumnToModelColumnMap = AbstractTableExporter.buildModelColumnIndexMap(desiredColumnCodes, modelColumnCodes);
		
		assertEquals("wrong map size", expectedColumnCodes.size(), tableColumnToModelColumnMap.size());
		Set<Integer> tableColumnKeys = tableColumnToModelColumnMap.keySet();
		for(Integer tableColumnKey : tableColumnKeys)
		{
			int modelColumnIndex = tableColumnToModelColumnMap.get(tableColumnKey);
			String code = expectedColumnCodes.get(modelColumnIndex);
			int foundIndex = expectedColumnCodes.find(code);
			assertEquals("wrong model column index", foundIndex, modelColumnIndex);
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
