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

public class FactorLinkBidiFalseBooleanValueFixMigration
{
	public static void fixFactorLinkBidiFalseValues() throws Exception
	{
		if (!getFactorLinkDir().exists())
			return;
		
		if (!getFactorLinkManifestFile().exists())
			return;
		
		ObjectManifest factorLinkObjectManifest = new ObjectManifest(JSONFile.read(getFactorLinkManifestFile()));
		BaseId[] factorLinkIds = factorLinkObjectManifest.getAllKeys();
		for (int index = 0; index < factorLinkIds.length; ++index)
		{
			BaseId factorLinkId = factorLinkIds[index];
			File factorLinkFile = new File(getFactorLinkDir(), Integer.toString(factorLinkId.asInt()));
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
			String bidiValue = factorLinkJson.optString(BI_DIRECTIONAL_TAG);
			if (bidiValue.equals(NONE_USED_FALSE_VALUE))
			{
				factorLinkJson.put(BI_DIRECTIONAL_TAG, USED_FALSE_VALUE);
				DataUpgrader.writeJson(factorLinkFile, factorLinkJson);
			}
		}
	}

	private static File getFactorLinkDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), FACTOR_LINK_TYPE);
	}
	
	private static File getFactorLinkManifestFile()
	{
		return new File(getFactorLinkDir(), MANIFEST_FILE_NAME);
	}

	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}
	
	private static final int FACTOR_LINK_TYPE = 6;
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String BI_DIRECTIONAL_TAG = "BidirectionalLink";
	private static final String NONE_USED_FALSE_VALUE = "0";
	private static final String USED_FALSE_VALUE = "";
}
