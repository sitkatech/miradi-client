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

import java.io.BufferedReader;
import java.io.StringReader;

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.TaxonomyFileLoader;
import org.miradi.objecthelpers.TwoLevelEntry;

public class TestTaxonomyFileLoader extends EAMTestCase
{

	public TestTaxonomyFileLoader(String name)
	{
		super(name);
	}

	public void testNoDataReturnsDefautSelection() throws Exception
	{
		TwoLevelEntry[] twoLevelItem = loadDelimitedData(new StringReader(""));
		assertEquals(1, twoLevelItem.length);
	}

	public void testOne() throws Exception
	{
		TwoLevelEntry[] twoLevelItem = loadDelimitedData(new StringReader("# \t \"header\" \t \"xxxx\" \n" +
				" H10.10 \t \"my level 1 descriptor\"  \t  \"my level 2 descriptor\" "));
		assertEquals(3, twoLevelItem.length);
		assertEquals("--Select a classification--", twoLevelItem[0].getEntryDescription());
		assertEquals("H10", twoLevelItem[1].getEntryCode());		
		assertEquals(true, twoLevelItem[1].getEntryDescription().endsWith("my level 1 descriptor"));
		assertEquals("H10.10", twoLevelItem[2].getEntryCode());
		assertEquals(true, twoLevelItem[2].getEntryDescription().endsWith("my level 2 descriptor"));
	}
	

	private TwoLevelEntry[] loadDelimitedData(StringReader stringReader) throws Exception
	{
		BufferedReader reader = new BufferedReader( stringReader );
		TwoLevelEntry[] twoLevelItem = new TaxonomyFileLoader("").load(reader);
		return twoLevelItem;
	}


}
