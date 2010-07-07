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
package org.miradi.objecthelpers;

public class ObjectType
{
	public static final int INVALID = -1;

	public static final int FIRST_OBJECT_TYPE = 1;
	public static final int RATING_CRITERION = 1;
	public static final int VALUE_OPTION = 2;
	public static final int TASK = 3;
	public static final int FACTOR = 4;
	public static final int VIEW_DATA = 5;
	public static final int FACTOR_LINK = 6;
	public static final int PROJECT_RESOURCE = 7;
	public static final int INDICATOR = 8;
	public static final int OBJECTIVE = 9;
	public static final int GOAL = 10;
	public static final int PROJECT_METADATA = 11;
	public static final int FAKE = 12;
	public static final int DIAGRAM_LINK = 13;
	public static final int RESOURCE_ASSIGNMENT = 14;
	public static final int ACCOUNTING_CODE = 15;
	public static final int FUNDING_SOURCE = 16;
	public static final int KEY_ECOLOGICAL_ATTRIBUTE = 17;
	public static final int DIAGRAM_FACTOR = 18;
	public static final int CONCEPTUAL_MODEL_DIAGRAM = 19;
	public static final int CAUSE = 20;
	public static final int STRATEGY = 21;
	public static final int TARGET = 22;
	public static final int INTERMEDIATE_RESULT = 23;
	public static final int RESULTS_CHAIN_DIAGRAM = 24;
	public static final int THREAT_REDUCTION_RESULT = 25;
	public static final int TEXT_BOX = 26;
//	public static final int SLIDE = 27;		// Never used in production
//	public static final int SLIDESHOW = 28;	// Never used in production
	public static final int OBJECT_TREE_TABLE_CONFIGURATION = 29;
	public static final int WWF_PROJECT_DATA = 30;
	public static final int COST_ALLOCATION_RULE = 31;
	public static final int MEASUREMENT = 32;
	public static final int STRESS = 33;
	public static final int THREAT_STRESS_RATING = 34;
	public static final int GROUP_BOX = 35;
	public static final int SUB_TARGET = 36;
	public static final int PROGRESS_REPORT = 37;
	public static final int RARE_PROJECT_DATA = 38;
	public static final int WCS_PROJECT_DATA = 39;
	public static final int TNC_PROJECT_DATA = 40;
	public static final int FOS_PROJECT_DATA = 41;
	public static final int ORGANIZATION = 42;
	public static final int WCPA_PROJECT_DATA = 43;
	public static final int XENODATA = 44;
	public static final int PROGRESS_PERCENT = 45;
	public static final int REPORT_TEMPLATE = 46;
	public static final int TAGGED_OBJECT_SET = 47;
	public static final int TABLE_SETTINGS = 48;
	public static final int THREAT_RATING_COMMENTS_DATA = 49;
	public static final int SCOPE_BOX = 50;
	public static final int EXPENSE_ASSIGNMENT = 51;
	public static final int HUMAN_WELFARE_TARGET = 52;
	public static final int IUCN_REDLIST_SPECIES = 53;
	public static final int OTHER_NOTABLE_SPECIES = 54;
	public static final int AUDIENCE = 55;
	public static final int CATEGORY_ONE = 56;
	public static final int CATEGORY_TWO = 57;
	// When you add a new type, be sure to:
	// - increment OBJECT_TYPE_COUNT
	// - add a case to getUserFriendlyObjectTypeName below IF it is a user-visible object
	
	public static final int OBJECT_TYPE_COUNT = 58;

	public static String getUserFriendlyObjectTypeName(int objectType)
	{
		switch(objectType)
		{
			case TASK: return "Activity/Method/Task";
			case FACTOR_LINK: return "Internal Factor Link";
			case PROJECT_RESOURCE: return "Project Resource";
			case INDICATOR: return "Indicator";
			case OBJECTIVE: return "Objective";
			case GOAL: return "Goal";
			case DIAGRAM_LINK: return "Diagram Link";
			case RESOURCE_ASSIGNMENT: return "Resource Assignment";
			case ACCOUNTING_CODE: return "Accounting Code";
			case FUNDING_SOURCE: return "Funding Source";
			case KEY_ECOLOGICAL_ATTRIBUTE: return "Key Ecological Attribute";
			case DIAGRAM_FACTOR: return "Diagram Factor";
			case CAUSE: return "Contributing Factor/Direct Threat";
			case STRATEGY: return "Strategy";
			case TARGET: return "Biodiversity Target";
			case INTERMEDIATE_RESULT: return "Intermediate Result";
			case THREAT_REDUCTION_RESULT: return "Threat Reduction Result";
			case TEXT_BOX: return "Text Box";
			case MEASUREMENT: return "Measurement";
			case STRESS: return "Stress";
			case GROUP_BOX: return "Group Box";
			case SUB_TARGET: return "Sub-Target";
			case PROGRESS_REPORT: return "Progress Report";
			case ORGANIZATION: return "Organization";
			case PROGRESS_PERCENT: return "Progress Percent";
			case SCOPE_BOX: return "Scope Box";
			case EXPENSE_ASSIGNMENT: return "Expense Entry";
			case HUMAN_WELFARE_TARGET: return "Human Welfare Target";
			case IUCN_REDLIST_SPECIES: return "IUCN Redlist Species";
			case OTHER_NOTABLE_SPECIES: return "Other Notable Species";
			case AUDIENCE: return "Audience";
			case CATEGORY_ONE: return "Budget Category One";
			case CATEGORY_TWO: return "Budget Category Two";
			default: return "Internal Object";
		}
	}
}

