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

package org.miradi.xml.wcs;

public interface WcsXmlConstants
{
	public static final String PREFIX = "miradi:";
	public static final String SINGLE_SPACE = " ";
	public static final String ELEMENT_NAME = "element" + SINGLE_SPACE;
	public static final String OPTIONAL_ELEMENT = SINGLE_SPACE + "?" + SINGLE_SPACE;
	
	public static final String NAME_SPACE_VERSION = "13";
	public static final String PARTIAL_NAME_SPACE = "http://xml.miradi.org/schema/ConservationProject/";
	public static final String NAME_SPACE = PARTIAL_NAME_SPACE + NAME_SPACE_VERSION;
	public static final String XMLNS = "xmlns";
	
	public static final String CONTAINER_ELEMENT_TAG = "Container";
	public static final String POOL_ELEMENT_TAG = "Pool";
	
	public static final String ID = "Id";
	public static final String CONSERVATION_PROJECT = "ConservationProject";
	public static final String PROJECT_SUMMARY = "ProjectSummary";
	public static final String PROJECT_RESOURCE = "ProjectResource";
	public static final String ORGANIZATION = "Organization";
	public static final String PROJECT_SUMMARY_SCOPE = "ProjectSummaryScope";
	public static final String PROJECT_SUMMARY_LOCATION = "ProjectSummaryLocation";
	public static final String PROJECT_SUMMARY_PLANNING = "ProjectSummaryPlanning";
	public static final String TNC_PROJECT_DATA = "TncProjectData";
	public static final String WWF_PROJECT_DATA = "WwfProjectData";
	public static final String WCS_PROJECT_DATA = "WCSData";
	public static final String RARE_PROJECT_DATA = "RareProjectData";
	public static final String FOS_PROJECT_DATA = "FosProjectData";
	public static final String CONCEPTUAL_MODEL = "ConceptualModel";
	public static final String RESULTS_CHAIN = "ResultsChain";
	public static final String DIAGRAM_FACTOR = "DiagramFactor";
	public static final String DIAGRAM_LINK = "DiagramLink";
	public static final String BIODIVERSITY_TARGET = "BiodiversityTarget";
	public static final String HUMAN_WELFARE_TARGET = "HumanWelfareTarget";
	public static final String CAUSE = "Cause";
	public static final String STRATEGY = "Strategy";
	public static final String THREAT_REDUCTION_RESULTS = "ThreatReductionResult";
	public static final String INTERMEDIATE_RESULTS = "IntermediateResult";
	public static final String GROUP_BOX = "GroupBox";
	public static final String TEXT_BOX = "TextBox";
	public static final String SCOPE_BOX = "ScopeBox";
	public static final String KEY_ECOLOGICAL_ATTRIBUTE = "KeyEcologicalAttribute";
	public static final String STRESS = "Stress";
	public static final String SUB_TARGET = "SubTarget";
	public static final String GOAL = "Goal";
	public static final String OBJECTIVE = "Objective";
	public static final String INDICATOR = "Indicator";
	public static final String ACTIVITY = "Activity";
	public static final String METHOD = "Method";
	public static final String TASK = "Task";
	public static final String PROJECT_METADATA = "Metadata";
	public static final String RESOURCE_ASSIGNMENT = "ResourceAssignment";
	public static final String EXPENSE_ASSIGNMENT = "ExpenseAssignment";
	public static final String PROGRESS_REPORT = "ProgressReport";
	public static final String PROGRESS_PERCENT = "ProgressPercent";
	public static final String THREAT_RATING = "ThreatRating";
	public static final String THREAT = "Threat";
	public static final String TARGET = "Target";
	public static final String RATINGS = "Ratings";
	public static final String SCOPE = "Scope";
	public static final String SEVERITY = "Severity";
	public static final String IRREVERSIBILITY = "Irreversibility";
	public static final String CONTRIBUTION = "Contribution";
	public static final String COMMENTS = "Comments";
	public static final String SIMPLE_BASED_THREAT_RATING = "SimpleThreatRating";
	public static final String STRESS_BASED_THREAT_RATING = "StressBasedThreatRating";
	public static final String MEASUREMENT = "Measurement";
	public static final String ACCOUNTING_CODE = "AccountingCode";
	public static final String FUNDING_SOURCE = "FundingSource";
	public static final String PROJECT_LOCATION = "ProjectLocation";
	public static final String GEOSPATIAL_LOCATION = "GeospatialLocation";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String PROJECT_IDS_ELEMENT_NAME = "ProjectIds";
	public static final String DIAGRAM_FACTOR_ID_ELEMENT_NAME = "DiagramFactorId";
	public static final String DIAGRAM_LINK_ID_ELEMENT_NAME = "DiagramLinkId";
	public static final String SELECTED_TAGGED_OBJECT_SET_IDS = "SelectedTaggedObjectSetIds";
	public static final String DIAGRAM_POINT_ELEMENT_NAME = "DiagramPoint";
	public static final String DIAGRAM_SIZE_ELEMENT_NAME = "DiagramSize";
	public static final String X_ELEMENT_NAME = "x";
	public static final String Y_ELEMENT_NAME = "y";
	public static final String WIDTH_ELEMENT_NAME = "width";
	public static final String HEIGHT_ELEMENT_NAME = "height";
	public static final String WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME = "WrappedByDiagramFactorId";
	public static final String WRAPPED_FACTOR_ID_ELEMENT_NAME = "WrappedFactorId";
	public static final String ID_ELEMENT_NAME = "Id";
	public static final String LINKABLE_FACTOR_ID = "LinkableFactorId";
	public static final String FROM_DIAGRAM_FACTOR_ID = "FromDiagramFactorId";
	public static final String TO_DIAGRAM_FACTOR_ID = "ToDiagramFactorId";
	public static final String BEND_POINTS_ELEMENT_NAME = "BendPoints";
	public static final String GROUP_BOX_CHILDREN_IDS = "GroupBoxChildrenIds";
	public static final String GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID = "GroupedDiagramLinkIds";
	public static final String FUNDING_SOURCE_ID = "FundingSourceId";
	public static final String ACCOUNTING_CODE_ID = "AccountingCodeId";
	public static final String PROGRESS_REPORT_IDS = "ProgressReportIds";
	public static final String PROGRESS_PERCENT_IDS = "ProgressPercentIds";
	public static final String EXPENSE_IDS = "ExpenseIds";
	public static final String RELATED_THREAT_ID = "RelatedDirectThreatId";
	public static final String THREAT_ID = "ThreatId";
	public static final String RELEVANT_ACTIVITY_IDS = "RelevantActiviyIds";
	public static final String RELEVANT_STRATEGY_IDS = "RelevantStrategyIds";	
	public static final String RELEVANT_INDICATOR_IDS = "RelevantIndicatorIds";
	public static final String DATE_UNITS_EXPENSE = "DateUnitExpense";
	public static final String EXPENSE = "Expense";
	public static final String EXPENSES_DATE_UNIT = "ExpensesDateUnit";
	public static final String EXPENSES_FULL_PROJECT_TIMESPAN = "ExpensesFullProjectTimespan";
	public static final String WORK_UNITS_FULL_PROJECT_TIMESPAN = "WorkUnitsFullProjectTimespan";
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
	public static final String STYLING = "Style";
	public static final String Z_ORDER_BACK_CODE = "Back";
	public static final String STRESS_RATING = "StressRating";
	public static final String THREAT_STRESS_RATING = "ThreatStressRating";
	public static final String THREAT_TARGET_RATING = "ThreatTargetRating";
	public static final String TARGET_RATING = "TargetRating";
	public static final String OVERALL_PROJECT_THREAT_RATING = "OverallProjectThreatRating";
	public static final String IUCN_REDLIST_SPECIES = "IucnRedListSpecies";
	public static final String OTHER_NOTABLE_SPECIES = "OtherNotableSpecies";
	public static final String AUDIENCE = "Audience";
}
