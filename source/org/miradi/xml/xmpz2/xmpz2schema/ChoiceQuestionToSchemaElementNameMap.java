/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.questions.*;
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
		addItem(HabitatAssociationQuestion.class, VOCABULARY_BIODIVERSITY_TARGET_HABITAT_ASSOCIATION);
		addItem(TargetStatusQuestion.class, VOCABULARY_TARGET_STATUS);
		addItem(ViabilityModeQuestion.class, VOCABULARY_TARGET_VIABILITY_MODE);
		addItem(ThreatClassificationQuestionV11.class, VOCABULARY_CAUSE_STANDARD_CLASSIFICATION_V11_CODE);
		addItem(StressSeverityChoiceQuestion.class, VOCABULARY_SEVERITY_RATING);
		addItem(StressScopeChoiceQuestion.class, VOCABULARY_SCOPE_RATING);
		addItem(StrategyClassificationQuestionV11.class, VOCABULARY_STRATEGY_STANDARD_CLASSIFICATION_V11_CODE);
		addItem(StrategyClassificationQuestionV20.class, VOCABULARY_STRATEGY_STANDARD_CLASSIFICATION_V20_CODE);
		addItem(StrategyImpactQuestion.class, VOCABULARY_STRATEGY_IMPACT_RATING_CODE);
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
		addItem(DesireEvidenceConfidenceQuestion.class, VOCABULARY_EVIDENCE_CONFIDENCE_EXTERNAL);
		addItem(StrategyEvidenceConfidenceQuestion.class, VOCABULARY_EVIDENCE_CONFIDENCE_EXTERNAL);
		addItem(ViabilityRatingEvidenceConfidence.class, VOCABULARY_EVIDENCE_CONFIDENCE_EXTERNAL);
		addItem(MeasurementEvidenceConfidenceQuestion.class, VOCABULARY_EVIDENCE_CONFIDENCE_PROJECT);
		addItem(ThreatRatingEvidenceConfidenceQuestion.class, VOCABULARY_EVIDENCE_CONFIDENCE_PROJECT);
		addItem(AnalyticalQuestionEvidenceConfidenceTypeQuestion.class, VOCABULARY_EVIDENCE_CONFIDENCE_ANALYTICAL_QUESTION);
		addItem(AssumptionEvidenceConfidenceTypeQuestion.class, VOCABULARY_EVIDENCE_CONFIDENCE_ANALYTICAL_QUESTION);
		addItem(CountriesQuestion.class, VOCABULARY_COUNTRIES);
		addItem(ThreatRatingQuestion.class, VOCABULARY_THREAT_RATING);
		addItem(DiagramObjectDataInclusionQuestion.class, VOCABULARY_DIAGRAM_OBJECT_DATA_INCLUSION);
		addItem(StrategyObjectiveTreeOrderQuestion.class, VOCABULARY_PLANNING_TREE_OBJECTIVE_STRATEGY_NODE_ORDER);
		addItem(PlanningTreeTargetPositionQuestion.class, VOCABULARY_PLANNING_TREE_TARGET_NODE_POSITION);
		addItem(ThreatRatingModeChoiceQuestion.class, VOCABULARY_THREAT_RATING_MODE);
		addItem(DashboardFlagsQuestion.class, VOCABULARY_DASHBOARD_ROW_FLAGS);
		addItem(QuarterColumnsVisibilityQuestion.class, VOCABULARY_QUARTER_COLUMNS_VISIBILITY);
		addItem(DayColumnsVisibilityQuestion.class, VOCABULARY_DAY_COLUMNS_VISIBILITY);
		addItem(BudgetTimePeriodQuestion.class, VOCABULARY_WORK_PLAN_TIME_UNIT);
		addItem(CurrencyTypeQuestion.class, VOCABULARY_CURRENCY_TYPE);
		addItem(StrategyStatusQuestion.class, VOCABULARY_STRATEGY_STATUS);
		addItem(StatusQuestion.class, VOCABULARY_STATUS);
		addItem(OpenStandardsProgressStatusQuestion.class, VOCABULARY_DASHBOARD_ROW_PROGRESS);
		addItem(TargetModeQuestion.class, VOCABULARY_TARGET_MODE);
		addItem(FactorModeQuestion.class, VOCABULARY_FACTOR_MODE);
		addItem(TaxonomyMultiSelectModeQuestion.class, VOCABULARY_TAXONOMY_CLASSIFICATION_MULTISELECT_MODE);
		addItem(TaxonomyClassificationSelectionModeQuestion.class, VOCABULARY_TAXONOMY_CLASSIFICATION_SELECTION_MODE);
		addItem(PrecisionTypeQuestion.class, VOCABULARY_PRECISION_TYPE);
		addItem(InternalQuestionWithoutValues.class, URI_RESTRICTED_TEXT);
		addItem(new CustomPlanningAllRowsQuestion(), VOCABULARY_CUSTOM_ROWS);
		addItem(new CustomPlanningColumnsQuestion(), VOCABULARY_CUSTOM_COLUMNS);
		addItem(ProjectFocusQuestion.class, VOCABULARY_PROJECT_FOCUS);
		addItem(ProjectScaleQuestion.class, VOCABULARY_PROJECT_SCALE);
		addItem(ResultReportLongStatusQuestion.class, VOCABULARY_RESULT_REPORT_STATUS);
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
