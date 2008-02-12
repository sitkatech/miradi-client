/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.ratings;

import org.miradi.main.EAMTestCase;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;

public class TestStrategyRatingSummary extends EAMTestCase
{
	public TestStrategyRatingSummary(String name)
	{
		super(name);
	}

	public void testUnrated()
	{
		verifyGetResult("", "", "");
		verifyGetResult("", "", "1");
		verifyGetResult("", "1", "");
		verifyGetResult("1", "1", "1");
		verifyGetResult("1", "1", "2");
		verifyGetResult("1", "2", "1");
	}

	private void verifyGetResult(String resultCode, String impactCode, String feasibilityCode)
	{
		StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion();
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion();
		ChoiceItem impact = impactQuestion.findChoiceByCode(impactCode);
		ChoiceItem feasibility = feasibilityQuestion.findChoiceByCode(feasibilityCode);
		
		StrategyRatingSummaryQuestion summary = new StrategyRatingSummaryQuestion();
		ChoiceItem result = summary.getResult(impact, feasibility);
		assertEquals("wrong result?", resultCode, result.getCode());
	}
}
