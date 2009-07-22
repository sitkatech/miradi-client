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
		
		OptionalDouble optionalDoubleWithValue = new OptionalDouble(11.0);
		assertTrue("should have value?", optionalDoubleWithValue.hasValue());
		
	}
	
	public void testAdd() throws Exception
	{
		OptionalDouble emptyPlusNull = empty.add(null);
		assertFalse("should not have value?", emptyPlusNull.hasValue());
		
		OptionalDouble emptyPlusEmpty = empty.add(anotherEmpty);
		assertFalse("should not have value?", emptyPlusEmpty.hasValue());
		
		OptionalDouble emptyPlusTen = empty.add(anotherTen);
		assertTrue("should have value?", emptyPlusTen.hasValue());
		assertEquals("wrong value?", 10.0, emptyPlusTen.getValue());
		
		OptionalDouble tenPlusTen = ten.add(anotherTen);
		assertTrue("should have value?", tenPlusTen.hasValue());
		assertEquals("wrong value?", 20.0, tenPlusTen.getValue());
		
		OptionalDouble tenPlusEmpty = empty.add(anotherEmpty);
		assertTrue("should have value?", tenPlusEmpty.hasValue());
		assertEquals("wrong value?", 10.0, tenPlusEmpty.getValue());
		
		OptionalDouble tenPlusNull = empty.add(null);
		assertTrue("should have value?", tenPlusNull.hasValue());
		assertEquals("wrong value?", 10.0, tenPlusNull.getValue());
	}
	
	public void testMultiply()
	{
		OptionalDouble emptyTimesEmpty = empty.multiply(new OptionalDouble());
		assertFalse("should have no value?", emptyTimesEmpty.hasValue());
		
		OptionalDouble emptyTimes10 = empty.multiply(ten);
		assertFalse("should have no value?", emptyTimes10.hasValue());
		
		OptionalDouble tenTimesTen = ten.multiply(anotherTen);
		assertTrue("should have value?", tenTimesTen.hasValue());
		assertEquals("wrong value?", 100.0, tenTimesTen.getValue());
	}
	
	public void testEquals()
	{
		assertEquals("Empty OD not equal to itself?", empty, empty);
		
		assertEquals("OD with value not equal to itself?", ten, ten);
		
		OptionalDouble twenty = new OptionalDouble(20.0);
		assertNotEquals("Different ODs claimed to be equal?", ten, twenty);
		assertNotEquals("Different ODs claimed to be equal (args swapped)?", twenty, ten);
		
		assertEquals("ODs should be equal?", anotherTen, ten);
		assertEquals("ODs should be equal (args swapped)?", ten, anotherTen);
	}
	
	public void testSubtract()
	{
		OptionalDouble emptyMinusEmpty = empty.subtract(new OptionalDouble());
		assertTrue("should not have value", emptyMinusEmpty.hasNoValue());
		
		OptionalDouble tenMinusEmpty = ten.subtract(empty);
		assertEquals("wrong value after subtraction?", 10.0, tenMinusEmpty.getValue());
		
		OptionalDouble emptyMinusTen = empty.subtract(ten);
		assertEquals("wrong value after subtracting from empty?", 10.0, emptyMinusTen.getValue());
		
		OptionalDouble tenMinusNull = ten.subtract(null);
		assertEquals("wrong value after subtracting null", 10.0, tenMinusNull.getValue());
		
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
	
	private final OptionalDouble empty = new OptionalDouble();
	private final OptionalDouble anotherEmpty = new OptionalDouble();
	private final OptionalDouble ten = new OptionalDouble(10.0);
	private final OptionalDouble anotherTen = new OptionalDouble(10.0);
	
}
