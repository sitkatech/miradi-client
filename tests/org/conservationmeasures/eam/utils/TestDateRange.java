/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.martus.util.MultiCalendar;

public class TestDateRange extends EAMTestCase
{
	public TestDateRange(String name) throws Exception
	{
		super(name);
	}
	
	public void test() throws Exception
	{
		storeAndRestore();	
	}
	
	private void storeAndRestore() throws Exception
	{
		MultiCalendar start = MultiCalendar.createFromGregorianYearMonthDay(2006, 12, 1);
		MultiCalendar end = MultiCalendar.createFromGregorianYearMonthDay(2006, 12, 2);
		DateRange dateRange = new DateRange(start, end);
		
		EnhancedJsonObject json = dateRange.toJson();
		
		DateRange dateRange2 = new DateRange(json);
		assertEquals("start date is same?", start, dateRange2.getStartDate());
		assertEquals("end date is same?", end, dateRange2.getEndDate());	
	}
}