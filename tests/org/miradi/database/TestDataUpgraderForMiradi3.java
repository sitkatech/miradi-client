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
package org.miradi.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.martus.util.DirectoryUtils;
import org.miradi.database.migrations.ConvertHighLevelEstimatesIntoAssignments;
import org.miradi.database.migrations.EnsureNoMoreThanOneXenodataMigration;
import org.miradi.database.migrations.MigrationsForMiradi3;
import org.miradi.database.migrations.ShareSameLabeledScopeBoxesMigration;
import org.miradi.database.migrations.UpdateTncOpertingUnitMigration;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.ResourceAssignment;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class TestDataUpgraderForMiradi3 extends AbstractMigrationTestCase
{
	public TestDataUpgraderForMiradi3(String name)
	{
		super(name);
	}

	//FIXME urgent - these tests for migration are commented out and waiting to be activated 
	//after the xenodata (referred to by metadata) migration is comleted
	//Uncomment
//	public void testRemoveEmptyMethodsForEmptyProject() throws Exception
//	{
//		DataUpgrader.initializeStaticDirectory(tempDirectory);
//		MigrationsForMiradi3.upgradeToVersion5X();
//	}
//	
//	public void testRemoveEmptyMethodsForProjectWithoutConProId() throws Exception
//	{
//		File jsonDir = createJsonDir();
//		final int EXPECTED_NUMBER_OF_METHODS_DELETED = 0;
//		verifyDeleteEmptyMethods(jsonDir, EXPECTED_NUMBER_OF_METHODS_DELETED);
//	}
//	
//	public void testRemoveEmptyMethodsForProjectWithConProId() throws Exception
//	{
//		File jsonDir = createJsonDir();
//		
//		String xenoDataStringWithProjectId = "{\"AssignmentIds\":\"\",\"TimeStampModified\":\"1266952560931\",\"ExpenseRefs\":\"\",\"Label\":\"\",\"Id\":200,\"ProgressReportRefs\":\"\",\"ProjectId\":\"726\"}";
//		final int XENODATA_TYPE = 44;
//		createAndPopulateObjectDir(jsonDir, XENODATA_TYPE, xenoDataStringWithProjectId);
//		
//		String projectMetadataStringWithXenodata = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"2010-02-23\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"Arial\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"1\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"12\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"ProgressReportRefs\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1266952560987\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"{\\\"StringRefMap\\\":{\\\"ConPro\\\":\\\"{\\\\\\\"ObjectType\\\\\\\":44,\\\\\\\"ObjectId\\\\\\\":200}\\\"}}\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
//		final int PROJECT_METADATA_TYPE = 11;
//		createAndPopulateObjectDir(jsonDir, PROJECT_METADATA_TYPE, projectMetadataStringWithXenodata);
//		
//		final int EXPECTED_NUMBER_OF_METHODS_DELETED = 1;
//		verifyDeleteEmptyMethods(jsonDir, EXPECTED_NUMBER_OF_METHODS_DELETED);
//	}
//	
//	private void verifyDeleteEmptyMethods(File jsonDir, final int EXPECTED_NUMBER_OF_METHODS_DELETED) throws Exception
//	{
//		String indictorWithMethods = "{\"ThresholdDetails\":\"\",\"RatingSource\":\"\",\"FutureStatusDetail\":\"\",\"IndicatorThresholds\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"FutureStatusSummary\":\"\",\"ExpenseRefs\":\"\",\"ShortLabel\":\"\",\"MeasurementRefs\":\"\",\"Priority\":\"\",\"Detail\":\"\",\"FutureStatusRating\":\"\",\"TaskIds\":\"{\\\"Ids\\\":[29,30,31,32,33,34,35,36]}\",\"TimeStampModified\":\"1266951177581\",\"FutureStatusDate\":\"\",\"Label\":\"\",\"Id\":118,\"FutureStatusComment\":\"\",\"ProgressReportRefs\":\"\",\"ViabilityRatingsComment\":\"\"}";
//		
//		String emptyTask = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":28,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		
//		String emptyMethod = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":29,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		String methodWithDetails = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"Some Details\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":30,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		String methodWithShortLabel = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"Some Short Label\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":31,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		String methodWithLabel = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":32,\"Label\":\"SomeLabel\",\"ProgressReportRefs\":\"\"}";
//		String methodWithProgressReport = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":33,\"Label\":\"\",\"ProgressReportRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":37,\\\"ObjectId\\\":29}]}\"}";
//		String methodWithResourceAssignment = "{\"AssignmentIds\":\"{\\\"Ids\\\":[115]}\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":34,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		String methodWithExpenseAssignment = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":51,\\\"ObjectId\\\":116}]}\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":35,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		String methodWithSubTask = "{\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"SubtaskIds\":\"{\\\"Ids\\\":[117]}\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1266947025641\",\"Id\":36,\"Label\":\"\",\"ProgressReportRefs\":\"\"}";
//		
//		final int INDICATOR_TYPE = 8;
//		createAndPopulateObjectDir(jsonDir, INDICATOR_TYPE, indictorWithMethods);
//		
//		final int TASK_TYPE = 3;
//		final String[] methodStrings = new String[]{emptyMethod, methodWithDetails, methodWithShortLabel, methodWithLabel, methodWithProgressReport, methodWithResourceAssignment, methodWithExpenseAssignment, methodWithSubTask, };
//		final String[] taskStrings = new String[]{emptyTask, };
//		Vector<String> allTasks = new Vector<String>();
//		allTasks.addAll(Arrays.asList(methodStrings));
//		allTasks.addAll(Arrays.asList(taskStrings));
//		
//		int[] taskIds = createAndPopulateObjectDir(jsonDir, TASK_TYPE, allTasks.toArray(new String[0]));
//		
//		DataUpgrader.initializeStaticDirectory(tempDirectory);
//		MigrationsForMiradi3.upgradeToVersion5X();
//	
//		File taskDir = DataUpgrader.getObjectsDir(jsonDir, TASK_TYPE);
//		File manifestFile = new File(taskDir, "manifest");
//		assertTrue("manifest file could not be found?", manifestFile.exists());
//	
//		int expectedTaskCountAfterMigration = taskStrings.length + methodStrings.length - EXPECTED_NUMBER_OF_METHODS_DELETED;
//		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
//		assertEquals("manifest has wrong key count?", expectedTaskCountAfterMigration, manifestObject.size());
//		if (EXPECTED_NUMBER_OF_METHODS_DELETED > 1)
//			assertFalse("empty method was not deleted?", manifestObject.has(new BaseId(29)));
//		
//		assertTrue("empty task was deleted?", manifestObject.has(new BaseId(28)));
//	}
	
	public void testEnsureProjectMetadataRefersToCorrectXenodaForEmptyProject() throws Exception
	{
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion56();
	}
	
	public void testEnsureProjectMetadataWithCorrectXenodataReferenceRefersToCorrectXenoda() throws Exception
	{
		String xenodataIdMatchesIdInProjectMetadata = "{\"AssignmentIds\":\"\",\"TimeStampModified\":\"1266952560931\",\"ExpenseRefs\":\"\",\"Label\":\"\",\"Id\":19,\"ProgressReportRefs\":\"\",\"ProjectId\":\"726\"}";
		verifyProjectMetadataRefersToExistingXenodata(xenodataIdMatchesIdInProjectMetadata);
	}
	
	public void testEnsureProjectMetadataWithIncorrectXenodataReferenceRefersToCorrectXenoda() throws Exception
	{
		String xenodataIdDoesNotMatchIdInProjectMetadata = "{\"AssignmentIds\":\"\",\"TimeStampModified\":\"1266952560931\",\"ExpenseRefs\":\"\",\"Label\":\"\",\"Id\":200,\"ProgressReportRefs\":\"\",\"ProjectId\":\"726\"}";
		verifyProjectMetadataRefersToExistingXenodata(xenodataIdDoesNotMatchIdInProjectMetadata);
	}
	
	public void testEnsureProjecMetadataWithoutXenodataTagRefersToCorrectXenodata() throws Exception
	{
		String projectMetadataStringWithoutXenodataTag = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"2010-02-23\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"Arial\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"1\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"12\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"ProgressReportRefs\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1266952560987\",\"ProjectAreaNote\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		String xenodataIdDoesNotMatchIdInProjectMetadata = "{\"AssignmentIds\":\"\",\"TimeStampModified\":\"1266952560931\",\"ExpenseRefs\":\"\",\"Label\":\"\",\"Id\":200,\"ProgressReportRefs\":\"\",\"ProjectId\":\"726\"}";
		
		verifyProjectMetadataRefersToExistingXenodata(projectMetadataStringWithoutXenodataTag, xenodataIdDoesNotMatchIdInProjectMetadata);
	}

	private void verifyProjectMetadataRefersToExistingXenodata(String xenoDataStringWithProjectId) throws Exception
	{
		String projectMetadataStringWithXenodataId19 = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"2010-02-23\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"Arial\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"1\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"12\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"ProgressReportRefs\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1266952560987\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"{\\\"StringRefMap\\\":{\\\"ConPro\\\":\\\"{\\\\\\\"ObjectType\\\\\\\":44,\\\\\\\"ObjectId\\\\\\\":19}\\\"}}\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		verifyProjectMetadataRefersToExistingXenodata(projectMetadataStringWithXenodataId19, xenoDataStringWithProjectId);
	}
	
	private void verifyProjectMetadataRefersToExistingXenodata(String projectMetadataString, String xenoDataStringWithProjectId) throws Exception
	{
		File jsonDir = createJsonDir();
		final int PROJECT_METADATA_TYPE = 11;
		int[] projectMetadataRawIds = createAndPopulateObjectDir(jsonDir, PROJECT_METADATA_TYPE, projectMetadataString);

		final int XENODATA_TYPE = 44;
		createAndPopulateObjectDir(jsonDir, XENODATA_TYPE, xenoDataStringWithProjectId);
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion56();
		
		EnhancedJsonObject projectMetadataJson = getObjectFileAsJson(jsonDir, PROJECT_METADATA_TYPE, projectMetadataRawIds[0]);
		String xenodataStringRefMapAsString = projectMetadataJson.getString("XenodataRefs");
		StringRefMap map = new StringRefMap(xenodataStringRefMapAsString);
		ORef xenodataRef = map.getValue("ConPro");
		
		File xenodataDir = DataUpgrader.getObjectsDir(jsonDir, XENODATA_TYPE);
		File xenodataManifestFile = new File(xenodataDir, "manifest");
		assertTrue("xenodata manifest file could not be found?", xenodataManifestFile.exists());
		
		ObjectManifest xenodataManifestObject = new ObjectManifest(JSONFile.read(xenodataManifestFile));
		BaseId[] xenodataIdsArray = xenodataManifestObject.getAllKeys();
		Vector<BaseId> xenodataIds = new Vector(Arrays.asList(xenodataIdsArray));
		if (!xenodataIds.isEmpty())
			assertTrue("Xenodata ref in project metadata is incorrect?", xenodataIds.contains(xenodataRef.getObjectId()));
	}
	
	public void testFixFactorLinkBidiFalseValues() throws Exception
	{
		String factorLinkWithCorrectBidiFalseValue = "{\"FromRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":31}\",\"TimeStampModified\":\"1250886880018\",\"ToRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":25}\",\"Label\":\"\",\"Id\":42,\"BidirectionalLink\":\"\"}";
		String factorLinkWithIncorrectBidiFalseValue = "{\"FromRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":25}\",\"TimeStampModified\":\"1250886897871\",\"ToRef\":\"{\\\"ObjectType\\\":26,\\\"ObjectId\\\":39}\",\"Label\":\"\",\"Id\":43,\"BidirectionalLink\":\"0\"}";
		String factorLinkWithBidiTrueValue = "{\"FromRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":23}\",\"TimeStampModified\":\"1250886897871\",\"ToRef\":\"{\\\"ObjectType\\\":26,\\\"ObjectId\\\":39}\",\"Label\":\"\",\"Id\":44,\"BidirectionalLink\":\"1\"}";
		
		File jsonDir = createJsonDir();
		
		final int FACTOR_LINK_TYPE = 6;
		File factorLinkDir = DataUpgrader.getObjectsDir(jsonDir, FACTOR_LINK_TYPE);
		final String[] factorLinkStrings = new String[]{factorLinkWithCorrectBidiFalseValue, factorLinkWithIncorrectBidiFalseValue, factorLinkWithBidiTrueValue, };
		int[] factorLinkIds = createAndPopulateObjectDir(jsonDir, FACTOR_LINK_TYPE, factorLinkStrings);
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion55();
	
		File manifestFile = new File(factorLinkDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		
		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		assertEquals("manifest has wrong key count?", factorLinkStrings.length, manifestObject.size());
		
		verifyBidirectionality(factorLinkDir, factorLinkIds[0], "");
		verifyBidirectionality(factorLinkDir, factorLinkIds[1], "");
		verifyBidirectionality(factorLinkDir, factorLinkIds[2], "1");
	}

	private void verifyBidirectionality(File factorLinkDir, int factorLinkId, String expectedBidiValue) throws Exception
	{
		File factorLinkFile = new File(factorLinkDir, Integer.toString(factorLinkId));
		EnhancedJsonObject factorLinkJson = new EnhancedJsonObject(readFile(factorLinkFile));
		assertEquals("Wrong bidi value?", expectedBidiValue, factorLinkJson.getString("BidirectionalLink"));
	}
	
	public void testNewTpyesAgainstEmptyProject() throws Exception
	{
		createBlankProjectMetadata();		
		createBlankRareProjectData();
	
		File jsonDir = createJsonDir();
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":10}");
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion54();
		
		final int IUCN_REDLIST_SPECIES_TYPE = 53;
		assertFalse("IUCN RedList Species dir should not have been created", DataUpgrader.getObjectsDir(jsonDir, IUCN_REDLIST_SPECIES_TYPE).exists());
		
		final int OTHER_NOTABLE_SPECIES = 54;
		assertFalse("Other Notable Species dir should not have been created", DataUpgrader.getObjectsDir(jsonDir, OTHER_NOTABLE_SPECIES).exists());
		
		final int AUDIENCE_TYPE = 55;
		assertFalse("Audience dir should not have been created", DataUpgrader.getObjectsDir(jsonDir, AUDIENCE_TYPE).exists());
	}
	
	public void testIucnRedlistSpeciesList() throws Exception
	{
		EnhancedJsonObject projectMetadataJson = createProjectMetadata();		
		createRareProjectData();
		
		final int IUCN_REDLIST_SPECIES_TYPE = 53;
		String expectedValue = projectMetadataJson.optString("RedListSpecies");
		verifyNewType(IUCN_REDLIST_SPECIES_TYPE, expectedValue);
	}
	
	public void testOtherNotableSpeciesList() throws Exception
	{
		EnhancedJsonObject projectMetadataJson = createProjectMetadata();		
		createRareProjectData();
		
		final int OTHER_NOTABLE_SPECIES = 54;
		String expectedValue = projectMetadataJson.optString("OtherNotableSpecies");
		verifyNewType(OTHER_NOTABLE_SPECIES, expectedValue);
	}
	
	public void testAudienceList() throws Exception
	{
		createProjectMetadata();		
		EnhancedJsonObject rareProjectDataJson = createRareProjectData();
		
		final int AUDIENCE_TYPE = 55;
		String expectedValue = rareProjectDataJson.optString("Audience");
		verifyNewType(AUDIENCE_TYPE, expectedValue);
	}
	
	private void verifyNewType(int newListType, String expectedValue) throws Exception
	{
		File jsonDir = createJsonDir();
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":10}");
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion54();
		
		File newTypeDir = DataUpgrader.getObjectsDir(jsonDir, newListType);
		assertTrue("new object type dir was not created?", newTypeDir.exists());
		File manifestFile = new File(newTypeDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		assertEquals("manifest has wrong key count?", 1, manifestObject.size());
		
		BaseId[] newlyCreatedObjectIds = manifestObject.getAllKeys();
		File newTypeFile = new File(newTypeDir, newlyCreatedObjectIds[0].toString());
		assertTrue("IUCN RedList Species file was not created?", newTypeFile.exists());
		EnhancedJsonObject newTypeJson = new EnhancedJsonObject(readFile(newTypeFile));
		String label = newTypeJson.optString("Label");
		assertEquals("wrong lanel?", expectedValue, label);
	}
	
	private EnhancedJsonObject createRareProjectData() throws Exception
	{
		final int RARE_PROJECT_DATA_TYPE = 38;
		String rareProjectDataWithAudience = "{\"CampaignTheoryOfChange\":\"\",\"BiodiversityHotspots\":\"\",\"SummaryOfKeyMessages\":\"\",\"ExpenseRefs\":\"\",\"FlagshipSpeciesDetail\":\"\",\"ThreatsAddressedNotes\":\"\",\"MainActivitiesNotes\":\"\",\"NumberOfCommunitiesInCampaignArea\":\"\",\"RegionalDirectorNotes\":\"\",\"MonitoringPartnerContactNotes\":\"\",\"BingoPartnerContactNotes\":\"\",\"Audience\":\"Village folks on the east side\",\"ProgressReportRefs\":\"\",\"FlagshipSpeciesCommonName\":\"\",\"FlagshipSpeciesScientificName\":\"\",\"CourseManagerNotes\":\"\",\"CampaignManagerNotes\":\"\",\"AssignmentIds\":\"\",\"ThreatReductionObjectiveNotes\":\"\",\"ThreatReductionPartnerContactNotes\":\"\",\"LocalPartnerContactNotes\":\"\",\"Cohort\":\"\",\"CampaignSlogan\":\"\",\"TimeStampModified\":\"1263290478834\",\"MonitoringObjectiveNotes\":\"\",\"Label\":\"\",\"Id\":13}";
		
		return createSingletonContainer(RARE_PROJECT_DATA_TYPE, rareProjectDataWithAudience);
	}
	
	private void createBlankRareProjectData() throws Exception
	{
		final int RARE_PROJECT_DATA_TYPE = 38;
		String rareProjectDataWithAudience = "{\"CampaignTheoryOfChange\":\"\",\"BiodiversityHotspots\":\"\",\"SummaryOfKeyMessages\":\"\",\"ExpenseRefs\":\"\",\"FlagshipSpeciesDetail\":\"\",\"ThreatsAddressedNotes\":\"\",\"MainActivitiesNotes\":\"\",\"NumberOfCommunitiesInCampaignArea\":\"\",\"RegionalDirectorNotes\":\"\",\"MonitoringPartnerContactNotes\":\"\",\"BingoPartnerContactNotes\":\"\",\"Audience\":\"\",\"ProgressReportRefs\":\"\",\"FlagshipSpeciesCommonName\":\"\",\"FlagshipSpeciesScientificName\":\"\",\"CourseManagerNotes\":\"\",\"CampaignManagerNotes\":\"\",\"AssignmentIds\":\"\",\"ThreatReductionObjectiveNotes\":\"\",\"ThreatReductionPartnerContactNotes\":\"\",\"LocalPartnerContactNotes\":\"\",\"Cohort\":\"\",\"CampaignSlogan\":\"\",\"TimeStampModified\":\"1263290478834\",\"MonitoringObjectiveNotes\":\"\",\"Label\":\"\",\"Id\":13}";
		createSingletonContainer(RARE_PROJECT_DATA_TYPE, rareProjectDataWithAudience);
	}

	private EnhancedJsonObject createProjectMetadata() throws Exception
	{
		final int PROJECT_METADATA_TYPE = 11;
		String projectMetadataJsonString = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"Arial\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"12\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"Some RedList species like wild salmon\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"ProgressReportRefs\":\"\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"Some Other Notable species like white tigers\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1263290457814\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		
		return createSingletonContainer(PROJECT_METADATA_TYPE, projectMetadataJsonString);
	}
	
	private void createBlankProjectMetadata() throws Exception
	{
		final int PROJECT_METADATA_TYPE = 11;
		String projectMetadataJsonString = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"Arial\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"12\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"\",\"ProgressReportRefs\":\"\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1263290457814\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		createSingletonContainer(PROJECT_METADATA_TYPE, projectMetadataJsonString);
	}
	
	private EnhancedJsonObject createSingletonContainer(int singletonObjectType, String jsonString) throws Exception
	{
		File jsonDir = createJsonDir();
		File legacyFieldContainerDir = DataUpgrader.getObjectsDir(jsonDir, singletonObjectType);
		int[] legacyFieldContainerIds = createAndPopulateObjectDir(jsonDir, singletonObjectType, new String[]{jsonString, });
		File legacyFieldContainerFile = new File(legacyFieldContainerDir, Integer.toString(legacyFieldContainerIds[0]));
		
		return new EnhancedJsonObject(readFile(legacyFieldContainerFile));
	}
	
	public void testCorrectHumanWelfareTargetScopeBoxColorCode() throws Exception
	{
		verifyEmptyProject();
		String scopeBoxWithBadScopeBoxColorCode = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253735690524\",\"Id\":712,\"Label\":\"New Scope Box\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"darkGray\"}";
		String scopeBoxWithNoScopeBoxColorCode = "{\"Text\":\"The marine resources of Eastern Bay Village's traditional fishing grounds.\",\"Comments\":\"\",\"Id\":\"713\",\"Label\":\"Project Scope :Our Marine Resources\"}";
		String scopeBoxWithEmptyScopeBoxColorCode = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253735690524\",\"Id\":714,\"Label\":\"New Scope Box\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"\"}";
		String scopeBoxWithCorrectScopeBoxColorCode = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253735690524\",\"Id\":715,\"Label\":\"New Scope Box\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"HumanWelfareTargetBrown\"}";

		verifyScopeBoxColorCodeCorrection(scopeBoxWithBadScopeBoxColorCode, "HumanWelfareTargetBrown");	
		verifyScopeBoxColorCodeCorrection(scopeBoxWithNoScopeBoxColorCode, "");
		verifyScopeBoxColorCodeCorrection(scopeBoxWithEmptyScopeBoxColorCode, "");
		verifyScopeBoxColorCodeCorrection(scopeBoxWithCorrectScopeBoxColorCode, "HumanWelfareTargetBrown");
	}
	
	private void verifyEmptyProject() throws Exception
	{
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion53();
	}

	private void verifyScopeBoxColorCodeCorrection(String scopeBoxString, String expectedColorCode) throws Exception
	{
		File jsonDir = createJsonDir();
		final int SCOPE_BOX_TYPE = 50;
		File scopeBoxDir = DataUpgrader.getObjectsDir(jsonDir, SCOPE_BOX_TYPE);
		if (scopeBoxDir.exists())
			DirectoryUtils.deleteEntireDirectoryTree(scopeBoxDir);
		
		int[] scopeBoxIds = createAndPopulateObjectDir(jsonDir, SCOPE_BOX_TYPE, new String[]{scopeBoxString, });
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion53();
		
		int onlyIdAsInt = scopeBoxIds[0];
		File scopeBoxFile = new File(scopeBoxDir, Integer.toString(onlyIdAsInt));
		
		EnhancedJsonObject scopeBoxJson = new EnhancedJsonObject(readFile(scopeBoxFile));
		String scopeBoxColorCode = scopeBoxJson.optString("ScopeBoxColorCode");
		assertEquals("Scope Box color code was not changed?", expectedColorCode, scopeBoxColorCode);
	}

	public void testEnsureNoMoreThanOneXenodataExists() throws Exception
	{
		String[] noXenodataObjects = new String[]{};
		verifyXenodata(noXenodataObjects, "");
		
		String xenodataString = "{\"WhenOverride\":\"\",\"TimeStampModified\":\"1251135048078\",\"BudgetCostOverride\":\"\",\"BudgetCostMode\":\"\",\"Label\":\"\",\"Id\":3927,\"WhoOverrideRefs\":\"\",\"ProjectId\":\"1463\"}";
		verifyXenodata(new String[]{xenodataString, }, "1463");
		
		String xenodataWithSameProjectIdString = "{\"WhenOverride\":\"\",\"TimeStampModified\":\"1251139509421\",\"BudgetCostOverride\":\"\",\"BudgetCostMode\":\"\",\"Label\":\"\",\"Id\":3928,\"WhoOverrideRefs\":\"\",\"ProjectId\":\"1463\"}";
		verifyXenodata(new String[]{xenodataString, xenodataWithSameProjectIdString, }, "1463");
		
		String xenodataWithDifferentProjectIdString = "{\"WhenOverride\":\"\",\"TimeStampModified\":\"1251139509421\",\"BudgetCostOverride\":\"\",\"BudgetCostMode\":\"\",\"Label\":\"\",\"Id\":3929,\"WhoOverrideRefs\":\"\",\"ProjectId\":\"9999999\"}";
		verifyXenodata(new String[]{xenodataString, xenodataWithSameProjectIdString, xenodataWithDifferentProjectIdString}, "");
	}
	
	private void verifyXenodata(String[] xenodataStrings, String expectedProjectId) throws Exception
	{
		File jsonDir = createJsonDir();
		final int XENODATA_TYPE = 44;
		File xenodataDir = DataUpgrader.getObjectsDir(jsonDir, XENODATA_TYPE);
		if (xenodataDir.exists())
			DirectoryUtils.deleteEntireDirectoryTree(xenodataDir);
		
		createAndPopulateObjectDir(jsonDir, XENODATA_TYPE, xenodataStrings);
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		Vector<EnhancedJsonObject> leftOverXenodata = EnsureNoMoreThanOneXenodataMigration.enureNoMoreThanOneXenodata();
	
		File manifestFile = new File(xenodataDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		assertEquals("manifest has wrong key count?", leftOverXenodata.size(), manifestObject.size());
		
		if (leftOverXenodata.size() == 1)
		{
			EnhancedJsonObject exnodataJson = leftOverXenodata.iterator().next();
			String projectId = exnodataJson.getString("ProjectId");
			assertEquals("wrong project id?", expectedProjectId, projectId);
		}
		
		if (leftOverXenodata.size() > 1)
		{
			assertEquals("xenodata was deleted", xenodataStrings.length, leftOverXenodata.size());
		}
	}

	public void testMoveTncProjectAreaSizeToProjectMetadata() throws Exception
	{
		String projectMetadataWithBothAreaSizes = "{\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"BudgetCostMode\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"DiagramFontSize\":\"\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"WhoOverrideRefs\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"SocialContext\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"OtherOrgRelatedProjects\":\"\",\"ProjectScope\":\"\",\"TNC.SizeInHectares\":\"456\",\"TNC.WorkbookVersionNumber\":\"\",\"BudgetCostOverride\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"WhenOverride\":\"\",\"ShortProjectScope\":\"\",\"HumanPopulationNotes\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1258406972469\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"945\"}";
		verifyMigratingProjectAreaSizeData(projectMetadataWithBothAreaSizes, 945);
		
		String projectMetadataWithOnlyTncAreaSize = "{\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"BudgetCostMode\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"DiagramFontSize\":\"\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"WhoOverrideRefs\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"SocialContext\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"OtherOrgRelatedProjects\":\"\",\"ProjectScope\":\"\",\"TNC.SizeInHectares\":\"456\",\"TNC.WorkbookVersionNumber\":\"\",\"BudgetCostOverride\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"WhenOverride\":\"\",\"ShortProjectScope\":\"\",\"HumanPopulationNotes\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1258406972469\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		verifyMigratingProjectAreaSizeData(projectMetadataWithOnlyTncAreaSize, 456);
		
		String projectMetadataWithNoAreaSizeValues = "{\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"BudgetCostMode\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"DiagramFontSize\":\"\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"WhoOverrideRefs\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"SocialContext\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"OtherOrgRelatedProjects\":\"\",\"ProjectScope\":\"\",\"TNC.SizeInHectares\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"BudgetCostOverride\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"WhenOverride\":\"\",\"ShortProjectScope\":\"\",\"HumanPopulationNotes\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1258406972469\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		verifyMigratingProjectAreaSizeData(projectMetadataWithNoAreaSizeValues, 0);
		
		String projectMetadataWithMissingTags = "{\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"BudgetCostMode\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"DiagramFontSize\":\"\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"WhoOverrideRefs\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"SocialContext\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"OtherOrgRelatedProjects\":\"\",\"ProjectScope\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"BudgetCostOverride\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"WhenOverride\":\"\",\"ShortProjectScope\":\"\",\"HumanPopulationNotes\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1258406972469\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\"}";
		verifyMigratingProjectAreaSizeData(projectMetadataWithMissingTags, 0);
	}

	private void verifyMigratingProjectAreaSizeData(String projectMetadata, int expectedAreaSize) throws Exception
	{
		File jsonDir = createJsonDir();
		final int PROJECT_METADATA_TYPE = 11;
		File projectMetadataDir = DataUpgrader.getObjectsDir(jsonDir, PROJECT_METADATA_TYPE);
		if (projectMetadataDir.exists())
			DirectoryUtils.deleteEntireDirectoryTree(projectMetadataDir);
			
			
		int[] projectMetadataIds = createAndPopulateObjectDir(jsonDir, PROJECT_METADATA_TYPE, new String[]{projectMetadata});
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion51();
		
		int onlyProjectMetadataId = projectMetadataIds[0];
		File projectMetadataJsonFile = new File(projectMetadataDir, Integer.toString(onlyProjectMetadataId));		

		EnhancedJsonObject projectMetadataJson = new EnhancedJsonObject(readFile(projectMetadataJsonFile));
		int tncSizeInHectars = projectMetadataJson.optInt("ProjectArea");
		assertEquals("wrong project area size?", expectedAreaSize, tncSizeInHectars);
	}
	
	public void testRemoveNonExistingRelatedThreatRefFromThreatReductionResult() throws Exception
	{
		File jsonDir = createJsonDir();
		String tRRWithoutRelatedThreat = "{\"ObjectiveIds\":\"\",\"RelatedDirectThreatRef\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"Threat Reduction Result\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1257363433052\",\"Id\":44,\"Label\":\"TRR without related threat\",\"ProgressReportRefs\":\"\"}";
		String tRRWithNonExistingRelatedThreat = "{\"ObjectiveIds\":\"\",\"RelatedDirectThreatRef\":\"{\\\"ObjectType\\\":20,\\\"ObjectId\\\":99999}\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"Threat Reduction Result\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1257363443471\",\"Id\":45,\"Label\":\"TRR with non existing related threat\",\"ProgressReportRefs\":\"\"}";
		String tRRWithExistingRelatedThreat = "{\"ObjectiveIds\":\"\",\"RelatedDirectThreatRef\":\"{\\\"ObjectType\\\":20,\\\"ObjectId\\\":35}\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"Threat Reduction Result\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1257363458038\",\"Id\":42,\"Label\":\"TRR with existing related threat\",\"ProgressReportRefs\":\"\"}";
		
		String threatRelatedToTRR = "{\"ObjectiveIds\":\"{\\\"Ids\\\":[37]}\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Factor\",\"ExpenseRefs\":\"\",\"TaxonomyCode\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1257363377280\",\"Id\":35,\"Label\":\"New Factor\",\"ProgressReportRefs\":\"\",\"IsDirectThreat\":\"1\"}";
		
		
		final int THREAT_REDUCTION_RESULT_TYPE = 25;
		int[] threatReductionResultIds = createAndPopulateObjectDir(jsonDir, THREAT_REDUCTION_RESULT_TYPE, new String[]{tRRWithoutRelatedThreat, tRRWithNonExistingRelatedThreat, tRRWithExistingRelatedThreat, });
	
		final int CAUSE_TYPE = 20;
		createAndPopulateObjectDir(jsonDir, CAUSE_TYPE, new String[]{threatRelatedToTRR, });
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion50();
		
		verifyRelatedThreatId(jsonDir, THREAT_REDUCTION_RESULT_TYPE, threatReductionResultIds[0], BaseId.INVALID.asInt());
		verifyRelatedThreatId(jsonDir, THREAT_REDUCTION_RESULT_TYPE, threatReductionResultIds[1], BaseId.INVALID.asInt());
		verifyRelatedThreatId(jsonDir, THREAT_REDUCTION_RESULT_TYPE, threatReductionResultIds[2], 35);
	}

	private void verifyRelatedThreatId(File jsonDir, final int THREAT_REDUCTION_RESULT_TYPE, int threatReductionResultId, int expectedRelatedThreatId) throws Exception
	{
		File threatReductionResultDir = DataUpgrader.getObjectsDir(jsonDir, THREAT_REDUCTION_RESULT_TYPE);
		File threatReductionResultJsonFile = new File(threatReductionResultDir, Integer.toString(threatReductionResultId));		

		EnhancedJsonObject threatReductionResultJson = new EnhancedJsonObject(readFile(threatReductionResultJsonFile));
		ORef relatedThreatRef = threatReductionResultJson.getRef("RelatedDirectThreatRef");
		assertEquals("wrong related threat id?", expectedRelatedThreatId, relatedThreatRef.getObjectId().asInt());
	}
	
	public void testShareSameLabeledScopeBoxesAcrossAllDiagramsForEmptyProject() throws Exception
	{
		createJsonDir();
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		ShareSameLabeledScopeBoxesMigration.shareSameLabeledScopeBoxesAcrossAllDiagrams();		
	}
	
	public void testShareSameLabeledScopeBoxesAcrossAllDiagrams() throws Exception
	{
		File jsonDir = createJsonDir();
		
		String conceptualModel = "{\"SelectedTaggedObjectSetRefs\":\"\",\"Detail\":\"\",\"AssignmentIds\":\"\",\"DiagramFactorIds\":\"{\\\"Ids\\\":[33]}\",\"TimeStampModified\":\"1253125566109\",\"DiagramFactorLinkIds\":\"\",\"ExpenseRefs\":\"\",\"HiddenTypes\":\"\",\"Label\":\"[Main Diagram]\",\"Id\":9,\"ShortLabel\":\"\",\"ProgressReportRefs\":\"\"}";
		
		String resultsChainDiagramObject1 = "{\"SelectedTaggedObjectSetRefs\":\"\",\"Detail\":\"\",\"AssignmentIds\":\"\",\"DiagramFactorIds\":\"{\\\"Ids\\\":[42,44]}\",\"TimeStampModified\":\"1253125709324\",\"DiagramFactorLinkIds\":\"\",\"ExpenseRefs\":\"\",\"HiddenTypes\":\"\",\"Label\":\"[New Results Chain]\",\"Id\":38,\"ShortLabel\":\"\",\"ProgressReportRefs\":\"\"}";
		String resultsChainDiagramObject2 = "{\"SelectedTaggedObjectSetRefs\":\"\",\"Detail\":\"\",\"AssignmentIds\":\"\",\"DiagramFactorIds\":\"{\\\"Ids\\\":[37]}\",\"TimeStampModified\":\"1253125558183\",\"DiagramFactorLinkIds\":\"\",\"ExpenseRefs\":\"\",\"HiddenTypes\":\"\",\"Label\":\"[New Results Chain]\",\"Id\":34,\"ShortLabel\":\"\",\"ProgressReportRefs\":\"\"}";
		
		String scopeBoxToBeShared = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253125745208\",\"Id\":32,\"Label\":\"ScopeBoxWithSameLabel\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"\"}";
		String scopeBoxToBeDeleted = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253125738517\",\"Id\":36,\"Label\":\"ScopeBoxWithSameLabel\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"\"}";
		String scopeBoxOnRc1 = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253125583641\",\"Id\":41,\"Label\":\"New Scope Box\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"\"}";
		String scopeBoxOnRc2 = "{\"ObjectiveIds\":\"\",\"Comments\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"ScopeBox\",\"ShortLabel\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1253125734375\",\"Id\":43,\"Label\":\"ScopeBoxWithSameLabel\",\"ProgressReportRefs\":\"\",\"ScopeBoxColorCode\":\"\"}";
		
		String scopeBoxDiagramFactorOnCm = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":50,\\\"ObjectId\\\":32}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":270,\\\"X\\\":210}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1253125571085\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":33,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":120,\\\"Width\\\":180}\"}";
		String scopeBoxDiagramFactorOnRc2 = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":50,\\\"ObjectId\\\":36}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":105,\\\"X\\\":135}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1253125560164\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":37,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":240,\\\"Width\\\":180}\"}";
		String scopeBoxDiagramFactor1OnRc1 = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":50,\\\"ObjectId\\\":43}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":255,\\\"X\\\":225}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1253125712160\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":44,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":240,\\\"Width\\\":180}\"}";
		String scopeBoxDiagramFactor2OnRc1 = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":50,\\\"ObjectId\\\":41}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":180,\\\"X\\\":225}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1253125586368\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":42,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":60,\\\"Width\\\":120}\"}";

		final int SCOPE_BOX_TYPE = 50;
		int[] scopeBoxIds = createAndPopulateObjectDir(jsonDir, SCOPE_BOX_TYPE, new String[]{scopeBoxToBeShared, scopeBoxToBeDeleted, scopeBoxOnRc1, scopeBoxOnRc2, });
		
		final int CONCEPTUAL_MODEL_TYPE = 19;
		createAndPopulateObjectDir(jsonDir, CONCEPTUAL_MODEL_TYPE, new String[]{conceptualModel, });
		
		final int RESULTS_CHAIN_TYPE = 24;
		createAndPopulateObjectDir(jsonDir, RESULTS_CHAIN_TYPE, new String[]{resultsChainDiagramObject1, resultsChainDiagramObject2, });
		
		final int DIAGRAM_FACTOR_TYPE = 18;
		int[] diagramFactorScopeBoxIds = createAndPopulateObjectDir(jsonDir, DIAGRAM_FACTOR_TYPE, new String[]{scopeBoxDiagramFactorOnCm, scopeBoxDiagramFactor1OnRc1, scopeBoxDiagramFactor2OnRc1, scopeBoxDiagramFactorOnRc2, });
		

		DataUpgrader.initializeStaticDirectory(tempDirectory);
		ShareSameLabeledScopeBoxesMigration.shareSameLabeledScopeBoxesAcrossAllDiagrams();
		
		
		verifyScopeBoxFileWasNotRemoved(jsonDir, SCOPE_BOX_TYPE, scopeBoxIds[0]);
		verifyScopeBoxFileWasNotRemoved(jsonDir, SCOPE_BOX_TYPE, scopeBoxIds[2]);
		verifyScopeBoxFileWasNotRemoved(jsonDir, SCOPE_BOX_TYPE, scopeBoxIds[3]);
		
		verifyScopeBoxFileWasRemoved(jsonDir, SCOPE_BOX_TYPE, scopeBoxIds[1]);
		
		verifyDiagramFactorWrappedFactorRef(jsonDir, DIAGRAM_FACTOR_TYPE, diagramFactorScopeBoxIds[0], scopeBoxIds[0]);
		verifyDiagramFactorWrappedFactorRef(jsonDir, DIAGRAM_FACTOR_TYPE, diagramFactorScopeBoxIds[1], scopeBoxIds[3]);
		verifyDiagramFactorWrappedFactorRef(jsonDir, DIAGRAM_FACTOR_TYPE, diagramFactorScopeBoxIds[2], scopeBoxIds[2]);
		verifyDiagramFactorWrappedFactorRef(jsonDir, DIAGRAM_FACTOR_TYPE, diagramFactorScopeBoxIds[3], scopeBoxIds[0]);
	}
	
	private void verifyDiagramFactorWrappedFactorRef(File jsonDir, final int DIAGRAM_FACTOR_TYPE, int diagramFactorId, int expectedWrappedId) throws Exception, ParseException
	{
		File diagramFactorDir = DataUpgrader.getObjectsDir(jsonDir, DIAGRAM_FACTOR_TYPE);
		File diagramFactorJsonFile = new File(diagramFactorDir, Integer.toString(diagramFactorId));		
		
		File manifestFile = new File(diagramFactorDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		
		EnhancedJsonObject diagramFactorJson = new EnhancedJsonObject(readFile(diagramFactorJsonFile));
		ORef wrappedRef = diagramFactorJson.getRef("WrappedFactorRef");
		assertEquals("wrong wrapped scope box id?", expectedWrappedId, wrappedRef.getObjectId().asInt());
	}

	private void verifyScopeBoxFileWasRemoved(File jsonDir, final int objectType, int objectId) throws Exception
	{
		verifyScopeBoxExistance(jsonDir, objectType, objectId, false);
	}
	
	private void verifyScopeBoxFileWasNotRemoved(File jsonDir, final int objectType, int objectId) throws Exception
	{
		verifyScopeBoxExistance(jsonDir, objectType, objectId, true);
	}

	private void verifyScopeBoxExistance(File jsonDir, final int objectType, int objectId, boolean expectedExistanceBoolean) throws IOException, ParseException
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		File objectFile = new File(objectDir, Integer.toString(objectId));
		assertEquals("object was removed?", expectedExistanceBoolean, objectFile.exists());
		
		File manifestFile = new File(objectDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		assertEquals("manifest does not contain key?", expectedExistanceBoolean, manifestObject.has(objectId));
	}
	
	public void testUpdateTncOperatingUnitCodesWithNoOparatingUnitCodes() throws Exception
	{
		String projectMetadataString = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"ProgressReportRefs\":\"\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.SizeInHectares\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1251992084820\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		verifyUpdatedTncOperatingUnitCodes(projectMetadataString, 0, new CodeList());
	}
	
	public void testUpdateTncOperatingUnitCodes() throws Exception
	{
		String projectMetadataString = "{\"FullTimeEmployeeDaysPerYear\":\"\",\"NextSteps\":\"\",\"FiscalYearStart\":\"\",\"BudgetSecuredPercent\":\"\",\"TNC.DatabaseDownloadDate\":\"\",\"SiteMapReference\":\"\",\"Countries\":\"\",\"StartDate\":\"\",\"Municipalities\":\"\",\"ProtectedAreaCategoryNotes\":\"\",\"LegislativeDistricts\":\"\",\"DiagramFontFamily\":\"\",\"ProtectedAreaCategories\":\"\",\"KeyFundingSources\":\"\",\"TotalBudgetForFunding\":\"\",\"LocationDetail\":\"\",\"TNC.LessonsLearned\":\"\",\"ProjectName\":\"\",\"AssignmentIds\":\"\",\"DiagramFontSize\":\"\",\"ProjectLatitude\":\"0.0\",\"TNC.OperatingUnitList\":\"{\\\"Codes\\\":[\\\"AL_US\\\",\\\"ATLFO\\\",\\\"CAMER\\\",\\\"CSAVA\\\",\\\"CUSRO\\\",\\\"EUSRO\\\",\\\"MCARO\\\",\\\"PNWRO\\\",\\\"RMTRO\\\",\\\"SAMRO\\\",\\\"SUSRO\\\"]}\",\"CurrencyType\":\"\",\"LocationComments\":\"\",\"RedListSpecies\":\"\",\"TargetMode\":\"\",\"ProjectLongitude\":\"0.0\",\"Id\":0,\"ScopeComments\":\"\",\"ExpectedEndDate\":\"\",\"CurrencySymbol\":\"$\",\"StateAndProvinces\":\"\",\"ProjectStatus\":\"\",\"OtherOrgProjectNumber\":\"\",\"CurrencyDecimalPlaces\":\"\",\"FinancialComments\":\"\",\"SocialContext\":\"\",\"ExpenseRefs\":\"\",\"TNC.PlanningTeamComment\":\"\",\"TNC.FreshwaterEcoRegion\":\"\",\"CurrentWizardScreenName\":\"\",\"TNC.TerrestrialEcoRegion\":\"\",\"WorkPlanEndDate\":\"\",\"ProjectDescription\":\"\",\"ThreatRatingMode\":\"\",\"PlanningComments\":\"\",\"ProjectURL\":\"\",\"WorkPlanTimeUnit\":\"YEARLY\",\"ProgressReportRefs\":\"\",\"ProjectScope\":\"\",\"OtherOrgRelatedProjects\":\"\",\"TNC.SizeInHectares\":\"\",\"TNC.WorkbookVersionNumber\":\"\",\"HumanPopulation\":\"\",\"OtherNotableSpecies\":\"\",\"DataEffectiveDate\":\"\",\"ProjectVision\":\"\",\"WorkPlanStartDate\":\"\",\"HumanPopulationNotes\":\"\",\"ShortProjectScope\":\"\",\"TNC.MarineEcoRegion\":\"\",\"TimeStampModified\":\"1251992084820\",\"ProjectAreaNote\":\"\",\"XenodataRefs\":\"\",\"TNC.WorkbookVersionDate\":\"\",\"Label\":\"\",\"ProjectArea\":\"\"}";
		final String TNC_OPERATING_UNITS_OBSOLETE_CODE = "OBSOLETE";
		CodeList withOnlyObsolete = new CodeList(new String[]{TNC_OPERATING_UNITS_OBSOLETE_CODE, });
		verifyUpdatedTncOperatingUnitCodes(projectMetadataString, 11, withOnlyObsolete);
	}
	
	public void verifyUpdatedTncOperatingUnitCodes(String projectMetadataString, int expectedRemoveCodeCount, CodeList expected) throws Exception
	{
		File jsonDir = createJsonDir();
		
		final int PROJECT_METADATA_TYPE = 11;
		int[] projectMetadataIds = createAndPopulateObjectDir(jsonDir, PROJECT_METADATA_TYPE, new String[]{projectMetadataString, });
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		CodeList operatingUnitCodesRemoved = UpdateTncOpertingUnitMigration.updateTncOperatingUnitsList();
		assertEquals("incorrect number of operating unit codes were removed?", expectedRemoveCodeCount, operatingUnitCodesRemoved.size());
		
		File projectMetadataDir = DataUpgrader.getObjectsDir(jsonDir, PROJECT_METADATA_TYPE);
		File projectMetadataFile = new File(projectMetadataDir, Integer.toString(projectMetadataIds[0]));
		EnhancedJsonObject projectMetadataJson = new EnhancedJsonObject(readFile(projectMetadataFile));
		final String TNC_OPERATING_UNIT_LIST_FIELD_NAME = "TNC.OperatingUnitList";
		assertTrue("should contain Tnc Operating Unit List?", projectMetadataJson.has(TNC_OPERATING_UNIT_LIST_FIELD_NAME));
		
		CodeList codesToBeStrippedOfAllExceptObsolete = projectMetadataJson.getCodeList(TNC_OPERATING_UNIT_LIST_FIELD_NAME);
		codesToBeStrippedOfAllExceptObsolete.retainAll(expected);
		assertEquals("incorrect obsolete count?", codesToBeStrippedOfAllExceptObsolete.size(), expected.size());
		
		CodeList tncOperatingUnitCodes = projectMetadataJson.getCodeList(TNC_OPERATING_UNIT_LIST_FIELD_NAME);
		String[] oldRemovedCodes = new String[]{"AL_US", "ATLFO", "CAMER", "CSAVA", "CUSRO", "EUSRO", "MCARO", "PNWRO", "RMTRO", "SAMRO", "SUSRO", };
		for (int index = 0; index < oldRemovedCodes.length; ++index)
		{
			assertFalse("new codes should not contain old value " + oldRemovedCodes[index] + "?", tncOperatingUnitCodes.contains(oldRemovedCodes[index]));
		}
	}
	
	public void testConvertMaterialToPersonCodeConverter() throws Exception
	{
		String projectResourceMaterial = "{\"ExpenseRefs\":\"\",\"Organization\":\"\",\"IMAddress\":\"\",\"RoleCodes\":\"\",\"ResourceType\":\"Material\",\"SurName\":\"\",\"AlternativeEmail\":\"\",\"ProgressReportRefs\":\"\",\"Custom.Custom1\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"PhoneNumberOther\":\"\",\"Custom.Custom2\":\"\",\"Location\":\"\",\"PhoneNumber\":\"\",\"CostPerUnit\":\"\",\"Name\":\"Semi Truck\",\"Email\":\"\",\"TimeStampModified\":\"1251478935415\",\"Initials\":\"\",\"PhoneNumberMobile\":\"\",\"PhoneNumberHome\":\"\",\"DateUpdated\":\"\",\"Position\":\"\",\"Label\":\"\",\"Id\":60,\"IMService\":\"\"}";
		String projectResourcePerson =   "{\"ExpenseRefs\":\"\",\"Organization\":\"\",\"IMAddress\":\"\",\"RoleCodes\":\"\",\"ResourceType\":\"\",\"SurName\":\"Doe\",\"AlternativeEmail\":\"\",\"ProgressReportRefs\":\"\",\"Custom.Custom1\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"PhoneNumberOther\":\"\",\"Custom.Custom2\":\"\",\"Location\":\"\",\"PhoneNumber\":\"\",\"CostPerUnit\":\"\",\"Name\":\"John\",\"Email\":\"\",\"TimeStampModified\":\"1251478937866\",\"Initials\":\"\",\"PhoneNumberMobile\":\"\",\"PhoneNumberHome\":\"\",\"DateUpdated\":\"\",\"Position\":\"\",\"Label\":\"\",\"Id\":61,\"IMService\":\"\"}";
		
		File jsonDir = createJsonDir();
		
		final int PROJECT_RESOURCE_TYPE = 7;
		int[] resourceIds = createAndPopulateObjectDir(jsonDir, PROJECT_RESOURCE_TYPE, new String[]{projectResourceMaterial, projectResourcePerson, });
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion47();
		
		String PERSON_TYPE = "";
		verifyResourceType(jsonDir, PROJECT_RESOURCE_TYPE, resourceIds[0], PERSON_TYPE);
		verifyResourceType(jsonDir, PROJECT_RESOURCE_TYPE, resourceIds[1], PERSON_TYPE);
	}

	private void verifyResourceType(File jsonDir, final int ObjectType, int resourceId, String expectedResourceType) throws Exception
	{
		File resourceDir = DataUpgrader.getObjectsDir(jsonDir, ObjectType);
		File resourceFile = new File(resourceDir, Integer.toString(resourceId));
		assertTrue("resource was removed?", resourceFile.exists());
		
		EnhancedJsonObject resourceJson = new EnhancedJsonObject(readFile(resourceFile));
		assertTrue("should contain resource type field?", resourceJson.has("ResourceType"));
		
		String resourceType = resourceJson.getString("ResourceType");
		assertEquals("resource type was not updated?", expectedResourceType, resourceType);
	}
	
	public void testRemoveTextBoxLinksFromEmtyProject() throws Exception
	{
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion46();
	}
	
	public void testRemoveTextBoxLinksInResultsChain() throws Exception
	{
		String resultsChain = "{\"SelectedTaggedObjectSetRefs\":\"\",\"Detail\":\"\",\"AssignmentIds\":\"\",\"DiagramFactorIds\":\"{\\\"Ids\\\":[38,40,41]}\",\"TimeStampModified\":\"1250886897878\",\"DiagramFactorLinkIds\":\"{\\\"Ids\\\":[42,44]}\",\"ExpenseRefs\":\"\",\"HiddenTypes\":\"\",\"Label\":\"New Strategy\",\"Id\":37,\"ShortLabel\":\"\",\"ProgressReportRefs\":\"\"}";
		File jsonDir = createJsonDir();
		final int RESULTS_CHAIN_DIAGRAM_TYPE = 24;
		int[] resultsChainDiagramIds = createAndPopulateObjectDir(jsonDir, RESULTS_CHAIN_DIAGRAM_TYPE, new String[]{resultsChain, });

		verifyTextBoxFactorLinks(jsonDir, RESULTS_CHAIN_DIAGRAM_TYPE, resultsChainDiagramIds);
	}
	
	public void testRemoveTextBoxLinks() throws Exception 
	{
		File jsonDir = createJsonDir();
		String diagramObjectString = "{\"SelectedTaggedObjectSetRefs\":\"\",\"Detail\":\"\",\"AssignmentIds\":\"\",\"DiagramFactorIds\":\"{\\\"Ids\\\":[38,40,41]}\",\"TimeStampModified\":\"1250886897878\",\"DiagramFactorLinkIds\":\"{\\\"Ids\\\":[42,44]}\",\"ExpenseRefs\":\"\",\"HiddenTypes\":\"\",\"Label\":\"New Strategy\",\"Id\":37,\"ShortLabel\":\"\",\"ProgressReportRefs\":\"\"}";
		final int CONCEPTUAL_MODEL_DIAGRAM_TYPE = 19;
		int[] conceptualModelDiagramIds = createAndPopulateObjectDir(jsonDir, CONCEPTUAL_MODEL_DIAGRAM_TYPE, new String[]{diagramObjectString,});
		
		verifyTextBoxFactorLinks(jsonDir, CONCEPTUAL_MODEL_DIAGRAM_TYPE, conceptualModelDiagramIds);
	}

	private void verifyTextBoxFactorLinks(File jsonDir, final int diagramObjectType, int[] diagramObjectIds) throws Exception
	{
		String strategy1 = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"ExpenseRefs\":\"\",\"LegacyTncStrategyRanking\":\"\",\"TaxonomyCode\":\"\",\"ShortLabel\":\"\",\"ImpactRating\":\"\",\"Status\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1250886879948\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"Label\":\"New Strategy\",\"Id\":31,\"ProgressReportRefs\":\"\"}";
		String strategy2 = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"ExpenseRefs\":\"\",\"LegacyTncStrategyRanking\":\"\",\"TaxonomyCode\":\"\",\"ShortLabel\":\"\",\"ImpactRating\":\"\",\"Status\":\"\",\"Text\":\"\",\"TimeStampModified\":\"1250886878681\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"Label\":\"New Strategy\",\"Id\":25,\"ProgressReportRefs\":\"\"}";
		String textBox = "{\"ObjectiveIds\":\"\",\"Text\":\"\",\"IndicatorIds\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"TimeStampModified\":\"1250886894475\",\"Type\":\"TextBox\",\"ExpenseRefs\":\"\",\"Label\":\"[ New Text Box ]\",\"Id\":39,\"ShortLabel\":\"\",\"ProgressReportRefs\":\"\"}";
		
		String strategy1DiagramFactor = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":31}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":165,\\\"X\\\":0}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1250886894468\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":38,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":60,\\\"Width\\\":120}\"}";
		String strategy2DiagramFactor = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":25}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":165,\\\"X\\\":210}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1250886894491\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":41,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":60,\\\"Width\\\":120}\"}";
		String textBoxDiagramFactor = "{\"WrappedFactorRef\":\"{\\\"ObjectType\\\":26,\\\"ObjectId\\\":39}\",\"FontColor\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Location\":\"{\\\"Y\\\":180,\\\"X\\\":480}\",\"FontSize\":\"\",\"BackgroundColor\":\"\",\"TimeStampModified\":\"1250886894484\",\"GroupBoxChildrenRefs\":\"\",\"TextBoxZOrderCode\":\"\",\"Id\":40,\"Label\":\"\",\"ProgressReportRefs\":\"\",\"FontStyle\":\"\",\"Size\":\"{\\\"Height\\\":30,\\\"Width\\\":180}\"}";
		
		String fromStrategy1ToStrategy2FactorLink = "{\"AssignmentIds\":\"\",\"FromRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":31}\",\"TimeStampModified\":\"1250886880018\",\"ExpenseRefs\":\"\",\"ToRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":25}\",\"Label\":\"\",\"Id\":33,\"BidirectionalLink\":\"\",\"ProgressReportRefs\":\"\"}";
		String fromStrategy2ToTextBoxFactorLink   = "{\"AssignmentIds\":\"\",\"FromRef\":\"{\\\"ObjectType\\\":21,\\\"ObjectId\\\":25}\",\"TimeStampModified\":\"1250886897871\",\"ExpenseRefs\":\"\",\"ToRef\":\"{\\\"ObjectType\\\":26,\\\"ObjectId\\\":39}\",\"Label\":\"\",\"Id\":43,\"BidirectionalLink\":\"\",\"ProgressReportRefs\":\"\"}";
		
		String fromStrategy1ToStrategy2DiagramLink = "{\"FromDiagramFactorId\":38,\"ToDiagramFactorId\":41,\"AssignmentIds\":\"\",\"TimeStampModified\":\"1250886894500\",\"GroupedDiagramLinkRefs\":\"\",\"ExpenseRefs\":\"\",\"Label\":\"\",\"Id\":42,\"WrappedLinkId\":33,\"ProgressReportRefs\":\"\",\"BendPoints\":\"\",\"Color\":\"\"}";
		String fromStratefy2ToTextBoxDiagramLink = "{\"FromDiagramFactorId\":41,\"ToDiagramFactorId\":40,\"AssignmentIds\":\"\",\"TimeStampModified\":\"1250886897875\",\"GroupedDiagramLinkRefs\":\"\",\"ExpenseRefs\":\"\",\"Label\":\"\",\"Id\":44,\"WrappedLinkId\":43,\"ProgressReportRefs\":\"\",\"BendPoints\":\"\",\"Color\":\"\"}";
		
		final int TEXTBOX_TYPE = 26;
		int[] textBoxIds = createAndPopulateObjectDir(jsonDir, TEXTBOX_TYPE, new String[]{textBox, });
		
		final int STRATEGY_TYPE = 21;
		int[] strategyIds = createAndPopulateObjectDir(jsonDir, STRATEGY_TYPE, new String[]{strategy1, strategy2, });
		
		final int DIAGRAM_FACTOR_TYPE = 18;
		int[] diagramFactorIds = createAndPopulateObjectDir(jsonDir, DIAGRAM_FACTOR_TYPE, new String[]{strategy1DiagramFactor, strategy2DiagramFactor, textBoxDiagramFactor, });
		
		final int FACTOR_LINK_TYPE = 6;
		int[] factorLinkIds = createAndPopulateObjectDir(jsonDir, FACTOR_LINK_TYPE, new String[]{fromStrategy1ToStrategy2FactorLink, fromStrategy2ToTextBoxFactorLink, });
		
		final int DIAGRAM_LINK_TYPE = 13;
		int[] diagramLinkIds = createAndPopulateObjectDir(jsonDir, DIAGRAM_LINK_TYPE, new String[]{fromStrategy1ToStrategy2DiagramLink, fromStratefy2ToTextBoxDiagramLink, });
		
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion46();
		
		
		verifyObjectFileWasNotRemoved(jsonDir, TEXTBOX_TYPE, textBoxIds);
		verifyObjectFileWasNotRemoved(jsonDir, STRATEGY_TYPE, strategyIds);
		
		verifyObjectFileWasNotRemoved(jsonDir, DIAGRAM_FACTOR_TYPE, diagramFactorIds);
		
		verifyObjectFileWasNotRemoved(jsonDir, FACTOR_LINK_TYPE, factorLinkIds[0]);
		verifyObjectFileWasRemoved(jsonDir, FACTOR_LINK_TYPE, factorLinkIds[1]);
		
		verifyObjectFileWasNotRemoved(jsonDir, DIAGRAM_LINK_TYPE, diagramLinkIds[0]);
		verifyObjectFileWasRemoved(jsonDir, DIAGRAM_LINK_TYPE, diagramLinkIds[1]);
		
		verifyObjectFileWasNotRemoved(jsonDir, diagramObjectType, diagramObjectIds);
		
		File diagramDir = DataUpgrader.getObjectsDir(jsonDir, diagramObjectType);
		File diagramFile = new File(diagramDir, Integer.toString(diagramObjectIds[0]));
		EnhancedJsonObject diagramJson = new EnhancedJsonObject(readFile(diagramFile));
		IdList diagramLinkIdList = diagramJson.getIdList(DIAGRAM_FACTOR_TYPE, "DiagramFactorLinkIds");
		assertEquals("diagram link ids for diagram was not updated?", 1, diagramLinkIdList.size());
	}

	private void verifyObjectFileWasRemoved(File jsonDir, final int objectType, int objectId) throws Exception
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		File objectFile = new File(objectDir, Integer.toString(objectId));
		assertFalse("object was not removed?", objectFile.exists());
		
		File manifestFile = new File(objectDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		assertFalse("manifest should not contain key?", manifestObject.has(objectId));
	}
	
	private void verifyObjectFileWasNotRemoved(File jsonDir, final int objectType, int[] objectIds) throws Exception
	{
		for (int index = 0; index < objectIds.length; ++index)
		{
			verifyObjectFileWasNotRemoved(jsonDir, objectType, objectIds[index]);
		}
	}

	private void verifyObjectFileWasNotRemoved(File jsonDir, final int objectType, int objectId) throws Exception
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		File objectFile = new File(objectDir, Integer.toString(objectId));
		assertTrue("object was removed?", objectFile.exists());
		
		File manifestFile = new File(objectDir, "manifest");
		assertTrue("manifest file could not be found?", manifestFile.exists());
		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		assertTrue("manifest does not contain key?", manifestObject.has(objectId));
	}
	
	public void testRenameCommentFieldToComments() throws Exception
	{
		String taskString = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"ExpenseRefs\":\"\",\"Details\":\"\",\"Comment\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429693315\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"\",\"Id\":32,\"ProgressReportRefs\":\"\"}";
		String causeString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Factor\",\"ExpenseRefs\":\"\",\"Comment\":\"some cause comment\",\"TaxonomyCode\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429716763\",\"KeyEcologicalAttributeIds\":\"\",\"Id\":33,\"Label\":\"New Factor\",\"IsDirectThreat\":\"\"}";
		String strategyString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"ExpenseRefs\":\"\",\"Comment\":\"some strategy comment\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"ImpactRating\":\"\",\"Status\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429730578\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":35,\"ProgressReportRefs\":\"\"}";
		String targetString  = "{\"ObjectiveIds\":\"\",\"SpeciesLatinName\":\"\",\"ViabilityMode\":\"\",\"IndicatorIds\":\"{\\\"Ids\\\":[29]}\",\"AssignmentIds\":\"\",\"Type\":\"Target\",\"ExpenseRefs\":\"\",\"Comment\":\"some target comment\",\"ShortLabel\":\"\",\"StressRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":33,\\\"ObjectId\\\":30}]}\",\"Text\":\"\",\"HabitatAssociation\":\"\",\"TargetStatus\":\"\",\"SubTargetRefs\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429551827\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Target\",\"Id\":23,\"CurrentStatusJustification\":\"\"}";
		String intermediateResultString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"Intermediate Result\",\"Comment\":\"some intermediate result comment\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429867772\",\"KeyEcologicalAttributeIds\":\"\",\"Id\":53,\"Label\":\"New Intermediate Result\"}";
		String threatReductionResultString  = "{\"ObjectiveIds\":\"\",\"RelatedDirectThreatRef\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Threat Reduction Result\",\"ExpenseRefs\":\"\",\"Comment\":\"some threat reduction result comment\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429849533\",\"KeyEcologicalAttributeIds\":\"\",\"Id\":47,\"Label\":\"New Threat Reduction Result\"}";
		String textBoxString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"TextBox\",\"Comment\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429739700\",\"KeyEcologicalAttributeIds\":\"\",\"Id\":37,\"Label\":\"New Text Box\"}";
		String indicatorString  = "{\"ThresholdDetails\":\"\",\"RatingSource\":\"\",\"FutureStatusDetail\":\"\",\"IndicatorThresholds\":\"\",\"AssignmentIds\":\"\",\"FutureStatusSummary\":\"\",\"ExpenseRefs\":\"\",\"Comment\":\"some indicator comment\",\"ShortLabel\":\"\",\"MeasurementRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":32,\\\"ObjectId\\\":31}]}\",\"Priority\":\"\",\"Detail\":\"\",\"FutureStatusRating\":\"\",\"TaskIds\":\"{\\\"Ids\\\":[32]}\",\"TimeStampModified\":\"1245429675754\",\"FutureStatusDate\":\"\",\"Label\":\"\",\"Id\":29,\"FutureStatusComment\":\"\",\"ProgressReportRefs\":\"\",\"ViabilityRatingsComment\":\"\"}";
		String measurementString  = "{\"Status\":\"\",\"Detail\":\"\",\"StatusConfidence\":\"\",\"AssignmentIds\":\"\",\"Date\":\"\",\"TimeStampModified\":\"1245429622038\",\"ExpenseRefs\":\"\",\"Comment\":\"some measurement comment\",\"Label\":\"\",\"Id\":31,\"Trend\":\"\",\"Summary\":\"\"}";
		String stressString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Stress\",\"ExpenseRefs\":\"\",\"Comment\":\"some stress comment\",\"ShortLabel\":\"\",\"Text\":\"\",\"Detail\":\"\",\"Severity\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429543143\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"\",\"Id\":30,\"Scope\":\"\"}";
		String groupBoxString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"ExpenseRefs\":\"\",\"Type\":\"GroupBox\",\"Comment\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429759616\",\"KeyEcologicalAttributeIds\":\"\",\"Id\":39,\"Label\":\"New Group Box\"}";
		String reportTemplateString  = "{\"AssignmentIds\":\"\",\"IncludeSectionCodes\":\"\",\"TimeStampModified\":\"1245429885769\",\"ExpenseRefs\":\"\",\"Comment\":\"some report template comment\",\"Label\":\"\",\"Id\":58,\"ShortLabel\":\"\"}";
		String taggedObjectSetString  = "{\"AssignmentIds\":\"\",\"TimeStampModified\":\"1245429777788\",\"ExpenseRefs\":\"\",\"Comment\":\"some taggedObjectSet comment\",\"Label\":\"\",\"Id\":41,\"ShortLabel\":\"\",\"TaggedObjectRefs\":\"\"}";
		String scopeBoxString  = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"ScopeBox\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429785629\",\"KeyEcologicalAttributeIds\":\"\",\"Id\":42,\"Label\":\"\",\"ScopeBoxColorCode\":\"\"}";
		String humanWelfareTargetString  = "{\"ObjectiveIds\":\"\",\"ViabilityMode\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"HumanWelfareTarget\",\"ExpenseRefs\":\"\",\"Comment\":\"some human welfare target comment\",\"ShortLabel\":\"\",\"Text\":\"\",\"TargetStatus\":\"\",\"SubTargetRefs\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245429826030\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Human Welfare Target\",\"Id\":44,\"CurrentStatusJustification\":\"\"}";
		
		File jsonDir = createJsonDir();
		final int TASK_TYPE = 3;
		int[] taskIds = createAndPopulateObjectDir(jsonDir, TASK_TYPE, new String[] {taskString, });
		
		final int CAUSE_TYPE = 20;
		int[] causeIds = createAndPopulateObjectDir(jsonDir, CAUSE_TYPE, new String[] {causeString});
		
		final int STRATEGY_TYPE = 21;
		int[] strategyIds = createAndPopulateObjectDir(jsonDir, STRATEGY_TYPE, new String[] {strategyString});
		
		final int TARGET_TYPE = 22;
		int[] targetIds = createAndPopulateObjectDir(jsonDir, TARGET_TYPE, new String[] {targetString});
		 
		final int INTERMEDIATE_RESULT_TYPE = 23;
		int[] intermediateResultIds = createAndPopulateObjectDir(jsonDir, INTERMEDIATE_RESULT_TYPE, new String[] {intermediateResultString});
		
		final int THREAT_REDUCTION_RESULT_TYPE = 25;
		int[] threatReductionResultIds = createAndPopulateObjectDir(jsonDir, THREAT_REDUCTION_RESULT_TYPE, new String[]{threatReductionResultString});
		
		final int TEXT_BOX_TYPE = 26;
		int[] textBoxIds = createAndPopulateObjectDir(jsonDir, TEXT_BOX_TYPE, new String[]{textBoxString});
		
		final int INDICATOR_TYPE = 8;
		int[] indicatorIds = createAndPopulateObjectDir(jsonDir, INDICATOR_TYPE, new String[]{indicatorString});
		
		final int MEASUREMENT_TYPE = 32;
		int[] measurementIds = createAndPopulateObjectDir(jsonDir, MEASUREMENT_TYPE, new String[]{measurementString});
		
		final int STRESS_TYPE = 33;
		int[] stressIds = createAndPopulateObjectDir(jsonDir, STRESS_TYPE, new String[]{stressString});
		
		final int GROUP_BOX_TYPE = 35;
		int[] groupBoxIds = createAndPopulateObjectDir(jsonDir, GROUP_BOX_TYPE, new String[]{groupBoxString});
		
		final int REPORT_TEMPLATE_TYPE = 46;
		int[] reportTemplateIds = createAndPopulateObjectDir(jsonDir, REPORT_TEMPLATE_TYPE, new String[]{reportTemplateString});
		
		final int TAGGED_OBJECT_SET_TYPE = 47;
		int[] taggedObjectSetIds = createAndPopulateObjectDir(jsonDir, TAGGED_OBJECT_SET_TYPE, new String[]{taggedObjectSetString});
		
		final int SCOPE_BOX_TYPE = 50;
		int[] scopeBoxIds = createAndPopulateObjectDir(jsonDir, SCOPE_BOX_TYPE, new String[]{scopeBoxString});
		
		final int HUMAN_WELFARE_TARGET_TYPE = 52;
		int[] humanWelfareTargetIds = createAndPopulateObjectDir(jsonDir, HUMAN_WELFARE_TARGET_TYPE, new String[]{humanWelfareTargetString});
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion45();
		
		verifyCommentsField(jsonDir, TASK_TYPE, taskIds, "");		
		verifyCommentsField(jsonDir, CAUSE_TYPE, causeIds, "some cause comment");
		verifyCommentsField(jsonDir, STRATEGY_TYPE, strategyIds, "some strategy comment");
		verifyCommentsField(jsonDir, TARGET_TYPE, targetIds, "some target comment");
		verifyCommentsField(jsonDir, INTERMEDIATE_RESULT_TYPE, intermediateResultIds, "some intermediate result comment");
		verifyCommentsField(jsonDir, THREAT_REDUCTION_RESULT_TYPE, threatReductionResultIds, "some threat reduction result comment");
		verifyCommentsField(jsonDir, TEXT_BOX_TYPE, textBoxIds, "");
		verifyCommentsField(jsonDir, INDICATOR_TYPE, indicatorIds, "some indicator comment");
		verifyCommentsField(jsonDir, MEASUREMENT_TYPE, measurementIds, "some measurement comment");
		verifyCommentsField(jsonDir, STRESS_TYPE, stressIds, "some stress comment");
		verifyCommentsField(jsonDir, GROUP_BOX_TYPE, groupBoxIds, "");
		verifyCommentsField(jsonDir, REPORT_TEMPLATE_TYPE, reportTemplateIds, "some report template comment");
		verifyCommentsField(jsonDir, TAGGED_OBJECT_SET_TYPE, taggedObjectSetIds, "some taggedObjectSet comment");
		verifyCommentsField(jsonDir, SCOPE_BOX_TYPE, scopeBoxIds, "");
		verifyCommentsField(jsonDir, HUMAN_WELFARE_TARGET_TYPE, humanWelfareTargetIds, "some human welfare target comment");
	}

	private void verifyCommentsField(File jsonDir, final int objectType, int[] objectIds, String expectedCommensValue) throws Exception
	{	
		int objectId = objectIds[0];
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);		
		File objectFile =  new File(objectDir, Integer.toString(objectId));
		EnhancedJsonObject json = new EnhancedJsonObject(readFile(objectFile));
		
		assertFalse("should not contain comment field?", json.has("Comment"));
		assertTrue("should contain comments field?", json.has("Comments"));
		
		String commentsData = json.getString("Comments");
		assertEquals("wrong comments value?", expectedCommensValue, commentsData);
	}
		
	public void testMoveStressCommentsToCommentField() throws Exception
	{
		String stressWithNoComments = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Stress\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"Detail\":\"\",\"Severity\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245332434233\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"\",\"Id\":29,\"Scope\":\"\"}";
		String stressWithComments = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"Comments\":\"Some Stress Comments\",\"AssignmentIds\":\"\",\"Type\":\"Stress\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"ShortLabel\":\"\",\"Text\":\"\",\"Detail\":\"\",\"Severity\":\"\",\"GoalIds\":\"\",\"TimeStampModified\":\"1245332454419\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"\",\"Id\":30,\"Scope\":\"\"}";
		
		File jsonDir = createJsonDir();
		
		final int STRESS_TYPE = 33;
		int[] stressIds = createAndPopulateObjectDir(jsonDir, STRESS_TYPE, new String[] {stressWithNoComments, stressWithComments, });
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion44();
		
		verifyCommentsValue(jsonDir, STRESS_TYPE, stressIds[0], "");
		verifyCommentsValue(jsonDir, STRESS_TYPE, stressIds[1], "Some Stress Comments");
	}

	private void verifyCommentsValue(File jsonDir, final int STRESS_TYPE, final int stressId, String expectedCommentValue)	throws Exception
	{
		File stressDir = DataUpgrader.getObjectsDir(jsonDir, STRESS_TYPE);		
		File stressWithNoCommentsFile =  new File(stressDir, Integer.toString(stressId));
		EnhancedJsonObject stressWithNoCommentsJson = new EnhancedJsonObject(readFile(stressWithNoCommentsFile));
		
		String commentsData = stressWithNoCommentsJson.getString("Comments");
		assertEquals("wrong comments value, should have been empty?", "", commentsData);
		
		String commentData = stressWithNoCommentsJson.getString("Comment");
		assertEquals("worng comment value", expectedCommentValue, commentData);
	}
	
	public void testConvertToDateUnitEffortList() throws Exception
	{
		String resourceAssignment = "{\"AssignmentIds\":\"\",\"AccountingCode\":\"\",\"ResourceId\":\"36\",\"Details\":\"{\\\"DateRangeEfforts\\\":[{\\\"NumberOfUnits\\\":15,\\\"DateRange\\\":{\\\"EndDate\\\":\\\"2010-12-31\\\",\\\"StartDate\\\":\\\"2010-01-01\\\"},\\\"CostUnitCode\\\":\\\"\\\"}]}\",\"ExpenseRefs\":\"\",\"BudgetCostOverride\":\"\",\"FundingSource\":\"\",\"WhoOverrideRefs\":\"\",\"WhenOverride\":\"\",\"TimeStampModified\":\"1242142436461\",\"BudgetCostMode\":\"\",\"Id\":35,\"Label\":\"\"}";
		String expenseAssignment = "{\"AssignmentIds\":\"\",\"WhenOverride\":\"\",\"AccountingCodeRef\":\"\",\"FundingSourceRef\":\"\",\"TimeStampModified\":\"1242143768537\",\"BudgetCostOverride\":\"\",\"ExpenseRefs\":\"\",\"Details\":\"{\\\"DateRangeEfforts\\\":[{\\\"NumberOfUnits\\\":10,\\\"DateRange\\\":{\\\"EndDate\\\":\\\"2010-12-31\\\",\\\"StartDate\\\":\\\"2010-01-01\\\"},\\\"CostUnitCode\\\":\\\"\\\"}]}\",\"BudgetCostMode\":\"\",\"Label\":\"n\",\"Id\":34,\"WhoOverrideRefs\":\"\"}";
		
		File jsonDir = createJsonDir();
		
		int[] resourceAssignmentRawIds = {35, };
		final int RESOURCE_ASSIGNMENT_TYPE = 14;
		createAndPopulateObjectDir(jsonDir, RESOURCE_ASSIGNMENT_TYPE, new String[] {resourceAssignment, });
		
		int[] expenseAssignmentRawIds = {34, };
		final int EXPENSE_ASSIGNMENT_TYPE = 51;
		createAndPopulateObjectDir(jsonDir, EXPENSE_ASSIGNMENT_TYPE, new String[] {expenseAssignment, });
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion42();
		
		verifyDateRangeEffortListWasConverted(15, jsonDir, RESOURCE_ASSIGNMENT_TYPE, resourceAssignmentRawIds);
		verifyDateRangeEffortListWasConverted(10, jsonDir, EXPENSE_ASSIGNMENT_TYPE, expenseAssignmentRawIds);
	}

	private void verifyDateRangeEffortListWasConverted(int expectedNumberOfUnits, File jsonDir, final int ASSIGNMENT_TYPE, int[] assignmentRawIds) throws Exception
	{
		File assignmentDir = DataUpgrader.getObjectsDir(jsonDir, ASSIGNMENT_TYPE);
		File assignmentFile =  new File(assignmentDir, Integer.toString(assignmentRawIds[0]));
		EnhancedJsonObject assignmentJson = new EnhancedJsonObject(readFile(assignmentFile));
		EnhancedJsonObject detailsJson = new EnhancedJsonObject(assignmentJson.getString("Details"));
		
		EnhancedJsonArray dateUnitEffortsJsonArray = detailsJson.getJsonArray("DateUnitEfforts");
		assertEquals("wrong number of date unit efforts?", 1, dateUnitEffortsJsonArray.length());
		
		EnhancedJsonObject dateUnitEffortJson = new EnhancedJsonObject(dateUnitEffortsJsonArray.getString(0));
		assertEquals("incorrect number of units?", expectedNumberOfUnits, dateUnitEffortJson.getInt("NumberOfUnits"));
		
		EnhancedJsonObject dateUnitJson = dateUnitEffortJson.getJson("DateUnit");
		assertEquals("wrong date unit?", "YEARFROM:2010-01", dateUnitJson.getString("DateUnitCode"));
	}
	
	public void testConvertHighLevelEstimatesIntoAssignments() throws Exception
	{
		
		verifyHighLevelConversion(0);
		
		String projectResourceJohny = "{\"ExpenseRefs\":\"\",\"Organization\":\"\",\"IMAddress\":\"\",\"RoleCodes\":\"\",\"ResourceType\":\"\",\"SurName\":\"Doo\",\"BudgetCostMode\":\"\",\"AlternativeEmail\":\"\",\"Custom.Custom1\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"PhoneNumberOther\":\"\",\"Custom.Custom2\":\"\",\"BudgetCostOverride\":\"\",\"Location\":\"\",\"CostUnit\":\"\",\"PhoneNumber\":\"\",\"CostPerUnit\":\"100.0\",\"WhoOverrideRefs\":\"\",\"Name\":\"Johny\",\"WhenOverride\":\"\",\"Email\":\"\",\"TimeStampModified\":\"1242671324141\",\"Initials\":\"JD\",\"PhoneNumberMobile\":\"\",\"PhoneNumberHome\":\"\",\"Position\":\"\",\"DateUpdated\":\"\",\"Label\":\"\",\"Id\":30,\"IMService\":\"\"}";
		String projectResourceJenny = "{\"ExpenseRefs\":\"\",\"Organization\":\"\",\"IMAddress\":\"\",\"RoleCodes\":\"\",\"ResourceType\":\"\",\"SurName\":\"Boo\",\"BudgetCostMode\":\"\",\"AlternativeEmail\":\"\",\"Custom.Custom1\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"PhoneNumberOther\":\"\",\"Custom.Custom2\":\"\",\"BudgetCostOverride\":\"\",\"Location\":\"\",\"CostUnit\":\"\",\"PhoneNumber\":\"\",\"CostPerUnit\":\"135.0\",\"WhoOverrideRefs\":\"\",\"Name\":\"Jenny\",\"WhenOverride\":\"\",\"Email\":\"\",\"TimeStampModified\":\"1242671363962\",\"Initials\":\"JB\",\"PhoneNumberMobile\":\"\",\"PhoneNumberHome\":\"\",\"Position\":\"\",\"DateUpdated\":\"\",\"Label\":\"\",\"Id\":36,\"IMService\":\"\"}";
		
		String taskWithCostWhenWho = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"This is a task detail\",\"ExpenseRefs\":\"\",\"BudgetCostOverride\":\"2000.0\",\"Comment\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36}]}\",\"Text\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-05-18\\\",\\\"StartDate\\\":\\\"2009-01-18\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242671492099\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"\",\"Id\":39,\"ProgressReportRefs\":\"\"}";
		
		String indicatorWithCostWhenWho = "{\"RatingSource\":\"\",\"FutureStatusDetail\":\"\",\"ExpenseRefs\":\"\",\"MeasurementRefs\":\"\",\"Detail\":\"This is some indicator sample details.\",\"FutureStatusRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"FutureStatusDate\":\"\",\"FutureStatusComment\":\"\",\"ViabilityRatingsComment\":\"\",\"ProgressReportRefs\":\"\",\"ThresholdDetails\":\"\",\"IndicatorThresholds\":\"\",\"AssignmentIds\":\"\",\"FutureStatusSummary\":\"\",\"BudgetCostOverride\":\"5000.0\",\"Comment\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36},{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",\"Priority\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-03-18\\\",\\\"StartDate\\\":\\\"2009-01-01\\\"}\",\"TimeStampModified\":\"1242671448223\",\"TaskIds\":\"\",\"Label\":\"\",\"Id\":38}";
		String indicatorInRollupMode = "{\"RatingSource\":\"\",\"FutureStatusDetail\":\"\",\"ExpenseRefs\":\"\",\"MeasurementRefs\":\"\",\"Detail\":\"\",\"FutureStatusRating\":\"\",\"BudgetCostMode\":\"\",\"FutureStatusDate\":\"\",\"FutureStatusComment\":\"\",\"ViabilityRatingsComment\":\"\",\"ProgressReportRefs\":\"\",\"ThresholdDetails\":\"\",\"IndicatorThresholds\":\"\",\"AssignmentIds\":\"\",\"FutureStatusSummary\":\"\",\"BudgetCostOverride\":\"100.0\",\"Comment\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36},{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",\"Priority\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2010-05-18\\\",\\\"StartDate\\\":\\\"2009-05-18\\\"}\",\"TimeStampModified\":\"1242674469821\",\"TaskIds\":\"\",\"Label\":\"\",\"Id\":37}";
		
		String strategyStringWithAll = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"125.0\", \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36}]}\",                                           \"ImpactRating\":\"\",\"Text\":\"This is some sample details.\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-06-27\\\",\\\"StartDate\\\":\\\"2009-05-18\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242671457090\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":24,\"ProgressReportRefs\":\"\"}";
		String strategyWithNoData =    "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242750411791\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":45,\"ProgressReportRefs\":\"\"}";
		String strategyWithCost =      "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"4500.0\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242750671823\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":51,\"ProgressReportRefs\":\"\"}";
		String strategyWithWhen =      "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2010-05-19\\\",\\\"StartDate\\\":\\\"2009-05-19\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242750770272\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":53,\"ProgressReportRefs\":\"\"}";
		String strategyWithWho =       "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36},{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",\"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242750832311\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":55,\"ProgressReportRefs\":\"\"}";
		String strategyWithCostWhen =  "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"3300.0\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-12-31\\\",\\\"StartDate\\\":\\\"2009-01-01\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242751095849\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":57,\"ProgressReportRefs\":\"\"}";
		String strategyWithCostWho =   "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"1300.0\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",                                           \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242751193459\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":61,\"ProgressReportRefs\":\"\"}";
		String strategyWithWhoWhen =   "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36}]}\",                                           \"ImpactRating\":\"\"              ,\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-12-31\\\",\\\"StartDate\\\":\\\"2009-01-01\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242751270397\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":63,\"ProgressReportRefs\":\"\"}";
		
		File jsonDir = createJsonDir();
		
		final int TASK_TYPE = 3;
		int[] taskRawIds = createAndPopulateObjectDir(jsonDir, TASK_TYPE, new String[] {taskWithCostWhenWho, });
		
		final int INDICATOR_TYPE = 8;
		int[] indicatorRawIds = createAndPopulateObjectDir(jsonDir, INDICATOR_TYPE, new String[] {indicatorWithCostWhenWho, indicatorInRollupMode, });
		
		final int STRATEGY_TYPE = 21;
		int[] strategyRawIds = createAndPopulateObjectDir(jsonDir, STRATEGY_TYPE, new String[] {strategyStringWithAll, strategyWithNoData, strategyWithCost, strategyWithWhen, strategyWithWho, 
																				strategyWithCostWhen, strategyWithCostWho, strategyWithWhoWhen, });
		
		final int PROJECT_RESOURCE_TYPE = 7;
		createAndPopulateObjectDir(jsonDir, PROJECT_RESOURCE_TYPE, new String[]{projectResourceJohny, projectResourceJenny, });
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":90}");
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		verifyHighLevelConversion(10);
		
		Vector<Integer> expected36ResourceList = new Vector<Integer>();
		expected36ResourceList.add(36);
		
		Vector<Integer> expected30ResourceList = new Vector<Integer>();
		expected30ResourceList.add(30);
		
		Vector<Integer> allResources = new Vector();
		allResources.addAll(expected36ResourceList);
		allResources.addAll(expected30ResourceList);
		
		
		EnhancedJsonObject taskJson = getObjectFileAsJson(jsonDir, TASK_TYPE, taskRawIds[0]);
		verifyDetailsField(jsonDir, taskJson, "Details", Double.toString(2000.0), expected36ResourceList, "2009-01-18 - 2009-05-18", "This is a task detail", "JB Jenny Boo");
		verifyResourceAssignments(jsonDir, taskJson, expected36ResourceList);
		verifyExpenseAssignment(jsonDir, taskJson, 2000.0, 1);
		
		
		EnhancedJsonObject indicator1Json = getObjectFileAsJson(jsonDir, INDICATOR_TYPE, indicatorRawIds[0]);
		verifyDetailsField(jsonDir, indicator1Json, "Detail", Double.toString(5000.0), allResources, "2009-01-01 - 2009-03-18", "This is some indicator sample details.", "JB Jenny Boo, JD Johny Doo");
		verifyResourceAssignments(jsonDir, indicator1Json, allResources);
		verifyExpenseAssignment(jsonDir, indicator1Json, 5000.0, 1);
		
		EnhancedJsonObject indicator2Json = getObjectFileAsJson(jsonDir, INDICATOR_TYPE, indicatorRawIds[1]);
		Vector<Integer> expectedResourceIds = new Vector();
		verifyDetailsField(jsonDir, indicator2Json, "Detail", Double.toString(0.0), expectedResourceIds, "", "", "");
		verifyResourceAssignments(jsonDir, indicator2Json, expectedResourceIds);
		verifyExpenseAssignment(jsonDir, indicator2Json, 0.0, 0);
		
		
		EnhancedJsonObject strategy1Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[0]);
		verifyDetailsField(jsonDir, strategy1Json, "Text", Double.toString(125.0), expected36ResourceList, "2009-05-18 - 2009-06-27", "This is some sample details.", "JB Jenny Boo");
		verifyResourceAssignments(jsonDir, strategy1Json, expected36ResourceList);
		verifyExpenseAssignment(jsonDir, strategy1Json, 125.0, 1);
		
		EnhancedJsonObject strategy2Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[1]);
		Vector<Integer> expectedResourceIds1 = new Vector();
		verifyDetailsField(jsonDir, strategy2Json, "Text", "", expectedResourceIds1, "", "", "");
		verifyResourceAssignments(jsonDir, strategy2Json, expectedResourceIds1);
		verifyExpenseAssignment(jsonDir, strategy2Json, Double.NaN, 0);
		
		EnhancedJsonObject strategy3Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[2]);
		verifyDetailsField(jsonDir, strategy3Json, "Text", Double.toString(4500.0), new Vector(), "", "", "");
		verifyResourceAssignments(jsonDir, strategy3Json, new Vector());
		verifyExpenseAssignment(jsonDir, strategy3Json, 4500.0, 1);
		
		EnhancedJsonObject strategy4Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[3]);
		verifyDetailsField(jsonDir, strategy4Json, "Text", "", new Vector(), "2009-05-19 - 2010-05-19", "", "");
		verifyResourceAssignments(jsonDir, strategy4Json, new Vector());
		verifyExpenseAssignment(jsonDir, strategy4Json, Double.NaN, 0);
		
		EnhancedJsonObject strategy5Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[4]);
		verifyDetailsField(jsonDir, strategy5Json, "Text", "", allResources, "", "", "JB Jenny Boo, JD Johny Doo");
		verifyResourceAssignments(jsonDir, strategy5Json, allResources);
		verifyExpenseAssignment(jsonDir, strategy5Json, Double.NaN, 0);
		
		EnhancedJsonObject strategy6Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[5]);
		verifyDetailsField(jsonDir, strategy6Json, "Text", Double.toString(3300.0), new Vector(), "2009-01-01 - 2009-12-31", "", "");
		verifyResourceAssignments(jsonDir, strategy6Json, new Vector());
		verifyExpenseAssignment(jsonDir, strategy6Json, 3300.0, 1);
		
		EnhancedJsonObject strategy7Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[6]);
		verifyDetailsField(jsonDir, strategy7Json, "Text", Double.toString(1300.0), expected30ResourceList, "", "", "JD Johny Doo");
		verifyResourceAssignments(jsonDir, strategy7Json, expected30ResourceList);
		verifyExpenseAssignment(jsonDir, strategy7Json, 1300.0, 1);
		
		EnhancedJsonObject strategy8Json = getObjectFileAsJson(jsonDir, STRATEGY_TYPE, strategyRawIds[7]);
		verifyDetailsField(jsonDir, strategy8Json, "Text", "", expected36ResourceList, "2009-01-01 - 2009-12-31", "", "JB Jenny Boo");
		verifyResourceAssignments(jsonDir, strategy8Json, expected36ResourceList);
		verifyExpenseAssignment(jsonDir, strategy8Json, Double.NaN, 0);
	}

	private void verifyHighLevelConversion(int expectedHighLevelConversionCount) throws Exception
	{
		int convertHighLevelEstimatesCount = ConvertHighLevelEstimatesIntoAssignments.convertToAssignments();
		assertEquals("wrong number of high level estimates were converted?", expectedHighLevelConversionCount, convertHighLevelEstimatesCount);
	}

	private EnhancedJsonObject getObjectFileAsJson(File jsonDir, final int objectType, final int rawId) throws Exception
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		File objectFile =  new File(objectDir, Integer.toString(rawId));
		
		return new EnhancedJsonObject(readFile(objectFile));
	}
	
	private void verifyDetailsField(File jsonDir, EnhancedJsonObject parentJson, String detailTag, String expectedExpenseAmount, Vector<Integer> expectedResourceIds, String expectedOverrideWhen, String originalDetailsString, String appendedResourceNames) throws Exception
	{
		if (parentJson.getString("BudgetCostMode").equals(""))
				return;
				
		String migrationDetialsText = "Migrated High Level Estimate:";
		final String NEW_LINE = "\n";
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("Budget Override was: " + expectedExpenseAmount) ;
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("When Override was: " + expectedOverrideWhen);
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("Who Override was: " + appendedResourceNames);
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += "---------------------------------------------------";
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += originalDetailsString;
		
		String expectedDetailsString = parentJson.getString(detailTag);
		assertEquals("wrong detials for object?", migrationDetialsText, expectedDetailsString);
	}

	private void verifyResourceAssignments(File jsonDir, EnhancedJsonObject json, Vector<Integer> expectedResourceIds) throws Exception
	{
		final int RESOURCE_ASSIGNMENT_TYPE = 14;
		File resourceAssignmentDir = DataUpgrader.getObjectsDir(jsonDir, RESOURCE_ASSIGNMENT_TYPE);
		IdList resourceAssignmentIds = json.getIdList(ResourceAssignment.getObjectType(), "AssignmentIds");
		assertEquals("wrong resource assignment count?", expectedResourceIds.size(), resourceAssignmentIds.size());
		for (int index = 0; index < resourceAssignmentIds.size(); ++index)
		{
			BaseId resourceAssignmentId = resourceAssignmentIds.get(index);
			File resourceAssignmentFile = new File(resourceAssignmentDir, Integer.toString(resourceAssignmentId.asInt()));
			EnhancedJsonObject resourceAssignmentJson = new EnhancedJsonObject(readFile(resourceAssignmentFile));
			
			verifyManifestFile(jsonDir, RESOURCE_ASSIGNMENT_TYPE, resourceAssignmentId);
			assertTrue("wrong resource id for resourceAssignment?", expectedResourceIds.contains(resourceAssignmentJson.getId("ResourceId").asInt()));
			verifyDateUnitEffortList(resourceAssignmentJson, new DateUnit(), 0);
		}
	}

	private void verifyExpenseAssignment(File jsonDir, EnhancedJsonObject parentJson, double expectedExpenseAmount, int expectedExpenseAssignmentCount) throws Exception
	{
		ORefList expenseAssignmentRefs = parentJson.getRefList("ExpenseRefs");
		assertEquals("wrong expense assignment count?", expectedExpenseAssignmentCount, expenseAssignmentRefs.size());
		if (expectedExpenseAssignmentCount == 0)
			return;
		
		BaseId expenseAssignmentId = expenseAssignmentRefs.get(0).getObjectId();
		final int EXPENSE_ASSIGNMENT_TYPE = 51;
		verifyManifestFile(jsonDir, EXPENSE_ASSIGNMENT_TYPE, expenseAssignmentId);
		
		EnhancedJsonObject expenseAssignmentJson = getObjectFileAsJson(jsonDir, EXPENSE_ASSIGNMENT_TYPE, expenseAssignmentId.asInt());
		verifyDateUnitEffortList(expenseAssignmentJson, new DateUnit(), expectedExpenseAmount);
	}

	private void verifyManifestFile(File jsonDir, final int objectType, BaseId idInsideManifest) throws Exception
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		File manifestFile = new File(objectDir, "manifest");
		assertTrue("assignment manifest file could not be found?", manifestFile.exists());
		
		ObjectManifest manifest = new ObjectManifest(JSONFile.read(manifestFile));
		assertTrue("manifest does not contain id as a key?", manifest.has(idInsideManifest));
	}
	
	private void verifyDateUnitEffortList(EnhancedJsonObject assignmentJson, DateUnit expectedDateUnit, double expectedQuantity) throws Exception
	{
		DateUnitEffortList assignmentDetails = new DateUnitEffortList(assignmentJson.getString("Details"));
		assertEquals("wrong dateUnitEffortList element count for expense assignment details?", 1, assignmentDetails.size());
		DateUnitEffort dateUnitEffort = assignmentDetails.getDateUnitEffort(0);
		assertEquals("wrong quantity?", expectedQuantity, dateUnitEffort.getQuantity());
		assertEquals("wrong date unit for effort?", expectedDateUnit, dateUnitEffort.getDateUnit());
	}
}
