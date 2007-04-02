/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.database;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ProjectZipper;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.UnicodeWriter;

public class DataUpgrader extends ProjectServer
{
	public class MigrationTooOldException extends Exception
	{
	}

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
		catch (DataUpgrader.MigrationTooOldException e)
		{
			EAM.errorDialog(EAM.text("That project is too old to be migrated by this version of Miradi. " +
					"You can use Miradi 1.0 to migrate it to a modern data format, " +
					"and after that it can be opened and migrated by this version."));
			return;
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
				"Please seek technical help from the Miradi team."));
	}

	public DataUpgrader(File projectDirectory) throws IOException
	{
		super();
		setTopDirectory(projectDirectory);
	}

	void upgrade() throws Exception
	{
		if(readDataVersion(getTopDirectory()) < 15)
			throw new MigrationTooOldException();
		
		if (readDataVersion(getTopDirectory()) == 15)
			upgradeToVersion16();
	}

	public void upgradeToVersion16() throws Exception
	{
		HashMap mappedFactorIds = createDiagramFactorsFromRawFactors();
		createDiagramFactorLinksFromRawFactorLinks(mappedFactorIds);
		writeVersion(16);
	}
	
	public void createDiagramFactorLinksFromRawFactorLinks(HashMap mappedFactorIds) throws Exception
	{
		File jsonDir = new File(topDirectory, "json");
		File objects13Dir = new File(jsonDir, "objects-13");
		if (objects13Dir.exists())
			throw new RuntimeException("objects-13 directory already exists " + objects13Dir.getAbsolutePath());
		
		objects13Dir.mkdirs();
		int highestId = readHighestIdInProjectFile(jsonDir);
		
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
			createFile(idFile, diagramFactorLinkJson.toString());
		}
		manifest13Contents += "}";
		File manifestFile = new File(objects13Dir, "manifest");
		createFile(manifestFile, manifest13Contents);
		writeHighestIdToProjectFile(jsonDir, highestId);
	}

	public HashMap createDiagramFactorsFromRawFactors() throws Exception
	{
		File jsonDir = new File(topDirectory, "json");
		File objects18Dir = new File(jsonDir, "objects-18");
		if (objects18Dir.exists())
			throw new RuntimeException("objects-18 directory already exists " + objects18Dir.getAbsolutePath());
		
		objects18Dir.mkdir();
		
		//TODO the content of main file inside diagrams should be cleaned up.  
		File diagramsDir =  new File(jsonDir, "diagrams");
		File diagramMainFile = new File(diagramsDir, "main");
		EnhancedJsonObject readIn = readFile(diagramMainFile);
		EnhancedJsonObject nodes = new EnhancedJsonObject(readIn.getJson("Nodes"));
		int highestId = readHighestIdInProjectFile(jsonDir);
		HashMap factorIdsMap = new HashMap();
		String manifest18Contents = "{\"Type\":\"ObjectManifest\"";
		IdList ids = new IdList();
		
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
			createFile(idFile, newDiagramFactor.toString());
			
			factorIdsMap.put(new Integer(wrappedId), new Integer(highestId));
		}
		
		readIn.put("DiagramFactorIds", ids.toJson());
		writeJson(diagramMainFile, readIn);
		
		manifest18Contents += "}";
		File manifestFile = new File(objects18Dir, "manifest");
		createFile(manifestFile, manifest18Contents);
		writeHighestIdToProjectFile(jsonDir, highestId);
		
		return factorIdsMap;
	}
	
	private int readHighestIdInProjectFile(File dirToUse) throws Exception
	{
		File projectFile = new File(dirToUse, "project");
		EnhancedJsonObject readIn = readFile(projectFile);
		int gotId = readIn.getInt("HighestUsedNodeId");
		
		return gotId;
	}
	
	private void writeHighestIdToProjectFile(File dirToUse, int highestIdToWrite) throws Exception
	{
		File projectFile = new File(dirToUse, "project");
		EnhancedJsonObject readIn = readFile(projectFile);
		readIn.put("HighestUsedNodeId", highestIdToWrite);
		writeJson(projectFile, readIn);
	}

	private Object getPointAsString(EnhancedJsonObject locationJson)
	{
		int x = locationJson.getInt("X");
		int y = locationJson.getInt("Y");
		Point point = new Point(x, y);
		
		return EnhancedJsonObject.convertFromPoint(point);
	}

	private String getDimensionAsString(EnhancedJsonObject sizeJson)
	{
		int width = sizeJson.getInt("Width");
		int height = sizeJson.getInt("Height");
		Dimension dimension = new Dimension(width, height);
		
		return EnhancedJsonObject.convertFromDimension(dimension);
	}
	
	private EnhancedJsonObject readFile(File file) throws Exception
	{
		EnhancedJsonObject objectRead = JSONFile.read(file);
		return objectRead;
	}

	private void writeJson(File file, EnhancedJsonObject jsonToWrite) throws Exception
	{
		JSONFile.write(file, jsonToWrite);
	}
	
	void createFile(File file, String contents) throws Exception
	{
		UnicodeWriter writer = new UnicodeWriter(file);
		writer.writeln(contents);
		writer.close();
	}

}
