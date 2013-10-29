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
		verifyColumnIndexes("bacd", "abcd", "ebag");
		verifyColumnIndexes("cabb", "abbc", "cab");
	}
	
	private void verifyColumnIndexes(String expectedExpectedColumnCodesAsString, String modelColumnCodesAsString, String desiredColumnCodesAsString)
	{
		CodeList expectedColumnCodes = createCodeList(expectedExpectedColumnCodesAsString);
		CodeList modelColumnCodes = createCodeList(modelColumnCodesAsString);
		CodeList desiredColumnCodes = createCodeList(desiredColumnCodesAsString);
		HashMap<Integer, Integer> tableColumnToModelColumnMap = AbstractTableExporter.buildModelColumnIndexMap(desiredColumnCodes, modelColumnCodes);
		
		assertEquals("wrong map size", expectedColumnCodes.size(), tableColumnToModelColumnMap.size());
		for (int tableColumn = 0; tableColumn < expectedColumnCodes.size(); ++tableColumn)
		{
			String expectedColumnCode = expectedColumnCodes.get(tableColumn);
			int modelColumn = tableColumnToModelColumnMap.get(tableColumn);
			String modelColumnCode = modelColumnCodes.get(modelColumn);
			assertEquals("wrong model column code?", expectedColumnCode, modelColumnCode);
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
