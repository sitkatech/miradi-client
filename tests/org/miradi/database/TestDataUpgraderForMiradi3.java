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
import java.util.Vector;

import org.miradi.database.migrations.MigrationsForMiradi3;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ResourceAssignment;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class TestDataUpgraderForMiradi3 extends AbstractMigration
{
	public TestDataUpgraderForMiradi3(String name)
	{
		super(name);
	}
	
	public void testConvertToDateUnitEffortList() throws Exception
	{
		String resourceAssignment = "{\"AssignmentIds\":\"\",\"AccountingCode\":\"\",\"ResourceId\":\"36\",\"Details\":\"{\\\"DateRangeEfforts\\\":[{\\\"NumberOfUnits\\\":15,\\\"DateRange\\\":{\\\"EndDate\\\":\\\"2010-12-31\\\",\\\"StartDate\\\":\\\"2010-01-01\\\"},\\\"CostUnitCode\\\":\\\"\\\"}]}\",\"ExpenseRefs\":\"\",\"BudgetCostOverride\":\"\",\"FundingSource\":\"\",\"WhoOverrideRefs\":\"\",\"WhenOverride\":\"\",\"TimeStampModified\":\"1242142436461\",\"BudgetCostMode\":\"\",\"Id\":35,\"Label\":\"\"}";
		String expenseAssignment = "{\"AssignmentIds\":\"\",\"WhenOverride\":\"\",\"AccountingCodeRef\":\"\",\"FundingSourceRef\":\"\",\"TimeStampModified\":\"1242143768537\",\"BudgetCostOverride\":\"\",\"ExpenseRefs\":\"\",\"Details\":\"{\\\"DateRangeEfforts\\\":[{\\\"NumberOfUnits\\\":10,\\\"DateRange\\\":{\\\"EndDate\\\":\\\"2010-12-31\\\",\\\"StartDate\\\":\\\"2010-01-01\\\"},\\\"CostUnitCode\\\":\\\"\\\"}]}\",\"BudgetCostMode\":\"\",\"Label\":\"n\",\"Id\":34,\"WhoOverrideRefs\":\"\"}";
		
		File jsonDir = createJsonDir();
		
		int[] resourceAssignmentRawIds = {35, };
		final int RESOURCE_ASSIGNMENT_TYPE = 14;
		createObjectFiles(jsonDir, RESOURCE_ASSIGNMENT_TYPE, new String[] {resourceAssignment, });
		
		int[] expenseAssignmentRawIds = {34, };
		final int EXPENSE_ASSIGNMENT_TYPE = 51;
		createObjectFiles(jsonDir, EXPENSE_ASSIGNMENT_TYPE, new String[] {expenseAssignment, });
		
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
		String projectResourceJohny = "{\"ExpenseRefs\":\"\",\"Organization\":\"\",\"IMAddress\":\"\",\"RoleCodes\":\"\",\"ResourceType\":\"\",\"SurName\":\"Doo\",\"BudgetCostMode\":\"\",\"AlternativeEmail\":\"\",\"Custom.Custom1\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"PhoneNumberOther\":\"\",\"Custom.Custom2\":\"\",\"BudgetCostOverride\":\"\",\"Location\":\"\",\"CostUnit\":\"\",\"PhoneNumber\":\"\",\"CostPerUnit\":\"100.0\",\"WhoOverrideRefs\":\"\",\"Name\":\"Johny\",\"WhenOverride\":\"\",\"Email\":\"\",\"TimeStampModified\":\"1242671324141\",\"Initials\":\"\",\"PhoneNumberMobile\":\"\",\"PhoneNumberHome\":\"\",\"Position\":\"\",\"DateUpdated\":\"\",\"Label\":\"\",\"Id\":30,\"IMService\":\"\"}";
		String projectResourceJenny = "{\"ExpenseRefs\":\"\",\"Organization\":\"\",\"IMAddress\":\"\",\"RoleCodes\":\"\",\"ResourceType\":\"\",\"SurName\":\"Boo\",\"BudgetCostMode\":\"\",\"AlternativeEmail\":\"\",\"Custom.Custom1\":\"\",\"Comments\":\"\",\"AssignmentIds\":\"\",\"PhoneNumberOther\":\"\",\"Custom.Custom2\":\"\",\"BudgetCostOverride\":\"\",\"Location\":\"\",\"CostUnit\":\"\",\"PhoneNumber\":\"\",\"CostPerUnit\":\"135.0\",\"WhoOverrideRefs\":\"\",\"Name\":\"Jenny\",\"WhenOverride\":\"\",\"Email\":\"\",\"TimeStampModified\":\"1242671363962\",\"Initials\":\"\",\"PhoneNumberMobile\":\"\",\"PhoneNumberHome\":\"\",\"Position\":\"\",\"DateUpdated\":\"\",\"Label\":\"\",\"Id\":36,\"IMService\":\"\"}";
		
		String taskWithCostWhenWho = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Activity\",\"Details\":\"\",\"ExpenseRefs\":\"\",\"BudgetCostOverride\":\"2000.0\",\"Comment\":\"\",\"SubtaskIds\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36}]}\",\"Text\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-05-18\\\",\\\"StartDate\\\":\\\"2009-01-18\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242671492099\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"\",\"Id\":39,\"ProgressReportRefs\":\"\"}";
		
		String indicatorWithCostWhenWho = "{\"RatingSource\":\"\",\"FutureStatusDetail\":\"\",\"ExpenseRefs\":\"\",\"MeasurementRefs\":\"\",\"Detail\":\"This is some indicator sample details.\",\"FutureStatusRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"FutureStatusDate\":\"\",\"FutureStatusComment\":\"\",\"ViabilityRatingsComment\":\"\",\"ProgressReportRefs\":\"\",\"ThresholdDetails\":\"\",\"IndicatorThresholds\":\"\",\"AssignmentIds\":\"\",\"FutureStatusSummary\":\"\",\"BudgetCostOverride\":\"5000.0\",\"Comment\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36},{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",\"Priority\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-03-18\\\",\\\"StartDate\\\":\\\"2009-01-01\\\"}\",\"TimeStampModified\":\"1242671448223\",\"TaskIds\":\"\",\"Label\":\"\",\"Id\":38}";
		String indicatorInRollupMode = "{\"RatingSource\":\"\",\"FutureStatusDetail\":\"\",\"ExpenseRefs\":\"\",\"MeasurementRefs\":\"\",\"Detail\":\"\",\"FutureStatusRating\":\"\",\"BudgetCostMode\":\"\",\"FutureStatusDate\":\"\",\"FutureStatusComment\":\"\",\"ViabilityRatingsComment\":\"\",\"ProgressReportRefs\":\"\",\"ThresholdDetails\":\"\",\"IndicatorThresholds\":\"\",\"AssignmentIds\":\"\",\"FutureStatusSummary\":\"\",\"BudgetCostOverride\":\"100.0\",\"Comment\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36},{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",\"Priority\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2010-05-18\\\",\\\"StartDate\\\":\\\"2009-05-18\\\"}\",\"TimeStampModified\":\"1242674469821\",\"TaskIds\":\"\",\"Label\":\"\",\"Id\":37}";
		
		String strategyStringWithAll = "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"125.0\", \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36}]}\",                                           \"ImpactRating\":\"\",\"Text\":\"This is some sample details.\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-06-27\\\",\\\"StartDate\\\":\\\"2009-05-18\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242671457090\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":24,\"ProgressReportRefs\":\"\"}";
		String strategyWithNoData =    "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242750411791\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":45,\"ProgressReportRefs\":\"\"}";
		String strategyWithCost =      "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"4500.0\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242750671823\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":51,\"ProgressReportRefs\":\"\"}";
		String strategyWithWhen =      "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2010-05-19\\\",\\\"StartDate\\\":\\\"2009-05-19\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242750770272\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":53,\"ProgressReportRefs\":\"\"}";
		String strategyWithWho =       "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36},{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",\"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242750832311\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":55,\"ProgressReportRefs\":\"\"}";
		String strategyWithCostWhen =  "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"3300.0\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"\",                                                                                                            \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-12-31\\\",\\\"StartDate\\\":\\\"2009-01-01\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242751095849\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":57,\"ProgressReportRefs\":\"\"}";
		String strategyWithCostWho =   "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"1300.0\",\"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":30}]}\",                                           \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"\",                                                                         \"GoalIds\":\"\",\"TimeStampModified\":\"1242751193459\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":61,\"ProgressReportRefs\":\"\"}";
		String strategyWithWhoWhen =   "{\"ObjectiveIds\":\"\",\"IndicatorIds\":\"\",\"AssignmentIds\":\"\",\"Type\":\"Intervention\",\"BudgetCostOverride\":\"\",      \"ExpenseRefs\":\"\",\"Comment\":\"\",\"TaxonomyCode\":\"\",\"LegacyTncStrategyRanking\":\"\",\"ShortLabel\":\"\",\"WhoOverrideRefs\":\"{\\\"References\\\":[{\\\"ObjectType\\\":7,\\\"ObjectId\\\":36}]}\",                                           \"ImpactRating\":\"\",\"Text\":\"\",\"Status\":\"\",\"WhenOverride\":\"{\\\"EndDate\\\":\\\"2009-12-31\\\",\\\"StartDate\\\":\\\"2009-01-01\\\"}\",\"GoalIds\":\"\",\"TimeStampModified\":\"1242751270397\",\"ActivityIds\":\"\",\"FeasibilityRating\":\"\",\"BudgetCostMode\":\"BudgetOverrideMode\",\"KeyEcologicalAttributeIds\":\"\",\"Label\":\"New Strategy\",\"Id\":63,\"ProgressReportRefs\":\"\"}";
		
		File jsonDir = createJsonDir();
		
		final int TASK_TYPE = 3;
		int[] taskRawIds = createObjectFiles(jsonDir, TASK_TYPE, new String[] {taskWithCostWhenWho, });
		
		final int INDICATOR_TYPE = 8;
		int[] indicatorRawIds = createObjectFiles(jsonDir, INDICATOR_TYPE, new String[] {indicatorWithCostWhenWho, indicatorInRollupMode, });
		
		final int STRATEGY_TYPE = 21;
		int[] strategyRawIds = createObjectFiles(jsonDir, STRATEGY_TYPE, new String[] {strategyStringWithAll, strategyWithNoData, strategyWithCost, strategyWithWhen, strategyWithWho, 
																				strategyWithCostWhen, strategyWithCostWho, strategyWithWhoWhen, });
		
		final int PROJECT_RESOURCE_TYPE = 7;
		createObjectFiles(jsonDir, PROJECT_RESOURCE_TYPE, new String[]{projectResourceJohny, projectResourceJenny, });
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":90}");
		
		DataUpgrader.initializeStaticDirectory(tempDirectory);
		MigrationsForMiradi3.upgradeToVersion43();
		
		Vector<Integer> expected36ResourceList = new Vector<Integer>();
		expected36ResourceList.add(36);
		
		Vector<Integer> expected30ResourceList = new Vector<Integer>();
		expected30ResourceList.add(30);
		
		Vector<Integer> allResources = new Vector();
		allResources.addAll(expected36ResourceList);
		allResources.addAll(expected30ResourceList);
		
		
		verifyAssignments(jsonDir, TASK_TYPE, taskRawIds[0], 2000.0, expected36ResourceList, 1, "Text", "2009-01-18 - 2009-05-18", "", "Jenny Boo");
		
		verifyAssignments(jsonDir, INDICATOR_TYPE, indicatorRawIds[0], 5000.0, allResources, 1, "Detail", "2009-01-01 - 2009-03-18", "This is some indicator sample details.", "Jenny Boo, Johny Doo");		
		verifyAssignments(jsonDir, INDICATOR_TYPE, indicatorRawIds[1], 0.0, new Vector(),      0, "Detail", "", "", "");
		
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[0], 125.0, expected36ResourceList,    1, "Text", "2009-05-18 - 2009-06-27", "This is some sample details.", "Jenny Boo");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[1], Double.NaN, new Vector(),        0, "Text", "", "", "");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[2], 4500.0, new Vector(),   1, "Text", "", "", "");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[3], Double.NaN, new Vector(),        0, "Text", "2009-05-19 - 2010-05-19", "", "");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[4], Double.NaN, allResources,        0, "Text", "", "", "Jenny Boo, Johny Doo");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[5], 3300.0, new Vector(),   1, "Text", "2009-01-01 - 2009-12-31", "", "");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[6], 1300.0, expected30ResourceList,   1, "Text", "", "", "Johny Doo");
		verifyAssignments(jsonDir, STRATEGY_TYPE, strategyRawIds[7], Double.NaN, expected36ResourceList,        0, "Text", "2009-01-01 - 2009-12-31", "", "Jenny Boo");
	}
	
	private void verifyAssignments(File jsonDir, final int objectType, int idAsInt, double expectedExpenseAmount, Vector<Integer> expectedResourceIds, int expectedExpenseAssignmentCount, String detailTag, String expectedOverrideWhen, String originalDetailsString, String appendedResourceNames) throws Exception
	{
		File objectsDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		File objectFile =  new File(objectsDir, Integer.toString(idAsInt));
		EnhancedJsonObject parentJson = new EnhancedJsonObject(readFile(objectFile));
		verifyDetailsField(jsonDir, parentJson, detailTag, Double.toString(expectedExpenseAmount), expectedResourceIds, expectedOverrideWhen, originalDetailsString, appendedResourceNames);
		verifyResourceAssignments(jsonDir, parentJson, expectedResourceIds);
		verifyExpenseAssignment(jsonDir, parentJson, expectedExpenseAmount, expectedExpenseAssignmentCount);
	}

	private void verifyDetailsField(File jsonDir, EnhancedJsonObject parentJson, String detailTag, String expectedExpenseAmount, Vector<Integer> expectedResourceIds, String expectedOverrideWhen, String originalDetailsString, String appendedResourceNames) throws Exception
	{
		if (parentJson.getString("BudgetCostMode").equals(""))
				return;
				
		String migrationDetialsText = "Migrated High Level Estimate:";
		final String NEW_LINE = "\n";
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("Budget Override was:" + expectedExpenseAmount) ;
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("When Override was:" + expectedOverrideWhen);
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("Who Override was:" + appendedResourceNames);
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
			File resourceAssignmentFile = new File(resourceAssignmentDir, Integer.toString(resourceAssignmentIds.get(index).asInt()));
			EnhancedJsonObject resourceAssignmentJson = new EnhancedJsonObject(readFile(resourceAssignmentFile));
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
		
		int expenseAssignmentId = expenseAssignmentRefs.get(0).getObjectId().asInt();
		
		final int EXPENSE_ASSIGNMENT_TYPE = 51;
		File expenseAssignmentDir = DataUpgrader.getObjectsDir(jsonDir, EXPENSE_ASSIGNMENT_TYPE);
		File expenseAssignmentFile = new File(expenseAssignmentDir, Integer.toString(expenseAssignmentId));
		EnhancedJsonObject expenseAssignmentJson = new EnhancedJsonObject(readFile(expenseAssignmentFile));
		
		verifyDateUnitEffortList(expenseAssignmentJson, new DateUnit(), expectedExpenseAmount);
	}
	
	private void verifyDateUnitEffortList(EnhancedJsonObject assignmentJson, DateUnit expectedDateUnit, double expectedCost) throws Exception
	{
		DateUnitEffortList assignmentDetails = new DateUnitEffortList(assignmentJson.getString("Details"));
		assertEquals("wrong dateUnitEffortList element count for expense assignment details?", 1, assignmentDetails.size());
		DateUnitEffort dateUnitEffort = assignmentDetails.getDateUnitEffort(0);
		assertEquals("wrong expense ?", expectedCost, dateUnitEffort.getUnitQuantity());
		assertEquals("wrong date unit for effort?", expectedDateUnit, dateUnitEffort.getDateUnit());
	}
}
