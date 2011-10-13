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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.Audience;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.CostAllocationRule;
import org.miradi.objects.Dashboard;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.FundingSource;
import org.miradi.objects.IucnRedlistSpecies;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.objects.Organization;
import org.miradi.objects.OtherNotableSpecies;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.ReportTemplate;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.TableSettings;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.ValueOption;
import org.miradi.objects.ViewData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.project.Project;

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
	public static final int BUDGET_CATEGORY_ONE = 56;
	public static final int BUDGET_CATEGORY_TWO = 57;
	public static final int DASHBOARD = 58;
	// When you add a new type, be sure to:
	// - increment OBJECT_TYPE_COUNT
	// - IF it is a user-visible object, add a case to getUserFriendlyObjectTypeName below
	// - IF it is a top-level object, add it to getTopLevelObjectTypes below
	
	public static final int OBJECT_TYPE_COUNT = 59;

	public static String getUserFriendlyObjectTypeName(Project project, int objectType)
	{
		return EAM.fieldLabel(objectType, project.getObjectManager().getInternalObjectTypeName(objectType));
	}
	
	public static Set<Integer> getTopLevelObjectTypes()
	{
		Integer[] types = new Integer[] 
		{
			RatingCriterion.getObjectType(),
			ValueOption.getObjectType(),
			ViewData.getObjectType(),
			ProjectResource.getObjectType(),
			ProjectMetadata.getObjectType(),
			AccountingCode.getObjectType(),
			FundingSource.getObjectType(),
			ConceptualModelDiagram.getObjectType(),
			ResultsChainDiagram.getObjectType(),
			ObjectTreeTableConfiguration.getObjectType(),
			WwfProjectData.getObjectType(),
			CostAllocationRule.getObjectType(),
			ThreatStressRating.getObjectType(),
			RareProjectData.getObjectType(),
			WcsProjectData.getObjectType(),
			TncProjectData.getObjectType(),
			FosProjectData.getObjectType(),
			Organization.getObjectType(),
			WcpaProjectData.getObjectType(),
			ReportTemplate.getObjectType(),
			TaggedObjectSet.getObjectType(),
			TableSettings.getObjectType(),
			ThreatRatingCommentsData.getObjectType(),
			IucnRedlistSpecies.getObjectType(),
			OtherNotableSpecies.getObjectType(),
			Audience.getObjectType(),
			BudgetCategoryOne.getObjectType(),
			BudgetCategoryTwo.getObjectType(),
			Dashboard.getObjectType(),
		};
		
		return new HashSet<Integer>(Arrays.asList(types));
	}
}

