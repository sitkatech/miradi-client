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
package org.miradi.questions;

import java.util.HashMap;

import org.miradi.main.EAM;
import org.miradi.utils.CodeList;

public class StaticQuestionManager
{
	public static void initialize()
	{
		questions = new HashMap<String, ChoiceQuestion>();
		addQuestion(new InternalQuestionWithoutValues());
		
		addQuestion(new BudgetTimePeriodQuestion());
		addQuestion(new CountriesQuestion());
		addQuestion(new CurrencyTypeQuestion());
		addQuestion(new DiagramFactorBackgroundQuestion());
		addQuestion(new DiagramFactorFontColorQuestion());
		addQuestion(new DiagramFactorFontSizeQuestion());
		addQuestion(new DiagramFactorFontStyleQuestion());
		addQuestion(new DiagramLegendQuestion());
		addQuestion(new FiscalYearStartQuestion());
		addQuestion(new FontFamiliyQuestion());
		addQuestion(new FontSizeQuestion());
		addQuestion(new KeyEcologicalAttributeTypeQuestion());
		addQuestion(new PriorityRatingQuestion());
		addQuestion(new ProgressReportLongStatusQuestion());
		addQuestion(new ProgressReportShortStatusQuestion());
		addQuestion(new ProgressReportDiagramStatusQuestion());
		addQuestion(new ProtectedAreaCategoryQuestion());
		addQuestion(new RatingSourceQuestion());
		addQuestion(new ResourceRoleQuestion());
		addQuestion(new ResourceTypeQuestion());
		addQuestion(new StatusConfidenceQuestion());
		addQuestion(new StatusQuestion());
		addQuestion(new TargetStatusQuestion());
		addQuestion(new MeasurementStatusQuestion());
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
		addQuestion(new ScopeThreatRatingQuestion());
		addQuestion(new SeverityThreatRatingQuestion());
		addQuestion(new IrreversibilityThreatRatingQuestion());
		addQuestion(new ScopeStressBasedThreatRatingQuestion());
		addQuestion(new SeverityStressBasedThreatRatingQuestion());
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
		addQuestion(new ColorSchemeQuestion());
		addQuestion(new LookAndFeelThemeQuestion());
		addQuestion(new RtfLegendObjectsQuestion());
		addQuestion(new ProjectSharingQuestion());
		addQuestion(new TextBoxZOrderQuestion());
		addQuestion(new CustomPlanningColumnsQuestion());
		addQuestion(new TncProjectPlaceTypeQuestion());
		addQuestion(new TargetModeQuestion());
		addQuestion(new FactorModeQuestion());
		addQuestion(new WorkPlanColumnConfigurationQuestion());
		addQuestion(new WorkPlanRowConfigurationQuestion());
		addQuestion(new MonthAbbreviationsQuestion());
		addQuestion(new TncOrganizationalPrioritiesQuestion());
		addQuestion(new SortDirectionQuestion());
		addQuestion(new QuarterColumnsVisibilityQuestion());
		addQuestion(new DayColumnsVisibilityQuestion());
		addQuestion(new DiagramObjectDataInclusionQuestion());
		addQuestion(new PlanningTreeTargetPositionQuestion());
		addQuestion(new WorkPlanCategoryTypesQuestion());
		addQuestion(new DashboardFlagsQuestion());
		addQuestion(new StrategyObjectiveTreeOrderQuestion());
		addQuestion(new WorkPlanVisibleRowsQuestion());
		addQuestion(new WorkPlanVisibleRowsQuestion());
		addQuestion(new MonitoringTreeConfigurationQuestion());
		addQuestion(new ActionTreeConfigurationQuestion());
		addQuestion(new WorkPlanAnalysisConfigurationQuestion());
		addQuestion(new OpenStandardsProgressStatusQuestion());
		addQuestion(new StrategyStatusQuestion());
		addQuestion(new DiagramModeQuestion());
		addQuestion(new TaxonomyMultiSelectModeQuestion());
		addQuestion(new TaxonomyClassificationSelectionModeQuestion());
		addQuestion(new PrecisionTypeQuestion());
		addQuestion(new ProjectFocusQuestion());
		addQuestion(new ProjectScaleQuestion());
		addQuestion(new DiagramChoiceQuestion());
		addQuestion(new WorkPlanProjectResourceConfigurationQuestion());
		addQuestion(new StrategyEvidenceConfidenceQuestion());
		addQuestion(new DesireEvidenceConfidenceQuestion());
		addQuestion(new ViabilityRatingEvidenceConfidence());
		addQuestion(new MeasurementEvidenceConfidenceQuestion());
		addQuestion(new ThreatRatingEvidenceConfidenceQuestion());
		addQuestion(new AnalyticalQuestionEvidenceConfidenceTypeQuestion());
		addQuestion(new AssumptionEvidenceConfidenceTypeQuestion());
		addQuestion(new ResultReportShortStatusQuestion());
		addQuestion(new ResultReportLongStatusQuestion());
		addQuestion(new ResultReportDiagramStatusQuestion());
	}
	
	private static void addQuestion(ChoiceQuestion question)
	{
		validateSingleSelectionQuestion(question);
		questions.put(question.getClass().getSimpleName(), question);
	}
	
	private static void validateSingleSelectionQuestion(ChoiceQuestion question)
	{
		CodeList allCodes = question.getAllCodes();
		final boolean containsDefaultEmptyChoice = allCodes.contains("");
		if (isSingleSelectQuestion(question) && !containsDefaultEmptyChoice)
			EAM.logError("Single selection question does not contain default \"\" value.  Question class:" + question.getClass().getSimpleName());
		
		if (isMultipleSelectQuestion(question) && containsDefaultEmptyChoice)
			EAM.logError("Multi selection question can not contain default \"\" value.  Question class:" + question.getClass().getSimpleName());
	}

	private static boolean isMultipleSelectQuestion(ChoiceQuestion question)
	{
		return question.canSelectMultiple();
	}
	
	private static boolean isSingleSelectQuestion(ChoiceQuestion question)
	{
		return question.canSelectSingleOnly();
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
