/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.HashMap;

import org.conservationmeasures.eam.main.EAM;

public class QuestionManager
{
	private static void initialize()
	{
		questions = new HashMap();
		addQuestion(new InternalQuestionWithoutValues());
		
		addQuestion(new BudgetCostModeQuestion());
		addQuestion(new BudgetCostUnitQuestion());
		addQuestion(new BudgetTimePeriodQuestion());
		addQuestion(new CountriesQuestion());
		addQuestion(new CurrencyDecimalQuestion());
		addQuestion(new CurrencyTypeQuestion());
		addQuestion(new DiagramFactorBackgroundQuestion());
		addQuestion(new DiagramFactorFontColorQuestion());
		addQuestion(new DiagramFactorFontSizeQuestion());
		addQuestion(new DiagramFactorFontStyleQuestion());
		addQuestion(new DiagramLegendQuestion());
		addQuestion(new FiscalYearStartQuestion());
		addQuestion(new FontFamiliyQuestion());
		addQuestion(new FontSizeQuestion());
		addQuestion(new IndicatorStatusRatingQuestion());
		addQuestion(new KeyEcologicalAttributeTypeQuestion());
		addQuestion(new PriorityRatingQuestion());
		addQuestion(new ProgressReportStatusQuestion());
		addQuestion(new ProtectedAreaCategoryQuestion());
		addQuestion(new RatingSourceQuestion());
		addQuestion(new ResourceRoleQuestion());
		addQuestion(new ResourceTypeQuestion());
		addQuestion(new StatusConfidenceQuestion());
		addQuestion(new StatusQuestion());
		addQuestion(new StrategyClassificationQuestion());
		addQuestion(new StrategyCostQuestion());
		addQuestion(new StrategyDurationQuestion());
		addQuestion(new StrategyFeasibilityQuestion());
		addQuestion(new StrategyImpactQuestion());
		addQuestion(new StrategyRatingSummaryQuestion());
		addQuestion(new StrategyTaxonomyQuestion());
		addQuestion(new StressContributionQuestion());
		addQuestion(new StressIrreversibilityQuestion());
		addQuestion(new StressRatingChoiceQuestion());
		addQuestion(new StressScopeChoiceQuestion());
		addQuestion(new StressSeverityChoiceQuestion());
		addQuestion(new ThreatClassificationQuestion());
		addQuestion(new ThreatRatingModeChoiceQuestion());
		addQuestion(new ThreatRatingQuestion());
		addQuestion(new ThreatStressRatingChoiceQuestion());
		addQuestion(new TncFreshwaterEcoRegionQuestion());
		addQuestion(new TncMarineEcoRegionQuestion());
		addQuestion(new TncOperatingUnitsQuestion());
		addQuestion(new TncTerrestrialEcoRegionQuestion());
		addQuestion(new TrendQuestion());
		addQuestion(new ViabilityModeQuestion());
		addQuestion(new WwfEcoRegionsQuestion());
		addQuestion(new WwfManagingOfficesQuestion());
		addQuestion(new WwfRegionsQuestion());
	}
	
	private static void addQuestion(ChoiceQuestion question)
	{
		questions.put(question.getClass().getSimpleName(), question);
	}
	
	public static ChoiceQuestion getQuestion(Class questionClass)
	{
		return getQuestion(questionClass.getSimpleName());
	}
	
	public static ChoiceQuestion getQuestion(String questionName)
	{
		if(questions == null)
			initialize();
		
		ChoiceQuestion question = questions.get(questionName);
		if(question == null)
			EAM.logWarning("Unknown question: " + questionName);
		return question;
	}
	
	private static HashMap<String, ChoiceQuestion> questions;

}
