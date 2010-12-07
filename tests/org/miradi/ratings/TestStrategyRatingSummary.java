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
