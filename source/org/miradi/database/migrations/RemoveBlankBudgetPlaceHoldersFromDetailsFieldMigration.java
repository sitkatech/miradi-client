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

public class RemoveBlankBudgetPlaceHoldersFromDetailsFieldMigration
{
	public static void removeBlankBudgetPlaceHoldersFromDetailsField() throws Exception
	{
		possiblyStripBlankBudgetDetailsPlaceHolders(TASK_TYPE, TASK_DETAILS_TAG);
		possiblyStripBlankBudgetDetailsPlaceHolders(INDICATOR_TYPE, INDICATOR_DETAILS_TAG);
		possiblyStripBlankBudgetDetailsPlaceHolders(STRATEGY_TYPE, STRATEGY_DETAILS_TAG);
	}

	private static void possiblyStripBlankBudgetDetailsPlaceHolders(final int objectType, String detailsTag) throws Exception
	{
		if (!getObjectsDir(objectType).exists())
			return;

		if (!getManifestFile(objectType).exists())
			return;

		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(getManifestFile(objectType)));
		BaseId[] objectIds = manifestObject.getAllKeys();
		for (int index = 0; index < objectIds.length; ++index)
		{
			BaseId id = objectIds[index];
			File objectFile = new File(getObjectsDir(objectType), id.toString());
			EnhancedJsonObject objectJson = DataUpgrader.readFile(objectFile);
			String detailsString = objectJson.optString(detailsTag);
			if (shouldStringOutBlankBudgetOverride(detailsString))
			{
				String withoutBlankBudgetOverride = getDetailsWithoutBlankBudgetOverride(detailsString);
				objectJson.put(detailsTag, withoutBlankBudgetOverride);
				DataUpgrader.writeJson(objectFile, objectJson);
			}
		}
	}

	private static boolean shouldStringOutBlankBudgetOverride(String detailsString)
	{
		if (detailsString.startsWith(getBlankBudgetOverrideValue()))
			return true;
		
		return false;
	}

	private static String getDetailsWithoutBlankBudgetOverride(String detailsString)
	{
		return detailsString.replaceFirst(getBlankBudgetOverrideValue(), "");
	}

	private static String getBlankBudgetOverrideValue()
	{
		String blankBudgetOverrideString = EAM.text("Migrated High Level Estimate:");
		final String NEW_LINE = "\n";
		blankBudgetOverrideString += NEW_LINE;
		blankBudgetOverrideString += EAM.text("Budget Override was: ");
		blankBudgetOverrideString += NEW_LINE;
		blankBudgetOverrideString += EAM.text("When Override was: ");
		blankBudgetOverrideString += NEW_LINE;
		blankBudgetOverrideString += EAM.text("Who Override was: ");
		blankBudgetOverrideString += NEW_LINE;
		blankBudgetOverrideString += "---------------------------------------------------";
		blankBudgetOverrideString += NEW_LINE;
		
		return blankBudgetOverrideString;
	}

	private static File getManifestFile(File objectsDir)
	{
		return new File(objectsDir, MANIFEST_FILE_NAME);
	}
	
	private static File getObjectsDir(final int objectType)
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), objectType);
	}
	
	private static File getManifestFile(final int objectType)
	{
		return getManifestFile(getObjectsDir(objectType));
	}
	
	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final int TASK_TYPE = 3;
	private static final int INDICATOR_TYPE = 8;
	private static final int STRATEGY_TYPE = 21;
	
	private static final String TASK_DETAILS_TAG = "Details";
	private static final String INDICATOR_DETAILS_TAG = "Detail";
	private static final String STRATEGY_DETAILS_TAG = "Text";
}
