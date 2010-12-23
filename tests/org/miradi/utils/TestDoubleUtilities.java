/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.util.Locale;

import org.miradi.main.MiradiTestCase;

public class TestDoubleUtilities extends MiradiTestCase
{
	public TestDoubleUtilities(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		
		previousLocale = Locale.getDefault();
	}
	
	@Override
	public void tearDown() throws Exception
	{
		setLocale(previousLocale);
		
		super.tearDown();
	}
	
	public void testToDoubleForData() throws Exception
	{
		switchToGermanLocale();
		assertEquals("incorrect value?", 0.5, DoubleUtilities.toDoubleFromDataFormat("0,5"));
		assertEquals("incorrect value?", 1000.5, DoubleUtilities.toDoubleFromDataFormat("1.000,5"));
		assertEquals("incorrect value?", -1000.5, DoubleUtilities.toDoubleFromDataFormat("-1.000,5"));	

		switchToUsLocale();
		assertEquals("incorrect value?", 0.5, DoubleUtilities.toDoubleFromDataFormat("0.5"));
		assertEquals("incorrect value?", 1000.5, DoubleUtilities.toDoubleFromDataFormat("1,000.5"));
		assertEquals("incorrect value?", -1000.5, DoubleUtilities.toDoubleFromDataFormat("-1,000.5"));
	}

	public void testToDoubleForHumans() throws Exception
	{
		switchToGermanLocale();
		assertEquals("incorrect value?", 0.5, DoubleUtilities.toDoubleForHumans("0,5"));
		assertEquals("incorrect value?", 1000.5, DoubleUtilities.toDoubleForHumans("1.000,5"));
		assertEquals("incorrect value?", -1000.5, DoubleUtilities.toDoubleForHumans("-1.000,5"));
		
		switchToUsLocale();
		assertEquals("incorrect value?", 0.5, DoubleUtilities.toDoubleForHumans("0.5"));
		assertEquals("incorrect value?", 1000.5, DoubleUtilities.toDoubleForHumans("1000.5"));
		assertEquals("incorrect value?", -1000.5, DoubleUtilities.toDoubleForHumans("-1000.5"));
	}
	
	public void testToStringForData()
	{
		switchToGermanLocale();
		assertEquals("incorrect value?", "0.5", DoubleUtilities.toStringForData(0.5));
		assertEquals("incorrect value?", "1000.5", DoubleUtilities.toStringForData(1000.5));
		assertEquals("incorrect value?", "-1000.5", DoubleUtilities.toStringForData(-1000.5));
		
		switchToUsLocale();
		assertEquals("incorrect value", "0.5", DoubleUtilities.toStringForData(0.5));
		assertEquals("incorrect value", "1000.5", DoubleUtilities.toStringForData(1000.5));
		assertEquals("incorrect value", "-1000.5", DoubleUtilities.toStringForData(-1000.5));
	}
	
	public void testToStringForHumans()
	{
		switchToGermanLocale();
		assertEquals("incorrect value?", "0,5", DoubleUtilities.toStringForHumans(0.5));
		assertEquals("incorrect value?", "1.000,5", DoubleUtilities.toStringForHumans(1000.5));
		assertEquals("incorrect value?", "-1.000,5", DoubleUtilities.toStringForHumans(-1000.5));
		
		switchToUsLocale();
		assertEquals("incorrect value?", "0.5", DoubleUtilities.toStringForHumans(0.5));
		assertEquals("incorrect value?", "1,000.5", DoubleUtilities.toStringForHumans(1000.5));
		assertEquals("incorrect value?", "-1,000.5", DoubleUtilities.toStringForHumans(-1000.5));
	}
	
	private void switchToGermanLocale()
	{
		setLocale(Locale.GERMANY);
		assertEquals("didnt change to german locale", "Deutschland", Locale.getDefault().getDisplayCountry());
	}

	private void switchToUsLocale()
	{
		setLocale(Locale.US);
	}

	private void setLocale(Locale locale)
	{
		Locale.setDefault(locale);
	}
	
	private Locale previousLocale;
}
