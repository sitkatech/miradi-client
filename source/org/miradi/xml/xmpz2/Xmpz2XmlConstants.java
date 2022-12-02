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

package org.miradi.xml.xmpz2;

import org.miradi.objects.DiagramFactor;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.schemas.*;

public interface Xmpz2XmlConstants
{
	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String RAW_PREFIX = "miradi";
	public static final String COLON = ":";
	public static final String PREFIX = RAW_PREFIX + COLON;
	public static final String SINGLE_SPACE = " ";
	public static final String ELEMENT_NAME = "element" + SINGLE_SPACE;
	public static final String DOT_ELEMENT = ".element";
	
	public static final String NAME_SPACE_VERSION = "248";

	public static final String NAME_SPACE_VERSION_248 = "248";
	public static final String NAME_SPACE_VERSION_247 = "247";
	public static final String NAME_SPACE_VERSION_246 = "246";
	public static final String NAME_SPACE_VERSION_245 = "245";
	public static final String NAME_SPACE_VERSION_244 = "244";
	public static final String NAME_SPACE_VERSION_243 = "243";
	public static final String NAME_SPACE_VERSION_242 = "242";
	public static final String NAME_SPACE_VERSION_237 = "237";
	public static final String NAME_SPACE_VERSION_236 = "236";
	public static final String NAME_SPACE_VERSION_235 = "235";
	public static final String NAME_SPACE_VERSION_234 = "234";
	public static final String NAME_SPACE_VERSION_233 = "233";
	public static final String NAME_SPACE_VERSION_232 = "232";
	public static final String NAME_SPACE_VERSION_228 = "228";

	public static final int LOWEST_SCHEMA_VERSION = 228;

	public static final String W3_XMLNS = "http://www.w3.org/2000/xmlns/";
	public static final String PARTIAL_NAME_SPACE = "http://xml.miradi.org/schema/ConservationProject/";
	public static final String NAME_SPACE = PARTIAL_NAME_SPACE + NAME_SPACE_VERSION;
	public static final String XMLNS = "xmlns";
	public static final String NAME_SPACE_ATTRIBUTE_NAME = XMLNS + COLON + RAW_PREFIX;
	
	public static final String CONTAINER_ELEMENT_TAG = "Container";
	public static final String POOL_ELEMENT_TAG = "Pool";
	public static final String TEXT_ELEMENT_TYPE = "text";
	public static final String FORMATTED_TEXT_TYPE = "formatted_text";
	public static final String URI_RESTRICTED_TEXT = "uri_restricted_text";
	public static final String NON_EMPTY_STRING = "non_empty_string";
	public static final String HEX_COLOR_CODE = "color";

	public static final String UUID = "uuid";
	public static final String ID = "Id";
	public static final String IDS = ID + "s";
	public static final String IDENTIFIER = "Identifier";
	public static final String CONSERVATION_PROJECT = "ConservationProject";
	public static final String EXPORT_DETAILS = "ExportDetails";
	public static final String EXPORTER_NAME = "ExporterName";
	public static final String EXPORTER_VERSION = "ExporterVersion";
	public static final String EXPORT_TIME = "ExportTime";
	public static final String PROJECT_SUMMARY = "ProjectSummary";
	public static final String PROJECT_RESOURCE = "Resource";
	public static final String ORGANIZATION = OrganizationSchema.OBJECT_NAME;
	public static final String PROJECT_SUMMARY_SCOPE = "ProjectScope";
	public static final String PROJECT_SUMMARY_LOCATION = "ProjectLocation";
	public static final String PROJECT_SUMMARY_PLANNING = "ProjectPlanning";
	public static final String DAY_COLUMNS_VISIBILITY = "DayColumnsVisibility";
	public static final String TNC_PROJECT_DATA = "TNCProjectData";
	public static final String WWF_PROJECT_DATA = "WWFProjectData";
	public static final String WCS_PROJECT_DATA = "WCSProjectData";
	public static final String RARE_PROJECT_DATA = "RareProjectData";
	public static final String FOS_PROJECT_DATA = "FOSProjectData";
	public static final String MIRADI_SHARE_PROJECT_DATA = "MiradiShareProjectData";
	public static final String CONCEPTUAL_MODEL = "ConceptualModel";
	public static final String RESULTS_CHAIN = "ResultsChain";
	public static final String DIAGRAM_FACTOR = DiagramFactorSchema.OBJECT_NAME;
	public static final String DIAGRAM_LINK = DiagramLinkSchema.OBJECT_NAME;
	public static final String DIAGRAM_LINK_IDS = "DiagramLinkIds";
	public static final String BIODIVERSITY_TARGET = "BiodiversityTarget";
	public static final String HUMAN_WELFARE_TARGET = HumanWelfareTargetSchema.HUMAN_WELLBEING_TARGET;
	public static final String BIOPHYSICAL_FACTOR = BiophysicalFactorSchema.BIOPHYSICAL_FACTOR;
	public static final String BIOPHYSICAL_RESULT = BiophysicalResultSchema.OBJECT_NAME;
	public static final String THREAT_REDUCTION_RESULT = ThreatReductionResultSchema.OBJECT_NAME;
	public static final String INTERMEDIATE_RESULT = IntermediateResultSchema.OBJECT_NAME;
	public static final String GROUP_BOX = GroupBoxSchema.OBJECT_NAME;
	public static final String TEXT_BOX = TextBoxSchema.OBJECT_NAME;
	public static final String SCOPE_BOX = ScopeBoxSchema.OBJECT_NAME;
	public static final String KEY_ECOLOGICAL_ATTRIBUTE = KeyEcologicalAttributeSchema.OBJECT_NAME;
	public static final String CAUSE = CauseSchema.OBJECT_NAME;
	public static final String STRATEGY = StrategySchema.OBJECT_NAME;
	public static final String STRESS = StressSchema.OBJECT_NAME;
	public static final String STRESS_ID = STRESS + ID;
	public static final String STRESS_IDS_ELEMENT = "StressIds";
	public static final String SUB_TARGET = "SubTarget";
	public static final String SUB_TARGET_IDS_ELEMENT = "SubTargetIds";
	public static final String GOAL = GoalSchema.OBJECT_NAME;
	public static final String OBJECTIVE = ObjectiveSchema.OBJECT_NAME;
	public static final String INDICATOR = IndicatorSchema.OBJECT_NAME;
	public static final String ACTIVITY = TaskSchema.ACTIVITY_NAME;
	public static final String ORDERED_ACTIVITY_IDS = "OrderedActivityIds";
	public static final String METHOD = MethodSchema.OBJECT_NAME;
	public static final String TASK = TaskSchema.OBJECT_NAME;
	public static final String OUTPUT = OutputSchema.OBJECT_NAME;
	public static final String LEGACY_ANALYTICAL_QUESTION = "AnalyticalQuestion";
	public static final String LEGACY_ASSUMPTION = "Assumption";
	public static final String ASSUMPTION = AssumptionSchema.OBJECT_NAME;
	public static final String SUB_ASSUMPTION = SubAssumptionSchema.OBJECT_NAME;
	public static final String SUB_TASK = "SubTask";
	public static final String ASSIGNED_LEADER_RESOURCE_ID = "AssignedLeaderResourceId";
	public static final String TIMEFRAME = "Timeframe";
	public static final String CALCULATED_TIMEFRAME = "Calculated" + TIMEFRAME;
	public static final String ASSIGNMENT = "Assignment";
	public static final String RESOURCE_ASSIGNMENT = "ResourceAssignment";
	public static final String EXPENSE_ASSIGNMENT = "ExpenseAssignment";
	public static final String PROGRESS_REPORT = "ProgressReport";
	public static final String EXTENDED_PROGRESS_REPORT = "ExtendedProgressReport";
	public static final String RESULT_REPORT = "ResultReport";
	public static final String PROGRESS_PERCENT = "ProgressPercent";
	public static final String THREAT = "Threat";
	public static final String TARGET = TargetSchema.OBJECT_NAME;
	public static final String RATINGS = "Ratings";
	public static final String SCOPE = "ScopeRating";
	public static final String SEVERITY = "SeverityRating";
	public static final String IRREVERSIBILITY = "IrreversibilityRating";
	public static final String CONTRIBUTION = "ContributionRating";
	public static final String IS_ACTIVE = "IsActive";
	public static final String COMMENTS = "Comments";
	public static final String EVIDENCE_NOTES = "EvidenceNotes";
	public static final String EVIDENCE_CONFIDENCE = "EvidenceConfidence";
	public static final String THREAT_STRESS_RATING = "ThreatStressRating";
	public static final String SIMPLE_BASED_THREAT_RATING = "SimpleThreatRating";
	public static final String STRESS_BASED_THREAT_RATING = "StressBasedThreatRating";
	public static final String IS_NOT_APPLICABLE = "IsNotApplicable";
	public static final String MEASUREMENT = MeasurementSchema.OBJECT_NAME;
	public static final String MEASUREMENT_IDS = "MeasurementIds";
	public static final String FUTURE_STATUS_IDS = "FutureStatusIds";
	public static final String FUTURE_STATUS = FutureStatusSchema.OBJECT_NAME;
	public static final String ACCOUNTING_CODE = AccountingCodeSchema.OBJECT_NAME;
	public static final String FUNDING_SOURCE = FundingSourceSchema.OBJECT_NAME;
	public static final String BUDGET_CATEGORY_ONE = "BudgetCategoryOne";
	public static final String BUDGET_CATEGORY_TWO = "BudgetCategoryTwo";
	public static final String PROJECT_LOCATION = "ProjectLocation";
	public static final String GEOSPATIAL_LOCATION = "GeospatialLocation";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String TAGGED_OBJECT_SET_ELEMENT_NAME = "TaggedObjectSet";
	public static final String TAGGED_OBJECT_SET_IDS = TAGGED_OBJECT_SET_ELEMENT_NAME + IDS;
	public static final String SELECTED_TAGGED_OBJECT_SET_IDS = "Selected" + TAGGED_OBJECT_SET_IDS;
	public static final String TAGGED_FACTOR_IDS = "FactorIds";
	public static final String DIAGRAM_POINT_ELEMENT_NAME = "DiagramPoint";
	public static final String DIAGRAM_SIZE_ELEMENT_NAME = "DiagramSize";
	public static final String X_ELEMENT_NAME = "x";
	public static final String Y_ELEMENT_NAME = "y";
	public static final String WIDTH_ELEMENT_NAME = "width";
	public static final String HEIGHT_ELEMENT_NAME = "height";
	public static final String WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME = "WrappedByDiagramFactorId";
	public static final String WRAPPED_FACTOR_ID_ELEMENT_NAME = "WrappedFactorId";
	public static final String ID_ELEMENT_NAME = "Id";
	public static final String LINKABLE_FACTOR = "LinkableFactor";
	public static final String LINKABLE_FACTOR_ID = "LinkableFactorId";
	public static final String FROM_DIAGRAM_FACTOR_ID = "FromDiagramFactorId";
	public static final String TO_DIAGRAM_FACTOR_ID = "ToDiagramFactorId";
	public static final String DIAGRAM_ID = "DiagramId";
	public static final String BEND_POINTS_ELEMENT_NAME = "BendPoints";
	public static final String GROUP_BOX_CHILDREN_IDS = "GroupBoxChildrenIds";
	public static final String GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID = "GroupedDiagramLinkIds";
	public static final String RESOURCE_ID = "ResourceId";
	public static final String RESOURCE = "Resource";
	public static final String FUNDING_SOURCE_ID = "FundingSourceId";
	public static final String ACCOUNTING_CODE_ID = "AccountingCodeId";
	public static final String BUDGET_CATEGORY_ONE_ID = "BudgetCategoryOneId";
	public static final String BUDGET_CATEGORY_TWO_ID = "BudgetCategoryTwoId";
	public static final String PROGRESS_REPORT_IDS = "ProgressReportIds";
	public static final String EXTENDED_PROGRESS_REPORT_IDS = "ExtendedProgressReportIds";
	public static final String RESULTS_REPORT_IDS = "ResultReportIds";
	public static final String PROGRESS_PERCENT_IDS = "ProgressPercentIds";
	public static final String OUTPUT_IDS = "OutputIds";
	public static final String RELATED_THREAT_ID = "RelatedDirectThreat" + ID;
	public static final String THREAT_ID = "ThreatId";
	public static final String RELEVANT_ACTIVITY_IDS = "RelevantActivityIds";
	public static final String RELEVANT_STRATEGY_IDS = "RelevantStrategyIds";	
	public static final String RELEVANT_GOAL_IDS = "RelevantGoalIds";
	public static final String RELEVANT_OBJECTIVE_IDS = "RelevantObjectiveIds";
	public static final String RELEVANT_INDICATOR_IDS = "RelevantIndicatorIds";
	public static final String RELEVANT_DIAGRAM_FACTOR_IDS = "RelevantDiagramFactorIds";
	public static final String DATE_UNIT = "DateUnit";
	public static final String DATE_UNITS_EXPENSE = "DateUnitExpense";
	public static final String EXPENSE = "Expense";
	public static final String EXPENSES_DATE_UNIT = "ExpensesDateUnit";
	public static final String EXPENSES_FULL_PROJECT_TIMESPAN = "ExpensesFullProjectTimespan";
	public static final String WORK_UNITS_FULL_PROJECT_TIMESPAN = "WorkUnitsFullProjectTimespan";
	public static final String FULL_PROJECT_TIMESPAN = "FullProjectTimespan";
	public static final String VOCABULARY_FULL_PROJECT_TIMESPAN = "vocabulary_full_project_timespan";
	public static final String TOTAL = "Total";
	public static final String EXPENSES_YEAR = "ExpensesYear";
	public static final String EXPENSES_QUARTER = "ExpensesQuarter";
	public static final String EXPENSES_MONTH = "ExpensesMonth";
	public static final String EXPENSES_DAY = "ExpensesDay";
	public static final String START_YEAR = "StartYear";
	public static final String YEAR = "Year";
	public static final String DATE = "Date";
	public static final String START_MONTH = "StartMonth";
	public static final String MONTH = "Month";
	public static final String WORK_UNITS = "NumberOfUnits";
	public static final String DATE_UNIT_WORK_UNITS = "DateUnitWorkUnits";
	public static final String WORK_UNITS_DATE_UNIT = "WorkUnitsDateUnit";
	public static final String WORK_UNITS_DAY = "WorkUnitsDay";
	public static final String WORK_UNITS_MONTH = "WorkUnitsMonth";
	public static final String WORK_UNITS_QUARTER = "WorkUnitsQuarter";
	public static final String WORK_UNITS_YEAR = "WorkUnitsYear";
	public static final String DATE_UNITS_TIMEFRAME = "DateUnitTimeframe";
	public static final String TIMEFRAMES_DATE_UNIT = "TimeframesDateUnit";
	public static final String TIMEFRAMES_FULL_PROJECT_TIMESPAN = "TimeframesFullProjectTimespan";
	public static final String TIMEFRAMES_YEAR = "TimeframesYear";
	public static final String TIMEFRAMES_QUARTER = "TimeframesQuarter";
	public static final String TIMEFRAMES_MONTH = "TimeframesMonth";
	public static final String TIMEFRAMES_DAY = "TimeframesDay";
	public static final String STYLE = "Style";
	public static final String CALCULATED_STRESS_RATING = "CalculatedStressRating";
	public static final String CALCULATED_THREAT_STRESS_RATING = "CalculatedThreatStressRating";
	public static final String CALCULATED_THREAT_TARGET_RATING = "CalculatedThreatTargetRating";
	public static final String SIMPLE_THREAT_TARGET_CALCULATED_RATING = "CalculatedThreatTargetRating";
	public static final String CALCULATED_THREAT_RATING = "CalculatedThreatRating";
	public static final String OVERALL_PROJECT_THREAT_RATING = "CalculatedOverallProjectThreatRating";
	public static final String OVERALL_PROJECT_VIABILITY_RATING = "CalculatedOverallProjectViabilityRating";
	public static final String OVERALL_PROJECT_VIABILITY_FUTURE_RATING = "CalculatedOverallProjectViabilityFutureRating";
	public static final String IUCN_REDLIST_SPECIES = "IUCNRedListSpecies";
	public static final String OTHER_NOTABLE_SPECIES = "OtherNotableSpecies";
	public static final String AUDIENCE = "Audience";
	public static final String OBJECT_TREE_TABLE_CONFIGURATION = "PlanningViewConfiguration";
	public static final String METHOD_IDS = "OrderedMethodIds";
	public static final String THRESHOLDS = "Thresholds";
	public static final String THRESHOLD = "IndicatorThreshold";
	public static final String THRESHOLD_VALUE = "ThresholdValue";
	public static final String THRESHOLD_DETAILS = "ThresholdDetails";
	public static final String STATUS_CODE = "StatusCode";
	public static final String SUB_TASK_IDS = "OrderedSubTaskIds";
	public static final String SUB_ASSUMPTION_IDS = "OrderedSubAssumptionIds";
	public static final String EXTERNAL_APP_ELEMENT_NAME = "ExternalApp";
	public static final String PROJECT_ID = "ProjectId";
	public static final String LOCATION = "Location";
	public static final String SIZE = "Size";
	public static final String EXTERNAL_PROJECT_ID_ELEMENT_NAME = "ExternalProjectId";
	public static final String DASHBOARD = "Dashboard";
	public static final String DASHBOARD_STATUS_ENTRIES = "StatusEntries";
	public static final String DASHBOARD_STATUS_ENTRY = "StatusEntry";
	public static final String KEY_ATTRIBUTE_NAME = "Key";
	public static final String DASHBOARD_PROGRESS = "Progress";
	public static final String DASHBOARD_COMMENTS = "Comments";
	public static final String DASHBOARD_FLAGS = "Flags";
	public static final String EXTRA_DATA = "ExtraData";
	public static final String EXTRA_DATA_SECTION = "ExtraDataSection";
	public static final String EXTRA_DATA_SECTION_OWNER_ATTRIBUTE = "owner";
	public static final String MIRADI_CLIENT_EXTRA_DATA_SECTION = "MiradiClient";
	public static final String MIRADI_SHARE_EXTRA_DATA_SECTION = MiradiShareProjectData.MIRADI_SHARE_PROJECT_CODE;
	public static final String EXTRA_DATA_ITEM = "ExtraDataItem";
	public static final String EXTRA_DATA_ITEM_NAME = "ExtraDataItemName";
	public static final String EXTRA_DATA_ITEM_VALUE = "ExtraDataItemValue";
	public static final String TIME_PERIOD_COSTS = "CalculatedCosts";
	public static final String EXPENSE_ENTRY = "CalculatedExpenseEntry";
	public static final String WORK_UNITS_ENTRY = "CalculatedWorkUnitsEntry";
	public static final String CALCULATED_EXPENSE_TOTAL = "CalculatedExpenseTotal";
	public static final String CALCULATED_WORK_UNITS_TOTAL = "CalculatedWorkUnitsTotal";
	public static final String CALCULATED_TOTAL_BUDGET_COST = "CalculatedTotalBudgetCost";
	public static final String CALCULATED_WORK_COST_TOTAL = "CalculatedWorkCostTotal";
	public static final String CALCULATED_WHO = "CalculatedWho";
	public static final String CALCULATED_START_DATE = "CalculatedStartDate";
	public static final String CALCULATED_END_DATE = "CalculatedEndDate";
	public static final String CALCULATED_EXPENSE_ENTRIES = "CalculatedExpenseEntries";
	public static final String CALCULATED_WORK_UNITS_ENTRIES = "CalculatedWorkUnitsEntries";
	public static final String DETAILS = "Details";
	public static final String CHAIN_MODE_FACTORS = "ChainFactors";
	public static final String DELETED_ORPHANS_ELEMENT_NAME = "DeletedOrphans";
	public static final String SCOPE_BOX_COLOR_ELEMENT_NAME = "ScopeBoxTypeCode";
	public static final String PROJECT_SHARE_OUTSIDE_ORGANIZATION = "ShareOutsideOrganization";
	public static final String LESSONS_LEARNED = "LessonsLearned";
	public static final String TARGET_STATUS_ELEMENT_NAME = "SimpleViabilityStatus";
	public static final String TARGET_FUTURE_STATUS_ELEMENT_NAME = "SimpleViabilityFutureStatus";
	public static final String TARGET_CALCULATED_STATUS_ELEMENT_NAME = "CalculatedViabilityStatus";
	public static final String TARGET_CALCULATED_FUTURE_STATUS_ELEMENT_NAME = "CalculatedViabilityFutureStatus";
	public static final String TNC_OPERATING_UNITS = "TNCOperatingUnits";
	public static final String TNC_CAP_STANDARDS_SCORECARD = "CAPStandardsScorecard";
	public static final String TNC_TERRESTRIAL_ECO_REGION = "TNCTerrestrialEcoRegion";
	public static final String TNC_MARINE_ECO_REGION = "TNCMarineEcoRegion";
	public static final String TNC_FRESHWATER_ECO_REGION = "TNCFreshwaterEcoRegion";
	public static final String TNC_DATABASE_DOWNLOAD_DATE = "DatabaseDownloadDate";
	public static final String TNC_PLANNING_TEAM_COMMENTS = "PlanningTeamComments";
	public static final String TNC_LESSONS_LEARNED = "LessonsLearned";
	public static final String CODE_ELEMENT_NAME = "code";
	public static final String RESOURCE_ID_ELEMENT_NAME = "Resource";
	public static final String STANDARD_CLASSIFICATION = "StandardClassification";
	public static final String GIVEN_NAME = "GivenName";
	public static final String REFS = "Refs";
	public static final String SURNAME = "Surname";
	public static final String SOURCE = "Source";
	public static final String MEASUREMENT_VALUE = "MeasurementValue";
	public static final String RATING = "Rating";
	public static final String MAGNITUDE = "Magnitude";
	public static final String OFFICE_PHONE_NUMBER = "OfficePhoneNumber";
	public static final String NAME = "Name";
	public static final String CUSTOM1 = "Custom1";
	public static final String CUSTOM2 = "Custom2";
	public static final String VIABILITY_RATINGS_COMMENTS = "ViabilityRatingsComments";
	public static final String FUTURE_STATUS_COMMENTS = "FutureStatusComments";
	public static final String FUTURE_STATUS_DETAILS = "FutureStatusDetails";
	public static final String LOCATION_DETAILS = "LocationDetails";
	public static final String VISION_LABEL = "VisionLabel";
	public static final String VISION_STATEMENT_TEXT = "VisionStatementText";
	public static final String SCOPE_NAME = "ScopeName";
	public static final String SCOPE_DESCRIPTION = "ScopeDescription";
	public static final String DIAGRAM_DATA_INCLUSION = "DiagramDataInclusion";
	public static final String DIAGRAM_FILTER = "DiagramFilter";
	public static final String COLUMN_CONFIGURATION_CODES = "ColumnNames";
	public static final String ROW_CONFIGURATION_CODES = "RowObjectTypes";

	public static final String DAILY_RATE = "DailyRate";
	public static final String DIAGRAM_FACTOR_BACKGROUND_COLOR_ELEMENT_NAME = DiagramFactor.TAG_BACKGROUND_COLOR;
	public static final String DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME = DiagramFactor.TAG_FOREGROUND_COLOR;
	public static final String DIAGRAM_FACTOR_FONT_STYLE_ELEMENT_NAME = DiagramFactor.TAG_FONT_STYLE;
	public static final String DIAGRAM_FACTOR_FONT_SIZE_ELEMENT_NAME = DiagramFactor.TAG_FONT_SIZE;
	public static final String Z_INDEX = DiagramFactor.TAG_Z_INDEX;
	public static final String THREAT_REDUCTION_RESULT_THREAT = THREAT_REDUCTION_RESULT + THREAT;
	public static final String MIRADI_SHARE_PROJECT_DATA_TAXONOMY_ASSOCIATION_POOL = "MiradiShareProjectDataTaxonomyAssociationPool";
	public static final String BIODIVERSITY_TARGET_TAXONOMY_ASSOCIATION_POOL = "BiodiversityTargetTaxonomyAssociationPool";
	public static final String HUMAN_WELLBEING_TARGET_TAXONOMY_ASSOCIATION_POOL = "HumanWellbeingTargetTaxonomyAssociationPool";
	public static final String BIOPHYSICAL_FACTOR_TAXONOMY_ASSOCIATION_POOL = "BiophysicalFactorTaxonomyAssociationPool";
	public static final String BIOPHYSICAL_RESULT_TAXONOMY_ASSOCIATION_POOL = "BiophysicalResultTaxonomyAssociationPool";
	public static final String CONTRIBUTING_FACTOR_TAXONOMY_ASSOCIATION_POOL = "ContributingFactorTaxonomyAssociationPool";
	public static final String DIRECT_THREAT_TAXONOMY_ASSOCIATION_POOL = "DirectThreatTaxonomyAssociationPool";
	public static final String INTERMEDIATE_RESULT_TAXONOMY_ASSOCIATION_POOL = "IntermediateResultTaxonomyAssociationPool";
	public static final String STRATEGY_TAXONOMY_ASSOCIATION_POOL = "StrategyTaxonomyAssociationPool";
	public static final String RESULTS_CHAIN_TAXONOMY_ASSOCIATION_POOL = "ResultsChainTaxonomyAssociationPool";
	public static final String CONCEPTUAL_MODEL_TAXONOMY_ASSOCIATION_POOL = "ConceptualModelTaxonomyAssociationPool";
	public static final String THREAT_REDUCTION_RESULT_TAXONOMY_ASSOCIATION_POOL = "ThreatReductionResultTaxonomyAssociationPool";
	public static final String GOAL_TAXONOMY_ASSOCIATION_POOL = "GoalTaxonomyAssociationPool";
	public static final String KEY_ECOLOGICAL_ATTRIBUTE_TAXONOMY_ASSOCIATION_POOL = "KeyEcologicalAttributeTaxonomyAssociationPool";
	public static final String INDICATOR_TAXONOMY_ASSOCIATION_POOL = "IndicatorTaxonomyAssociationPool";
	public static final String METHOD_TAXONOMY_ASSOCIATION_POOL = "MethodTaxonomyAssociationPool";
	public static final String OBJECTIVE_TAXONOMY_ASSOCIATION_POOL = "ObjectiveTaxonomyAssociationPool";
	public static final String STRESS_TAXONOMY_ASSOCIATION_POOL = "StressTaxonomyAssociationPool";
	public static final String TASK_TAXONOMY_ASSOCIATION_POOL = "TaskTaxonomyAssociationPool";
	public static final String PROJECT_RESOURCE_TAXONOMY_ASSOCIATION_POOL = "ProjectResourceTaxonomyAssociationPool";
	public static final String RESOURCE_ASSIGNMENT_TAXONOMY_ASSOCIATION_POOL = "ResourceAssignmentTaxonomyAssociationPool";
	public static final String EXPENSE_ASSIGNMENT_TAXONOMY_ASSOCIATION_POOL = "ExpenseAssignmentTaxonomyAssociationPool";
	public static final String OUTPUT_TAXONOMY_ASSOCIATION_POOL = "OutputTaxonomyAssociationPool";
	public static final String ASSUMPTION_TAXONOMY_ASSOCIATION_POOL = "AssumptionTaxonomyAssociationPool";
	public static final String SUB_ASSUMPTION_TAXONOMY_ASSOCIATION_POOL = "SubAssumptionTaxonomyAssociationPool";
	public static final String TAXONOMY = "Taxonomy";
	public static final String TAXONOMY_POOL = "TaxonomyPool";
	public static final String TAXONOMY_ASSOCIATION = "TaxonomyAssociation";
	public static final String TAXONOMY_CLASSIFICATION = "TaxonomyClassification";
	public static final String TAXONOMY_CLASSIFICATION_CONTAINER = TAXONOMY_CLASSIFICATION + "Container";
	public static final String TAXONOMY_ELEMENT = TAXONOMY + "Element";
	public static final String TAXONOMY_ELEMENTS = TAXONOMY_ELEMENT + "s";
	public static final String TAXONOMY_VERSION = "Version";
	public static final String CODE = "Code";
	public static final String TAXONOMY_ELEMENT_CODE = TAXONOMY_ELEMENT + CODE;
	public static final String TAXONOMY_ELEMENT_CHILD_CODE = TAXONOMY_ELEMENT + "ChildCode";
	public static final String TAXONOMY_ELEMENT_LABEL = TAXONOMY_ELEMENT + "Label";
	public static final String TAXONOMY_ELEMENT_USER_CODE = TAXONOMY_ELEMENT + "UserCode";
	public static final String TAXONOMY_ELEMENT_DESCRIPTION = TAXONOMY_ELEMENT + "Description";
	public static final String TAXONOMY_CLASSIFICATION_TAXONOMY_ELEMENT_CODE = TAXONOMY_CLASSIFICATION + TAXONOMY_ELEMENT_CODE;
	public static final String TAXONOMY_CODE = TAXONOMY + CODE;
	public static final String TAXONOMY_CLASSIFICATION_TAXONOMY_CODE = TAXONOMY_CLASSIFICATION + TAXONOMY + CODE;
	public static final String TAXONOMY_TOP_LEVEL_ELEMENT_CODE = "TopLevelElementCode";

	public static final String STRATEGY_STANDARD_CLASSIFICATION = STRATEGY + STANDARD_CLASSIFICATION;
	public static final String STRATEGY_STANDARD_CLASSIFICATION_CONTAINER = STRATEGY_STANDARD_CLASSIFICATION + "Container";
	public static final String STRATEGY_STANDARD_CLASSIFICATION_CODE = STRATEGY_STANDARD_CLASSIFICATION + CODE;

	public static final String CAUSE_STANDARD_CLASSIFICATION = CAUSE + STANDARD_CLASSIFICATION;
	public static final String CAUSE_STANDARD_CLASSIFICATION_CONTAINER = CAUSE_STANDARD_CLASSIFICATION + "Container";
	public static final String CAUSE_STANDARD_CLASSIFICATION_CODE = CAUSE_STANDARD_CLASSIFICATION + CODE;

	public static final String RESOURCE_ASSIGNMENT_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL = "ResourceAssignmentAccountingClassificationAssociationPool";
	public static final String EXPENSE_ASSIGNMENT_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL = "ExpenseAssignmentAccountingClassificationAssociationPool";
	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION = "AccountingClassificationAssociation";

	public static final String ACCOUNTING_CLASSIFICATION = "AccountingClassification";
	public static final String ACCOUNTING_CLASSIFICATION_CONTAINER = ACCOUNTING_CLASSIFICATION + "Container";
	public static final String ACCOUNTING_CLASSIFICATION_TAXONOMY_ELEMENT_CODE = ACCOUNTING_CLASSIFICATION + TAXONOMY_ELEMENT_CODE;
	public static final String ACCOUNTING_CLASSIFICATION_TAXONOMY_CODE = ACCOUNTING_CLASSIFICATION + TAXONOMY + CODE;

	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION_SELECTION_TYPE= "AccountingClassificationAssociationSelectionType";
	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION_LABEL = "AccountingClassificationAssociationLabel";
	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION_DESCRIPTION = "AccountingClassificationAssociationDescription";
	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION_TAXONOMY_CODE = "AccountingClassificationAssociationTaxonomyCode";
	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION_SEQUENCE_NO = "AccountingClassificationAssociationSequenceNo";
	public static final String ACCOUNTING_CLASSIFICATION_ASSOCIATION_CODE = ACCOUNTING_CLASSIFICATION_ASSOCIATION + CODE;

	public static final String TAXONOMY_ASSOCIATION_MULTI_SELECT = "TaxonomyAssociationMultiSelect";
	public static final String TAXONOMY_ASSOCIATION_SELECTION_TYPE= "TaxonomyAssociationSelectionType";
	public static final String TAXONOMY_ASSOCIATION_LABEL = "TaxonomyAssociationLabel";
	public static final String TAXONOMY_ASSOCIATION_DESCRIPTION = "TaxonomyAssociationDescription";
	public static final String TAXONOMY_ASSOCIATION_TAXONOMY_CODE = "TaxonomyAssociationTaxonomyCode";
	public static final String TAXONOMY_ASSOCIATION_CODE = TAXONOMY_ASSOCIATION + CODE;

	public static final String INCLUDE_WORK_PLAN_DIAGRAM_DATA = "IncludeWorkPlanDiagramData";
	
	public static final String VOCABULARY_TAXONOMY_CLASSIFICATION_MULTISELECT_MODE = "vocabulary_taxonomy_classification_multiselect_mode";
	public static final String VOCABULARY_TAXONOMY_CLASSIFICATION_SELECTION_MODE = "vocabulary_taxonomy_classification_selection_mode";
	public static final String VOCABULARY_LANGUAGE_CODE = "vocabulary_language_code";
	public static final String VOCABULARY_FISCAL_YEAR_START = "vocabulary_fiscal_year_start_month";
	public static final String VOCABULARY_PROTECTED_AREA_CATEGORIES = "vocabulary_protected_area_categories";
	public static final String VOCABULARY_RESOURCE_TYPE = "vocabulary_resource_type";
	public static final String VOCABULARY_RESOURCE_ROLE_CODES = "vocabulary_resource_role_codes";
	public static final String VOCABULARY_MEASUREMENT_STATUS = "vocabulary_measurement_status";
	public static final String VOCABULARY_HIDDEN_TYPES = "vocabulary_hidden_types";
	public static final String VOCABULARY_DIAGRAM_FACTOR_FONT_SIZE = "vocabulary_diagram_factor_font_size";
	public static final String VOCABULARY_DIAGRAM_FACTOR_FONT_STYLE = "vocabulary_diagram_factor_font_style";
	public static final String VOCABULARY_BIODIVERSITY_TARGET_HABITAT_ASSOCIATION = "vocabulary_biodiversity_target_habitat_association";
	public static final String VOCABULARY_TARGET_STATUS = "vocabulary_target_status";
	public static final String VOCABULARY_TARGET_VIABILITY_MODE = "vocabulary_target_viability_mode";
	public static final String VOCABULARY_STRATEGY_STANDARD_CLASSIFICATION_V11_CODE = "vocabulary_strategy_standard_classification_v11_code";
	public static final String VOCABULARY_STRATEGY_STANDARD_CLASSIFICATION_V20_CODE = "vocabulary_strategy_standard_classification_v20_code";
	public static final String VOCABULARY_SCOPE_BOX_TYPE = "vocabulary_scope_box_type";
	public static final String VOCABULARY_SEVERITY_RATING = "vocabulary_severity_rating";
	public static final String VOCABULARY_SCOPE_RATING = "vocabulary_scope_rating";
	public static final String VOCABULARY_STRATEGY_IMPACT_RATING_CODE = "vocabulary_strategy_impact_rating_code";
	public static final String VOCABULARY_STRATEGY_FEASIBILITY_RATING_CODE = "vocabulary_strategy_feasibility_rating_code";
	public static final String VOCABULARY_PRIORITY_RATING_CODE = "vocabulary_priority_rating_code";
	public static final String VOCABULARY_KEA_TYPE = "vocabulary_key_ecological_attribute_type";
	public static final String VOCABULARY_IRREVERSIBILITY_CODE = "vocabulary_irreversibility_rating";
	public static final String VOCABULARY_CAUSE_STANDARD_CLASSIFICATION_V11_CODE = "vocabulary_cause_standard_classification_v11_code";
	public static final String VOCABULARY_CAUSE_STANDARD_CLASSIFICATION_V20_CODE = "vocabulary_cause_standard_classification_v20_code";
	public static final String VOCABULARY_THREAT_STRESS_RATING_CONTRIBUTION_CODE = "vocabulary_contribution_rating";
	public static final String VOCABULARY_TNC_OPERATING_UNTIS = "vocabulary_tnc_operating_units";
	public static final String VOCABULARY_TNC_TERRESTRIAL_ECO_REGION = "vocabulary_tnc_terrestrial_eco_region";
	public static final String VOCABULARY_TNC_TERRESTRIAL_ECO_REGION_SUBSET_1 = "vocabulary_tnc_terrestrial_eco_region_subset_1";
	public static final String VOCABULARY_TNC_TERRESTRIAL_ECO_REGION_SUBSET_2 = "vocabulary_tnc_terrestrial_eco_region_subset_2";
	public static final String VOCABULARY_TNC_MARINE_ECO_REGION = "vocabulary_tnc_marine_eco_region";
	public static final String VOCABULARY_TNC_FRESHWATER_ECO_REGION = "vocabulary_tnc_freshwater_eco_region";
	public static final String VOCABULARY_SHARE_OUTSIDE_ORGANIZATION = "vocabulary_share_outside_organization";
	public static final String VOCABULARY_WWF_MANAGING_OFFICES = "vocabulary_wwf_managing_offices";
	public static final String VOCABULARY_WWF_REGIONS = "vocabulary_wwf_regions";
	public static final String VOCABULARY_WWF_ECOREGIONS = "vocabulary_wwf_ecoregions";
	public static final String VOCABULARY_FOS_TRAINING_TYPE = "vocabulary_fos_training_type";
	public static final String VOCABULARY_PROGRESS_REPORT_STATUS = "vocabulary_progress_report_status";
	public static final String VOCABULARY_RESULT_REPORT_STATUS = "vocabulary_result_report_status";
	public static final String VOCABULARY_MEASUREMENT_TREND = "vocabulary_measurement_trend";
	
	public static final String VOCABULARY_EVIDENCE_CONFIDENCE_EXTERNAL = "vocabulary_evidence_confidence_external";
	public static final String VOCABULARY_EVIDENCE_CONFIDENCE_PROJECT = "vocabulary_evidence_confidence_project";
	public static final String VOCABULARY_EVIDENCE_CONFIDENCE_ASSUMPTION = "vocabulary_evidence_confidence_assumption";
	public static final String VOCABULARY_COUNTRIES = "vocabulary_countries";
	public static final String VOCABULARY_THREAT_RATING = "vocabulary_threat_rating";
	public static final String VOCABULARY_DIAGRAM_OBJECT_DATA_INCLUSION = "vocabulary_included_diagram_types";
	public static final String VOCABULARY_PLANNING_TREE_TARGET_NODE_POSITION = "vocabulary_planning_tree_target_node_position";
	public static final String VOCABULARY_THREAT_RATING_MODE = "vocabulary_threat_rating_mode";
	public static final String VOCABULARY_DASHBOARD_ROW_PROGRESS = "vocabulary_dashboard_row_progress";
	public static final String VOCABULARY_DASHBOARD_ROW_FLAGS = "vocabulary_dashboard_row_flags";
	public static final String VOCABULARY_PLANNING_TREE_OBJECTIVE_STRATEGY_NODE_ORDER = "vocabulary_planning_tree_objective_strategy_node_order";
	public static final String VOCABULARY_QUARTER_COLUMNS_VISIBILITY = "vocabulary_quarter_columns_visibility";
	public static final String VOCABULARY_DAY_COLUMNS_VISIBILITY = "vocabulary_day_columns_visibility";
	public static final String VOCABULARY_WORK_PLAN_TIME_UNIT = "vocabulary_work_plan_time_unit";
	public static final String VOCABULARY_WORKPLAN_DISPLAY_MODE = "vocabulary_work_plan_display_mode";
	public static final String VOCABULARY_RATING_SOURCE = "vocabulary_rating_source";
	public static final String VOCABULARY_CURRENCY_TYPE = "vocabulary_currency_type";
	public static final String VOCABULARY_STATUS = "vocabulary_status";
	public static final String VOCABULARY_STRATEGY_STATUS = "vocabulary_strategy_status";
	public static final String VOCABULARY_DATE = "vocabulary_date";
	public static final String VOCABULARY_MONTH = "vocabulary_month";
	public static final String VOCABULARY_YEAR = "vocabulary_year";
	public static final String VOCABULARY_TARGET_MODE = "vocabulary_target_mode";
	public static final String VOCABULARY_FACTOR_MODE = "vocabulary_factor_mode";
	public static final String VOCABULARY_PRECISION_TYPE = "vocabulary_precision_type";
	public static final String VOCABULARY_CUSTOM_ROWS = "vocabulary_custom_rows";
	public static final String VOCABULARY_CUSTOM_COLUMNS = "vocabulary_custom_columns";
	public static final String VOCABULARY_PROJECT_FOCUS = "vocabulary_tnc_project_focus";
	public static final String VOCABULARY_PROJECT_SCALE = "vocabulary_tnc_project_scale";
}
