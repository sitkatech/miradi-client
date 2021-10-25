/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.project;

import org.martus.util.TestCaseEnhanced;
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.CodeList;

public class TestKEAViabilityFormula extends TestCaseEnhanced
{
	public TestKEAViabilityFormula(String name)
	{
		super(name);
	}

	public void testGetValueFromRatingCode() throws Exception
	{
		assertEquals(0, KEAViabilityFormula.getValueFromRatingCode(StatusQuestion.UNSPECIFIED), 0.0001);
		assertEquals(1, KEAViabilityFormula.getValueFromRatingCode(StatusQuestion.POOR), 0.0001);
		assertEquals(2.5, KEAViabilityFormula.getValueFromRatingCode(StatusQuestion.FAIR), 0.0001);
		assertEquals(3.5, KEAViabilityFormula.getValueFromRatingCode(StatusQuestion.GOOD), 0.0001);
		assertEquals(4.0, KEAViabilityFormula.getValueFromRatingCode(StatusQuestion.VERY_GOOD), 0.0001);
		try
		{
			KEAViabilityFormula.getValueFromRatingCode("8");
			fail("Should have thrown for unknown value");
		}
		catch(KEAViabilityFormula.UnexpectedValueException ignoreExpected)
		{
		}
	}

	public void testGetRatingCodeFromValue() throws Exception
	{
		assertEquals(StatusQuestion.POOR, KEAViabilityFormula.getRatingCodeFromValue(-0.01));
		assertEquals(StatusQuestion.POOR, KEAViabilityFormula.getRatingCodeFromValue(0.0));
		assertEquals(StatusQuestion.POOR, KEAViabilityFormula.getRatingCodeFromValue(1.74));
		assertEquals(StatusQuestion.FAIR, KEAViabilityFormula.getRatingCodeFromValue(1.75));
		assertEquals(StatusQuestion.FAIR, KEAViabilityFormula.getRatingCodeFromValue(2.99));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getRatingCodeFromValue(3.0));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getRatingCodeFromValue(3.74));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getRatingCodeFromValue(3.75));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getRatingCodeFromValue(4.5));
	}
	
	public void testGetAverageRatingCode() throws Exception
	{
		assertEquals(StatusQuestion.UNSPECIFIED, KEAViabilityFormula.getAverageRatingCode(
				new CodeList()));
		assertEquals(StatusQuestion.UNSPECIFIED, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.UNSPECIFIED})));
		assertEquals(StatusQuestion.POOR, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.POOR})));
		assertEquals(StatusQuestion.POOR, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.POOR, StatusQuestion.UNSPECIFIED})));
		assertEquals(StatusQuestion.FAIR, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.POOR, StatusQuestion.FAIR})));
		assertEquals(StatusQuestion.FAIR, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.POOR, StatusQuestion.FAIR, 
						StatusQuestion.FAIR, StatusQuestion.FAIR})));
		assertEquals(StatusQuestion.FAIR, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.FAIR})));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.FAIR, StatusQuestion.GOOD})));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.GOOD, StatusQuestion.GOOD, 
						StatusQuestion.GOOD, StatusQuestion.FAIR})));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.GOOD})));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.GOOD, StatusQuestion.VERY_GOOD})));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.GOOD, StatusQuestion.VERY_GOOD, 
						StatusQuestion.VERY_GOOD, StatusQuestion.VERY_GOOD})));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {StatusQuestion.VERY_GOOD})));
		try
		{
			KEAViabilityFormula.getAverageRatingCode(new CodeList(new String[] {"8"}));
			fail("Should have thrown for unknown value");
		}
		catch(KEAViabilityFormula.UnexpectedValueException ignoreExpected)
		{
		}

	}
	
	public void testGetTotalCategoryRatingCode() throws Exception
	{
		assertEquals(StatusQuestion.UNSPECIFIED, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {})));
		assertEquals(StatusQuestion.UNSPECIFIED, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.UNSPECIFIED})));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.VERY_GOOD, StatusQuestion.UNSPECIFIED, 
						StatusQuestion.VERY_GOOD})));
		assertEquals(StatusQuestion.POOR, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.VERY_GOOD, StatusQuestion.POOR, 
						StatusQuestion.VERY_GOOD})));
		assertEquals(StatusQuestion.FAIR, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.VERY_GOOD, StatusQuestion.FAIR, 
						StatusQuestion.VERY_GOOD})));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.GOOD})));
		assertEquals(StatusQuestion.GOOD, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.GOOD, StatusQuestion.VERY_GOOD})));
		assertEquals(StatusQuestion.VERY_GOOD, KEAViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {StatusQuestion.VERY_GOOD})));
		try
		{
			KEAViabilityFormula.getTotalCategoryRatingCode(new CodeList(new String[] {"8"}));
			fail("Should have thrown for unknown value");
		}
		catch(KEAViabilityFormula.UnexpectedValueException ignoreExpected)
		{
		}
		
	}

}
