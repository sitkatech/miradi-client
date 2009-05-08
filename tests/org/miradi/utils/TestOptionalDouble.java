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

import org.miradi.main.EAMTestCase;

public class TestOptionalDouble extends EAMTestCase
{
	public TestOptionalDouble(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		OptionalDouble optionalDouble = new OptionalDouble();
		assertFalse("should not have value?", optionalDouble.hasValue());
		
		OptionalDouble returnedOptionalDouble1 = optionalDouble.add(null);
		assertFalse("should not have value?", returnedOptionalDouble1.hasValue());
		assertNull("should have been null?", returnedOptionalDouble1.getValue());
		
		OptionalDouble returnedOptionalDouble2 = optionalDouble.add(new OptionalDouble(10.0));
		assertTrue("should have value?", returnedOptionalDouble2.hasValue());
		assertEquals("wrong value?", 10.0, returnedOptionalDouble2.getValue());
		
		OptionalDouble optionalDoubleWithValue = new OptionalDouble(11.0);
		assertTrue("should have value?", optionalDoubleWithValue.hasValue());
		
		OptionalDouble returendOptionalDouble3 = optionalDoubleWithValue.add(new OptionalDouble(11.0));
		assertTrue("should have value?", returendOptionalDouble3.hasValue());
		assertEquals("wrong value?", 22.0, returendOptionalDouble3.getValue());
	}
	
	public void testMultiplication()
	{
		OptionalDouble optionalDouble = new OptionalDouble();
		OptionalDouble returnedValue1 = optionalDouble.multiply(new OptionalDouble());
		assertFalse("should have no value?", returnedValue1.hasValue());
		
		OptionalDouble returnedValue2 = optionalDouble.multiply(new OptionalDouble(10.0));
		assertFalse("should have no value?", returnedValue2.hasValue());
		
		OptionalDouble optionalDoubleWithValue = new OptionalDouble(10.0);
		OptionalDouble returnedValue3 = optionalDoubleWithValue.multiply(new OptionalDouble(10.0));
		assertTrue("should have value?", returnedValue3.hasValue());
		assertEquals("wrong value?", 100.0, returnedValue3.getValue());
	}
	
	public void testEquals()
	{
		OptionalDouble optionalDouble1 = new OptionalDouble();
		assertEquals("should be equal to itself?", optionalDouble1, optionalDouble1);
		
		OptionalDouble optionalDouble2 = new OptionalDouble(10.0);
		assertEquals("should be equal to itself?", optionalDouble2, optionalDouble2);
		
		OptionalDouble optionalDouble3 = new OptionalDouble(20.0);
		assertNotEquals("should not be equals", optionalDouble2, optionalDouble3);
	}
}
