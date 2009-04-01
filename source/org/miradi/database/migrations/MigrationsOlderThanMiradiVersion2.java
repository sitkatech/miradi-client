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

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.json.JSONObject;
import org.martus.util.DirectoryUtils;
import org.miradi.database.DataUpgrader;
import org.miradi.database.DataUpgraderDiagramObjectLinkAdder;
import org.miradi.database.JSONFile;
import org.miradi.database.ObjectManifest;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.factortypes.FactorType;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.Cause;
import org.miradi.objects.Measurement;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.questions.TwoLevelQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.PointList;

public class MigrationsOlderThanMiradiVersion2
{

	public static void upgradeToVersion40() throws Exception
	{
		CreateScopeBoxesSuroundingTargetsMigration migration = new CreateScopeBoxesSuroundingTargetsMigration(DataUpgrader.getTopJsonDir());
		migration.surroundTargetsWithNewScopeBoxType();		
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 40);
	}

	public static void upgradeToVersion39() throws Exception
	{
		enableThreats();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 39);
	}

	public static void upgradeToVersion38() throws Exception
	{
		moveFactorLinkCommentFieldsIntoThreatRatingCommentsData();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 38);
	}

	public static void upgradeToVersion37() throws Exception
	{
		addThreatRefAndRemoveThreatStressRatingRefsFromFactorLinks();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 37);
	}

	public static void upgradeToVersion36() throws Exception
	{
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 36);
	}

	public static void upgradeToVersion35() throws Exception
	{
		moveFactorsToSpecificDirs();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 35);
	}

	public static void upgradeToVersion34() throws Exception
	{
		while(deleteOrphanedTasks() > 0)
		{
		}
		
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 34);
	}

	public static void upgradeToVersion33() throws Exception
	{
		boolean isNonBlankEcoRegions = copyTncEcoRegionFieldOverToDividedTerrestrailMarineFreshwaterEcoRegions(); 
		if (isNonBlankEcoRegions)
			EAM.notifyDialog(EAM.text("<HTML>The TNC ecoregion field has been changed from a single text field to three picklists. <BR>" +
									  "Miradi has attempted to migrate the ecoregion data, but may not have been successful. <BR>" +
									  "Please go to the Summary View, TNC tab, and verify that the ecoregion(s) are correct for this project.</HTML>"));
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 33);
	}

	public static void upgradeToVersion32() throws Exception
	{
		boolean isNonBlankOperatingUnit = copyTncOperatingUnitsFieldDataOverToNewPickListField();
		if (isNonBlankOperatingUnit)
			EAM.notifyDialog(EAM.text("<HTML>The TNC Operating Unit field has been changed from a text field to a picklist. <BR>" +
									  "Miradi has attemped to migrate existing data, but it may not have been successful. <BR>" +
									  "Please go to the Summary View, TNC tab and confirm that the Operating Unit is set correctly for this project.</HTML>"));
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 32);
	}

	public static void upgradeToVersion31() throws Exception
	{
		copyTncProjectDataSizeInHectaresFieldOverToProjectMetaDataProjectAreaField();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 31);	
	}

	public static void upgradeToVersion30() throws Exception
	{
		notifyIfNonBlankTncCountryCode();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 30);	
	}

	public static void upgradeToVersion29() throws Exception
	{
		copyWwfProjectDataCountriesFieldOverToProjectMetaData();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 29);
	}

	public static void upgradeToVersion28() throws Exception
	{
		EAM.notifyDialog(EAM.text("<html>" +
				"Miradi now allows you to specify what currency is being used for budgeting, " +
				"<br>and to specify Fiscal Years that are different from Calendar Years." +
				"<br>Both of these options can be changed in the Summary View, on the Planning Settings tab." +
				"<br>" +
				"<br>The default currency is United States dollars; the default calendar is " +
				"<br>to have fiscal years equal to calendar years" +
				"<br>" +
				"<br>If your fiscal year does not run January-December, and if you have entered " +
				"<br>budget data into Miradi treating calendar years as fiscal years, your data may appear " +
				"<br>off by one or more quarters. If you have a lot of budget data like this, please contact " +
				"<br>the Miradi team to learn about options for realigning it to match your " +
				"<br>fiscal year." +
				""
				));
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 28);
	}

	public static void upgradeToVersion27() throws Exception
	{
		removeDuplicateBendPoints();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 27);
	}

	public static void upgradeToVersion26() throws Exception
	{
		notifyUserOfDeletedDuratingAndCostFields();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 26);
	}

	public static void upgradeToVersion25() throws Exception
	{
		createThreatStressRatingsForTargetThreatLinks();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 25);
	}

	public static void upgradeToVersion24() throws Exception
	{
		createdStressesFromFactorLinks();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 24);
	}

	public static void upgradeToVersion23() throws Exception
	{ 
		createMeasurementFromDataInIndicator();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 23);
	}

	public static void upgradeToVersion22() throws Exception
	{
		switchDiagramFactorWrappedIdsToRefs();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 22);
	}

	public static void upgradeToVersion21() throws Exception
	{
		new DataUpgraderDiagramObjectLinkAdder(DataUpgrader.getTopDirectory()).addLinksInAllDiagramsWhereNeeded();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 21);
	}

	public static void upgradeToVersion20() throws Exception
	{
		changeLinkFromToIdsToORefs();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 20);
	}

	public static void upgradeToVersion19() throws Exception
	{
		possiblyNotifyUserAfterUpgradingToVersion19();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 19);
	}

	public static void upgradeToVersion18() throws Exception
	{
		addLinksToDiagramContentsObject();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 18);
	}

	public static void upgradeToVersion17() throws Exception
	{
		createObject19DirAndFillFromDiagram();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 17);
	}

	public static void upgradeToVersion16() throws Exception
	{
		HashMap mappedFactorIds = createDiagramFactorsFromRawFactors();
		createDiagramFactorLinksFromRawFactorLinks(mappedFactorIds);
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 16);
	}

	public static void enableThreats() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int FACTOR_LINK_TYPE = 6;
		File factorLinkDir = DataUpgrader.getObjectsDir(jsonDir, FACTOR_LINK_TYPE);
		if (! factorLinkDir.exists())
			return;
		
		File factorLinkManifestFile = new File(factorLinkDir, "manifest");
		if (! factorLinkManifestFile.exists())
			return;
		
		final int CAUSE_TYPE = 20;
		File causeDir = DataUpgrader.getObjectsDir(jsonDir, CAUSE_TYPE);
		if (!causeDir.exists())
			return;
		
		ObjectManifest factorLinkManifestObject = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		BaseId[] factorLinkIds = factorLinkManifestObject.getAllKeys();
		for (int i = 0; i < factorLinkIds.length; ++i)
		{
			BaseId thisFactorLinkId = factorLinkIds[i];
			File factorLinkJsonFile = new File(factorLinkDir, Integer.toString(thisFactorLinkId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkJsonFile);
			ORef threatRef = getCauseIfDirectlyUpstreamFromTarget(factorLinkJson);
			if (!threatRef.isInvalid())
			{	
				File threatFile = new File(causeDir, threatRef.getObjectId().toString());
				EnhancedJsonObject threatJson = DataUpgrader.readFile(threatFile);
				threatJson.put("IsDirectThreat", BooleanData.BOOLEAN_TRUE);
				writeJsonFile(threatFile, threatJson);
			}
		}
	}

	public static void moveFactorLinkCommentFieldsIntoThreatRatingCommentsData() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int FACTOR_LINK_TYPE = 6;
		File factorLinkDir = DataUpgrader.getObjectsDir(jsonDir, FACTOR_LINK_TYPE);
		if (! factorLinkDir.exists())
			return;
		
		File factorLinkManifestFile = new File(factorLinkDir, "manifest");
		if (! factorLinkManifestFile.exists())
			return;
	
		final int THREAT_RATING_COMMENTS_DATA_TYPE = 49;
		File threatRatingCommentsDataDir = DataUpgrader.getObjectsDir(jsonDir, THREAT_RATING_COMMENTS_DATA_TYPE);
		if (threatRatingCommentsDataDir.exists())
			throw new RuntimeException("ThreatRatingCommentsData dirs exists");
		
		threatRatingCommentsDataDir.mkdirs();
		
		EnhancedJsonObject threatRatingCommentsDataManifestJson = new EnhancedJsonObject();
		threatRatingCommentsDataManifestJson.put("Type", "ObjectManifest");
		createEmptySingletonThreatRatingCommentsDataObject(threatRatingCommentsDataDir, threatRatingCommentsDataManifestJson, jsonDir);
		
		if (!threatRatingCommentsDataDir.exists())
			throw new RuntimeException("no ThreatRatingCommentsData dirs exists");
		
		File threatRatingCommentsDataManifestFile = new File(threatRatingCommentsDataDir, "manifest");
		if (! threatRatingCommentsDataManifestFile.exists())
			throw new RuntimeException("no ThreatRatingCommentsData manifest exist");
		
		StringMap simpleThreatRatingCommentsMap = new StringMap();
		StringMap stressThreatRatingCommentsMap = new StringMap();
		ObjectManifest factorLinkManifestObject = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		BaseId[] factorLinkIds = factorLinkManifestObject.getAllKeys();
		for (int i = 0; i < factorLinkIds.length; ++i)
		{
			BaseId thisFactorLinkId = factorLinkIds[i];
			File factorLinkJsonFile = new File(factorLinkDir, Integer.toString(thisFactorLinkId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkJsonFile);
			ORef threatRef = getCauseIfDirectlyUpstreamFromTarget(factorLinkJson);
			if (!threatRef.isInvalid())
			{	
				String threatRatingCommentsKey = createThreatRatingCommentsKey(factorLinkJson);
				
				String simpleThreatRatingComment = factorLinkJson.optString("SimpleThreatRatingComment");
				simpleThreatRatingCommentsMap.add(threatRatingCommentsKey, simpleThreatRatingComment);
				removeCommentsField(factorLinkJsonFile, factorLinkJson, "SimpleThreatRatingComment");
				
				String stressThreatRatingComment = factorLinkJson.optString("Comment");
				stressThreatRatingCommentsMap.add(threatRatingCommentsKey, stressThreatRatingComment);
				removeCommentsField(factorLinkJsonFile, factorLinkJson, "Comment");
				
				writeJsonFile(factorLinkJsonFile, factorLinkJson);
			}
		}
		
		ObjectManifest threatRatingCommentsDataManifest = new ObjectManifest(JSONFile.read(threatRatingCommentsDataManifestFile));
		BaseId[] threatRatingCommentsDataIds = threatRatingCommentsDataManifest.getAllKeys();
		if (threatRatingCommentsDataIds.length != 1)
			throw new RuntimeException("Incorrect number of ThreatRatingCommentsData files found,  this is a singleton object");
			
		File threatRatingCommentsDataFile = new File(threatRatingCommentsDataDir, Integer.toString(threatRatingCommentsDataIds[0].asInt()));
		EnhancedJsonObject threatRatingCommentsDataJson = DataUpgrader.readFile(threatRatingCommentsDataFile);
		threatRatingCommentsDataJson.put("SimpleThreatRatingCommentsMap", simpleThreatRatingCommentsMap.toString());
		threatRatingCommentsDataJson.put("StressBasedThreatRatingCommentsMap", stressThreatRatingCommentsMap.toString());
		writeJsonFile(threatRatingCommentsDataFile, threatRatingCommentsDataJson);
	}

	private static void writeJsonFile(File file, JSONObject json) throws IOException
	{
		file.getParentFile().mkdirs();
		JSONFile.write(file, json);
	}

	private static void createEmptySingletonThreatRatingCommentsDataObject(File threatRatingCommentsDataDir, EnhancedJsonObject threatRatingCommentsDataManifestJson, File jsonDir) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		int id = ++highestId;
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, id);
		
		threatRatingCommentsDataManifestJson.put(Integer.toString(id), "true");
			
		EnhancedJsonObject threatStressRatingJson = new EnhancedJsonObject();
		threatStressRatingJson.put("Id", Integer.toString(id));
		
		File threatRatingCommentsDataFile = new File(threatRatingCommentsDataDir, Integer.toString(id));
		DataUpgrader.createFile(threatRatingCommentsDataFile, threatStressRatingJson.toString());
		
		writeManifest(threatRatingCommentsDataDir, threatRatingCommentsDataManifestJson);
	}

	private static void removeCommentsField(File factorLinkJsonFile, EnhancedJsonObject factorLinkJson, String commentsField) throws Exception
	{
		factorLinkJson.remove(commentsField);
		DataUpgrader.writeJson(factorLinkJsonFile, factorLinkJson);
	}

	private static String createThreatRatingCommentsKey(EnhancedJsonObject factorLinkJson)
	{
		ORef fromRef = factorLinkJson.getRef("FromRef");
		ORef toRef = factorLinkJson.getRef("ToRef");
		if (Target.is(fromRef))
			return toRef.toString() + fromRef.toString();
		
		return fromRef.toString() + toRef.toString();
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
			ORef threatRef = getCauseIfDirectlyUpstreamFromTarget(factorLinkJson);
			if (!threatRef.isInvalid())
			{
				addThreatRefToThreatStressRatings(threatStressRatingDir, factorLinkJson, threatRef);
				removeThreatStressRatingField(factorLinkJsonFile, factorLinkJson);
			}
		}
	}

	public static void addThreatRefToThreatStressRatings(File threatStressRatingDir,EnhancedJsonObject factorLinkJson, ORef threatRef) throws Exception
	{
		ORefList threatStressRatingRefsForLink = factorLinkJson.optRefList("ThreatStressRatingRefs");
		for (int index = 0; index < threatStressRatingRefsForLink.size(); ++index)
		{
			BaseId threatStressRatingId = threatStressRatingRefsForLink.get(index).getObjectId();
			File threatStressRatingJsonFile = new File(threatStressRatingDir, Integer.toString(threatStressRatingId.asInt()));
			EnhancedJsonObject threatStressRatingJson = DataUpgrader.readFile(threatStressRatingJsonFile);
			threatStressRatingJson.put("ThreatRef", threatRef.toString());
			DataUpgrader.writeJson(threatStressRatingJsonFile, threatStressRatingJson);
		}
	}

	public static void removeThreatStressRatingField(File factorLinkJsonFile, EnhancedJsonObject factorLinkJson) throws Exception
	{
		factorLinkJson.remove("ThreatStressRatingRefs");
		DataUpgrader.writeJson(factorLinkJsonFile, factorLinkJson);
	}

	public static ORef getCauseIfDirectlyUpstreamFromTarget(EnhancedJsonObject factorLinkJson)
	{
		ORef fromRef = factorLinkJson.getRef("FromRef");
		ORef toRef = factorLinkJson.getRef("ToRef");
		if (isFromCauseAndToTarget(fromRef, toRef))
			return fromRef;
		
		String isBidirectionalAsString = factorLinkJson.optString("BidirectionalLink");
		if (isFromCauseAndToTarget(toRef, fromRef) && asBoolean(isBidirectionalAsString))
			return toRef;
		
		return ORef.INVALID;
	}

	public static boolean asBoolean(String booleanAsString)
	{
		if (booleanAsString.length() == 0)
			return false;
		
		if (booleanAsString.equals(BooleanData.BOOLEAN_FALSE))
			return false;
		
		if (booleanAsString.equals(BooleanData.BOOLEAN_TRUE))
			return true;
		
		throw new RuntimeException("Invalid boolean value :" + booleanAsString);
	}

	public static boolean isFromCauseAndToTarget(ORef threatRef, ORef targetRef)
	{
		if (!Cause.is(threatRef.getObjectType()))
			return false;
		
		if (!Target.is(targetRef.getObjectType()))
			return false;
		
		return true;
	}

	public static void moveFactorsToSpecificDirs() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int FACTOR_TYPE = 4;
		File factorDir = DataUpgrader.getObjectsDir(jsonDir, FACTOR_TYPE);
		if (! factorDir.exists())
			return;
		
		File factorManifestFile = new File(factorDir, "manifest");
		if (! factorManifestFile.exists())
			return;
	
		final int TARGET_TYPE = 22;
		final int CAUSE_TYPE = 20;
		final int STRATEGY_TYPE = 21;
		File targetDir = DataUpgrader.createObjectsDir(jsonDir, TARGET_TYPE);
		File causeDir = DataUpgrader.createObjectsDir(jsonDir, CAUSE_TYPE);
		File strategyDir = DataUpgrader.createObjectsDir(jsonDir, STRATEGY_TYPE);
		
		final String TARGET_TYPE_NAME = "Target";
		final String CAUSE_TYPE_NAME = "Factor";
		final String STRATEGY_TYPE_NAME = "Intervention";
		copyFactorToDir(factorDir, factorManifestFile, targetDir, TARGET_TYPE_NAME); 
		copyFactorToDir(factorDir, factorManifestFile, causeDir, CAUSE_TYPE_NAME);
		copyFactorToDir(factorDir, factorManifestFile, strategyDir, STRATEGY_TYPE_NAME);
		
		try
		{
			DirectoryUtils.deleteEntireDirectoryTree(factorDir);
		}
		catch (Exception e)
		{
			EAM.logError("Error occurred during cleanup after migration");
		}
	}

	private static void copyFactorToDir(File factorDir, File factorManifestFile, File objectDir, String typeName) throws Exception
	{
		EnhancedJsonObject manifestJson = new EnhancedJsonObject();
		manifestJson.put("Type", "ObjectManifest");
		
		ObjectManifest factorManifest = new ObjectManifest(JSONFile.read(factorManifestFile));
		BaseId[] allFactorIds = factorManifest.getAllKeys();
		for (int i = 0; i < allFactorIds.length; ++i)
		{
			String idFileName = allFactorIds[i].toString();
			File factorFile = new File(factorDir, idFileName);
			EnhancedJsonObject factorJson = DataUpgrader.readFile(factorFile);
			String typeFromJson = factorJson.getString("Type");
			if (typeFromJson.equals(typeName))
			{
				File targetFile = new File(objectDir, idFileName);
				DataUpgrader.createFile(targetFile, factorJson.toString());
				manifestJson.put(idFileName, "true");
			}
		}
		
		writeManifest(objectDir, manifestJson);
	}

	public static void writeManifest(File objectDir, EnhancedJsonObject manifestJson) throws Exception
	{
		File targetManifestFile = new File(objectDir, "manifest");
		DataUpgrader.writeJson(targetManifestFile, manifestJson);
	}

	private  static int deleteOrphanedTasks() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		
		final int TASK_TYPE = 3;
		File taskDir = DataUpgrader.getObjectsDir(jsonDir, TASK_TYPE);
		if (! taskDir.exists())
			return 0;
	
		File taskManifestFile = new File(taskDir, "manifest");
		if (! taskManifestFile.exists())
			return 0;
	
		ObjectManifest taskManifest = new ObjectManifest(JSONFile.read(taskManifestFile));
		HashSet<BaseId> allTaskIdSet = new HashSet<BaseId>(Arrays.asList(taskManifest.getAllKeys()));
		IdList allTaskIdList = new IdList(TASK_TYPE, taskManifest.getAllKeys());
		IdList orphandIdList = new IdList(TASK_TYPE, allTaskIdSet.toArray(new BaseId[0]));
		
		HashSet<BaseId> allOwnedTaskIds = new HashSet<BaseId>();
		allOwnedTaskIds.addAll(getTaskIds(jsonDir));
		allOwnedTaskIds.addAll(getActivityIds(jsonDir));
		allOwnedTaskIds.addAll(getTaskChildren(taskDir, taskManifestFile, "SubtaskIds"));
	
		
		orphandIdList.subtract(new IdList(TASK_TYPE, allOwnedTaskIds.toArray(new BaseId[0])));
		
		int[] orphandIdsAsInts = orphandIdList.toIntArray();		
		for (int i = 0; i < orphandIdsAsInts.length; ++i)
		{
			File targetFile = new File(taskDir, Integer.toString(orphandIdsAsInts[i]));
			targetFile.delete();
		}
		EAM.logVerbose("deleted orphan tasks: " + orphandIdList.toString());
		
		allTaskIdList.subtract(orphandIdList);
		DataUpgrader.createManifestFile(taskDir, allTaskIdList.toIntArray());
		
		return orphandIdsAsInts.length;
	}

	private static HashSet<BaseId> getTaskIds(File jsonDir) throws Exception
	{
		File indicatorDir = DataUpgrader.getObjectsDir(jsonDir, 8);
		if (! indicatorDir.exists())
			return new HashSet<BaseId>();
	
		File indicatorManifestFile = new File(indicatorDir, "manifest");
		if (! indicatorManifestFile.exists())
			return new HashSet<BaseId>();
		
		return getTaskChildren(indicatorDir, indicatorManifestFile, "TaskIds");
	}

	private static HashSet<BaseId> getActivityIds(File jsonDir) throws Exception
	{
		File strategyDir = DataUpgrader.getObjectsDir(jsonDir, 4);
		if (! strategyDir.exists())
			return new HashSet<BaseId>();
	
		File strategyManifestFile = new File(strategyDir, "manifest");
		if (! strategyManifestFile.exists())
			return new HashSet<BaseId>();
				
		return getTaskChildren(strategyDir, strategyManifestFile, "ActivityIds");
	}

	public static HashSet<BaseId> getTaskChildren(File parentDir, File manifestFile, String taskIdsTag) throws Exception
	{
		final int TASK_TYPE = 3;
		HashSet<BaseId> taskIds = new HashSet();
		ObjectManifest manifest = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] ids = manifest.getAllKeys();
		for (int i = 0; i < ids.length; ++i)
		{
			BaseId thisId = ids[i];
			File objectFile = new File(parentDir, Integer.toString(thisId.asInt()));
			EnhancedJsonObject json = DataUpgrader.readFile(objectFile);
			IdList thisTaskIds = new IdList(TASK_TYPE, json.optString(taskIdsTag));
			taskIds.addAll(thisTaskIds.asVector());
		}
		
		return taskIds;
	}

	public static boolean copyTncEcoRegionFieldOverToDividedTerrestrailMarineFreshwaterEcoRegions() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File projectMetaDataDir = DataUpgrader.getObjectsDir(jsonDir, 11);
		if (! projectMetaDataDir.exists())
			return false;
	
		File projectMetaDataManifestFile = new File(projectMetaDataDir, "manifest");
		if (! projectMetaDataManifestFile.exists())
			return false;
		
		ObjectManifest projectMetaDataManifest = new ObjectManifest(JSONFile.read(projectMetaDataManifestFile));
		BaseId[] projectMetaDataIds = projectMetaDataManifest.getAllKeys();
		if (projectMetaDataIds.length == 0)
			return false;
		
		if (projectMetaDataIds.length > 1)
			throw new RuntimeException("More than one Project Meta exists.");
	
		BaseId projectMetaDataId = projectMetaDataIds[0];
		File projectMetaDataFile = new File(projectMetaDataDir, Integer.toString(projectMetaDataId.asInt()));
		EnhancedJsonObject projectMetaDataJson = DataUpgrader.readFile(projectMetaDataFile);
		
		String oldEcorRegionsString = projectMetaDataJson.optString("TNC.Ecoregion");
		String[] oldEcoRegions = oldEcorRegionsString.split(",");
		
		TncTerrestrialEcoRegionQuestion terrestrialQuestion = new TncTerrestrialEcoRegionQuestion();
		CodeList newTerrestrialCodes = findEcoRegionCodes(oldEcoRegions, terrestrialQuestion);
		projectMetaDataJson.put("TNC.TerrestrialEcoRegion", newTerrestrialCodes.toString());
		
		TncMarineEcoRegionQuestion marineQuestion = new TncMarineEcoRegionQuestion();
		CodeList newMarineCodes = findEcoRegionCodes(oldEcoRegions, marineQuestion);
		projectMetaDataJson.put("TNC.MarineEcoRegion", newMarineCodes.toString());
	
		TncFreshwaterEcoRegionQuestion freshwaterQuestion = new TncFreshwaterEcoRegionQuestion();
		CodeList newFreshwaterCodes = findEcoRegionCodes(oldEcoRegions, freshwaterQuestion);
		projectMetaDataJson.put("TNC.FreshwaterEcoRegion", newFreshwaterCodes.toString());
		DataUpgrader.writeJson(projectMetaDataFile, projectMetaDataJson);
		
		return (oldEcorRegionsString.length() > 0);		
	}

	private static CodeList findEcoRegionCodes(String[] oldEcoRegions, TwoLevelQuestion question)
	{
		CodeList newCodes = new CodeList();
		for (int i = 0; i < oldEcoRegions.length; ++i)
		{
			String oldOperatingUnit = oldEcoRegions[i].trim();
			ChoiceItem foundChoice = question.findChoiceByLabel(oldOperatingUnit);
			if (foundChoice != null)
				newCodes.add(foundChoice.getCode());
		}
		return newCodes;
	}

	public static boolean copyTncOperatingUnitsFieldDataOverToNewPickListField() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File projectMetaDataDir = DataUpgrader.getObjectsDir(jsonDir, 11);
		if (! projectMetaDataDir.exists())
			return false;
	
		File projectMetaDataManifestFile = new File(projectMetaDataDir, "manifest");
		if (! projectMetaDataManifestFile.exists())
			return false;
		
		ObjectManifest projectMetaDataManifest = new ObjectManifest(JSONFile.read(projectMetaDataManifestFile));
		BaseId[] projectMetaDataIds = projectMetaDataManifest.getAllKeys();
		if (projectMetaDataIds.length == 0)
			return false;
		
		if (projectMetaDataIds.length > 1)
			throw new RuntimeException("More than one Project Meta exists.");
	
		BaseId projectMetaDataId = projectMetaDataIds[0];
		File projectMetaDataFile = new File(projectMetaDataDir, Integer.toString(projectMetaDataId.asInt()));
		EnhancedJsonObject projectMetaDataJson = DataUpgrader.readFile(projectMetaDataFile);
		
		String oldOperatingUnitsAsString = projectMetaDataJson.optString("TNC.OperatingUnits");
		String[] oldOperatingUnits = oldOperatingUnitsAsString.split(",");
		
		TncOperatingUnitsQuestion operatingUnitsQuestion = new TncOperatingUnitsQuestion();
		
		CodeList newOperatingUnitCodes = findEcoRegionCodes(oldOperatingUnits, operatingUnitsQuestion);
		projectMetaDataJson.put("TNC.OperatingUnitsField", newOperatingUnitCodes.toString());
		DataUpgrader.writeJson(projectMetaDataFile, projectMetaDataJson);
		
		return (oldOperatingUnitsAsString.length() > 0);
	}

	public static void copyTncProjectDataSizeInHectaresFieldOverToProjectMetaDataProjectAreaField() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File projectMetaDataDir = DataUpgrader.getObjectsDir(jsonDir, 11);
		if (! projectMetaDataDir.exists())
			return;
	
		File projectMetaDataManifestFile = new File(projectMetaDataDir, "manifest");
		if (! projectMetaDataManifestFile.exists())
			return;
		
		ObjectManifest projectMetaDataManifest = new ObjectManifest(JSONFile.read(projectMetaDataManifestFile));
		BaseId[] projectMetaDataIds = projectMetaDataManifest.getAllKeys();
		if (projectMetaDataIds.length == 0)
			return;
		
		if (projectMetaDataIds.length > 1)
			throw new RuntimeException("More than one Project Meta exists.");
	
		BaseId projectMetaDataId = projectMetaDataIds[0];
		File projectMetaDataFile = new File(projectMetaDataDir, Integer.toString(projectMetaDataId.asInt()));
		EnhancedJsonObject projectMetaDataJson = DataUpgrader.readFile(projectMetaDataFile);
		
		projectMetaDataJson.put("ProjectArea", projectMetaDataJson.optString("TNC.SizeInHectares"));
		DataUpgrader.writeJson(projectMetaDataFile, projectMetaDataJson);
	}

	public static void notifyIfNonBlankTncCountryCode() throws Exception
	{
		if (isTncCountryCodeBlank())
			return;
			
		EAM.notifyDialog("<HTML>The Country field on the TNC tab has been replaced by a Countries <BR>field on the Location tab. Please ensure that the new Countries field is correct for this project.</HTML>");
	}

	public static boolean isTncCountryCodeBlank() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File projectMetaDataDir = DataUpgrader.getObjectsDir(jsonDir, 11);
		if (! projectMetaDataDir.exists())
			return true;
	
		File projectMetaDataManifestFile = new File(projectMetaDataDir, "manifest");
		if (! projectMetaDataManifestFile.exists())
			return true;
		
		ObjectManifest projectMetaDataManifest = new ObjectManifest(JSONFile.read(projectMetaDataManifestFile));
		BaseId[] projectMetaDataIds = projectMetaDataManifest.getAllKeys();
		if (projectMetaDataIds.length != 1)
			return true;
		
		BaseId projectMetaDataId = projectMetaDataIds[0];
		File projectMetaDataFile = new File(projectMetaDataDir, Integer.toString(projectMetaDataId.asInt()));
		EnhancedJsonObject projectMetaDataJson = DataUpgrader.readFile(projectMetaDataFile);
		String tncCountryValue = projectMetaDataJson.optString("TNC.Country");
		if (tncCountryValue.length() == 0)
			return true;
		
		return false;
	}

	public static void copyWwfProjectDataCountriesFieldOverToProjectMetaData() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File wwfProjectDataDir = DataUpgrader.getObjectsDir(jsonDir, 30);
		if (! wwfProjectDataDir.exists())
			return;
	
		File wwfProjectDataManifestFile = new File(wwfProjectDataDir, "manifest");
		if (! wwfProjectDataManifestFile.exists())
			return;
		
		File projectMetaDataDir = DataUpgrader.getObjectsDir(jsonDir, 11);
		if (! projectMetaDataDir.exists())
			throw new RuntimeException("Project Meta Data dir does not exist");
	
		File projectMetaDataManifestFile = new File(projectMetaDataDir, "manifest");
		if (! projectMetaDataManifestFile.exists())
			throw new RuntimeException("Project Meta Data manifest file does not exist");
		
		ObjectManifest wwfProjectDataManifest = new ObjectManifest(JSONFile.read(wwfProjectDataManifestFile));
		ObjectManifest projectMetaDataManifest = new ObjectManifest(JSONFile.read(projectMetaDataManifestFile));
		BaseId[] wwfProjectDataIds = wwfProjectDataManifest.getAllKeys();
		BaseId[] projectMetaDataIds = projectMetaDataManifest.getAllKeys();
		if (wwfProjectDataIds.length != 1)
			return;
		
		if (projectMetaDataIds.length != 1)
			return;
		
		BaseId wwfProjectDataId = wwfProjectDataIds[0];
		File wwfProjectDataFile = new File(wwfProjectDataDir, Integer.toString(wwfProjectDataId.asInt()));
		EnhancedJsonObject wwfProjectDataJson = DataUpgrader.readFile(wwfProjectDataFile);
		CodeList countryCodes = new CodeList(wwfProjectDataJson.optString("Countries"));
		
		BaseId projectMetaDataId = projectMetaDataIds[0];
		File projectMetaDataFile = new File(projectMetaDataDir, Integer.toString(projectMetaDataId.asInt()));
		EnhancedJsonObject projectMetaDataJson = DataUpgrader.readFile(projectMetaDataFile);
		
		projectMetaDataJson.put("Countries", countryCodes.toString());
		DataUpgrader.writeJson(projectMetaDataFile, projectMetaDataJson);
	}

	private static void removeDuplicateBendPoints() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File diagramLinkDir = getObjects13DiagramLinkDir(jsonDir);
		if (! diagramLinkDir.exists())
			return;
	
		File diagramLinkManifestFile = new File(diagramLinkDir, "manifest");
		if (! diagramLinkManifestFile.exists())
			return;
	
		ObjectManifest diagramLinkManifest = new ObjectManifest(JSONFile.read(diagramLinkManifestFile));
		BaseId[] diagramLinkIds = diagramLinkManifest.getAllKeys();
		for (int i = 0; i < diagramLinkIds.length; ++i)
		{
			BaseId diagramLinkId = diagramLinkIds[i];
			File diagramLinkFile = new File(diagramLinkDir, Integer.toString(diagramLinkId.asInt()));
			EnhancedJsonObject diagramLinkJson = DataUpgrader.readFile(diagramLinkFile);
			PointList bendPointsWithPossibleDuplicates = new PointList(diagramLinkJson.optString("BendPoints"));
			PointList nonDuplicateBendPointList = omitDuplicateBendPoints(bendPointsWithPossibleDuplicates);
			if (nonDuplicateBendPointList.size() != bendPointsWithPossibleDuplicates.size())
			{ 
				diagramLinkJson.put("BendPoints", nonDuplicateBendPointList.toString());
				DataUpgrader.writeJson(diagramLinkFile, diagramLinkJson);
			}
		}
	}

	public static PointList omitDuplicateBendPoints(PointList bendPoints) throws Exception
	{
		
		PointList nonDuplicates = new PointList();
		for (int i = 0; i < bendPoints.size(); ++i)
		{
			Point point = bendPoints.get(i);
			if (!nonDuplicates.contains(point))
				nonDuplicates.add(point);
		}
	
		return nonDuplicates;
	}

	public static void notifyUserOfDeletedDuratingAndCostFields()
	{
		EAM.notifyDialog(EAM.text("<html>" +
				"This version of Miradi has changed the Strategy rating options.<br>" +
				"The Duration and Cost fields have been removed, and the <br>" +
				"Impact and Feasibility fields now encompass those attribues.<br>" +
				"<br>" +
				"Please review your Strategy ratings to ensure they are appropriate."));
	}

	public static void createThreatStressRatingsForTargetThreatLinks() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File factorLinkDir = getObjects6FactorLinkDir(jsonDir);
		if (! factorLinkDir.exists())
			return;
	
		File factorLinkManifestFile = new File(factorLinkDir, "manifest");
		if (! factorLinkManifestFile.exists())
			return;
	
		File threatStressRatingDir = DataUpgrader.getObjectsDir(jsonDir, 34);
		if (threatStressRatingDir.exists())
			return;
	
		threatStressRatingDir.mkdirs();
		
		EnhancedJsonObject threatStressRatingManifestJson = new EnhancedJsonObject();
		threatStressRatingManifestJson.put("Type", "ObjectManifest");
		ObjectManifest factorLinkManifest = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		BaseId[] factorLinkIds = factorLinkManifest.getAllKeys();
		for (int i = 0; i < factorLinkIds.length; ++i)
		{
			BaseId factorLinkId = factorLinkIds[i];
			File factorLinkFile = new File(factorLinkDir, Integer.toString(factorLinkId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
			ORef targetRef = getPossibleTargetEnd(factorLinkJson);
			if (targetRef.isInvalid())
				continue;
			
			File targetDir = getObjects4TargetDir(jsonDir);
			File targetFile = new File(targetDir, Integer.toString(targetRef.getObjectId().asInt()));
			EnhancedJsonObject targetJson = DataUpgrader.readFile(targetFile);
			ORefList stressRefs = new ORefList(targetJson.optString("StressRefs"));
			ORefList threatStressRatingRefs = createThreatStressRatings(threatStressRatingDir, stressRefs, threatStressRatingManifestJson, jsonDir);
								
			factorLinkJson.put("ThreatStressRatingRefs", threatStressRatingRefs.toString());
			DataUpgrader.writeJson(factorLinkFile, factorLinkJson);	
		}
		
		File manifestFile = new File(threatStressRatingDir, "manifest");
		DataUpgrader.writeJson(manifestFile, threatStressRatingManifestJson);
	}

	public static ORefList createThreatStressRatings(File threatStressRatingDir, ORefList stressRefs, EnhancedJsonObject threatStressRatingManifestJson, File jsonDir) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		ORefList threatStressRatingRefs = new ORefList();
		for (int i = 0; i < stressRefs.size(); ++i)
		{
			int id = ++highestId;
			threatStressRatingManifestJson.put(Integer.toString(id), "true");
			EnhancedJsonObject threatStressRatingJson = new EnhancedJsonObject();
			threatStressRatingJson.put("Id", Integer.toString(id));
			threatStressRatingJson.put("StressRef", stressRefs.get(i).toString());
			File threatStressRatingFile = new File(threatStressRatingDir, Integer.toString(id));
			DataUpgrader.createFile(threatStressRatingFile, threatStressRatingJson.toString());	
		
			threatStressRatingRefs.add(new ORef(34, new BaseId(id)));
			DataUpgrader.writeHighestIdToProjectFile(jsonDir, id);
		}
		
		return threatStressRatingRefs;
	}

	private static void createdStressesFromFactorLinks() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File factorLinkDir = getObjects6FactorLinkDir(jsonDir);
		if (! factorLinkDir.exists())
			return;
	
		File factorLinkManifestFile = new File(factorLinkDir, "manifest");
		if (! factorLinkManifestFile.exists())
			return;
	
		File stressDir = getObject33StresDir(jsonDir);
		if (stressDir.exists())
			return;
		
		stressDir.mkdirs();
		EnhancedJsonObject stressManifestJson = new EnhancedJsonObject();
		stressManifestJson.put("Type", "ObjectManifest");
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		ObjectManifest factorLinkManifest = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		BaseId[] factorLinkIds = factorLinkManifest.getAllKeys();
		for (int i = 0; i < factorLinkIds.length; ++i)
		{
			BaseId factorLinkId = factorLinkIds[i];
			File factorLinkFile = new File(factorLinkDir, Integer.toString(factorLinkId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
			String stressLabel = factorLinkJson.optString("StressLabel");
			if (stressLabel.length() == 0)
				continue;
			
			ORef targetWithStressRef = getTargetEnd(factorLinkJson);
			File targetDir = getObjects4TargetDir(jsonDir);
			File targetFile = new File(targetDir, Integer.toString(targetWithStressRef.getObjectId().asInt()));
			EnhancedJsonObject targetJson = DataUpgrader.readFile(targetFile);
			
			int id = ++highestId;
			stressManifestJson.put(Integer.toString(id), "true");
			EnhancedJsonObject stressJson = new EnhancedJsonObject();
			stressJson.put("Id", Integer.toString(id));
			stressJson.put("Label", stressLabel);
		
			File stressFile = new File(stressDir, Integer.toString(id));
			DataUpgrader.createFile(stressFile, stressJson.toString());
			
			ORefList stressRefs = new ORefList();
			stressRefs.add(new ORef(Stress.getObjectType(), new BaseId(id)));
			targetJson.put("StressRefs", stressRefs.toString());
			DataUpgrader.writeJson(targetFile, targetJson);
			
			DataUpgrader.writeHighestIdToProjectFile(jsonDir, id);
		}
		
		File manifestFile = new File(stressDir, "manifest");
		DataUpgrader.writeJson(manifestFile, stressManifestJson);
	}

	public static ORef getTargetEnd(EnhancedJsonObject factorLinkJson)
	{
		ORef targetEnd = getPossibleTargetEnd(factorLinkJson);
		if (!targetEnd.isInvalid())
			return targetEnd;
		
		throw new RuntimeException("Link does not link to target");
	}

	private static ORef getPossibleTargetEnd(EnhancedJsonObject factorLinkJson)
	{
		ORef fromRef = factorLinkJson.getRef("FromRef");
		ORef toRef = factorLinkJson.getRef("ToRef");
		if (toRef.getObjectType() == Target.getObjectType())
			return toRef;
		
		if (fromRef.getObjectType() == Target.getObjectType())
			return fromRef;
		
		return ORef.INVALID;
	}

	private static void createMeasurementFromDataInIndicator() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		
		File indicatorDir = new File(jsonDir, "objects-8");
		if (! indicatorDir.exists())
			return;
	
		File measurementDir = new File(jsonDir, "objects-32");
		if (measurementDir.exists())
			return;
		
		File indicatorManifestFile = new File(indicatorDir, "manifest");
		if (! indicatorManifestFile.exists())
			return;
		
		measurementDir.mkdirs();
		
		ObjectManifest indicatorManifest = new ObjectManifest(JSONFile.read(indicatorManifestFile));
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		EnhancedJsonObject measurementManifestJson = new EnhancedJsonObject();
		measurementManifestJson.put("Type", "ObjectManifest");
		BaseId[] indicatorIds = indicatorManifest.getAllKeys();
		for (int i = 0; i < indicatorIds.length; ++i)
		{	
			BaseId indicatorId = indicatorIds[i];
			File indicatorFile = new File(indicatorDir, Integer.toString(indicatorId.asInt()));
			EnhancedJsonObject indicatorJson = DataUpgrader.readFile(indicatorFile);
			String trend = indicatorJson.optString("MeasurementTrend");
			String status = indicatorJson.optString("MeasurementStatus");
			String date = indicatorJson.optString("MeasurementDate");
			String summary = indicatorJson.optString("MeasurementSummary");
			String detail = indicatorJson.optString("MeasurementDetail");
			String statusConfidence = indicatorJson.optString("MeasurementStatusConfidence");
			boolean hasMeasurementData = trend.length() > 0 || status.length() > 0 || date.length() > 0 || summary.length() > 0 || detail.length() > 0 || statusConfidence.length() > 0;
			if (!hasMeasurementData)
				continue;
			
			measurementManifestJson.put(Integer.toString(++highestId), "true");
			
			EnhancedJsonObject measurementJson = new EnhancedJsonObject();
			measurementJson.put("Id", Integer.toString(highestId));
			measurementJson.put("Label", "");
			measurementJson.put("Trend", trend);
			measurementJson.put("Status", status);
			measurementJson.put("Date", date);
			measurementJson.put("Summary", summary);
			measurementJson.put("Detail", detail);
			measurementJson.put("StatusConfidence", statusConfidence);
		
			ORefList measurementRefs = new ORefList();
			measurementRefs.add(new ORef(Measurement.getObjectType(), new BaseId(highestId)));
			indicatorJson.put("MeasurementRefs", measurementRefs.toString());
			DataUpgrader.writeJson(indicatorFile, indicatorJson);
			
			File measurementFile = new File(measurementDir, Integer.toString(highestId));
			DataUpgrader.createFile(measurementFile, measurementJson.toString());
		}
		
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, highestId);
		File manifestFile = new File(measurementDir, "manifest");
		DataUpgrader.writeJson(manifestFile, measurementManifestJson);
	}

	private static void switchDiagramFactorWrappedIdsToRefs() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		
		File factorDir = new File(jsonDir, "objects-4");
		if (! factorDir.exists())
			return;
		
		File factorManifestFile = new File(factorDir, "manifest");
		if (! factorManifestFile.exists())
			throw new RuntimeException("manifest for objects-4 (Factor) directory does not exist " + factorManifestFile.getAbsolutePath());
		
		
		File diagramFactorDir = new File(jsonDir, "objects-18");
		if (! diagramFactorDir.exists())
			return;
		
		File diagramFactorManifestFile = new File(diagramFactorDir, "manifest");
		if (! diagramFactorManifestFile.exists())
			throw new RuntimeException("manifest for objects-18 (DiagramFactor) folder does not exist " + diagramFactorManifestFile.getAbsolutePath());
		
		ObjectManifest diagramFactorManifest = new ObjectManifest(JSONFile.read(diagramFactorManifestFile));
		BaseId[] diagramFactorIds = diagramFactorManifest.getAllKeys();
		
		Vector allFactorTypeDirs = getAllFactorTypeDirs(jsonDir);
		Vector allFactorManifestFiles = getAllFactorManifestFiles(jsonDir);
		for (int i = 0; i < diagramFactorIds.length; ++i)
		{
			BaseId diagramFactorId = diagramFactorIds[i];
			File diagramFactorFile = new File(diagramFactorDir, Integer.toString(diagramFactorId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(diagramFactorFile);
			BaseId wrappedFactorId = new BaseId(factorLinkJson.getString("WrappedFactorId"));
			ORef wrappedRef = getORefForFactorId(allFactorTypeDirs, allFactorManifestFiles, wrappedFactorId);
			
			factorLinkJson.put("WrappedFactorRef", wrappedRef.toJson());
			DataUpgrader.writeJson(diagramFactorFile, factorLinkJson);
		}
	}

	public static void changeLinkFromToIdsToORefs() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		
		File factorLinkDir = new File(jsonDir, "objects-6");
		if (! factorLinkDir.exists())
			return;
		
		File linkManifestFile = new File(factorLinkDir, "manifest");
		if (! linkManifestFile.exists())
			throw new RuntimeException("manifest for objects-6 folder does not exist " + linkManifestFile.getAbsolutePath());
		
		Vector allFactorTypeDirs = getAllFactorTypeDirs(jsonDir);
		Vector allManifestFiles = getAllFactorManifestFiles(jsonDir);
		 
		ObjectManifest factorLinkManifest = new ObjectManifest(JSONFile.read(linkManifestFile));
		BaseId[] allFactorLinkIds = factorLinkManifest.getAllKeys();
		
		for (int i = 0; i < allFactorLinkIds.length; ++i)
		{
			File factorLinkFile = new File(factorLinkDir, Integer.toString(allFactorLinkIds[i].asInt()));
			
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
			BaseId fromId = new BaseId(factorLinkJson.optString("FromId"));
			ORef fromRef = getORefForFactorId(allFactorTypeDirs, allManifestFiles, fromId);
			
			BaseId toId = new BaseId(factorLinkJson.optString("ToId"));
			ORef toRef = getORefForFactorId(allFactorTypeDirs, allManifestFiles, toId);
			
			factorLinkJson.put("FromRef", fromRef.toJson());
			factorLinkJson.put("ToRef", toRef.toJson());
			DataUpgrader.writeJson(factorLinkFile, factorLinkJson);
		}
	}

	private static Vector getAllFactorManifestFiles(File jsonDir) throws Exception
	{
		Vector allManifestFiles = new Vector();
		int[] typesToConsider = getAllFactorTypes();
		for (int i = 0; i < typesToConsider.length; ++i)
		{
			File factorDir = new File(jsonDir, "objects-" + typesToConsider[i]);
			if (! factorDir.exists())
				continue;
					
			File factorManifestFile = new File(factorDir, "manifest");
			if (! factorManifestFile.exists())
				throw new RuntimeException("manifest for objects-" + typesToConsider[i] + " directory does not exist " + factorManifestFile.getAbsolutePath());
			
			allManifestFiles.add(new ObjectManifest(JSONFile.read(factorManifestFile)));
		}
		
		return allManifestFiles;
	}

	private static Vector getAllFactorTypeDirs(File jsonDir)
	{
		Vector allFactorTypeDirs = new Vector();
		int[] typesToConsider = getAllFactorTypes();
		for (int i = 0; i < typesToConsider.length; ++i)
		{
			File factorDir = new File(jsonDir, "objects-" + typesToConsider[i]);
			if (! factorDir.exists())
				continue;
					
			allFactorTypeDirs.add(factorDir);
		}
	
		return allFactorTypeDirs;
	}

	public static int[] getAllFactorTypes()
	{
		return new int[] {ObjectType.FACTOR, ObjectType.TARGET, ObjectType.STRATEGY, ObjectType.CAUSE, ObjectType.INTERMEDIATE_RESULT, ObjectType.THREAT_REDUCTION_RESULT, ObjectType.TEXT_BOX, ObjectType.GROUP_BOX};
	}

	public static ORef getORefForFactorId(Vector allFactorTypeDirs, Vector allManifestFiles, BaseId id) throws Exception
	{
		for (int i = 0; i < allFactorTypeDirs.size(); ++i)
		{
			ObjectManifest manifest = (ObjectManifest) allManifestFiles.get(i);
			File factorDir = (File) allFactorTypeDirs.get(i);
			BaseId[] allFactorIds = manifest.getAllKeys();
			
			for (int j = 0; j < allFactorIds.length; ++j)
			{
				if (allFactorIds[j].equals(id))
				{
					return getORefFromId(factorDir, allFactorIds[j]);
				}
			}
		}
		
		return ORef.INVALID;
	}

	private static ORef getORefFromId(File factorDir, BaseId id) throws Exception
	{
		File factorFile = new File(factorDir, Integer.toString(id.asInt()));
		JSONObject factorJson = JSONFile.read(factorFile);
		int type = FactorType.getFactorTypeFromString(factorJson.getString("Type"));
		
		return new ORef(type, id);
	}

	private static void possiblyNotifyUserAfterUpgradingToVersion19() throws Exception
	{
		BaseId[] newGoalIds = removeGoalsFromIndicators(); 
		if (newGoalIds.length > 0)
		{
			EAM.notifyDialog(EAM.text("One or more Goals that were associated with KEA Indicators have been deleted. " +
									"Please create new Goals as needed, or use the new Future Status section " +
									"of the Target Viability to store the same data."));
		}
	}

	public static BaseId[] removeGoalsFromIndicators() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		
		File indicatorDir = new File(jsonDir, "objects-8");
		if (! indicatorDir.exists())
			return new BaseId[0];
		
		File indicatrorManifestFile = new File(indicatorDir, "manifest");
		if (! indicatrorManifestFile.exists())
			throw new RuntimeException("manifest for objects-8 directory does not exist " + indicatrorManifestFile.getAbsolutePath());
	
		
		File goalsDir = new File(jsonDir, "objects-10");
		if (! goalsDir.exists())
			return new BaseId[0];
	
		File goalManifestFile = new File(goalsDir, "manifest");
		if (! goalManifestFile.exists())
			throw new RuntimeException("manifest for objects-10 directory does not exist " + goalManifestFile.getAbsolutePath());
	
		ObjectManifest indicatorManifestObject = new ObjectManifest(JSONFile.read(indicatrorManifestFile));
		BaseId[] allIndicatorIds = indicatorManifestObject.getAllKeys();
		
		int goalType = 10;
		IdList goalIdsToBeRemoved = new IdList(goalType);
		for (int i = 0; i < allIndicatorIds.length; ++i)
		{
			File indicatorFile = new File(indicatorDir, Integer.toString(allIndicatorIds[i].asInt()));
			JSONObject indicatorJson = JSONFile.read(indicatorFile);
			IdList goalIds = new IdList(goalType, indicatorJson.optString("GoalIds"));
			goalIdsToBeRemoved.addAll(goalIds);
			
			EnhancedJsonObject readIn = DataUpgrader.readFile(indicatorFile);
			readIn.put("GoalIds", "");
			DataUpgrader.writeJson(indicatorFile, readIn);
		}
		
		ObjectManifest goalManifestObject = new ObjectManifest(JSONFile.read(goalManifestFile));
		BaseId[] allGoalIds = goalManifestObject.getAllKeys();
		BaseId[] newGoalIds = removeGoalIdsFoundInIndicators(goalIdsToBeRemoved, allGoalIds);
		int[] goalIdsAsInts = new IdList(10, newGoalIds).toIntArray();
		String manifestContent = DataUpgrader.buildManifestContents(goalIdsAsInts);
		File manifestFile = new File(goalsDir, "manifest");
		DataUpgrader.createFile(manifestFile, manifestContent);
		
		return newGoalIds;
	}

	private static BaseId[] removeGoalIdsFoundInIndicators(IdList goalIdsToBeRemoved, BaseId[] allGoalIds)
	{
		Vector newGoalIds = new Vector();
		for (int i = 0; i < allGoalIds.length; ++i)
		{
			BaseId id = allGoalIds[i];
			if (! goalIdsToBeRemoved.contains(id))
				newGoalIds.add(id);
		}
		
		return (BaseId[]) newGoalIds.toArray(new BaseId[0]);
	}

	private static void addLinksToDiagramContentsObject() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		
		File objects13Dir = new File(jsonDir, "objects-13");
		if (! objects13Dir.exists())
			return;
	
		File manifest13File = new File(objects13Dir, "manifest");
		if (! manifest13File.exists())
			return;
		
		File objects19Dir = new File(jsonDir, "objects-19");
		if (! objects19Dir.exists())
			throw new RuntimeException("objects-19 directory does not exist " + objects19Dir.getAbsolutePath());
	
		File manifest19File = new File(objects19Dir, "manifest");
		if (! manifest19File.exists())
			throw new RuntimeException("manifest for objects-19 directory does not exist " + manifest19File.getAbsolutePath());
		
		ObjectManifest manifest19 = new ObjectManifest(JSONFile.read(manifest19File));
		BaseId[] linkIds = manifest19.getAllKeys();
		File onlyFile = new File(objects19Dir, Integer.toString(linkIds[0].asInt()));
		EnhancedJsonObject readInOnlyFile = DataUpgrader.readFile(onlyFile);
		
		ObjectManifest manifest13 = new ObjectManifest(JSONFile.read(manifest13File));
		BaseId[] ids = manifest13.getAllKeys();
		IdList idList = new IdList(13, ids);
		readInOnlyFile.put("DiagramFactorLinkIds", idList.toString());
							
		DataUpgrader.writeJson(onlyFile, readInOnlyFile);
	}

	private static void createObject19DirAndFillFromDiagram() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File objects19Dir = new File(jsonDir, "objects-19");
		if (objects19Dir.exists())
			throw new RuntimeException("objects-19 directory already exists " + objects19Dir.getAbsolutePath());
		
		objects19Dir.mkdirs();
		
		File diagramsDir = new File(jsonDir, "diagrams");
		if (! diagramsDir.exists())
			throw new RuntimeException("diagrams directory does not exist " + diagramsDir.getAbsolutePath());
		
		File mainDiagram = new File(diagramsDir, "main");
		if (! mainDiagram.exists())
			throw new RuntimeException("main file does not exist " + mainDiagram.getAbsolutePath());
		
		EnhancedJsonObject mainJson = JSONFile.read(mainDiagram);
		IdList diagramFactorIds = new IdList(18, mainJson.getString(DiagramModel.TAG_DIAGRAM_FACTOR_IDS));
		
		String manifest19Contents = "{\"Type\":\"ObjectManifest\"";
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		highestId++;
		manifest19Contents += ",\"" + highestId + "\":true";
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, highestId);
		manifest19Contents += "}";
		File manifestFile = new File(objects19Dir, "manifest");
		DataUpgrader.createFile(manifestFile, manifest19Contents);
	
		File idFile = new File(objects19Dir, Integer.toString(highestId));
		EnhancedJsonObject readIn = DataUpgrader.readFile(mainDiagram);
		readIn.put("DiagramFactorIds", diagramFactorIds.toJson());
		readIn.put("Id", highestId);
		DataUpgrader.writeJson(idFile, readIn);
	}

	public static int readDataVersion(File projectDirectory) throws Exception
	{
		return DataUpgrader.getProjectServer().readLocalDataVersion(projectDirectory);
	}

	public static void createDiagramFactorLinksFromRawFactorLinks(HashMap mappedFactorIds) throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File objects13Dir = new File(jsonDir, "objects-13");
		if (objects13Dir.exists())
			throw new RuntimeException("objects-13 directory already exists " + objects13Dir.getAbsolutePath());
		
		objects13Dir.mkdirs();
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		
		File objects6Dir = new File(jsonDir, "objects-6");
		if (! objects6Dir.exists())
			return;
		
		File manifestFor6File = new File(objects6Dir, "manifest");
		if (! manifestFor6File.exists())
			return;
		
		ObjectManifest manifest = new ObjectManifest(JSONFile.read(manifestFor6File));
		String manifest13Contents = "{\"Type\":\"ObjectManifest\"";
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			highestId++;
			manifest13Contents += ",\"" + highestId + "\":true";
			File nodeFile = new File(objects6Dir, Integer.toString(ids[i].asInt()));
			JSONObject factorLinkJson = JSONFile.read(nodeFile);
			
			int toFactorId = factorLinkJson.getInt("ToId");
			int fromFactorId = factorLinkJson.getInt("FromId");
			int wrappedId = factorLinkJson.getInt("Id");
			int wrappedToId = ((Integer)mappedFactorIds.get(new Integer(toFactorId))).intValue();
			int wrappedFromId = ((Integer)mappedFactorIds.get(new Integer(fromFactorId))).intValue();
			
			EnhancedJsonObject diagramFactorLinkJson = new EnhancedJsonObject();
			diagramFactorLinkJson.put("WrappedLinkId", wrappedId);
			diagramFactorLinkJson.put("Id", highestId);
			diagramFactorLinkJson.put("ToDiagramFactorId", wrappedToId);
			diagramFactorLinkJson.put("FromDiagramFactorId", wrappedFromId);
			
			File idFile = new File(objects13Dir, Integer.toString(highestId));
			DataUpgrader.createFile(idFile, diagramFactorLinkJson.toString());
		}
		manifest13Contents += "}";
		File manifestFile = new File(objects13Dir, "manifest");
		DataUpgrader.createFile(manifestFile, manifest13Contents);
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, highestId);
	}

	public static HashMap createDiagramFactorsFromRawFactors() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File objects18Dir = new File(jsonDir, "objects-18");
		if (objects18Dir.exists())
			throw new RuntimeException("objects-18 directory already exists " + objects18Dir.getAbsolutePath());
		
		objects18Dir.mkdir();
		
		//TODO the content of main file inside diagrams should be cleaned up.  
		File diagramsDir =  new File(jsonDir, "diagrams");
		File diagramMainFile = new File(diagramsDir, "main");
		EnhancedJsonObject readIn = DataUpgrader.readFile(diagramMainFile);
		EnhancedJsonObject nodes = new EnhancedJsonObject(readIn.getJson("Nodes"));
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		HashMap factorIdsMap = new HashMap();
		String manifest18Contents = "{\"Type\":\"ObjectManifest\"";
		IdList ids = new IdList(18);
		
		Iterator iter = nodes.keys();
		while(iter.hasNext())
		{
			highestId++;
			ids.add(new BaseId(highestId));
			manifest18Contents += ",\"" + highestId + "\":true";
			String key = (String)iter.next();
			EnhancedJsonObject oldDiagramFactor = nodes.getJson(key);
			EnhancedJsonObject sizeJson = oldDiagramFactor.getJson("Size");
			EnhancedJsonObject locationJson = oldDiagramFactor.getJson("Location");	
			String wrappedId = oldDiagramFactor.getString("WrappedId");
			
			EnhancedJsonObject newDiagramFactor = new EnhancedJsonObject();
			newDiagramFactor.put("Id", highestId);
			newDiagramFactor.put("WrappedFactorId", wrappedId);
			newDiagramFactor.put("Size", getDimensionAsString(sizeJson));
			newDiagramFactor.put("Location", getPointAsString(locationJson));
			
			File idFile = new File(objects18Dir, Integer.toString(highestId));
			DataUpgrader.createFile(idFile, newDiagramFactor.toString());
			
			factorIdsMap.put(new Integer(wrappedId), new Integer(highestId));
		}
		
		readIn.put("DiagramFactorIds", ids.toJson());
		DataUpgrader.writeJson(diagramMainFile, readIn);
		
		manifest18Contents += "}";
		File manifestFile = new File(objects18Dir, "manifest");
		DataUpgrader.createFile(manifestFile, manifest18Contents);
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, highestId);
		
		return factorIdsMap;
	}

	private static Object getPointAsString(EnhancedJsonObject locationJson)
	{
		int x = locationJson.getInt("X");
		int y = locationJson.getInt("Y");
		Point point = new Point(x, y);
		
		return EnhancedJsonObject.convertFromPoint(point);
	}

	private static String getDimensionAsString(EnhancedJsonObject sizeJson)
	{
		int width = sizeJson.getInt("Width");
		int height = sizeJson.getInt("Height");
		Dimension dimension = new Dimension(width, height);
		
		return EnhancedJsonObject.convertFromDimension(dimension);
	}

	public static File getObjects6FactorLinkDir(File jsonDir)
	{
		return new File(jsonDir, "objects-6");
	}

	public static File getObject33StresDir(File jsonDir)
	{
		return new File(jsonDir, "objects-33");
	}

	private static File getObjects4TargetDir(File jsonDir)
	{
		return new File(jsonDir, "objects-4");
	}

	public static File getObjects13DiagramLinkDir(File jsonDir)
	{
		return new File(jsonDir, "objects-13");
	}

}
