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
		
		assertEquals("different hashcodes for empties?", empty.hashCode(), anotherEmpty.hashCode());
		assertEquals("different hashcodes for tens?", ten.hashCode(), anotherTen.hashCode());
		
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
	
	public void testAdd() throws Exception
	{
		OptionalDouble emptyPlusNull = empty.add(null);
		assertFalse("should not have value?", emptyPlusNull.hasValue());
		
		OptionalDouble emptyPlusEmpty = empty.add(anotherEmpty);
		assertFalse("should not have value?", emptyPlusEmpty.hasValue());
		
		OptionalDouble emptyPlusTen = empty.add(anotherTen);
		assertTrue("should have value?", emptyPlusTen.hasValue());
		assertEquals("wrong value?", 10.0, emptyPlusTen.getValue());
		
		OptionalDouble tenPlusNull = empty.add(null);
		assertTrue("should have value?", tenPlusNull.hasValue());
		assertEquals("wrong value?", 10.0, tenPlusNull.getValue());

		OptionalDouble tenPlusEmpty = empty.add(anotherEmpty);
		assertTrue("should have value?", tenPlusEmpty.hasValue());
		assertEquals("wrong value?", 10.0, tenPlusEmpty.getValue());
		
		OptionalDouble tenPlusTen = ten.add(anotherTen);
		assertTrue("should have value?", tenPlusTen.hasValue());
		assertEquals("wrong value?", 20.0, tenPlusTen.getValue());
		
	}
	
	public void testMultiply()
	{
		OptionalDouble emptyTimesNull = empty.multiply(null);
		assertFalse("should have no value?", emptyTimesNull.hasValue());
		
		OptionalDouble emptyTimesEmpty = empty.multiply(anotherEmpty);
		assertFalse("should have no value?", emptyTimesEmpty.hasValue());
		
		OptionalDouble emptyTimesTen = empty.multiply(ten);
		assertFalse("should have no value?", emptyTimesTen.hasValue());
		
		OptionalDouble tenTimesNull = ten.multiply(null);
		assertFalse("should have no value?", tenTimesNull.hasValue());
		
		OptionalDouble tenTimesEmpty = ten.multiply(empty);
		assertFalse("should have no value?", tenTimesEmpty.hasValue());

		OptionalDouble tenTimesTen = ten.multiply(anotherTen);
		assertTrue("should have value?", tenTimesTen.hasValue());
		assertEquals("wrong value?", 100.0, tenTimesTen.getValue());
	}
	
	public void testSubtract()
	{
		OptionalDouble emptyMinusNull = empty.subtract(null);
		assertTrue("should not have value", emptyMinusNull.hasNoValue());
		
		OptionalDouble emptyMinusEmpty = empty.subtract(anotherEmpty);
		assertTrue("should not have value", emptyMinusEmpty.hasNoValue());
		
		OptionalDouble emptyMinusTen = empty.subtract(ten);
		assertEquals("wrong value after subtracting from empty?", 10.0, emptyMinusTen.getValue());
		
		OptionalDouble tenMinusNull = ten.subtract(null);
		assertEquals("wrong value after subtraction?", 10.0, tenMinusNull.getValue());
		
		OptionalDouble tenMinusEmpty = ten.subtract(empty);
		assertEquals("wrong value after subtraction?", 10.0, tenMinusEmpty.getValue());
		
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
