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
import org.miradi.utils.EnhancedJsonObject;

public class MoveTncProjectAreaSizeWithinProjectMetadataMigration
{
	public static void moveTncProjectAreaSize() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File projectMetadataDir = DataUpgrader.getObjectsDir(jsonDir, PROJECT_METADATA_TYPE);
		if (! projectMetadataDir.exists())
			return;
		
		File manifestFile = new File(projectMetadataDir, MANIFEST_FILE_NAME);
		if (! manifestFile.exists())
			return;
		
		ObjectManifest projectMetadataManifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] projectMetadataIds = projectMetadataManifestObject.getAllKeys();
		if (projectMetadataIds.length != 1)
			throw new RuntimeException("Project Metadata cound is incorrect. Count = " + projectMetadataIds.length);
		
		BaseId projectMetadataId = projectMetadataIds[0];
		File projectMetadataJsonFile = new File(projectMetadataDir, Integer.toString(projectMetadataId.asInt()));
		EnhancedJsonObject projectMetadataJson = DataUpgrader.readFile(projectMetadataJsonFile);
		String tncProjectAreaSize = projectMetadataJson.optString(TNC_PROJECT_AREA_SIZE);
		String projectArea = projectMetadataJson.optString(PROJECT_AREA);
		if (projectArea.length() == 0 && tncProjectAreaSize.length() > 0)
		{
			projectMetadataJson.put(PROJECT_AREA, tncProjectAreaSize);
			DataUpgrader.writeJson(projectMetadataJsonFile, projectMetadataJson);
		}
	}
	
	private static final int PROJECT_METADATA_TYPE = 11;
	private static final String TNC_PROJECT_AREA_SIZE = "TNC.SizeInHectares";
	private static final String PROJECT_AREA = "ProjectArea";
	private static final String MANIFEST_FILE_NAME = "manifest";
}
