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

import org.miradi.database.migrations.MigrationsForMiradi3;
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
		createObjectFiles(jsonDir, RESOURCE_ASSIGNMENT_TYPE, resourceAssignmentRawIds, new String[] {resourceAssignment, });
		
		int[] expenseAssignmentRawIds = {34, };
		final int EXPENSE_ASSIGNMENT_TYPE = 51;
		createObjectFiles(jsonDir, EXPENSE_ASSIGNMENT_TYPE, expenseAssignmentRawIds, new String[] {expenseAssignment, });
		
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
}
