/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.ProjectZipper;
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
	}

	void upgradeToVersion2() throws IOException, ParseException
	{
		// add manifest file to Objects-1 and Objects-2
		createManifestFromObjects(ObjectType.THREAT_RATING_CRITERION);
		createManifestFromObjects(ObjectType.THREAT_RATING_VALUE_OPTION);
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
			json.put(ConceptualModelNode.TAG_LABEL, json.opt(TAG_NAME));
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
		
		JSONObject diagram = JSONFile.read(getDiagramFile());
		JSONObject nodes = diagram.getJSONObject("Nodes");
		for(int i = 0; i < droppedIds.size(); ++i)
		{
			nodes.remove(Integer.toString(droppedIds.get(i).asInt()));
		}
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
			JSONObject nodeData = JSONFile.read(objectFile);
			String type = nodeData.optString("Type", "");
			if(type.equals("Target"))
			{
				JSONArray oldGoals = nodeData.optJSONArray("GoalIds");
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

	private static final int NODE_TYPE = 4;

}
