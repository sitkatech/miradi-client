/*
 * Copyright 2006, The Conservation Measures Partnership & Beneficent
 * Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.io.BufferedReader;
import java.io.StringReader;

import org.conservationmeasures.eam.dialogs.TaxonomyItem;
import org.conservationmeasures.eam.dialogs.TaxonomyLoader;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestTaxonomyLoader extends EAMTestCase
{

	public TestTaxonomyLoader(String name)
	{
		super(name);
	}

	public void testNoDataReturnsDefautSelection() throws Exception
	{
		TaxonomyItem[] taxonomyItem = loadDelimitedData(new StringReader(""));
		assertEquals(1, taxonomyItem.length);
	}

	public void testOne() throws Exception
	{
		TaxonomyItem[] taxonomyItem = loadDelimitedData(new StringReader(" # \"header\"  \t H10.10 \"my level 1 descriptor\"  \t  \"my level 2 descriptor\" "));
		assertEquals(1, taxonomyItem.length);
	}
	

	private TaxonomyItem[] loadDelimitedData(StringReader stringReader) throws Exception
	{
		BufferedReader reader = new BufferedReader( stringReader );
		TaxonomyItem[] taxonomyItem = TaxonomyLoader.load(reader);
		return taxonomyItem;
	}


}
