/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.project.ProjectZipper;
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
		
		try
		{
			ProjectZipper.createProjectZipFile(zipFile, projectDirectory);
			
			DataUpgrader upgrader = new DataUpgrader(projectDirectory);
			upgrader.upgrade();
			EAM.notifyDialog(EAM.text("Project was migrated to the current data format"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Attempt to migrate project to the current data format FAILED\n" +
					"The pre-migration project was archived in: " + zipFile));
		}
		
	}

	public DataUpgrader(File projectDirectory) throws IOException
	{
		super();
		setTopDirectory(projectDirectory);
	}

	void upgrade() throws IOException, ParseException
	{
		int dataVersion = readDataVersion(getTopDirectory());
		if(dataVersion == 1)
			upgradeToVersion2();
		if(dataVersion == 2)
			upgradeToVersion3();
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
		File objectDirectory = getObjectDirectory(type);
		File manifestFile = getObjectManifestFile(type);
		if(manifestFile.exists())
			throw new RuntimeException("Didn't expect manifest file " + manifestFile.getAbsolutePath());
		
		File[] files = objectDirectory.listFiles();
		for(int i = 0; i < files.length; ++i)
		{
			int id = -1;
			try
			{
				id = Integer.parseInt(files[i].getName());
			}
			catch (Exception e)
			{
				EAM.logWarning("Exception during migration (non-fatal)");
				EAM.logException(e);
			}
			addToObjectManifest(type, id);
		}
	}

	public void upgradeToVersion3() throws IOException, ParseException
	{
		renameNodeTagFromNameToLabel();
		writeVersion(3);
	}
	
	public void renameNodeTagFromNameToLabel() throws IOException, ParseException
	{
		final String TAG_NAME = "Name";

		File directory = getManifestFile(getNodesDirectory());
		NodeManifest manifest = new NodeManifest(JSONFile.read(directory));
		int[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
		{
			File nodeFile = getNodeFile(getTopDirectory(), ids[i]);
			JSONObject json = JSONFile.read(nodeFile);
			json.put(ConceptualModelNode.TAG_LABEL, json.get(TAG_NAME));
			// no need to clear out the old Name field
			JSONFile.write(nodeFile, json);
		}
	}
}
