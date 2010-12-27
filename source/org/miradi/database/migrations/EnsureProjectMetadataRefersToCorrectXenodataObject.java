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
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.utils.EnhancedJsonObject;

public class EnsureProjectMetadataRefersToCorrectXenodataObject
{
	public static void ensureCorrectXenodataReferrer() throws Exception
	{
		if (!getProjectMetadataDir().exists())
			return;
		
		if (!getProjectMetadataManifestFile().exists())
			return;
		
		if (!getXenodataDir().exists())
			return;
		
		if (!getXenodataManifestFile().exists())
			return;
		
		ObjectManifest projectMetadataManifestObject = new ObjectManifest(JSONFile.read(getProjectMetadataManifestFile()));
		BaseId[] projectMetadataIds = projectMetadataManifestObject.getAllKeys();
		if (projectMetadataIds.length != 1)
			throw new RuntimeException("Project Metadata manifest file has incorrect number if keys");
		
		BaseId projectMetadataId = projectMetadataIds[0];
		File projectMetadataJsonFile = new File(getProjectMetadataDir(), Integer.toString(projectMetadataId.asInt()));
		EnhancedJsonObject projectMetadataJson = DataUpgrader.readFile(projectMetadataJsonFile);
		
		String xenodataStringRefMapAsString = projectMetadataJson.optString(XENODATA_STRING_REF_MAP_TAG);
		StringRefMap xenodataStringRefMap = new StringRefMap(xenodataStringRefMapAsString);
		ORef xenodataRefForConpro = xenodataStringRefMap.getValue(CONPRO_PROJECT_ID_XENODATA_KEY);
		ObjectManifest xenodataManifestObject = new ObjectManifest(JSONFile.read(getXenodataManifestFile()));
		IdList xenodataIds = new IdList(XENODATA_TYPE, xenodataManifestObject.getAllKeys());
		if (xenodataIds.contains(xenodataRefForConpro))
			return;
		
		if (xenodataIds.size() != 1)
			return;
		
		StringRefMap stringRefMap = new StringRefMap();
		stringRefMap.add(CONPRO_PROJECT_ID_XENODATA_KEY, new ORef(XENODATA_TYPE, xenodataIds.get(0)));
		projectMetadataJson.put(XENODATA_STRING_REF_MAP_TAG, stringRefMap.toString());
		DataUpgrader.writeJson(projectMetadataJsonFile, projectMetadataJson);
	}

	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}
	
	private static File getProjectMetadataDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), PROJECT_METADATA_TYPE);
	}
	
	private static File getXenodataDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), XENODATA_TYPE);
	}
	
	private static File getProjectMetadataManifestFile()
	{
		return getManifestFile(getProjectMetadataDir());
	}
	
	private static File getXenodataManifestFile()
	{
		return getManifestFile(getXenodataDir());
	}
	
	private static File getManifestFile(File objectsDir)
	{
		return new File(objectsDir, MANIFEST_FILE_NAME);
	}
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String CONPRO_PROJECT_ID_XENODATA_KEY = "ConPro";
	private static final String XENODATA_STRING_REF_MAP_TAG = "XenodataRefs";
	
	private static final int PROJECT_METADATA_TYPE = 11;
	private static final int XENODATA_TYPE = 44;
}
