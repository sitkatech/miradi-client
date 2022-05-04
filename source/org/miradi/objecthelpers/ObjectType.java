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
package org.miradi.objecthelpers;

import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.schemas.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	public static final int THREAT_STRESS_RATING_DATA = 49;
	public static final int SCOPE_BOX = 50;
	public static final int EXPENSE_ASSIGNMENT = 51;
	public static final int HUMAN_WELFARE_TARGET = 52;
	public static final int IUCN_REDLIST_SPECIES = 53;
	public static final int OTHER_NOTABLE_SPECIES = 54;
	public static final int AUDIENCE = 55;
	public static final int BUDGET_CATEGORY_ONE = 56;
	public static final int BUDGET_CATEGORY_TWO = 57;
	public static final int DASHBOARD = 58;
	public static final int XSL_TEMPLATE = 59;
	public static final int MIRADI_SHARE_PROJECT_DATA = 60;
	public static final int MIRADI_SHARE_TAXONOMY = 61;
	public static final int TAXONOMY_ASSOCIATION = 62;
	//NOTE: 63 is currently reserved for image type.
	//public static final int IMAGE = 63;
	public static final int FUTURE_STATUS = 64;
	public static final int BIOPHYSICAL_FACTOR = 65;
	public static final int BIOPHYSICAL_RESULT = 66;
	public static final int TIMEFRAME = 67;
	public static final int ACCOUNTING_CLASSIFICATION_ASSOCIATION = 68;
	public static final int METHOD = 69;
	public static final int EXTENDED_PROGRESS_REPORT = 70;
	public static final int RESULT_REPORT = 71;
	public static final int THREAT_SIMPLE_RATING_DATA = 72;
	public static final int OUTPUT = 73;
	public static final int ASSUMPTION = 74;
	public static final int ANALYTICAL_QUESTION = 75;

	// When you add a new type, be sure to:
	// - increment OBJECT_TYPE_COUNT
	// - IF it is a user-visible object, add a case to getUserFriendlyObjectTypeName below
	// - IF it is a top-level object, add it to getTopLevelObjectTypes below
	// - add appropriate entry to FieldLabels.properties file
	// - update requiresUUID (and related importer / exporter, etc.) as applicable

	public static final int OBJECT_TYPE_COUNT = 76;

	public static String getUserFriendlyObjectTypeName(Project project, int objectType)
	{
		return EAM.fieldLabel(objectType, project.getObjectManager().getInternalObjectTypeName(objectType));
	}
	
	public static Set<Integer> getTopLevelObjectTypes()
	{
		Integer[] types = new Integer[] 
		{
			RatingCriterionSchema.getObjectType(),
			ValueOptionSchema.getObjectType(),
			ViewDataSchema.getObjectType(),
			ProjectResourceSchema.getObjectType(),
			ProjectMetadataSchema.getObjectType(),
			AccountingCodeSchema.getObjectType(),
			FundingSourceSchema.getObjectType(),
			ConceptualModelDiagramSchema.getObjectType(),
			ResultsChainDiagramSchema.getObjectType(),
			ObjectTreeTableConfigurationSchema.getObjectType(),
			WwfProjectDataSchema.getObjectType(),
			CostAllocationRuleSchema.getObjectType(),
			ThreatStressRatingSchema.getObjectType(),
			RareProjectDataSchema.getObjectType(),
			WcsProjectDataSchema.getObjectType(),
			TncProjectDataSchema.getObjectType(),
			FosProjectDataSchema.getObjectType(),
			OrganizationSchema.getObjectType(),
			WcpaProjectDataSchema.getObjectType(),
			ReportTemplateSchema.getObjectType(),
			TaggedObjectSetSchema.getObjectType(),
			TableSettingsSchema.getObjectType(),
			ThreatStressRatingDataSchema.getObjectType(),
			ThreatSimpleRatingDataSchema.getObjectType(),
			IucnRedlistSpeciesSchema.getObjectType(),
			OtherNotableSpeciesSchema.getObjectType(),
			AudienceSchema.getObjectType(),
			BudgetCategoryOneSchema.getObjectType(),
			BudgetCategoryTwoSchema.getObjectType(),
			DashboardSchema.getObjectType(),
			XslTemplateSchema.getObjectType(),
			MiradiShareProjectDataSchema.getObjectType(),
			MiradiShareTaxonomySchema.getObjectType(),
			TaxonomyAssociationSchema.getObjectType(),
			AccountingClassificationAssociationSchema.getObjectType(),
		};
		
		return new HashSet<Integer>(Arrays.asList(types));
	}

	public static boolean requiresUUID(int objectType)
	{
		if (objectType == ObjectType.FAKE)
			return false;

		if (objectType == ObjectType.RATING_CRITERION)
			return false;

		if (objectType == ObjectType.FACTOR_LINK)
			return false;

		if (objectType == ObjectType.TABLE_SETTINGS)
			return false;

		if (objectType == ObjectType.THREAT_STRESS_RATING_DATA)
			return false;

		if (objectType == ObjectType.THREAT_SIMPLE_RATING_DATA)
			return false;

		if (objectType == ObjectType.VALUE_OPTION)
			return false;

		if (objectType == ObjectType.FOS_PROJECT_DATA)
			return false;

		if (objectType == ObjectType.MIRADI_SHARE_PROJECT_DATA)
			return false;

		if (objectType == ObjectType.MIRADI_SHARE_TAXONOMY)
			return false;

		if (objectType == ObjectType.TAXONOMY_ASSOCIATION)
			return false;

		if (objectType == ObjectType.ACCOUNTING_CLASSIFICATION_ASSOCIATION)
			return false;

		if (objectType == ObjectType.PROJECT_METADATA)
			return false;

		if (objectType == ObjectType.RARE_PROJECT_DATA)
			return false;

		if (objectType == ObjectType.TNC_PROJECT_DATA)
			return false;

		if (objectType == ObjectType.WCPA_PROJECT_DATA)
			return false;

		if (objectType == ObjectType.WCS_PROJECT_DATA)
			return false;

		if (objectType == ObjectType.WWF_PROJECT_DATA)
			return false;

		return true;
	}
}

