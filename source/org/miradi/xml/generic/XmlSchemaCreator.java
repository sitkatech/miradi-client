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

package org.miradi.xml.generic;

import org.miradi.main.Miradi;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.questions.DiagramLinkColorQuestion;
import org.miradi.questions.FiscalYearStartQuestion;
import org.miradi.questions.FosTrainingTypeQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProtectedAreaCategoryQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.questions.ScopeBoxColorQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.questions.StressScopeChoiceQuestion;
import org.miradi.questions.StressSeverityChoiceQuestion;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectPlaceTypeQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.questions.WwfEcoRegionsQuestion;
import org.miradi.questions.WwfManagingOfficesQuestion;
import org.miradi.questions.WwfRegionsQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.Translation;

public class XmlSchemaCreator
{
	public static void main(String[] args) throws Exception
	{
		new XmlSchemaCreator().printXmlRncSchema(new SchemaWriter(System.out));
	}

	public XmlSchemaCreator() throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();
	}

	public void printXmlRncSchema(SchemaWriter writer) throws Exception
	{
		ProjectSchemaElement rootElement = new ProjectSchemaElement();
		writer.println("namespace miradi = 'http://xml.miradi.org/schema/ConservationProject/1'");
		writer.defineAlias("start", rootElement.getProjectElementName() + ".element");
		rootElement.output(writer);
		
		writer.println("vocabulary_work_units_full_project_timespan = xsd:NMTOKEN { pattern = 'Total' } ");
		writer.println("vocabulary_year = xsd:NMTOKEN { pattern = '[0-9]{4}' } ");
		writer.println("vocabulary_month = xsd:NMTOKEN { pattern = '[0-9]{2}' } ");
		writer.println("vocabulary_date = xsd:NMTOKEN { pattern = '[0-9]{4}-[0-9]{2}-[0-9]{2}' }");
		writer.printlnIndented("vocabulary_iso_country_code = xsd:NMTOKEN { pattern = '[A-Z]{3}' }");
		defineVocabulary(writer, VOCABULARY_FISCAL_YEAR_START, new FiscalYearStartQuestion());
		defineVocabulary(writer, VOCABULARY_PROTECTED_AREA_CATEGORIES, new ProtectedAreaCategoryQuestion());
		defineVocabulary(writer, VOCABULARY_RESOURCE_TYPE, new ResourceTypeQuestion());
		defineVocabulary(writer, VOCABULARY_RESOURCE_ROLE_CODES, new ResourceRoleQuestion());
		defineVocabulary(writer, VOCABULARY_HIDDEN_TYPES, new DiagramLegendQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE, new DiagramFactorFontSizeQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE, new DiagramFactorFontStyleQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion());
		defineVocabulary(writer, VOCABULARY_DIAGRAM_LINK_COLOR, new DiagramLinkColorQuestion());
		defineVocabulary(writer, VOCABULARY_BIODIVERSITY_TARGET_HABITAT_ASSICIATION, new HabitatAssociationQuestion());
		defineVocabulary(writer, VOCABULARY_TARGET_STATUS, new StatusQuestion());
		defineVocabulary(writer, VOCABULARY_TARGET_VIABILITY_MODE, new ViabilityModeQuestion());
		defineVocabulary(writer, VOCABULARY_THREAT_TAXONOMY_CODE, new ThreatClassificationQuestion());
		defineVocabulary(writer, VOCABULARY_SCOPE_BOX_COLOR, new ScopeBoxColorQuestion());
		defineVocabulary(writer, VOCABULARY_STRESS_SEVERITY, new StressSeverityChoiceQuestion());
		defineVocabulary(writer, VOCABULARY_STRESS_SCOPE, new StressScopeChoiceQuestion());
		defineVocabulary(writer, VOCABULARY_STRATEGY_TAXONOMY_CODE, new StrategyTaxonomyQuestion());
		defineVocabulary(writer, VOCABULARY_STRATEGY_IMAPACT_RATING_CODE, new StrategyImpactQuestion());
		defineVocabulary(writer, VOCABULARY_STRATEGY_FEASIBILITY_RATING_CODE, new StrategyFeasibilityQuestion());
		defineVocabulary(writer, VOCABULARY_PRIORITY_RATING_CODE, new PriorityRatingQuestion());
		defineVocabulary(writer, VOCABULARY_STATUS_CODE, new StatusQuestion());
		defineVocabulary(writer, VOCABULARY_KEA_TYPE, new KeyEcologicalAttributeTypeQuestion());
		defineVocabulary(writer, VOCABULARY_THREAT_STRESS_RATING_CONTRIBUTION_CODE, new StressContributionQuestion());
		defineVocabulary(writer, VOCABULARY_THREAT_STRESS_RATING_IRREVERSIBILITY_CODE, new StressIrreversibilityQuestion());
		defineVocabulary(writer, VOCABULARY_SIMPLE_THREAT_RATING_SCOPE_CODE, new ThreatRatingQuestion());
		defineVocabulary(writer, VOCABULARY_SIMPLE_THREAT_RATING_SEVERITY_CODE, new ThreatRatingQuestion());
		defineVocabulary(writer, VOCABULARY_SIMPLE_THREAT_RATING_IRREVERSIBILITY_CODE, new ThreatRatingQuestion());
		defineVocabulary(writer, VOCABULARY_TNC_PROJECT_PLACE_TYPES, new TncProjectPlaceTypeQuestion());
		defineVocabulary(writer, VOCABULARY_TNC_ORGANIZATIONAL_PRIORITIES, new TncOrganizationalPrioritiesQuestion());
		defineVocabulary(writer, VOCABULARY_TNC_OPERATING_UNTIS, new TncOperatingUnitsQuestion());
		defineVocabulary(writer, VOCABULARY_TNC_TERRESTRIAL_ECO_REGION, new TncTerrestrialEcoRegionQuestion());
		defineVocabulary(writer, VOCABULARY_TNC_MARINE_ECO_REGION, new TncMarineEcoRegionQuestion());
		defineVocabulary(writer, VOCABULARY_TNC_FRESHWATER_ECO_REGION, new TncFreshwaterEcoRegionQuestion());
		defineVocabulary(writer, VOCABULARY_WWF_MANAGING_OFFICES, new WwfManagingOfficesQuestion());
		defineVocabulary(writer, VOCABULARY_WWF_REGIONS, new WwfRegionsQuestion());
		defineVocabulary(writer, VOCABULARY_WWF_ECOREGIONS, new WwfEcoRegionsQuestion());
		defineVocabulary(writer, VOCABULARY_FOS_TRAINING_TYPE, new FosTrainingTypeQuestion());
		
		defineIdElement(writer, "ConceptualModel");
		defineIdElement(writer, "ResultsChain");

		defineIdElement(writer, "DiagramFactor");
		defineIdElement(writer, BIODIVERSITY_TARGET_ID_ELEMENT_NAME);
		defineIdElement(writer, "HumanWelfareTarget");
		defineIdElement(writer, CAUSE_ID_ELEMENT_NAME);
		defineIdElement(writer, "Strategy");
		defineIdElement(writer, "ThreatReductionResult");
		defineIdElement(writer, "IntermediateResult");
		defineIdElement(writer, "GroupBox");
		defineIdElement(writer, "TextBox");
		defineIdElement(writer, "ScopeBox");
		
		defineIdElement(writer, ACITIVTY_ID_ELEMENT_NAME);
		defineIdElement(writer, STRESS_ID_ELEMENT_NAME);
		
		defineIdElement(writer, "DiagramLink");
		defineIdElement(writer, "FactorLink");
		 
		defineIdElement(writer, GOAL_ELEMENT_NAME);
		defineIdElement(writer, OBJECTIVE_ID_ELEMENT_NAME);
		defineIdElement(writer, INDICATOR_ID_ELEMENT_NAME);
		defineIdElement(writer, KEA_ID_ELEMENT_NAME);
		defineIdElement(writer, TAGGED_OBJECT_SET_ELEMENT_NAME);
		defineIdElement(writer, SUB_TARGET_ID_ELEMENT_NAME);
		defineIdElement(writer, THREAT_ID_ELEMENT_NAME);
		defineIdElement(writer, ACCOUNTING_CODE_ID_ELEMENT_NAME);
		defineIdElement(writer, FUNDING_SOURCE_ID_ELEMENT_NAME);
		defineIdElement(writer, PROGRESS_REPORT_ID_ELEMENT_NAME);
		defineIdElement(writer, PROGRESS_PERCENT_ID_ELEMENT_NAME);
		defineIdElement(writer, EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME);
		defineIdElement(writer, RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME);
		defineIdElement(writer, RESOURCE_ID_ELEMENT_NAME);
		
		writer.defineAlias("WrappedByDiagramLinkId.element", "element WrappedByDiagramLinkId");
		writer.startBlock();
		writer.printlnIndented("FactorLinkId.element");
		writer.endBlock();
		
		writer.defineAlias("WrappedByDiagramFactorId.element", "element WrappedByDiagramFactorId");
		writer.startBlock();
		writer.printlnIndented("BiodiversityTargetId.element |");
		writer.printlnIndented("HumanWelfareTargetId.element |");
		writer.printlnIndented("CauseId.element |");
		writer.printlnIndented("StrategyId.element |");
		writer.printlnIndented("ThreatReductionResultId.element |");
		writer.printlnIndented("IntermediateResultId.element |");
		writer.printlnIndented("GroupBoxId.element |");
		writer.printlnIndented("TextBoxId.element |");
		writer.printlnIndented("ScopeBoxId.element |");
		writer.printlnIndented("ActivityId.element |");
		writer.printlnIndented("StressId.element ");
		writer.endBlock();
		
		
		writer.defineAlias("ThreatReductionResultThreatId.element", "element ThreatReductionResultThreatId");
		writer.startBlock();
		writer.printlnIndented("ThreatId.element");
		writer.endBlock();
		
		writer.defineAlias("LinkableFactorId.element", "element WrappedByDiagramFactorId");
		writer.startBlock();
		writer.printlnIndented("BiodiversityTargetId.element |");
		writer.printlnIndented("HumanWelfareTargetId.element |");
		writer.printlnIndented("CauseId.element |");
		writer.printlnIndented("StrategyId.element |");
		writer.printlnIndented("ThreatReductionResultId.element |");
		writer.printlnIndented("IntermediateResultId.element |");
		writer.printlnIndented("GroupBoxId.element ");
		writer.endBlock();
		
		writer.defineAlias("GeospatialLocation.element", "element miradi:GeospatialLocation");
		writer.startBlock();
		writer.printlnIndented("element miradi:latitude { xsd:decimal } &");
		writer.printlnIndented("element miradi:longitude { xsd:decimal } ");
		writer.endBlock();
		
		writer.defineAlias("DiagramPoint.element", "element miradi:DiagramPoint");
		writer.startBlock();
		writer.printlnIndented("element miradi:x { xsd:integer } &");
		writer.printlnIndented("element miradi:y { xsd:integer } ");
		writer.endBlock();
		
		writer.defineAlias("DiagramSize.element", "element miradi:DiagramSize");
		writer.startBlock();
		writer.printlnIndented("element miradi:width { xsd:integer } &");
		writer.printlnIndented("element miradi:height { xsd:integer } ");
		writer.endBlock();
		
		defineVocabularyDefinedAlias(writer, VOCABULARY_FISCAL_YEAR_START, "FiscalYearStartMonth");
		defineVocabularyDefinedAlias(writer, VOCABULARY_PROTECTED_AREA_CATEGORIES, PROTECTED_AREA_CATEGORIES_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_RESOURCE_TYPE, RESOURCE_TYPE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_RESOURCE_ROLE_CODES, RESOURCE_ROLE_CODES_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_HIDDEN_TYPES, HIDDEN_TYPES_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE, DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE, DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR, DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR, DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_DIAGRAM_LINK_COLOR, DIAGRAM_LINK_COLOR_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_BIODIVERSITY_TARGET_HABITAT_ASSICIATION, BIODIVERSITY_TARGET_HABITAT_ASSOCIATION_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TARGET_STATUS, TARGET_STATUS_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TARGET_VIABILITY_MODE, TARGET_VIABILITY_MODE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_SCOPE_BOX_COLOR, SCOPE_BOX_COLOR_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_STRESS_SEVERITY, STRESS_SEVERITY_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_STRESS_SCOPE, STRESS_SCOPE_ELEMENT_NAME);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TNC_PROJECT_PLACE_TYPES, TNC_PROJECT_PLACE_TYPES);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TNC_ORGANIZATIONAL_PRIORITIES, TNC_ORGANIZATIONAL_PRIORITIES);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TNC_OPERATING_UNTIS, TNC_OPERATING_UNITS);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TNC_TERRESTRIAL_ECO_REGION, TNC_TERRESTRIAL_ECO_REGION);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TNC_MARINE_ECO_REGION, TNC_MARINE_ECO_REGION);
		defineVocabularyDefinedAlias(writer, VOCABULARY_TNC_FRESHWATER_ECO_REGION, TNC_FRESHWATER_ECO_REGION);
		defineVocabularyDefinedAlias(writer, VOCABULARY_WWF_MANAGING_OFFICES, WWF_MANAGING_OFFICES);
		defineVocabularyDefinedAlias(writer, VOCABULARY_WWF_REGIONS, WWF_REGIONS);
		defineVocabularyDefinedAlias(writer, VOCABULARY_WWF_ECOREGIONS, WWF_ECOREGIONS);
		
		writer.flush();
    }

	private void defineVocabularyDefinedAlias(SchemaWriter writer, String vocabularyFiscalYearStartElementName, String elementName)
	{
		writer.defineAlias(elementName + ".element", "element miradi:" + elementName + "");
		writer.startBlock();
		writer.printlnIndented("element miradi:code { " + vocabularyFiscalYearStartElementName + " }* ");
		writer.endBlock();
	}
	
	private void defineIdElement(SchemaWriter writer, String baseName)
	{
		writer.println(baseName + "Id.element = element " + baseName + "Id { xsd:integer }");
	}
	
	private void defineVocabulary(SchemaWriter writer, String vocabularyName, ChoiceQuestion question)
	{
		writer.print(vocabularyName + " = ");
		CodeList codes = question.getAllCodes();
		for(int index = 0; index < codes.size(); ++index)
		{
			writer.write("'" + codes.get(index)+ "'");
			if (index < codes.size() - 1)
				writer.print("|");
		}
		
		writer.println();
	}
	
	private static final String VOCABULARY_FISCAL_YEAR_START = "vocabulary_fiscal_year_start_month";
	private static final String VOCABULARY_PROTECTED_AREA_CATEGORIES = "vocabulary_protected_area_categories";
	private static final String VOCABULARY_RESOURCE_TYPE = "vocabulary_resource_type";
	private static final String VOCABULARY_RESOURCE_ROLE_CODES = "vocabulary_resource_role_codes";
	private static final String VOCABULARY_HIDDEN_TYPES = "vocabulary_hidden_types";
	private static final String VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE = "vocabulary_diagram_factor_font_size";
	private static final String VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE = "vocabulary_diagram_factor_font_style";
	private static final String VOCABULARY_DIAGRAM_FACTOR_BACKGROUND_COLOR = "vocabulary_diagram_factor_background_color";
	private static final String VOCABULARY_DIAGRAM_FACTOR_FOREGROUND_COLOR = "vocabulary_diagram_factor_foreground_color";
	private static final String VOCABULARY_DIAGRAM_LINK_COLOR = "vocabulary_diagram_link_color";
	private static final String VOCABULARY_BIODIVERSITY_TARGET_HABITAT_ASSICIATION = "vocabulary_biodiversity_target_habitat_association";
	private static final String VOCABULARY_TARGET_STATUS = "vocabulary_target_status";
	private static final String VOCABULARY_TARGET_VIABILITY_MODE = "vocabulary_target_viability_mode";
	public static final String VOCABULARY_THREAT_TAXONOMY_CODE = "vocabulary_cause_taxonomy_code";
	public static final String VOCABULARY_STRATEGY_TAXONOMY_CODE = "vocabulary_strategy_taxonomy_code";
	private static final String VOCABULARY_SCOPE_BOX_COLOR = "vocabulary_scope_box_color";
	public static final String VOCABULARY_STRESS_SEVERITY = "vocabulary_stress_severity";
	public static final String VOCABULARY_STRESS_SCOPE = "vocabulary_stress_scope";
	public static final String VOCABULARY_STRATEGY_IMAPACT_RATING_CODE = "vocabulary_strategy_impact_rating_code";
	public static final String VOCABULARY_STRATEGY_FEASIBILITY_RATING_CODE = "vocabulary_strategy_feasibility_rating_code";
	public static final String VOCABULARY_PRIORITY_RATING_CODE = "vocabulary_priority_rating_code";
	public static final String VOCABULARY_STATUS_CODE = "vocabulary_status_code";
	public static final String VOCABULARY_KEA_TYPE = "vocabulary_key_ecological_attribute_type";
	public static final String VOCABULARY_THREAT_STRESS_RATING_IRREVERSIBILITY_CODE = "vocabulary_irreversibility_code";
	public static final String VOCABULARY_THREAT_STRESS_RATING_CONTRIBUTION_CODE = "vocabulary_contribution_code";
	public static final String VOCABULARY_SIMPLE_THREAT_RATING_SCOPE_CODE = "vocabulary_simple_threat_rating_scope_code";
	public static final String VOCABULARY_SIMPLE_THREAT_RATING_SEVERITY_CODE = "vocabulary_simple_threat_rating_severitiy_code";
	public static final String VOCABULARY_SIMPLE_THREAT_RATING_IRREVERSIBILITY_CODE = "vocabulary_simple_threat_rating_irreversibility_code";
	public static final String VOCABULARY_MONTH = "vocabulary_month";
	public static final String VOCABULARY_START_MONTH = "vocabulary_start_month";
	public static final String VOCABULARY_YEAR = "vocabulary_year";
	public static final String VOCABULARY_START_YEAR = "vocabulary_start_year";
	public static final String VOCABULARY_TNC_PROJECT_PLACE_TYPES = "vocabulary_tnc_project_place_types";
	public static final String VOCABULARY_TNC_ORGANIZATIONAL_PRIORITIES = "vocabulary_tnc_organizational_priorities";
	public static final String VOCABULARY_TNC_OPERATING_UNTIS = "vocabulary_tnc_operating_units";
	public static final String VOCABULARY_TNC_TERRESTRIAL_ECO_REGION = "vocabulary_tnc_terrestrial_eco_region";
	public static final String VOCABULARY_TNC_MARINE_ECO_REGION = "vocabulary_tnc_marine_eco_region";
	public static final String VOCABULARY_TNC_FRESHWATER_ECO_REGION = "vocabulary_tnc_freshwater_eco_region";
	public static final String VOCABULARY_WWF_MANAGING_OFFICES = "vocabulary_wwf_managing_offices";
	public static final String VOCABULARY_WWF_REGIONS = "vocabulary_wwf_regions";
	public static final String VOCABULARY_WWF_ECOREGIONS = "vocabulary_wwf_ecoregions";
	public static final String VOCABULARY_FOS_TRAINING_TYPE = "vocabulary_fos_training_type";
	
	public static final String PROTECTED_AREA_CATEGORIES_ELEMENT_NAME = ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES;
	public static final String RESOURCE_TYPE_ELEMENT_NAME = ProjectResource.TAG_RESOURCE_TYPE;
	public static final String RESOURCE_ROLE_CODES_ELEMENT_NAME = ProjectResource.TAG_ROLE_CODES;
	public static final String HIDDEN_TYPES_ELEMENT_NAME = DiagramObject.TAG_HIDDEN_TYPES;
	public static final String TAGGED_OBJECT_SET_ELEMENT_NAME = "TaggedObjectSet";
	public static final String KEA_ID_ELEMENT_NAME = "KeyEcologicalAttribute";
	public static final String SUB_TARGET_ID_ELEMENT_NAME = "SubTarget";
	public static final String THREAT_ID_ELEMENT_NAME = "Threat";
	private static final String FUNDING_SOURCE_ID_ELEMENT_NAME = "FundingSource";
	private static final String ACCOUNTING_CODE_ID_ELEMENT_NAME = "AccountingCode";
	public static final String ACITIVTY_ID_ELEMENT_NAME = "Activity";
	public static final String OBJECTIVE_ID_ELEMENT_NAME = "Objective";
	public static final String GOAL_ELEMENT_NAME = "Goal";
	public static final String PROGRESS_REPORT_ID_ELEMENT_NAME = "ProgressReport";
	public static final String PROGRESS_PERCENT_ID_ELEMENT_NAME = "ProgressPercent";
	public static final String EXPENSE_ASSIGNMENT_ID_ELEMENT_NAME = "ExpenseAssignment";
	public static final String RESOURCE_ASSIGNMENT_ID_ELEMENT_NAME = "ResourceAssignment";
	public static final String RESOURCE_ID_ELEMENT_NAME = "Resource";
	public static final String INDICATOR_ID_ELEMENT_NAME = "Indicator";
	public static final String BIODIVERSITY_TARGET_ID_ELEMENT_NAME = "BiodiversityTarget";
	public static final String DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME = DiagramFactor.TAG_FONT_SIZE;
	public static final String DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME = DiagramFactor.TAG_FONT_STYLE;
	public static final String DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME = DiagramFactor.TAG_BACKGROUND_COLOR;
	public static final String DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME = DiagramFactor.TAG_FOREGROUND_COLOR;
	public static final String DIAGRAM_LINK_COLOR_ELEMENT_NAME = DiagramLink.TAG_COLOR;
	public static final String BIODIVERSITY_TARGET_HABITAT_ASSOCIATION_ELEMENT_NAME = Target.TAG_HABITAT_ASSOCIATION;
	public static final String TARGET_STATUS_ELEMENT_NAME = AbstractTarget.TAG_TARGET_STATUS;
	public static final String TARGET_VIABILITY_MODE_ELEMENT_NAME = AbstractTarget.TAG_VIABILITY_MODE;
	public static final String CAUSE_TAXONOMY_ELEMENT_NAME = Cause.TAG_TAXONOMY_CODE;
	public static final String STRATEGY_TAXONOMY_ELEMENT_NAME = Strategy.TAG_TAXONOMY_CODE;
	public static final String STRATEGY_IMPACT_RATING_ELEMENT_NAME = Strategy.TAG_IMPACT_RATING;
	public static final String STRATEGY_FEASIBILITY_RATING_ELEMENT_NAME = Strategy.TAG_FEASIBILITY_RATING;
	public static final String SCOPE_BOX_COLOR_ELEMENT_NAME = ScopeBox.TAG_SCOPE_BOX_COLOR_CODE;
	public static final String STRESS_SEVERITY_ELEMENT_NAME = Stress.TAG_SEVERITY;
	public static final String STRESS_SCOPE_ELEMENT_NAME = Stress.TAG_SCOPE;
	public static final String STRESS_ID_ELEMENT_NAME = "Stress";
	private static final String CAUSE_ID_ELEMENT_NAME = "Cause";
	public static final String THREAT_STRESS_RATING_IRREVERSIBILITY_CODE = ThreatStressRating.TAG_IRREVERSIBILITY;
	public static final String THREAT_STRESS_RATING_CONTRIBUTION_CODE = ThreatStressRating.TAG_CONTRIBUTION;
	public static final String TNC_PROJECT_PLACE_TYPES = TncProjectData.TAG_PROJECT_PLACE_TYPES;
	public static final String TNC_ORGANIZATIONAL_PRIORITIES = TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES;
	public static final String TNC_OPERATING_UNITS = ProjectMetadata.TAG_TNC_OPERATING_UNITS;
	public static final String TNC_TERRESTRIAL_ECO_REGION = ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION;
	public static final String TNC_MARINE_ECO_REGION = ProjectMetadata.TAG_TNC_MARINE_ECO_REGION;
	public static final String TNC_FRESHWATER_ECO_REGION = ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION;
	public static final String WWF_MANAGING_OFFICES = WwfProjectData.TAG_MANAGING_OFFICES;
	public static final String WWF_REGIONS = WwfProjectData.TAG_REGIONS;
	public static final String WWF_ECOREGIONS = WwfProjectData.TAG_ECOREGIONS;
}
