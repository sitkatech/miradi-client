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
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.EnhancedJsonObject;

public class RemoveTextBoxLinksMigration
{
	@SuppressWarnings("unchecked")
	public static void removeTextBoxLinks() throws Exception
	{
		File jsonDir = getJsonDir();
		File factorLinkDir = DataUpgrader.getObjectsDir(jsonDir, FACTOR_LINK_TYPE);
		if (! factorLinkDir.exists())
			return;
		
		File factorLinkManifestFile = new File(factorLinkDir, MANIFEST_FILE_NAME);
		if (! factorLinkManifestFile.exists())
			return;
		
		if (! getDiagramLinkDir(jsonDir).exists())
			return;
		
		if (! getDiagramLinkManifestFile(jsonDir).exists())
			return;
		
		File textBoxDir = DataUpgrader.getObjectsDir(jsonDir, TEXTBOX_TYPE);
		if (! textBoxDir.exists())
			return;
		
		File textBoxManifestFile = new File(textBoxDir, MANIFEST_FILE_NAME);
		if (! textBoxManifestFile.exists())
			return;		
		
		File diagramFactorDir = DataUpgrader.getObjectsDir(jsonDir, DIAGRAM_FACTOR_TYPE);
		if (! diagramFactorDir.exists())
			return;
		
		File diagramFactorManifestFile = new File(diagramFactorDir, MANIFEST_FILE_NAME);
		if (! diagramFactorManifestFile.exists())
			return;
		
		ObjectManifest factorLinkManifestObject = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		ObjectManifest diagramLinkManifestObject = new ObjectManifest(JSONFile.read(getDiagramLinkManifestFile(jsonDir)));
		BaseId[] factorLinkIds = factorLinkManifestObject.getAllKeys();
		Vector<BaseId> factorLinkIdsToRemove = new Vector();
		Vector<BaseId> diagramLinkIdsToRemove = new Vector();
		for (int index = 0; index < factorLinkIds.length; ++index)
		{
			BaseId factorLinkId = factorLinkIds[index];
			File factorLinkJsonFile = new File(factorLinkDir, Integer.toString(factorLinkId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkJsonFile);
			ORef fromRef = factorLinkJson.optRef(FROM_REF);
			ORef toRef = factorLinkJson.optRef(TO_REF);
			if (fromRef.getObjectType() == TEXTBOX_TYPE || toRef.getObjectType() == TEXTBOX_TYPE)
			{
				factorLinkIdsToRemove.add(factorLinkId);
				diagramLinkIdsToRemove.addAll(deleteOwningDiagramLinkIds(factorLinkId));
			}
		}
		
		removeObjectFiles(factorLinkDir, factorLinkManifestObject, factorLinkIdsToRemove);
		removeObjectFiles(getDiagramLinkDir(jsonDir), diagramLinkManifestObject, diagramLinkIdsToRemove);
	}

	private static void removeObjectFiles(File objectDir, ObjectManifest objectManifestObject, Vector<BaseId> idsToRemove) throws Exception
	{
		for(BaseId idToRemove : idsToRemove)
		{
			File jsonFile = new File(objectDir, Integer.toString(idToRemove.asInt()));
			jsonFile.delete();			
			objectManifestObject.remove(idToRemove);
		}
		
		writeManifest(objectDir, objectManifestObject);
	}
	
	@SuppressWarnings("unchecked")
	private static Vector<BaseId> deleteOwningDiagramLinkIds(BaseId factorLinkId) throws Exception
	{
		ObjectManifest diagramLinkManifestObject = new ObjectManifest(JSONFile.read(getDiagramLinkManifestFile(getJsonDir())));
		BaseId[] allDiagramLinkIds = diagramLinkManifestObject.getAllKeys();
		Vector<BaseId> diagramLinkIdsToRemove = new Vector();
		for (int index = 0; index < allDiagramLinkIds.length; ++index)
		{
			BaseId diagramLinkId = allDiagramLinkIds[index];
			File diagramLinkJsonFile = new File(getDiagramLinkDir(getJsonDir()), Integer.toString(diagramLinkId.asInt()));
			EnhancedJsonObject diagramLinkJson = DataUpgrader.readFile(diagramLinkJsonFile);
			BaseId wrappedId = diagramLinkJson.optId(WRAPPED_LINK_ID);
			if (wrappedId.asInt() == factorLinkId.asInt())
			{
				removeDiagramLinkFromDiagramObject(diagramLinkId);
				diagramLinkIdsToRemove.add(diagramLinkId);
			}
		}
		
		return diagramLinkIdsToRemove;
	}
	
	private static void removeDiagramLinkFromDiagramObject(BaseId diagramLinkIdToRemove) throws Exception
	{
		removeDiagramLinkFromDiagramObject(CONCEPTUAL_MODEL_DIAGRAM_TYPE, diagramLinkIdToRemove);
		removeDiagramLinkFromDiagramObject(RESULTS_CHAIN_DIAGRAM_TYPE, diagramLinkIdToRemove);
	}

	private static void removeDiagramLinkFromDiagramObject(int diagramObjectType, BaseId diagramLinkIdToRemove) throws Exception
	{
		File jsonDir = getJsonDir();
		File diagramObjectDir = DataUpgrader.getObjectsDir(jsonDir, diagramObjectType);
		if (! diagramObjectDir.exists())
			return;
		
		File diagramObjectManifestFile = new File(diagramObjectDir, MANIFEST_FILE_NAME);
		if (! diagramObjectManifestFile.exists())
			return;
		
		ObjectManifest diagramObjectManifestObject = new ObjectManifest(JSONFile.read(diagramObjectManifestFile));
		BaseId[] diagramObjectIds = diagramObjectManifestObject.getAllKeys();
		for (int index = 0; index < diagramObjectIds.length; ++index)
		{
			BaseId diagramObjectId = diagramObjectIds[index];
			File diagramObjectJsonFile = new File(diagramObjectDir, Integer.toString(diagramObjectId.asInt()));
			EnhancedJsonObject diagramObjectJson = DataUpgrader.readFile(diagramObjectJsonFile);
			IdList diagramLinkIds = diagramObjectJson.optIdList(DIAGRAM_LINK_TYPE, DIAGRAM_FACTOR_LINK_IDS);
			if (diagramLinkIds.contains(diagramLinkIdToRemove))
			{
				diagramLinkIds.removeId(diagramLinkIdToRemove);
				diagramObjectJson.put(DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());
				DataUpgrader.writeJson(diagramObjectJsonFile, diagramObjectJson);
			}
		}
	}
	
	private static void writeManifest(File objectDir, ObjectManifest diagramLinkManifestObject) throws Exception
	{
		File manifestFile = new File(objectDir, MANIFEST_FILE_NAME);
		DataUpgrader.writeJson(manifestFile, diagramLinkManifestObject.toJson());
	}
	
	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}

	private static File getDiagramLinkDir(File jsonDir)
	{
		return DataUpgrader.getObjectsDir(jsonDir, DIAGRAM_LINK_TYPE);
	}
	
	private static File getDiagramLinkManifestFile(File jsonDir)
	{
		return new File(getDiagramLinkDir(jsonDir), MANIFEST_FILE_NAME);
	}

	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String FROM_REF = "FromRef";
	private static final String TO_REF = "ToRef";
	private static final String DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	private static final String WRAPPED_LINK_ID = "WrappedLinkId";
	
	private static final int FACTOR_LINK_TYPE = 6;
	private static final int DIAGRAM_LINK_TYPE = 13;
	private static final int DIAGRAM_FACTOR_TYPE = 18;
	private static final int TEXTBOX_TYPE = 26;
	private static final int RESULTS_CHAIN_DIAGRAM_TYPE = 24;
	private static final int CONCEPTUAL_MODEL_DIAGRAM_TYPE = 19;
}
