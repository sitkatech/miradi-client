/*
 * Copyright 2006, The Conservation Measures Partnership & Beneficent
 * Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.io.BufferedReader;
import java.io.StringReader;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.TwoLevelEntry;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;

public class TestTaxonomyLoader extends EAMTestCase
{

	public TestTaxonomyLoader(String name)
	{
		super(name);
	}

	public void testNoDataReturnsDefautSelection() throws Exception
	{
		TwoLevelEntry[] taxonomyItem = loadDelimitedData(new StringReader(""));
		assertEquals(1, taxonomyItem.length);
	}

	public void testOne() throws Exception
	{
		TwoLevelEntry[] taxonomyItem = loadDelimitedData(new StringReader(" # \t \"header\" \t \"xxxx\" \n" +
				" H10.10 \t \"my level 1 descriptor\"  \t  \"my level 2 descriptor\" "));
		assertEquals(3, taxonomyItem.length);
		assertEquals("--Select a classification--", taxonomyItem[0].getEntryDescription());
		assertEquals("H10", taxonomyItem[1].getEntryCode());		
		assertEquals(true, taxonomyItem[1].getEntryDescription().endsWith("my level 1 descriptor"));
		assertEquals("H10.10", taxonomyItem[2].getEntryCode());
		assertEquals(true, taxonomyItem[2].getEntryDescription().endsWith("my level 2 descriptor"));
	}
	

	private TwoLevelEntry[] loadDelimitedData(StringReader stringReader) throws Exception
	{
		BufferedReader reader = new BufferedReader( stringReader );
		TwoLevelEntry[] taxonomyItem = TaxonomyLoader.load(reader);
		return taxonomyItem;
	}


}
