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
		assertEquals("wrong hashcode for empty value?", new OptionalDouble().hashCode(), optionalDouble.hashCode());
		assertFalse("should not have value?", optionalDouble.hasValue());
		
		OptionalDouble returnedOptionalDouble1 = optionalDouble.add(null);
		assertFalse("should not have value?", returnedOptionalDouble1.hasValue());
		
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
		assertEquals("Empty OD not equal to itself?", optionalDouble1, optionalDouble1);
		
		OptionalDouble optionalDouble2 = new OptionalDouble(10.0);
		assertEquals("OD with value not equal to itself?", optionalDouble2, optionalDouble2);
		
		OptionalDouble optionalDouble3 = new OptionalDouble(20.0);
		assertNotEquals("Different ODs claimed to be equal?", optionalDouble2, optionalDouble3);
		assertNotEquals("Different ODs claimed to be equal (args swapped)?", optionalDouble3, optionalDouble2);
		
		OptionalDouble optionalDouble4 = new OptionalDouble(20.0);
		assertEquals("ODs should be equal?", optionalDouble4, optionalDouble3);
		assertEquals("ODs should be equal (args swapped)?", optionalDouble3, optionalDouble4);
	}
	
	public void testSubtraction()
	{
		OptionalDouble emptyValue = new OptionalDouble();
		OptionalDouble subtractedValue = emptyValue.subtract(new OptionalDouble());
		assertTrue("should not have value", subtractedValue.hasNoValue());
		
		OptionalDouble two = new OptionalDouble(2.0);
		OptionalDouble subtractedValue2 = two.subtract(emptyValue);
		assertEquals("wrong value after subtraction?", 2.0, subtractedValue2.getValue());
		
		OptionalDouble subtractedValue3 = emptyValue.subtract(two);
		assertEquals("wrong value after subtracting from empty?", 2.0, subtractedValue3.getValue());
		
		OptionalDouble subtractedValue4 = two.subtract(null);
		assertEquals("wrong value after subtracting null", 2.0, subtractedValue4.getValue());
		
		verifySubtraction(3.0, 5.0, 2.0);
		verifySubtraction(-3.0, 2.0, 5.0);
		verifySubtraction(-7.0, -5.0, 2.0);
		verifySubtraction(7.0, 5.0, -2.0);
		verifySubtraction(-3.0, -5.0, -2.0);
	}
	
	private void verifySubtraction(double expectedValue, double value1, double value2)
	{
		OptionalDouble optionalDouble1 = new OptionalDouble(value1);
		OptionalDouble optionalDouble2 = new OptionalDouble(value2);
		OptionalDouble subtractedValue = optionalDouble1.subtract(optionalDouble2);
		
		assertEquals("wrong value after subtraction?", expectedValue, subtractedValue.getValue());
	}
}
