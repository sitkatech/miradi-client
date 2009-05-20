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
package org.miradi.database.migrations;

import java.io.File;

import org.miradi.database.DataUpgrader;
import org.miradi.database.JSONFile;
import org.miradi.database.ObjectManifest;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;

public class ConvertHighLevelEstimatesIntoAssignments
{
	public static void convertToAssignments() throws Exception
	{	
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int TASK_TYPE = 3;
		convertToAssignments(jsonDir, TASK_TYPE, "Details");
		
		final int INDICATOR_TYPE = 8;
		convertToAssignments(jsonDir, INDICATOR_TYPE, "Detail");
		
		final int STRATEGY_TYPE = 21;
		convertToAssignments(jsonDir, STRATEGY_TYPE, "Text");
	}

	private static void convertToAssignments(File jsonDir, final int objectType, String detailsTag) throws Exception
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		if (! objectDir.exists())
			return;
		
		File manifestFile = createManifestFile(objectDir);
		if (! manifestFile.exists())
			return;
		
		ObjectManifest taskManifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] ids = taskManifestObject.getAllKeys();
		for (int index = 0; index < ids.length; ++index)
		{
			BaseId id = ids[index];
			File objectFile = new File(objectDir, Integer.toString(id.asInt()));
			EnhancedJsonObject objectJson = DataUpgrader.readFile(objectFile);
			if (objectJson.getString("BudgetCostMode").equals("BudgetOverrideMode"))
			{
				createExpenseAssignment(jsonDir, objectFile, objectJson);
				createResourceAssignment(jsonDir, objectFile, objectJson);
				updateDetailsTextWithOverrideData(jsonDir, objectFile, objectJson, detailsTag);
			}
		}
	}

	private static void createExpenseAssignment(File jsonDir, File objectFile, EnhancedJsonObject objectJson) throws Exception
	{
		double costOverride = objectJson.optDouble("BudgetCostOverride");
		if (Double.isNaN(costOverride))
			return;
		
		final int EXPENSE_ASSIGNMENT_TYPE = 51;
		File expenseAssignmentDir = DataUpgrader.getObjectsDir(jsonDir, EXPENSE_ASSIGNMENT_TYPE);
		if (! expenseAssignmentDir.exists())
			DataUpgrader.createObjectsDir(jsonDir, EXPENSE_ASSIGNMENT_TYPE);

		EnhancedJsonObject expenseAssignmentManifestJson = getOrCreateExpenseManifestObject(expenseAssignmentDir);
		
		EnhancedJsonObject expenseAssignmentJson = new EnhancedJsonObject();
		expenseAssignmentJson.put("Details", createSingleElementDateUnitEffortList(costOverride, new DateUnit()));
		int newlyCreatedId = createAssignment(jsonDir, expenseAssignmentDir, expenseAssignmentManifestJson, expenseAssignmentJson);
		
		ORefList currentExpenseAssignmentRefs = objectJson.optRefList("ExpenseRefs");
		currentExpenseAssignmentRefs.add(new ORef(EXPENSE_ASSIGNMENT_TYPE, new BaseId(newlyCreatedId)));
		objectJson.put("ExpenseRefs", currentExpenseAssignmentRefs.toString());
		DataUpgrader.writeJson(objectFile, objectJson);
	}

	private static void createResourceAssignment(File jsonDir, File objectFile, EnhancedJsonObject objectJson) throws Exception
	{
		ORefList whoOverrideRefs = objectJson.optRefList("WhoOverrideRefs");
		if (whoOverrideRefs.isEmpty())
			return;

		final int RESOURCE_ASSIGNMENT_TYPE =  14;
		File resourceAssignmentDir = DataUpgrader.getObjectsDir(jsonDir, RESOURCE_ASSIGNMENT_TYPE);
		if (! resourceAssignmentDir.exists())
			DataUpgrader.createObjectsDir(jsonDir, RESOURCE_ASSIGNMENT_TYPE);

		EnhancedJsonObject resourceAssignmentManifestJson = getOrCreateExpenseManifestObject(resourceAssignmentDir);
		
		IdList newlyCreatedResourceAssignmentIds = objectJson.optIdList(RESOURCE_ASSIGNMENT_TYPE, "AssignmentIds");
		for (int index = 0; index < whoOverrideRefs.size(); ++index)
		{
			EnhancedJsonObject resourceAssignmentJson = new EnhancedJsonObject();
			resourceAssignmentJson.put("ResourceId", whoOverrideRefs.get(index).getObjectId().toString());
			resourceAssignmentJson.put("Details", createSingleElementDateUnitEffortList(0.0, new DateUnit()));
			int newlyCreatedId = createAssignment(jsonDir, resourceAssignmentDir, resourceAssignmentManifestJson, resourceAssignmentJson);
			newlyCreatedResourceAssignmentIds.add(new BaseId(newlyCreatedId));
		}
		
		objectJson.put("AssignmentIds", newlyCreatedResourceAssignmentIds.toString());
		DataUpgrader.writeJson(objectFile, objectJson);
	}
	
	private static void updateDetailsTextWithOverrideData(File jsonDir,	File objectFile, EnhancedJsonObject objectJson, String detailsTag) throws Exception
	{
		String originalDetailsText = objectJson.optString(detailsTag);
		String migrationDetialsText = "Migrated High Level Estimate:";
		final String NEW_LINE = "\n";
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("Budget Override was:" + objectJson.optDouble("BudgetCostOverride")) ;
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("When Override was:" + createOverrideWhenString(objectJson));
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += ("Who Override was:" + createAppendedResourceNames(jsonDir, objectJson));
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += "---------------------------------------------------";
		migrationDetialsText += NEW_LINE;
		migrationDetialsText += originalDetailsText;
		
		objectJson.put(detailsTag, migrationDetialsText);
		DataUpgrader.writeJson(objectFile, objectJson);
	}

	private static String createOverrideWhenString(EnhancedJsonObject objectJson) throws Exception
	{
		EnhancedJsonObject whenOverrideJson = new EnhancedJsonObject(objectJson.getString("WhenOverride"));
		String startDateAsString = whenOverrideJson.optString("StartDate");
		String endDateAsString = whenOverrideJson.optString("EndDate");
		if (startDateAsString.isEmpty() || endDateAsString.isEmpty())
			return "";
		
		String overrideWhenDates = startDateAsString + " - " + endDateAsString;
		return overrideWhenDates;
	}

	private static String createAppendedResourceNames(File jsonDir,	EnhancedJsonObject objectJson) throws Exception
	{
		ORefList whoOverrideRefs = objectJson.getRefList("WhoOverrideRefs");
		final int PROJECT_RESOURCE_TYPE = 7;
		File resourceDir = DataUpgrader.getObjectsDir(jsonDir, PROJECT_RESOURCE_TYPE);
		String appendedNames = "";
		for (int index = 0; index < whoOverrideRefs.size(); ++index)
		{
			Integer integer = whoOverrideRefs.get(index).getObjectId().asInt();
			File projectResourceFile = new File(resourceDir, Integer.toString(integer));
			EnhancedJsonObject projectResourceJson = new EnhancedJsonObject(DataUpgrader.readFile(projectResourceFile));
			String name = projectResourceJson.getString("Name");
			String surName = projectResourceJson.getString("SurName");
			if (index > 0)
				appendedNames += ", ";

			appendedNames += (name + " " +surName);
		}

		return appendedNames;
	}

	private static EnhancedJsonObject getOrCreateExpenseManifestObject(File assignmentDir) throws Exception
	{
		File assignmentManifestFile = createManifestFile(assignmentDir);
		if (assignmentManifestFile.exists())
			return DataUpgrader.readFile(assignmentManifestFile);

		EnhancedJsonObject assignemtnManifestJson = new EnhancedJsonObject();
		assignemtnManifestJson.put("Type", "ObjectManifest");
		
		return assignemtnManifestJson;
	}
	
	private static int createAssignment(File jsonDir, File assignmentDir, EnhancedJsonObject assignmentManifestJson, EnhancedJsonObject assignmentJsonWithoutIdKey) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		int id = ++highestId;
		assignmentManifestJson.put(Integer.toString(id), "true");
		assignmentJsonWithoutIdKey.put("Id", Integer.toString(id));
		
		File expenseAssignmentFile = new File(assignmentDir, Integer.toString(id));
		DataUpgrader.createFile(expenseAssignmentFile, assignmentJsonWithoutIdKey.toString());	
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, id);
		File assignmentManifestFile = createManifestFile(assignmentDir);
		DataUpgrader.writeJson(assignmentManifestFile, assignmentManifestJson);
		
		return id;
	}
	
	private static File createManifestFile(File assignmentDir)
	{
		return new File(assignmentDir, "manifest");
	}
		
	private static String createSingleElementDateUnitEffortList(double cost, DateUnit dateUnitToUse)
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(cost, dateUnitToUse);
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		
		return dateUnitEffortList.toString();
	}
}
