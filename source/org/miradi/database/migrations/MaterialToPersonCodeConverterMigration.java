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

public class MaterialToPersonCodeConverterMigration
{
	public static void convertMaterialToPersonCode() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int PROJECT_RESOURCE_TYPE = 7;
		File resourceDir = DataUpgrader.getObjectsDir(jsonDir, PROJECT_RESOURCE_TYPE);
		if (! resourceDir.exists())
			return;
		
		File manifestFile = new File(resourceDir, MANIFEST_FILE_NAME);
		if (! manifestFile.exists())
			return;
		
		ObjectManifest resourceManifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] resourceIds = resourceManifestObject.getAllKeys();
		for (int i = 0; i < resourceIds.length; ++i)
		{
			BaseId resourceId = resourceIds[i];
			File resourceJsonFile = new File(resourceDir, Integer.toString(resourceId.asInt()));
			EnhancedJsonObject resourceJson = DataUpgrader.readFile(resourceJsonFile);
			String resourceType = resourceJson.optString(RESOURCE_TYPE);
			if (resourceType.equals(MATERIAL_TYPE_CODE))
			{
				resourceJson.put(RESOURCE_TYPE, PERSON_TYPE_CODE);
				DataUpgrader.writeJson(resourceJsonFile, resourceJson);
			}
		}
	}
	
	private static final String RESOURCE_TYPE = "ResourceType";
	private static final String MATERIAL_TYPE_CODE = "Material";
	private static final String PERSON_TYPE_CODE = "";
	private static final String MANIFEST_FILE_NAME = "manifest";
}
