/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StrategyCostQuestion;
import org.conservationmeasures.eam.questions.StrategyDurationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;

public class TestStrategyRatingSummary extends EAMTestCase
{
	public TestStrategyRatingSummary(String name)
	{
		super(name);
	}

	public void testUnrated()
	{
		verifyGetResult("", "", "", "", "", "All unrated");
		verifyGetResult("", "", "1", "1", "1", "Impact unrated");
		verifyGetResult("", "1", "", "1", "1", "Duration unrated");
		verifyGetResult("", "1", "", "1", "1", "Feasibility unrated");
		verifyGetResult("", "1", "1", "1", "", "Cost unrated");
		verifyGetResult("1", "1", "1", "1", "1", "All 1");
		verifyGetResult("2", "2", "2", "2", "2", "All 2");
		verifyGetResult("3", "3", "3", "3", "3", "All 3");
		verifyGetResult("4", "4", "4", "4", "4", "All 4");
		verifyGetResult("1", "1", "1", "1", "4", "1s and 4");
		verifyGetResult("1", "4", "4", "4", "1", "4s and 1");
	}

	private void verifyGetResult(String resultCode, String impactCode, String durationCode, String feasibilityCode, String costCode, String message)
	{
		StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion("impact");
		StrategyDurationQuestion durationQuestion = new StrategyDurationQuestion("impact");
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion("feasibility");
		StrategyCostQuestion costQuestion = new StrategyCostQuestion("feasibility");
		StrategyRatingSummaryQuestion summary = new StrategyRatingSummaryQuestion("summary");
		ChoiceItem impact = impactQuestion.findChoiceByCode(impactCode);
		ChoiceItem duration = durationQuestion.findChoiceByCode(durationCode);
		ChoiceItem feasibility = feasibilityQuestion.findChoiceByCode(feasibilityCode);
		ChoiceItem cost = costQuestion.findChoiceByCode(costCode);
		ChoiceItem result = summary.getResult(impact, duration, feasibility, cost);
		assertEquals(message, resultCode, result.getCode());
	}
}
