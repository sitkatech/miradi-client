/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objectdata;

import org.miradi.main.EAMTestCase;
import org.miradi.objectdata.DateData;


public class TestDateData extends EAMTestCase
{

	public TestDateData(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		DateData date = new DateData("tag");
		String sampleDate = "2006-01-01"; 
		date.set(sampleDate);
		assertEquals("Simple get/set failed?", sampleDate, date.get());
	}
}
