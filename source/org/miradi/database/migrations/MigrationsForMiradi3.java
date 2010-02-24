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
import java.util.Vector;

import org.miradi.database.DataUpgrader;
import org.miradi.database.JSONFile;
import org.miradi.database.ObjectManifest;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class MigrationsForMiradi3
{
	public static void upgradeToVersion37() throws Exception
	{
		MigrationsForMiradi3.addThreatRefAndRemoveThreatStressRatingRefsFromFactorLinks();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 37);
	}
	
	private static void addThreatRefAndRemoveThreatStressRatingRefsFromFactorLinks() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int FACTOR_LINK_TYPE = 6;
		File factorLinkDir = DataUpgrader.getObjectsDir(jsonDir, FACTOR_LINK_TYPE);
		if (! factorLinkDir.exists())
			return;
		
		File factorLinkManifestFile = new File(factorLinkDir, "manifest");
		if (! factorLinkManifestFile.exists())
			return;
		
		final int THREAT_STRESS_RATING_TYPE = 34;
		File threatStressRatingDir = DataUpgrader.getObjectsDir(jsonDir, THREAT_STRESS_RATING_TYPE);
		if (!threatStressRatingDir.exists())
			return;
		
		File threatStressRatingManifestFile = new File(factorLinkDir, "manifest");
		if (! threatStressRatingManifestFile.exists())
			return;
		
		ObjectManifest factorLinkManifestObject = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		BaseId[] factorLinkIds = factorLinkManifestObject.getAllKeys();
		for (int i = 0; i < factorLinkIds.length; ++i)
		{
			BaseId thisId = factorLinkIds[i];
			File factorLinkJsonFile = new File(factorLinkDir, Integer.toString(thisId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkJsonFile);
			ORef threatRef = MigrationsOlderThanMiradiVersion2.getCauseIfDirectlyUpstreamFromTarget(factorLinkJson);
			if (!threatRef.isInvalid())
			{
				MigrationsOlderThanMiradiVersion2.addThreatRefToThreatStressRatings(threatStressRatingDir, factorLinkJson, threatRef);
				MigrationsOlderThanMiradiVersion2.removeThreatStressRatingField(factorLinkJsonFile, factorLinkJson);
			}
		}
	}

	public static void upgradeToVersion38() throws Exception
	{
		MigrationsOlderThanMiradiVersion2.moveFactorLinkCommentFieldsIntoThreatRatingCommentsData();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 38);
	}

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
					"Any Budget overrides have been converted to Expenses, <br>" +
					"and any \"Who\" overrides have been converted to Resource Assignments. <br>" +
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
	
	public static void upgradeToVersion50() throws Exception
	{
		RemoveNonExistingRelatedThreatFromThreatReductionResultMigration.removeNonExistingRelatedThreatRef();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 50);
	}
	
	public static void upgradeToVersion51() throws Exception
	{
		MoveTncProjectAreaSizeWithinProjectMetadataMigration.moveTncProjectAreaSize();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 51);
	}
	
	public static void upgradeToVersion52() throws Exception
	{
		Vector<EnhancedJsonObject> duplicates = EnsureNoMoreThanOneXenodataMigration.enureNoMoreThanOneXenodata();
		if (duplicates.size() > 1)
			EAM.notifyDialog(EAM.text("<html>This project has more than one TNC ConPro project ID. It is safe to view and make <BR>" +
									  "changes to this project, but the ConPro ID will not be displayed until this problem <BR>" +
									  "is resolved.  Please contact the Miradi Team for further assistance.  </html>"));
		
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 52);
	}
	
	public static void upgradeToVersion53() throws Exception
	{
		ScopeBoxLegacyColorCodeUpdateMigration.updateLegacyScopeBoxColorCode();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 53);
	}

	public static void upgradeToVersion54() throws Exception
	{
		CreateObjectFromSingleLegacyTextField.createNewTypesFromLegacyFields();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 54);
	}
	
	public static void upgradeToVersion55() throws Exception
	{
		FactorLinkBidiFalseBooleanValueFixMigration.fixFactorLinkBidiFalseValues();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 55);
	}
	
	public static void upgradeToVersion56() throws Exception
	{
		EnsureProjectMetadataRefersToCorrectXenodataObject.ensureCorrectXenodataReferrer();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 56);
	}
}
