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
		assertFalse("empty has value?", empty.hasValue());
		assertTrue("empty hasNoValue returned false?", empty.hasNoValue());
		assertTrue("ten doesn't have value?", ten.hasValue());
		assertFalse("ten hasNoValue returned true?", ten.hasNoValue());
		
		assertEquals("different hashcodes for empties?", empty.hashCode(), anotherEmpty.hashCode());
		assertEquals("different hashcodes for tens?", ten.hashCode(), anotherTen.hashCode());		
	}
	
	public void testHasNonZeroValue()
	{
		assertFalse("empty OptionalDouble should not have value?", new OptionalDouble().hasNonZeroValue());
		assertTrue("OptionalDouble with 1 has zero value?", new OptionalDouble(1.0).hasNonZeroValue());
		assertFalse("OptionalDouble with 0 should has non zero value?", new OptionalDouble(0.0).hasNonZeroValue());
		assertTrue("OptionalDouble with -1 has zero value?", new OptionalDouble(-1.0).hasNonZeroValue());
	}
	
	public void testEquals()
	{
		assertNotEquals("OD equal to null?", empty, null);
		assertNotEquals("OD equal to String?", empty, "a string");

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
		assertFalse("emptyPlusNull has value?", emptyPlusNull.hasValue());
		
		OptionalDouble emptyPlusEmpty = empty.add(anotherEmpty);
		assertFalse("emptyPlusEmpty has value?", emptyPlusEmpty.hasValue());
		
		OptionalDouble emptyPlusTen = empty.add(anotherTen);
		assertTrue("emptyPlusTen doesn't have value?", emptyPlusTen.hasValue());
		assertEquals("emptyPlusTen wrong value?", 10.0, emptyPlusTen.getValue());
		
		OptionalDouble tenPlusNull = ten.add(null);
		assertTrue("tenPlusNull doesn't have value?", tenPlusNull.hasValue());
		assertEquals("tenPlusNull wrong value?", 10.0, tenPlusNull.getValue());

		OptionalDouble tenPlusEmpty = ten.add(empty);
		assertTrue("tenPlusEmpty doesn't have value?", tenPlusEmpty.hasValue());
		assertEquals("tenPlusEmpty wrong value?", 10.0, tenPlusEmpty.getValue());
		
		OptionalDouble tenPlusTen = ten.add(anotherTen);
		assertTrue("tenPlusEmpty doesn't have value?", tenPlusTen.hasValue());
		assertEquals("tenPlusEmpty wrong value?", 20.0, tenPlusTen.getValue());
		
	}
	
	public void testMultiply()
	{
		OptionalDouble emptyTimesNull = empty.multiply(null);
		assertFalse("emptyTimesNull has value?", emptyTimesNull.hasValue());
		
		OptionalDouble emptyTimesEmpty = empty.multiply(anotherEmpty);
		assertFalse("emptyTimesEmpty has value?", emptyTimesEmpty.hasValue());
		
		OptionalDouble emptyTimesTen = empty.multiply(ten);
		assertFalse("emptyTimesTen has value?", emptyTimesTen.hasValue());
		
		OptionalDouble tenTimesNull = ten.multiply(null);
		assertFalse("tenTimesNull has value?", tenTimesNull.hasValue());
		
		OptionalDouble tenTimesEmpty = ten.multiply(empty);
		assertFalse("tenTimesEmpty has value?", tenTimesEmpty.hasValue());

		OptionalDouble tenTimesTen = ten.multiply(anotherTen);
		assertTrue("tenTimesTen doesn't have value?", tenTimesTen.hasValue());
		assertEquals("tenTimesTen wrong value?", 100.0, tenTimesTen.getValue());
	}
	
	public void testDivideBy()
	{
		OptionalDouble emptyDividedByNull = empty.divideBy(null);
		assertFalse("emptyDividedByNull has value?", emptyDividedByNull.hasValue());
		
		OptionalDouble emptyDividedByEmpty = empty.divideBy(anotherEmpty);
		assertFalse("emptyDividedByEmpty has value?", emptyDividedByEmpty.hasValue());
		
		OptionalDouble emptyDividedByTen = empty.divideBy(ten);
		assertFalse("emptyDividedByTen has value?", emptyDividedByTen.hasValue());
		
		OptionalDouble tenDividedByNull = ten.divideBy(null);
		assertFalse("tenDividedByNull has value?", tenDividedByNull.hasValue());
		
		OptionalDouble tenDividedByEmpty = ten.divideBy(empty);
		assertFalse("tenDividedByEmpty has value?", tenDividedByEmpty.hasValue());

		OptionalDouble tenDividedByTen = ten.divideBy(anotherTen);
		assertTrue("tenDividedByTen doesn't have value?", tenDividedByTen.hasValue());
		assertEquals("tenDividedByTen wrong value?", 1.0, tenDividedByTen.getValue());
		
		OptionalDouble tenDividedByZero = ten.divideBy(zero);
		assertTrue("tenDividedByZero doesn't have value", tenDividedByZero.hasValue());
		assertEquals("tenDividedByZero has wrong value?", Double.POSITIVE_INFINITY, tenDividedByZero.getValue());
		
		OptionalDouble zeroDividedByTen = zero.divideBy(ten);
		assertTrue("zeroDividedByTen doesn't have value?", zeroDividedByTen.hasValue());
		assertEquals("zeroDividedByTen has wrong value?", 0.0, zeroDividedByTen.getValue());
	}
	
	public void testSubtract()
	{
		OptionalDouble emptyMinusNull = empty.subtract(null);
		assertFalse("emptyMinusNull has value?", emptyMinusNull.hasValue());
		
		OptionalDouble emptyMinusEmpty = empty.subtract(anotherEmpty);
		assertFalse("emptyMinusEmpty has value?", emptyMinusEmpty.hasValue());
		
		OptionalDouble emptyMinusTen = empty.subtract(ten);
		assertTrue("emptyMinusTen doesn't have value?", emptyMinusTen.hasValue());
		assertEquals("emptyMinusTen wrong value?", -10.0, emptyMinusTen.getValue());
		
		OptionalDouble tenMinusNull = ten.subtract(null);
		assertTrue("tenMinusNull doesn't have value?", tenMinusNull.hasValue());
		assertEquals("tenMinusNull wrong value?", 10.0, tenMinusNull.getValue());
		
		OptionalDouble tenMinusEmpty = ten.subtract(empty);
		assertTrue("tenMinusEmpty doesn't have value?", tenMinusEmpty.hasValue());
		assertEquals("tenMinusEmpty wrong value?", 10.0, tenMinusEmpty.getValue());
		
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
		
		assertEquals("wrong value for " + value1 + " minus " + value2 + "?", expectedValue, subtractedValue.getValue());
	}
	
	private final OptionalDouble empty = new OptionalDouble();
	private final OptionalDouble anotherEmpty = new OptionalDouble();
	private final OptionalDouble ten = new OptionalDouble(10.0);
	private final OptionalDouble anotherTen = new OptionalDouble(10.0);
	private final OptionalDouble zero = new OptionalDouble(0.0);
}
