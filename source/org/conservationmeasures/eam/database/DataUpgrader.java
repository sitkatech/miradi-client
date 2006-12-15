/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.ProjectZipper;
import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataUpgrader extends ProjectServer
{
	public static void attemptUpgrade(File projectDirectory)
	{
		String[] migrationText = {
				"This project was created with an older version of the app, " +
				"so it needs to be migrated to the current data format before it can be opened. " +
				"A backup will be saved first in case anything goes wrong. " +
				"Perform the automatic migration?"
				};
		String[] buttons = {EAM.text("Button|Migrate"), EAM.text("Button|Cancel"),};
		if(!EAM.confirmDialog("Project Migration Required", migrationText, buttons))
			return;
		
		File zipFile = new File(projectDirectory.getParent(), "backup-" + projectDirectory.getName() + ".zip");
		if(zipFile.exists())
		{
			String[] backupExistsText = {
					EAM.text("A backup archive for this project already exists." +
					"Continuing with this migration will replace the existing backup with a new copy." +
					"It is probably safe to do this, unless an earlier migration attempt failed.")
					};
			String[] replaceButtons = {EAM.text("Button|Replace Backup"), EAM.text("Button|Cancel"), };
			if(!EAM.confirmDialog("WARNING", backupExistsText, replaceButtons))
				return;
		}
		
		int versionAfterUpgrading = -1;
		try
		{
			ProjectZipper.createProjectZipFile(zipFile, projectDirectory);
			
			DataUpgrader upgrader = new DataUpgrader(projectDirectory);
			upgrader.upgrade();
			versionAfterUpgrading = readDataVersion(projectDirectory);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		if(versionAfterUpgrading == DATA_VERSION)
			EAM.notifyDialog(EAM.text("Project was migrated to the current data format"));
		else
			EAM.errorDialog(EAM.text("Attempt to migrate project to the current data format FAILED\n" +
				"The pre-migration project was archived in: " + zipFile + "\n" +
				"WARNING: Attempting to open this project again before repairing the problem " +
				"may result in losing data. \n" +
				"Please seek technical help from the e-AdaptiveManagement team."));
	}

	public DataUpgrader(File projectDirectory) throws IOException
	{
		super();
		setTopDirectory(projectDirectory);
	}

	void upgrade() throws Exception
	{
		if(readDataVersion(getTopDirectory()) == 1)
			upgradeToVersion2();
		if(readDataVersion(getTopDirectory()) == 2)
			upgradeToVersion3();
		if(readDataVersion(getTopDirectory()) == 3)
			upgradeToVersion4();
		if(readDataVersion(getTopDirectory()) == 4)
			upgradeToVersion5();
		if(readDataVersion(getTopDirectory()) == 5)
			upgradeToVersion6();
		if(readDataVersion(getTopDirectory()) == 6)
			upgradeToVersion7();
		if(readDataVersion(getTopDirectory()) == 7)
			upgradeToVersion8();
		if(readDataVersion(getTopDirectory()) == 8)
			upgradeToVersion9();
		if(readDataVersion(getTopDirectory()) == 9)
			upgradeToVersion10();
		if(readDataVersion(getTopDirectory()) == 10)
			upgradeToVersion11();
		if(readDataVersion(getTopDirectory()) == 11)
			upgradeToVersion12();
		if(readDataVersion(getTopDirectory()) == 12)
			upgradeToVersion13();
		if(readDataVersion(getTopDirectory()) == 13)
			upgradeToVersion14();
		if(readDataVersion(getTopDirectory()) == 14)
			upgradeToVersion15();
	}

	void upgradeToVersion2() throws IOException, ParseException
	{
		// add manifest file to Objects-1 and Objects-2
		createManifestFromObjects(ObjectType.RATING_CRITERION);
		createManifestFromObjects(ObjectType.VALUE_OPTION);
		writeVersion(2);
	}

	private void createManifestFromObjects(int type) throws IOException, ParseException
	{
		File jsonDirectory = new File(topDirectory, "json");
		File objectDirectory = new File(jsonDirectory, "objects-" + Integer.toString(type));
		objectDirectory.mkdir();
		File manifestFile = new File(objectDirectory, "manifest");
		if(manifestFile.exists())
			throw new RuntimeException("Didn't expect manifest file " + manifestFile.getAbsolutePath());

		ObjectManifest manifest = new ObjectManifest();
		File[] files = objectDirectory.listFiles();
		if(files == null)
			files = new File[0];
		for(int i = 0; i < files.length; ++i)
		{
			try
			{
				int id = Integer.parseInt(files[i].getName());
				manifest.put(id);
			}
			catch (Exception e)
			{
				EAM.logWarning("Exception during migration (non-fatal)");
				EAM.logException(e);
			}
		}
		manifest.write(manifestFile);
	}

	public void upgradeToVersion3() throws IOException, ParseException
	{
		renameNodeTagFromNameToLabel();
		writeVersion(3);
	}
	
	public void renameNodeTagFromNameToLabel() throws IOException, ParseException
	{
		final String TAG_NAME = "Name";

		File nodesDirectory = new File(topDirectory, "json/nodes");
		File manifestFile = new File(nodesDirectory, "manifest");
		if(!manifestFile.exists())
			return;
		
		ObjectManifest manifest = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			File nodeFile = new File(nodesDirectory, Integer.toString(ids[i].asInt()));
			JSONObject json = JSONFile.read(nodeFile);
			json.put(Factor.TAG_LABEL, json.opt(TAG_NAME));
			// no need to clear out the old Name field
			JSONFile.write(nodeFile, json);
		}
	}
	
	public void upgradeToVersion4() throws IOException, ParseException
	{
		moveNodesDirectoryToObjects4();
		writeVersion(4);
	}
	
	public void moveNodesDirectoryToObjects4() throws IOException 
	{
		File jsonDirectory = new File(getTopDirectory(), "json");
		File newNodesDirectory = new File(jsonDirectory, "objects-4");
		if(newNodesDirectory.exists())
			throw new IOException(newNodesDirectory + "already exists!");
		File oldNodesDirectory = new File(jsonDirectory, "nodes");
		if(!oldNodesDirectory.exists())
			return;
		
		boolean worked = oldNodesDirectory.renameTo(newNodesDirectory);
		if(!worked)
			throw new IOException("Rename failed from (" + 
					oldNodesDirectory.getAbsolutePath() + ") to (" +
					newNodesDirectory.getAbsolutePath() + ")");
	}

	public void upgradeToVersion5() throws IOException, ParseException
	{
		moveLinkagesDirectoryToObjects6();
		writeVersion(5);
	}
	
	public void moveLinkagesDirectoryToObjects6() throws IOException 
	{
		File jsonDirectory = new File(getTopDirectory(), "json");
		File newLinkagesDirectory = new File(jsonDirectory, "objects-6");
		if(newLinkagesDirectory.exists())
			throw new IOException(newLinkagesDirectory + "already exists!");
		File oldLinkagesDirectory = new File(jsonDirectory, "linkages");
		if(!oldLinkagesDirectory.exists())
			return;
		
		boolean worked = oldLinkagesDirectory.renameTo(newLinkagesDirectory);
		if(!worked)
			throw new IOException("Rename failed from (" + 
					oldLinkagesDirectory.getAbsolutePath() + ") to (" +
					newLinkagesDirectory.getAbsolutePath() + ")");
	}
	
	public void upgradeToVersion6() throws Exception
	{
		dropStressFactors();
		writeVersion(6);
	}
	
	public void dropStressFactors() throws Exception
	{
		File manifestFile = getObjectManifestFile(NODE_TYPE);
		if(!manifestFile.exists())
			return;
		
		IdList droppedIds = new IdList();
		ObjectManifest manifest = readObjectManifest(NODE_TYPE);
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			BaseId id = ids[i];
			JSONObject nodeData = JSONFile.read(getObjectFile(NODE_TYPE, id));
			String type = nodeData.optString("Type", "");
			String subtype = nodeData.optString("Subtype", "");
			if(type.equals("Factor") && subtype.equals("Stress"))
			{
				EAM.logDebug("Dropping Stress Factor: " + id);
				manifest.remove(id);
				droppedIds.add(id);
			}
		}
		writeObjectManifest(NODE_TYPE, manifest);
		
		EnhancedJsonObject diagram = JSONFile.read(getDiagramFile());
		JSONObject nodes = diagram.getJson("Nodes");
		for(int i = 0; i < droppedIds.size(); ++i)
		{
			nodes.remove(Integer.toString(droppedIds.get(i).asInt()));
		}
		diagram.put("Nodes", nodes);
		JSONFile.write(getDiagramFile(), diagram);
	}

	public void upgradeToVersion7() throws Exception
	{
		dropOldSampleGoals();
		writeVersion(7);
	}
	
	public void dropOldSampleGoals() throws Exception
	{
		File manifestFile = getObjectManifestFile(NODE_TYPE);
		if(!manifestFile.exists())
			return;
		
		JSONArray noGoals = new JSONArray();
		noGoals.put(-1);

		ObjectManifest manifest = readObjectManifest(NODE_TYPE);
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			BaseId id = ids[i];
			File objectFile = getObjectFile(NODE_TYPE, id);
			EnhancedJsonObject nodeData = JSONFile.read(objectFile);
			String type = nodeData.optString("Type", "");
			if(type.equals("Target"))
			{
				JSONArray oldGoals = nodeData.optJsonArray("GoalIds");
				if(oldGoals != null && oldGoals.equals(noGoals))
					continue;
				nodeData.put("GoalIds", noGoals);
				JSONFile.write(objectFile, nodeData);
			}
		}
	}
	
	public void upgradeToVersion8() throws Exception
	{
		captureExistingValueOptions();
		writeVersion(8);
	}
	
	public void captureExistingValueOptions() throws Exception
	{
		final int optionType = 2;
		final String frameworkFilename = "threatframework";
		final String valueOptionIdsTag = "ValueOptionIds";
		File jsonDirectory = new File(getTopDirectory(), "json");
		File threatFrameworkFile = new File(jsonDirectory, frameworkFilename);

		JSONObject frameworkJson = new EnhancedJsonObject();
		if(threatFrameworkFile.exists())
			frameworkJson = JSONFile.read(threatFrameworkFile);
		if(frameworkJson.has(valueOptionIdsTag))
			EAM.logWarning("DataUpgrader.captureExistingValueOptions: Not needed");
		
		
		File manifestFile = getObjectManifestFile(optionType);
		if(!manifestFile.exists())
			return;
		
		ObjectManifest manifest = readObjectManifest(optionType);
		BaseId[] ids = manifest.getAllKeys();

		IdList optionIds = new IdList();
		for(int i = 0; i < ids.length; ++i)
			 optionIds.add(ids[i]);
		
		frameworkJson.put(valueOptionIdsTag, optionIds.toJson());
		JSONFile.write(threatFrameworkFile, frameworkJson);
	}
	
	public void upgradeToVersion9() throws Exception
	{
		captureExistingThreatRatingCriteria();
		writeVersion(9);
	}
	
	public void captureExistingThreatRatingCriteria() throws Exception
	{
		final int criterionType = 1;
		final String frameworkFilename = "threatframework";
		final String criterionIdsTag = "CriterionIds";
		File jsonDirectory = new File(getTopDirectory(), "json");
		File threatFrameworkFile = new File(jsonDirectory, frameworkFilename);

		JSONObject frameworkJson = new EnhancedJsonObject();
		if(threatFrameworkFile.exists())
			frameworkJson = JSONFile.read(threatFrameworkFile);
		if(frameworkJson.has(criterionIdsTag))
			EAM.logWarning("DataUpgrader.captureExistingThreatRatingCriteria: Not needed");
		
		File manifestFile = getObjectManifestFile(criterionType);
		if(!manifestFile.exists())
			return;
		
		ObjectManifest manifest = readObjectManifest(criterionType);
		BaseId[] ids = manifest.getAllKeys();

		IdList criterionIds = new IdList();
		for(int i = 0; i < ids.length; ++i)
			 criterionIds.add(ids[i]);

		frameworkJson.put(criterionIdsTag, criterionIds.toJson());
		JSONFile.write(threatFrameworkFile, frameworkJson);
	}
	
	public void upgradeToVersion10() throws Exception
	{
		addDiagramNodeWrappedIds();
		writeVersion(10);
	}
	
	public void addDiagramNodeWrappedIds() throws Exception
	{
		File jsonDirectory = new File(getTopDirectory(), "json");
		File diagramsDirectory = new File(jsonDirectory, "diagrams");
		diagramsDirectory.mkdirs();
		File diagramFile = new File(diagramsDirectory, "main");
	
		EnhancedJsonObject diagram = JSONFile.read(diagramFile);
		EnhancedJsonObject nodes = diagram.getJson("Nodes");
		Iterator iter = nodes.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			EnhancedJsonObject node = nodes.getJson(key);
			if(node.has("WrappedId"))
				throw new Exception("DiagramNode already has WrappedId");
			node.put("WrappedId", node.getInt("Id"));
			nodes.put(key, node);
		}
		diagram.put("Nodes", nodes);
		JSONFile.write(diagramFile, diagram);
	}
	
	public void upgradeToVersion11() throws Exception
	{
		convertIndicatorIdToIdList();
		writeVersion(11);
	}
	
	public void convertIndicatorIdToIdList() throws Exception
	{
		int optionType = ObjectType.FACTOR;
		File jsonDirectory = new File(getTopDirectory(), "json");
		File modelNodesDirectory = new File(jsonDirectory, "objects-4");
		if(!modelNodesDirectory.exists())
			return;
		
		File manifestFile = getObjectManifestFile(optionType);
		if(!manifestFile.exists())
			return;
		
		ObjectManifest manifest = readObjectManifest(optionType);
		BaseId[] ids = manifest.getAllKeys();

		for(int i = 0; i < ids.length; ++i)
		{
			BaseId id = ids[i];
			File objectFile = getObjectFile(NODE_TYPE, id);
			EnhancedJsonObject nodeData = JSONFile.read(objectFile);
			if(nodeData.has("IndicatorIds"))
				throw new Exception("IndicatorIds field already exists in " + id);
		}
		
		for(int i = 0; i < ids.length; ++i)
		{
			BaseId id = ids[i];
			File objectFile = getObjectFile(NODE_TYPE, id);
			EnhancedJsonObject nodeData = JSONFile.read(objectFile);
			BaseId oldIndicatorId = nodeData.optId("IndicatorId");
			IdList indicatorsIds = new IdList();
			if(!oldIndicatorId.isInvalid())
				indicatorsIds.add(oldIndicatorId);
			nodeData.put("IndicatorIds", indicatorsIds.toString());
			JSONFile.write(objectFile, nodeData);
		}
	}
	
	public void upgradeToVersion12() throws Exception
	{
		convertGoalsAndObjectivesToIdLists();
		writeVersion(12);
	}
	
	public void convertGoalsAndObjectivesToIdLists() throws Exception
	{
		int objectType = ObjectType.FACTOR;
		
		File jsonDirectory = new File(getTopDirectory(), "json");
		File modelNodesDirectory = new File(jsonDirectory, "objects-4");
		if(!modelNodesDirectory.exists())
			return;
		
		File manifestFile = getObjectManifestFile(objectType);
		if(!manifestFile.exists())
			return;

		ObjectManifest manifest = readObjectManifest(objectType);
		BaseId[] ids = manifest.getAllKeys();

		
		for(int i = 0; i < ids.length; ++i)
		{
			BaseId id = ids[i];
			File objectFile = new File(modelNodesDirectory, Integer.toString(id.asInt()));
			EnhancedJsonObject nodeData = JSONFile.read(objectFile);
			
			convertJsonArrayToIdList("GoalIds", nodeData);
			convertJsonArrayToIdList("ObjectiveIds", nodeData);
			
			JSONFile.write(objectFile, nodeData);
		}
	}

	private void convertJsonArrayToIdList(String converTag, EnhancedJsonObject nodeDataToUse)
	{
		EnhancedJsonArray jsonArray = nodeDataToUse.optJsonArray(converTag);
		IdList newIds = new IdList();
		for (int aCounter = 0; aCounter < jsonArray.length(); aCounter++)
			newIds.add(new BaseId(jsonArray.getInt(aCounter))); 

		nodeDataToUse.put(converTag, newIds.toString());
	}
	
	public void upgradeToVersion13() throws Exception
	{
		// no changes, but we bumped the version to indicate that from now on,
		// diagram node ids are not necessarily the same as model node ids
		writeVersion(13);
	}
	
	public void upgradeToVersion14() throws Exception
	{
		convertTeamListToRoleCodes();
		writeVersion(14);
	}
	
	public void upgradeToVersion15() throws Exception
	{
		addParentRefToTasks();
		writeVersion(15);
	}
	
	public void convertTeamListToRoleCodes() throws Exception
	{
		File jsonDirectory = new File(getTopDirectory(), "json");
		
		File projectFile = new File(jsonDirectory, "project");
		EnhancedJsonObject projectJson = JSONFile.read(projectFile);
		BaseId metadataId = projectJson.optId("ProjectMetadataId");
		if(metadataId.isInvalid())
			return;
		
		File metadataDirectory = new File(jsonDirectory, "objects-11");
		if(!metadataDirectory.exists())
			return;
		File metadataFile = new File(metadataDirectory, metadataId.toString());
		EnhancedJsonObject metadataJson = JSONFile.read(metadataFile);
		IdList teamList = new IdList(metadataJson.optString("TeamResourceIds"));
		
		File resourcesDirectory = new File(jsonDirectory, "objects-7");
		if(!resourcesDirectory.exists())
			return;
		for(int i = 0; i < teamList.size(); ++i)
		{
			BaseId id = teamList.get(i);
			File resourceFile = new File(resourcesDirectory, id.toString());
			if(!resourceFile.exists())
			{
				EAM.logWarning("Could not add missing resource to team: " + id);
				continue;
			}
			EnhancedJsonObject json = JSONFile.read(resourceFile);
			json.put("RoleCodes", "{\"Codes\":[\"TeamMember\"]}");
			JSONFile.write(resourceFile, json);
		}
	}
	
	public void addParentRefToTasks() throws Exception
	{
		File jsonDir = new File(getTopDirectory(), "json");
		File factorDir = new File(jsonDir, "objects-4");
		if(! factorDir.exists())
			return;

		File factorManifestFile = getObjectManifestFile(ObjectType.FACTOR);
		if(! factorManifestFile.exists())
			return;


		File taskDir = new File(jsonDir, "objects-3");
		if(! taskDir.exists())
			return;
		
		File taskManifestFile = getObjectManifestFile(ObjectType.TASK);
		if(! taskManifestFile.exists())
			return;

		ObjectManifest factorManifest = readObjectManifest(ObjectType.FACTOR);
		BaseId[] factorIds = factorManifest.getAllKeys();
		for(int i = 0; i < factorIds.length; ++i)
		{
			BaseId id = factorIds[i];
			File objectFile = new File(factorDir, Integer.toString(id.asInt()));
			EnhancedJsonObject factorData = JSONFile.read(objectFile);
			IdList taskIds = new IdList(factorData.optString("ActivityIds"));
			
			BaseId parentId = factorData.getId("Id");
			ORef parentRef = new ORef(ObjectType.FACTOR, parentId);
			setParentRef(taskDir, parentRef, taskIds);
		}	
	}

	private void setParentRef(File taskDir, ORef parentRef, IdList taskIds) throws Exception
	{
		ObjectManifest taskManifest = readObjectManifest(ObjectType.TASK);
		BaseId[] allManifestTaskIds = taskManifest.getAllKeys();
		for(int i = 0; i < allManifestTaskIds.length; ++i)
		{
			BaseId id = allManifestTaskIds[i];
			File objectFile = new File(taskDir, Integer.toString(id.asInt()));
			EnhancedJsonObject taskData = JSONFile.read(objectFile);
		
			if (taskIds.contains(id))
			{
				taskData.put("ParentRef", parentRef);
				JSONFile.write(objectFile, taskData);
			}
		}
	}

	private static final int NODE_TYPE = 4;

}
