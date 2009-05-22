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
package org.miradi.questions;

import java.util.HashMap;

public class StaticQuestionManager
{
	public static void initialize()
	{
		questions = new HashMap();
		addQuestion(new InternalQuestionWithoutValues());
		
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
		addQuestion(new LanguageQuestion());
		addQuestion(new PriorityRatingQuestion());
		addQuestion(new ProgressReportStatusQuestion());
		addQuestion(new ProtectedAreaCategoryQuestion());
		addQuestion(new RatingSourceQuestion());
		addQuestion(new ResourceRoleQuestion());
		addQuestion(new ResourceTypeQuestion());
		addQuestion(new StatusConfidenceQuestion());
		addQuestion(new StatusQuestion());
		addQuestion(new StrategyClassificationQuestion());
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
		addQuestion(new HabitatAssociationQuestion());
		addQuestion(new FosTrainingTypeQuestion());
		addQuestion(new DiagramLinkColorQuestion());
		addQuestion(new TableRowHeightModeQuestion());
		addQuestion(new ReportTemplateContentQuestion());
		addQuestion(new RtfLegendObjectsQuestion());
		addQuestion(new TncProjectSharingQuestion());
		addQuestion(new TextBoxZOrderQuestion());
		addQuestion(new RowConfigurationQuestion());
		addQuestion(new ColumnConfigurationQuestion());
		addQuestion(new TncOrganizationalPrioritiesQuestion());
		addQuestion(new TncProjectPlaceTypeQuestion());
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
			throw new RuntimeException("Unknown question: " + questionName);
		return question;
	}
	
	private static HashMap<String, ChoiceQuestion> questions;

}
