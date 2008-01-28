/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.io.IOException;
import java.util.HashMap;

import org.martus.util.UnicodeWriter;

public class QuestionManager
{
	public QuestionManager()
	{
		questions = new HashMap();
		addQuestion(new BudgetCostModeQuestion(""));
		addQuestion(new BudgetCostUnitQuestion(""));
		addQuestion(new BudgetTimePeriodQuestion(""));
		addQuestion(new CountriesQuestion(""));
		addQuestion(new CurrencyDecimalQuestion(""));
		addQuestion(new CurrencyTypeQuestion(""));
		addQuestion(new DiagramFactorBackgroundQuestion(""));
		addQuestion(new DiagramFactorFontColorQuestion(""));
		addQuestion(new DiagramFactorFontSizeQuestion(""));
		addQuestion(new DiagramLegendQuestion(""));
		addQuestion(new FiscalYearStartQuestion(""));
		addQuestion(new FontFamiliyQuestion(""));
		addQuestion(new FontSizeQuestion(""));
		addQuestion(new IndicatorStatusRatingQuestion(""));
		addQuestion(new KeyEcologicalAttributeTypeQuestion(""));
		addQuestion(new PriorityRatingQuestion(""));
		addQuestion(new ProgressReportStatusQuestion(""));
		addQuestion(new RatingSourceQuestion(""));
		addQuestion(new ResourceRoleQuestion(""));
		addQuestion(new ResourceTypeQuestion(""));
		addQuestion(new StatusConfidenceQuestion(""));
		addQuestion(new StatusQuestion(""));
		addQuestion(new StrategyClassificationQuestion(""));
		addQuestion(new StrategyCostQuestion(""));
		addQuestion(new StrategyDurationQuestion(""));
		addQuestion(new StrategyFeasibilityQuestion(""));
		addQuestion(new StrategyImpactQuestion(""));
		addQuestion(new StrategyRatingSummaryQuestion(""));
		addQuestion(new StrategyTaxonomyQuestion(""));
		addQuestion(new StressContributionQuestion(""));
		addQuestion(new StressIrreversibilityQuestion(""));
		addQuestion(new StressRatingChoiceQuestion(""));
		addQuestion(new StressScopeChoiceQuestion(""));
		addQuestion(new StressSeverityChoiceQuestion(""));
		addQuestion(new TeamRoleQuestion(""));
		addQuestion(new ThreatClassificationQuestion(""));
		addQuestion(new ThreatRatingModeChoiceQuestion(""));
		addQuestion(new ThreatRatingQuestion(""));
		addQuestion(new ThreatStressRatingChoiceQuestion(""));
		addQuestion(new TrendQuestion(""));
		addQuestion(new ViabilityModeQuestion(""));
	}
	
	private void addQuestion(ChoiceQuestion question)
	{
		questions.put(question.getClass().getSimpleName(), question);
	}
	
	public ChoiceQuestion getQuestion(Class questionClass)
	{
		return getQuestion(questionClass.getSimpleName());
	}
	
	public ChoiceQuestion getQuestion(String questionName)
	{
		return questions.get(questionName);
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.writeln("<ChoiceQuestions>");
		for(String questionName : questions.keySet())
		{
			ChoiceQuestion question = getQuestion(questionName);
			question.toXml(out);
		}
		out.writeln("</ChoiceQuestions>");
	}

	HashMap<String, ChoiceQuestion> questions;

}
