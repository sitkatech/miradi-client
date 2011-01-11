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

public class ScopeBoxLegacyColorCodeUpdateMigration
{
	public static void updateLegacyScopeBoxColorCode() throws Exception
	{
		if (!getScopeBoxDir().exists())
			return;
		
		if (! getScopeBoxManifestFile().exists())
			return;
		
		ObjectManifest scopeBoxManifestObject = new ObjectManifest(JSONFile.read(getScopeBoxManifestFile()));
		BaseId[] scopeBoxIds = scopeBoxManifestObject.getAllKeys();
		for (int index = 0; index < scopeBoxIds.length; ++index)
		{
			BaseId scopeBoxId = scopeBoxIds[index];
			File scopeBoxFile = new File(getScopeBoxDir(), Integer.toString(scopeBoxId.asInt()));
			EnhancedJsonObject scopeBoxJson = DataUpgrader.readFile(scopeBoxFile);
			String scopeBoxColorCode = scopeBoxJson.optString(SCOPE_BOX_COLOR_TAG);
			if (scopeBoxColorCode.equals(LEGACY_SCOPE_BOX_COLOR_CODE))
			{
				scopeBoxJson.put(SCOPE_BOX_COLOR_TAG, NEW_SCOPE_BOX_COLOR_CODE);
				DataUpgrader.writeJson(scopeBoxFile, scopeBoxJson);
			}
		}
	}
	
	private static File getScopeBoxManifestFile()
	{
		return new File(getScopeBoxDir(), MANIFEST_FILE_NAME);
	}
	
	private static File getScopeBoxDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), SCOPE_BOX_TYPE);
	}
	
	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}

	private static final int SCOPE_BOX_TYPE = 50;
	private static final String SCOPE_BOX_COLOR_TAG = "ScopeBoxColorCode";
	private static final String NEW_SCOPE_BOX_COLOR_CODE = "HumanWelfareTargetBrown";
	private static final String LEGACY_SCOPE_BOX_COLOR_CODE = "darkGray";
	private static final String MANIFEST_FILE_NAME = "manifest";
}
