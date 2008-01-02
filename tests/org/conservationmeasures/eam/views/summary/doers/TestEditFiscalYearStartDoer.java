/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary.doers;

import org.conservationmeasures.eam.views.summary.doers.EditFiscalYearStartDoer;
import org.martus.util.TestCaseEnhanced;

public class TestEditFiscalYearStartDoer extends TestCaseEnhanced
{
	public TestEditFiscalYearStartDoer(String name)
	{
		super(name);
	}

	public void testGetSkewedMonthFromCode() throws Exception
	{
		verifySkewedMonth(1, "");
		verifySkewedMonth(4, "4");
		verifySkewedMonth(-5, "7");
		verifySkewedMonth(-2, "10");
	}
	
	public void testGetMonthDelta() throws Exception
	{
		verifyMonthDelta(0, 1, 1);
		verifyMonthDelta(3, 1, 4);
		verifyMonthDelta(-6, 1, -5);
		verifyMonthDelta(-3, 1, -2);

		verifyMonthDelta(-3, 4, 1);
		verifyMonthDelta(0, 4, 4);
		verifyMonthDelta(-9, 4, -5);
		verifyMonthDelta(-6, 4, -2);

		verifyMonthDelta(6, -5, 1);
		verifyMonthDelta(9, -5, 4);
		verifyMonthDelta(0, -5, -5);
		verifyMonthDelta(3, -5, -2);

		verifyMonthDelta(3, -2, 1);
		verifyMonthDelta(6, -2, 4);
		verifyMonthDelta(-3, -2, -5);
		verifyMonthDelta(0, -2, -2);

	}

	private void verifyMonthDelta(int expectedDelta, int oldMonth, int newMonth)
	{
		assertEquals(expectedDelta, EditFiscalYearStartDoer.getMonthDelta(oldMonth, newMonth));
	}

	private void verifySkewedMonth(int expectedMonth, String fiscalYearStartCode)
	{
		assertEquals(expectedMonth, EditFiscalYearStartDoer.getSkewedMonthFromCode(fiscalYearStartCode));
	}
}
