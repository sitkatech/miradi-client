/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.xmpz2schema;

import java.util.HashMap;

import org.miradi.questions.BudgetTimePeriodQuestion;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.CountriesQuestion;
import org.miradi.questions.CurrencyTypeQuestion;
import org.miradi.questions.CustomPlanningAllRowsQuestion;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.DashboardFlagsQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.questions.DiagramObjectDataInclusionQuestion;
import org.miradi.questions.FiscalYearStartQuestion;
import org.miradi.questions.FosTrainingTypeQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.MajorLanguagesQuestion;
import org.miradi.questions.MeasurementStatusQuestion;
import org.miradi.questions.OpenStandardsProgressStatusQuestion;
import org.miradi.questions.PlanningTreeTargetPositionQuestion;
import org.miradi.questions.PrecisionTypeQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportLongStatusQuestion;
import org.miradi.questions.ProjectSharingQuestion;
import org.miradi.questions.ProtectedAreaCategoryQuestion;
import org.miradi.questions.QuarterColumnsVisibilityQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.questions.ScopeBoxTypeQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyObjectiveTreeOrderQuestion;
import org.miradi.questions.StrategyStatusQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.questions.StressScopeChoiceQuestion;
import org.miradi.questions.StressSeverityChoiceQuestion;
import org.miradi.questions.TargetModeQuestion;
import org.miradi.questions.TargetStatusQuestion;
import org.miradi.questions.TaxonomyClassificationSelectionModeQuestion;
import org.miradi.questions.TaxonomyMultiSelectModeQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.questions.WwfEcoRegionsQuestion;
import org.miradi.questions.WwfManagingOfficesQuestion;
import org.miradi.questions.WwfRegionsQuestion;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

public class ChoiceQuestionToSchemaElementNameMap extends HashMap<ChoiceQuestion, String> implements Xmpz2XmlConstants
{
	public ChoiceQuestionToSchemaElementNameMap()
	{
		fillMap();
	}
	
	private void fillMap()
	{
		addItem(ScopeBoxTypeQuestion.createScopeBoxTypeQuestion(), VOCABULARY_SCOPE_BOX_TYPE);
		addItem(new MajorLanguagesQuestion(), VOCABULARY_LANGUAGE_CODE);
		addItem(FiscalYearStartQuestion.class, VOCABULARY_FISCAL_YEAR_START);
		addItem(ProtectedAreaCategoryQuestion.class, VOCABULARY_PROTECTED_AREA_CATEGORIES);
		addItem(ResourceTypeQuestion.class, VOCABULARY_RESOURCE_TYPE);
		addItem(ResourceRoleQuestion.class, VOCABULARY_RESOURCE_ROLE_CODES);
		addItem(DiagramLegendQuestion.class, VOCABULARY_HIDDEN_TYPES);
		addItem(DiagramFactorFontSizeQuestion.class, VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE);
		addItem(DiagramFactorFontStyleQuestion.class, VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE);
		addItem(DiagramFactorBackgroundQuestion.class, VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR);
		addItem(DiagramFactorFontColorQuestion.class, VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR);
		addItem(DiagramLinkColorQuestion.class, VOCABULARY_DIAGRAM_LINK_COLOR);
		addItem(HabitatAssociationQuestion.class, VOCABULARY_BIODIVERSITY_TARGET_HABITAT_ASSICIATION);
		addItem(TargetStatusQuestion.class, VOCABULARY_TARGET_STATUS);
		addItem(ViabilityModeQuestion.class, VOCABULARY_TARGET_VIABILITY_MODE);
		addItem(ThreatClassificationQuestion.class, VOCABULARY_THREAT_TAXONOMY_CODE);
		addItem(StressSeverityChoiceQuestion.class, VOCABULARY_SEVERITY_RATING);
		addItem(StressScopeChoiceQuestion.class, VOCABULARY_SCOPE_RATING);
		addItem(StrategyTaxonomyQuestion.class, VOCABULARY_STRATEGY_TAXONOMY_CODE);
		addItem(StrategyImpactQuestion.class, VOCABULARY_STRATEGY_IMAPACT_RATING_CODE);
		addItem(StrategyFeasibilityQuestion.class, VOCABULARY_STRATEGY_FEASIBILITY_RATING_CODE);
		addItem(PriorityRatingQuestion.class, VOCABULARY_PRIORITY_RATING_CODE);
		addItem(KeyEcologicalAttributeTypeQuestion.class, VOCABULARY_KEA_TYPE);
		addItem(StressContributionQuestion.class, VOCABULARY_THREAT_STRESS_RATING_CONTRIBUTION_CODE);
		addItem(StressIrreversibilityQuestion.class, VOCABULARY_IRREVERSIBILITY_CODE);
		addItem(TncOperatingUnitsQuestion.class, VOCABULARY_TNC_OPERATING_UNTIS);
		addItem(TncTerrestrialEcoRegionQuestion.class, VOCABULARY_TNC_TERRESTRIAL_ECO_REGION);
		addItem(TncMarineEcoRegionQuestion.class, VOCABULARY_TNC_MARINE_ECO_REGION);
		addItem(TncFreshwaterEcoRegionQuestion.class, VOCABULARY_TNC_FRESHWATER_ECO_REGION);
		addItem(ProjectSharingQuestion.class, VOCABULARY_SHARE_OUTSIDE_ORGANIZATION);
		addItem(WwfManagingOfficesQuestion.class, VOCABULARY_WWF_MANAGING_OFFICES);
		addItem(WwfRegionsQuestion.class, VOCABULARY_WWF_REGIONS);
		addItem(WwfEcoRegionsQuestion.class, VOCABULARY_WWF_ECOREGIONS);
		addItem(FosTrainingTypeQuestion.class, VOCABULARY_FOS_TRAINING_TYPE);
		addItem(ProgressReportLongStatusQuestion.class, VOCABULARY_PROGRESS_REPORT_STATUS);
		addItem(TrendQuestion.class, VOCABULARY_MEASUREMENT_TREND);
		addItem(MeasurementStatusQuestion.class, VOCABULARY_MEASUREMENT_STATUS);
		addItem(StatusConfidenceQuestion.class, VOCABULARY_MEASUREMENT_STATUS_CONFIDENCE);
		addItem(CountriesQuestion.class, VOCABULARY_COUNTRIES);
		addItem(ThreatRatingQuestion.class, VOCABULARY_THREAT_RATING);
		addItem(DiagramObjectDataInclusionQuestion.class, VOCABULARY_DIAGRAM_OBJECT_DATA_INCLUSION);
		addItem(StrategyObjectiveTreeOrderQuestion.class, VOCABULARY_PLANNING_TREE_OBJECTIVE_STRATEGY_NODE_ORDER);
		addItem(PlanningTreeTargetPositionQuestion.class, VOCABULARY_PLANNING_TREE_TARGET_NODE_POSITION);
		addItem(ThreatRatingModeChoiceQuestion.class, VOCABULARY_THREAT_RATING_MODE);
		addItem(DashboardFlagsQuestion.class, VOCABULARY_DASHBOARD_ROW_FLAGS);
		addItem(QuarterColumnsVisibilityQuestion.class, VOCABULARY_QUARTER_COLUMNS_VISIBILITY);
		addItem(BudgetTimePeriodQuestion.class, VOCABULARY_WORK_PLAN_TIME_UNIT);
		addItem(RatingSourceQuestion.class, VOCABULARY_RATING_SOURCE);
		addItem(CurrencyTypeQuestion.class, VOCABULARY_CURRENCY_TYPE);
		addItem(TextBoxZOrderQuestion.class, VOCABULARY_TEXT_BOX_Z_ORDER);
		addItem(StrategyStatusQuestion.class, VOCABULARY_STRATEGY_STATUS);
		addItem(StatusQuestion.class, VOCABULARY_STATUS);
		addItem(OpenStandardsProgressStatusQuestion.class, VOCABULARY_DASHBOARD_ROW_PROGRESS);
		addItem(TargetModeQuestion.class, VOCABULARY_TARGET_MODE);
		addItem(TaxonomyMultiSelectModeQuestion.class, VOCABULARY_TAXONOMY_CLASSIFICATION_MULTISELECT_MODE);
		addItem(TaxonomyClassificationSelectionModeQuestion.class, VOCABULARY_TAXONOMY_CLASSIFICATION_SELECTION_MODE);
		addItem(PrecisionTypeQuestion.class, VOCABULARY_PRECISION_TYPE);
		addItem(InternalQuestionWithoutValues.class, URI_RESTRICTED_TEXT);
		addItem(new CustomPlanningAllRowsQuestion(), VOCABULARY_CUSTOM_ROWS);
		addItem(new CustomPlanningColumnsQuestion(), VOCABULARY_CUSTOM_COLUMNS);
	}
	
	private void addItem(Class questionClass, String value)
	{
		final ChoiceQuestion question = StaticQuestionManager.getQuestion(questionClass);
		addItem(question, value);
	}

	private void addItem(final ChoiceQuestion question, String value)
	{
		put(question, value);
	}
	
	public String findVocabulary(ChoiceQuestion questionToFind)
	{
		for(ChoiceQuestion question : keySet())
		{
			if (questionToFind.getClass().equals(question.getClass()))
				return get(question); 
		}
		
		throw new RuntimeException("Could not find a vocabulary for question: " + questionToFind.getClass().getSimpleName());
	}
}
