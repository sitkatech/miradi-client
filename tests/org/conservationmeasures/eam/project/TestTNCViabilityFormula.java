/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.utils.CodeList;
import org.martus.util.TestCaseEnhanced;

public class TestTNCViabilityFormula extends TestCaseEnhanced
{
	public TestTNCViabilityFormula(String name)
	{
		super(name);
	}

	public void testGetValueFromRatingCode() throws Exception
	{
		assertEquals(0, TNCViabilityFormula.getValueFromRatingCode(TNCViabilityFormula.UNSPECIFIED), 0.0001);
		assertEquals(1, TNCViabilityFormula.getValueFromRatingCode(TNCViabilityFormula.POOR), 0.0001);
		assertEquals(2.5, TNCViabilityFormula.getValueFromRatingCode(TNCViabilityFormula.FAIR), 0.0001);
		assertEquals(3.5, TNCViabilityFormula.getValueFromRatingCode(TNCViabilityFormula.GOOD), 0.0001);
		assertEquals(4.0, TNCViabilityFormula.getValueFromRatingCode(TNCViabilityFormula.VERY_GOOD), 0.0001);
		try
		{
			TNCViabilityFormula.getValueFromRatingCode("8");
			fail("Should have thrown for unknown value");
		}
		catch(TNCViabilityFormula.UnexpectedValueException ignoreExpected)
		{
		}
	}

	public void testGetRatingCodeFromValue() throws Exception
	{
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getRatingCodeFromValue(-0.01));
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getRatingCodeFromValue(0.0));
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getRatingCodeFromValue(1.74));
		assertEquals(TNCViabilityFormula.FAIR, TNCViabilityFormula.getRatingCodeFromValue(1.75));
		assertEquals(TNCViabilityFormula.FAIR, TNCViabilityFormula.getRatingCodeFromValue(2.99));
		assertEquals(TNCViabilityFormula.GOOD, TNCViabilityFormula.getRatingCodeFromValue(3.0));
		assertEquals(TNCViabilityFormula.GOOD, TNCViabilityFormula.getRatingCodeFromValue(3.74));
		assertEquals(TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.getRatingCodeFromValue(3.75));
		assertEquals(TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.getRatingCodeFromValue(4.5));
	}
	
	public void testGetAverageRatingCode() throws Exception
	{
		assertEquals(TNCViabilityFormula.UNSPECIFIED, TNCViabilityFormula.getAverageRatingCode(
				new CodeList()));
		assertEquals(TNCViabilityFormula.UNSPECIFIED, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.UNSPECIFIED})));
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.POOR})));
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.POOR, TNCViabilityFormula.UNSPECIFIED})));
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.POOR, TNCViabilityFormula.FAIR})));
		assertEquals(TNCViabilityFormula.FAIR, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.POOR, TNCViabilityFormula.FAIR, 
						TNCViabilityFormula.FAIR, TNCViabilityFormula.FAIR})));
		assertEquals(TNCViabilityFormula.FAIR, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.FAIR})));
		assertEquals(TNCViabilityFormula.FAIR, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.FAIR, TNCViabilityFormula.GOOD})));
		assertEquals(TNCViabilityFormula.GOOD, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.GOOD})));
		assertEquals(TNCViabilityFormula.GOOD, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.GOOD, TNCViabilityFormula.VERY_GOOD})));
		assertEquals(TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.GOOD, TNCViabilityFormula.VERY_GOOD, 
						TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.VERY_GOOD})));
		assertEquals(TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.getAverageRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.VERY_GOOD})));
		try
		{
			TNCViabilityFormula.getAverageRatingCode(new CodeList(new String[] {"8"}));
			fail("Should have thrown for unknown value");
		}
		catch(TNCViabilityFormula.UnexpectedValueException ignoreExpected)
		{
		}

	}
	
	public void testGetTotalCategoryRatingCode() throws Exception
	{
		assertEquals(TNCViabilityFormula.UNSPECIFIED, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {})));
		assertEquals(TNCViabilityFormula.UNSPECIFIED, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.UNSPECIFIED})));
		assertEquals(TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.UNSPECIFIED, 
						TNCViabilityFormula.VERY_GOOD})));
		assertEquals(TNCViabilityFormula.POOR, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.POOR, 
						TNCViabilityFormula.VERY_GOOD})));
		assertEquals(TNCViabilityFormula.FAIR, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.FAIR, 
						TNCViabilityFormula.VERY_GOOD})));
		assertEquals(TNCViabilityFormula.GOOD, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.GOOD})));
		assertEquals(TNCViabilityFormula.GOOD, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.GOOD, TNCViabilityFormula.VERY_GOOD})));
		assertEquals(TNCViabilityFormula.VERY_GOOD, TNCViabilityFormula.getTotalCategoryRatingCode(
				new CodeList(new String[] {TNCViabilityFormula.VERY_GOOD})));
		try
		{
			TNCViabilityFormula.getTotalCategoryRatingCode(new CodeList(new String[] {"8"}));
			fail("Should have thrown for unknown value");
		}
		catch(TNCViabilityFormula.UnexpectedValueException ignoreExpected)
		{
		}
		
	}

}
