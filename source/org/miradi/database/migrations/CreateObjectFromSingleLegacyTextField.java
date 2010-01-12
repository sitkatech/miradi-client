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
import org.miradi.main.EAM;
import org.miradi.utils.EnhancedJsonObject;

public class CreateObjectFromSingleLegacyTextField
{
	public static void createNewTypesFromLegacyFields() throws Exception
	{
		createIucnRedListSpeciesObjectFromLegacyField();
		createOtherNotableSpeciesObjectFromLegacyField();
		createAudienceObjectFromLegacyField();
	}
	
	private static void createIucnRedListSpeciesObjectFromLegacyField() throws Exception
	{
		createObjectsFromLegacyFields(PROJECT_METADATA_TYPE, REDLIST_SPECIES_LEGACY_FIELD_TAG, IUCN_REDLIST_SPECIES_TYPE);
	}

	private static void createOtherNotableSpeciesObjectFromLegacyField() throws Exception
	{
		createObjectsFromLegacyFields(PROJECT_METADATA_TYPE, OTHER_NOTABLE_SPECIES_LEGACY_FIELD_TAG, OTHER_NOTABLE_SPECIES_TYPE);
	}

	private static void createAudienceObjectFromLegacyField() throws Exception
	{
		createObjectsFromLegacyFields(RARE_PROJECT_DATA_TYPE, AUDIENCE_LEGACY_FIELD_TAG, AUDIENCE_TYPE);
	}
	
	private static void createObjectsFromLegacyFields(int legacyFieldContainerType, String legacyFieldTagInContainer, int newListType) throws Exception
	{
		if (!getObjectDir(legacyFieldContainerType).exists())
			return;
		
		if (!getTypeManifestFile(legacyFieldContainerType).exists())
			return;
		
		if (getObjectDir(newListType).exists())
			throw new RuntimeException("Project already has new list type:" + newListType);
		
		ObjectManifest legacyFieldContainerObjectManifest = new ObjectManifest(JSONFile.read(getTypeManifestFile(legacyFieldContainerType)));
		BaseId[] legacyFieldContainerIds = legacyFieldContainerObjectManifest.getAllKeys();
		if (legacyFieldContainerIds.length == 0)
			return;
		
		if (legacyFieldContainerIds.length > 1)
			EAM.logWarning("An expected singleton object tpye of: " + legacyFieldContainerType + " has more than one object.");
		
		BaseId legacyFieldContainerId = legacyFieldContainerIds[0];
		File legacyFieldContainerFile = new File(getObjectDir(legacyFieldContainerType), Integer.toString(legacyFieldContainerId.asInt()));
		EnhancedJsonObject legacyFieldContainerJson = DataUpgrader.readFile(legacyFieldContainerFile);
		String legacyValue = legacyFieldContainerJson.optString(legacyFieldTagInContainer);
		if (legacyValue.length() > 0)
		{
			createNewType(newListType, legacyValue);
		}
	}
	
	private static void createNewType(int newTypeToCreate, String oldLabel) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(getJsonDir());
		highestId++;
		
		File newTypDir = DataUpgrader.createObjectsDir(getJsonDir(), newTypeToCreate);
		int[] newTypeSingleItemArray = new int[]{highestId};
		
		EnhancedJsonObject newTypeJson = new EnhancedJsonObject();
		newTypeJson.put("Id", highestId);
		newTypeJson.put(LABEL_TAG, oldLabel);
		
		File idFile = new File(newTypDir, Integer.toString(highestId));
		DataUpgrader.createFile(idFile, newTypeJson.toString());

		File newTypeManifestFile = DataUpgrader.createManifestFile(newTypDir, newTypeSingleItemArray);
		if (! newTypeManifestFile.exists())
			throw new RuntimeException("New manifest was not created");
		
		DataUpgrader.writeHighestIdToProjectFile(getJsonDir(), highestId);
	}
	
	private static File getTypeManifestFile(int objectType)
	{
		return new File(getObjectDir(objectType), MANIFEST_FILE_NAME);
	}

	private static File getObjectDir(int objectType)
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), objectType);
	}
	
	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}

	private static final int IUCN_REDLIST_SPECIES_TYPE = 53;
	private static final int OTHER_NOTABLE_SPECIES_TYPE = 54;
	private static final int AUDIENCE_TYPE = 55;
	
	private static final int PROJECT_METADATA_TYPE = 11;
	private static final int RARE_PROJECT_DATA_TYPE = 38;
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String REDLIST_SPECIES_LEGACY_FIELD_TAG = "RedListSpecies";
	private static final String OTHER_NOTABLE_SPECIES_LEGACY_FIELD_TAG = "OtherNotableSpecies";
	private static final String AUDIENCE_LEGACY_FIELD_TAG = "Audience";
	private static final String LABEL_TAG = "Label";
}
