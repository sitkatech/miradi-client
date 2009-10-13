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
	
	public static final String NAME_SPACE_VERSION = "7";
	public static final String PARTIAL_NAME_SPACE = "http://xml.miradi.org/schema/ConservationProject/";
	public static final String NAME_SPACE = PARTIAL_NAME_SPACE + NAME_SPACE_VERSION;
	public static final String XMLNS = "xmlns";
	
	public static final String CONTAINER_ELEMENT_TAG = "Container";
	
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
	public static final String THREAT_REDUCTION_RESULTS = "ThreatReductionResults";
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
	public static final String RESOURCE_ASSIGNMENT = "ResourceAssignment";
	public static final String EXPENSE_ASSIGNMENT = "ExpenseAssignment";
	public static final String PROGRESS_REPORT = "ProgressReport";
	public static final String PROGRESS_PERCENT = "ProgressPercent";
	public static final String THREAT_RATING = "ThreatRating";
	public static final String SIMPLE_BASED_THREAT_RATING = "SimpleThreatRating";
	public static final String STRESS_BASED_THREAT_RATING = "StressBasedThreatRating";
	public static final String MEASUREMENT = "Measurement";
	public static final String ACCOUNTING_CODE = "AccountingCode";
	public static final String FUNDING_SOURCE = "FundingSource";
}
