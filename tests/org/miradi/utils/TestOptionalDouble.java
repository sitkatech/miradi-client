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
	
	public void testAddition() throws Exception
	{
		OptionalDouble empty = new OptionalDouble();
		
		OptionalDouble emptyPlusNull = empty.add(null);
		assertFalse("should not have value?", emptyPlusNull.hasValue());
		
		OptionalDouble emptyPlusTen = empty.add(new OptionalDouble(10.0));
		assertTrue("should have value?", emptyPlusTen.hasValue());
		assertEquals("wrong value?", 10.0, emptyPlusTen.getValue());
		
		OptionalDouble eleven = new OptionalDouble(11.0);
		OptionalDouble elevenPlusEleven = eleven.add(new OptionalDouble(11.0));
		assertTrue("should have value?", elevenPlusEleven.hasValue());
		assertEquals("wrong value?", 22.0, elevenPlusEleven.getValue());
	}
	
	public void testMultiplication()
	{
		OptionalDouble empty = new OptionalDouble();
		OptionalDouble emptyTimesEmpty = empty.multiply(new OptionalDouble());
		assertFalse("should have no value?", emptyTimesEmpty.hasValue());
		
		OptionalDouble emptyTimes10 = empty.multiply(new OptionalDouble(10.0));
		assertFalse("should have no value?", emptyTimes10.hasValue());
		
		OptionalDouble ten = new OptionalDouble(10.0);
		OptionalDouble tenTimesTen = ten.multiply(new OptionalDouble(10.0));
		assertTrue("should have value?", tenTimesTen.hasValue());
		assertEquals("wrong value?", 100.0, tenTimesTen.getValue());
	}
	
	public void testEquals()
	{
		OptionalDouble empty = new OptionalDouble();
		assertEquals("Empty OD not equal to itself?", empty, empty);
		
		OptionalDouble ten = new OptionalDouble(10.0);
		assertEquals("OD with value not equal to itself?", ten, ten);
		
		OptionalDouble twenty = new OptionalDouble(20.0);
		assertNotEquals("Different ODs claimed to be equal?", ten, twenty);
		assertNotEquals("Different ODs claimed to be equal (args swapped)?", twenty, ten);
		
		OptionalDouble anotherTwenty = new OptionalDouble(20.0);
		assertEquals("ODs should be equal?", anotherTwenty, twenty);
		assertEquals("ODs should be equal (args swapped)?", twenty, anotherTwenty);
	}
	
	public void testSubtraction()
	{
		OptionalDouble emptyValue = new OptionalDouble();
		OptionalDouble emptyMinusEmpty = emptyValue.subtract(new OptionalDouble());
		assertTrue("should not have value", emptyMinusEmpty.hasNoValue());
		
		OptionalDouble two = new OptionalDouble(2.0);
		OptionalDouble twoMinusEmpty = two.subtract(emptyValue);
		assertEquals("wrong value after subtraction?", 2.0, twoMinusEmpty.getValue());
		
		OptionalDouble emptyMinusTwo = emptyValue.subtract(two);
		assertEquals("wrong value after subtracting from empty?", 2.0, emptyMinusTwo.getValue());
		
		OptionalDouble twoMinusNull = two.subtract(null);
		assertEquals("wrong value after subtracting null", 2.0, twoMinusNull.getValue());
		
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
