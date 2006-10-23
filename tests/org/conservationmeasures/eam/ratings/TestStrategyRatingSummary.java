/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestStrategyRatingSummary extends EAMTestCase
{
	public TestStrategyRatingSummary(String name)
	{
		super(name);
	}

	public void testUnrated()
	{
		verifyGetResult("", "", "", "Both unrated");
		verifyGetResult("", "1", "", "Impact unrated");
		verifyGetResult("", "", "1", "Feasibility unrated");
		verifyGetResult("1", "1", "1", "Both 1");
		verifyGetResult("2", "2", "2", "Both 2");
		verifyGetResult("3", "3", "3", "Both 3");
		verifyGetResult("4", "4", "4", "Both 4");
		verifyGetResult("2", "1", "4", "1 and 4");
		verifyGetResult("2", "4", "1", "1 and 4");
	}

	private void verifyGetResult(String resultCode, String impactCode, String feasibilityCode, String message)
	{
		StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion("impact");
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion("feasibility");
		StrategyRatingSummary summary = new StrategyRatingSummary("summary");
		RatingChoice impactUnrated = impactQuestion.findChoiceByCode(impactCode);
		RatingChoice feasibilityUnrated = feasibilityQuestion.findChoiceByCode(feasibilityCode);
		RatingChoice result = summary.getResult(impactUnrated, feasibilityUnrated);
		assertEquals(message, resultCode, result.getCode());
	}
}
