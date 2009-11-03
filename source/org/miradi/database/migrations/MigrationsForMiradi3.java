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

import org.miradi.database.DataUpgrader;
import org.miradi.main.EAM;
import org.miradi.utils.CodeList;

public class MigrationsForMiradi3
{
	public static void upgradeToVersion39() throws Exception
	{
		MigrationsOlderThanMiradiVersion2.enableThreats();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 39);
	}

	public static void upgradeToVersion40() throws Exception
	{
		CreateScopeBoxesSuroundingTargetsMigration migration = new CreateScopeBoxesSuroundingTargetsMigration(DataUpgrader.getTopJsonDir());
		migration.surroundTargetsWithNewScopeBoxType();		
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 40);
	}

	public static void upgradeToVersion41() throws Exception
	{
		//NOTE: Allow saving work units for more types of date units
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 41);
	}

	public static void upgradeToVersion42() throws Exception
	{
		ConvertDateRangeEffortListToDateUnitEffortList.convertToDateUnitEffortList();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 42);
	}
	
	public static void upgradeToVersion43() throws Exception
	{
		int convertedHightLevelEstimateCount = ConvertHighLevelEstimatesIntoAssignments.convertToAssignments();
		if (convertedHightLevelEstimateCount > 0)
			EAM.notifyDialog(EAM.text("<html>" +
					"One or more Overrides (High Level Estimates) have been migrated. <br>" +
					"<br>" +
					"Any Who overrides have been converted to Resource Assignments, <br>" +
					"and any Budget overrides have been converted to Expenses. <br>" +
					"<br>" +
					"The original overrides have been inserted as text into the Details fields,<br>" +
					"in case you want to refer to them. You can delete them at any time."));
		
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 43);
	}
	
	public static void upgradeToVersion44() throws Exception
	{
		MoveStressCommentsDataToCommentField.moveCommentsToComment();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 44);
	}
	
	public static void upgradeToVersion45() throws Exception
	{
		RenameCommentFieldToCommentsMigration.renameCommentFieldToComments();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 45);
	}
	
	public static void upgradeToVersion46() throws Exception
	{
		RemoveTextBoxLinksMigration.removeTextBoxLinks();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 46);
	}
	
	public static void upgradeToVersion47() throws Exception
	{
		MaterialToPersonCodeConverterMigration.convertMaterialToPersonCode();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 47);
	}
	
	public static void upgradeToVersion48() throws Exception
	{
		CodeList operatingUnitCodesRemoved = UpdateTncOpertingUnitMigration.updateTncOperatingUnitsList();
		if (operatingUnitCodesRemoved.size() > 0)
			EAM.notifyDialog(EAM.text("<html>An Operating Unit for this project has been <br>" +
									  "superseded. Please select its replacement on the TNC Tab of the Summary Page.</html>"));
	
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 48);
	}
	
	public static void upgradeToVersion49() throws Exception
	{
		ShareSameLabeledScopeBoxesMigration.shareSameLabeledScopeBoxesAcrossAllDiagrams();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 49);
	}
}
