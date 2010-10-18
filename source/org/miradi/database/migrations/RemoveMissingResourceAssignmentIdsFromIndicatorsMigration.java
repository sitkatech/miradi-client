/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.utils.EnhancedJsonObject;

public class RemoveMissingResourceAssignmentIdsFromIndicatorsMigration
{
	public static void removeMissingResourceAssignmentId() throws Exception
	{	
		if (!getIndicatorkDir().exists())
			return;
		
		if (!getIndicatorManifestFile().exists())
			return;
		
		ObjectManifest indicatorManifest = new ObjectManifest(JSONFile.read(getIndicatorManifestFile()));
		BaseId[] indicatorIds = indicatorManifest.getAllKeys();
		for (int index = 0; index < indicatorIds.length; ++index)
		{
			BaseId indicatorId = indicatorIds[index];
			File indicatorFile = new File(getIndicatorkDir(), Integer.toString(indicatorId.asInt()));
			EnhancedJsonObject indicatorJson = DataUpgrader.readFile(indicatorFile);
			IdList resourceAssignmentIds = indicatorJson.optIdList(RESOURCE_ASSIGNMENT_TYPE, ASSIGNMENT_IDS_TAG);
			IdList onlyExistingResourceAssignmentIds = extractExistingResourceAssignmentIds(resourceAssignmentIds);
			if (resourceAssignmentIds.size() != onlyExistingResourceAssignmentIds.size())
			{
				indicatorJson.put(ASSIGNMENT_IDS_TAG, onlyExistingResourceAssignmentIds.toString());
				DataUpgrader.writeJson(indicatorFile, indicatorJson);
			}
		}
	}

	private static IdList extractExistingResourceAssignmentIds(IdList resourceAssignmentIds) throws Exception
	{
		if (!getResourceAssignmentDir().exists())
			return new IdList(RESOURCE_ASSIGNMENT_TYPE);
		
		if (!getResourceAssignmentManifestFile().exists())
			return new IdList(RESOURCE_ASSIGNMENT_TYPE);
				
		ObjectManifest resourceAssignmentManifest = new ObjectManifest(JSONFile.read(getResourceAssignmentManifestFile()));
		BaseId[] resourceAssignmentIdsAsArray = resourceAssignmentManifest.getAllKeys();
		IdList allResourceAssignmentIds = new IdList(RESOURCE_ASSIGNMENT_TYPE, resourceAssignmentIdsAsArray);
		IdList clonedResourceAssignmentIds = new IdList(resourceAssignmentIds);
		clonedResourceAssignmentIds.retainAll(allResourceAssignmentIds);
		
		return clonedResourceAssignmentIds;
	}

	private static File getResourceAssignmentManifestFile()
	{
		return getmanifestFile(getResourceAssignmentDir());	
	}

	private static File getIndicatorkDir()
	{
		return getObjectDir(INDICATOR_TYPE);
	}
	
	private static File getResourceAssignmentDir()
	{
		return getObjectDir(RESOURCE_ASSIGNMENT_TYPE);
	}
	
	private static File getObjectDir(int objectType)
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), objectType);
	}

	private static File getIndicatorManifestFile()
	{
		return getmanifestFile(getIndicatorkDir());
	}

	private static File getmanifestFile(File factorLinkDir)
	{
		return new File(factorLinkDir, MANIFEST_FILE_NAME);
	}

	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}

	public static final int INDICATOR_TYPE = 8;
	public static final int RESOURCE_ASSIGNMENT_TYPE = 14;
	private static final String MANIFEST_FILE_NAME = "manifest";
	
	private static final String ASSIGNMENT_IDS_TAG = "AssignmentIds";
}
