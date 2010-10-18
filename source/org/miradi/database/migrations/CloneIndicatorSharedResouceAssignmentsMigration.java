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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.EnhancedJsonObject;

public class CloneIndicatorSharedResouceAssignmentsMigration
{
	public static IdList cloneSharedResourceAssignment() throws Exception
	{
		IdList updatedIndicatorIds = new IdList(INDICATOR_TYPE);
		if (!getIndicatorDir().exists())
			return updatedIndicatorIds;
		
		if (!getIndicatorManifestFile().exists())
			return updatedIndicatorIds;
		
		if (!getResourceAssignmentDir().exists())
			return updatedIndicatorIds;
		
		if (!getResourceAssignmentManifestFile().exists())
			return updatedIndicatorIds;
		
		ObjectManifest resourceAssignmentManifest = new ObjectManifest(JSONFile.read(getResourceAssignmentManifestFile()));
		BaseId[] resourceAssignmentIds = resourceAssignmentManifest.getAllKeys();
		for (int index = 0; index < resourceAssignmentIds.length; ++index)
		{
			BaseId resourceAssignmentId = resourceAssignmentIds[index];
			File resourceAssignmentFile = new File(getResourceAssignmentDir(), Integer.toString(resourceAssignmentId.asInt()));
			EnhancedJsonObject resourceAssignmentJson = DataUpgrader.readFile(resourceAssignmentFile);
			ORefList indicatorReferrers = findIndicatorReferrers(resourceAssignmentJson);
			if (indicatorReferrers.size() > 1)
			{
				IdList thisUpdatedIndicatorIds = cloneSharedResourceAssignment(indicatorReferrers, resourceAssignmentId, resourceAssignmentJson);
				updatedIndicatorIds.addAll(thisUpdatedIndicatorIds);				
			}
		}
		
		return updatedIndicatorIds;
	}
	
	private static IdList cloneSharedResourceAssignment(ORefList indicatorReferrers, BaseId resourceAssignmentIdToBeCloned, EnhancedJsonObject resourceAssignmentJson) throws Exception
	{
		IdList updatedIndicatorIds = new IdList(INDICATOR_TYPE);
		for (int index = 1; index < indicatorReferrers.size(); ++index)
		{
			int clonedIdAsInt = cloneResourceAssignment(resourceAssignmentJson);
			BaseId indicatorId = indicatorReferrers.get(index).getObjectId();
			updatedIndicatorIds.add(indicatorId);
			File indicatorFile = new File(getIndicatorDir(), Integer.toString(indicatorId.asInt()));
			EnhancedJsonObject indicatorJson = DataUpgrader.readFile(indicatorFile);
			IdList resourceAssignmentIds = indicatorJson.optIdList(RESOURCE_ASSIGNMENT_TYPE, ASSIGNMENT_IDS_TAG);
			resourceAssignmentIds.removeId(resourceAssignmentIdToBeCloned);
			resourceAssignmentIds.add(new BaseId(clonedIdAsInt));
			indicatorJson.put(ASSIGNMENT_IDS_TAG, resourceAssignmentIds.toString());
			DataUpgrader.writeJson(indicatorFile, indicatorJson);
		}
		
		return updatedIndicatorIds;
	}
	
	private static int cloneResourceAssignment(EnhancedJsonObject resourceAssignmentJson) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(getJsonDir());
		highestId++;
		
		EnhancedJsonObject clonedResourceAssignmentJson = new EnhancedJsonObject(resourceAssignmentJson);
		clonedResourceAssignmentJson.put("Id", highestId);
		
		File clonedResourceAssignmentFile = new File(getResourceAssignmentDir(), Integer.toString(highestId));
		DataUpgrader.createFile(clonedResourceAssignmentFile, clonedResourceAssignmentJson.toString());

		ObjectManifest resourceAssignmentManifestObject = new ObjectManifest(JSONFile.read(getResourceAssignmentManifestFile()));
		resourceAssignmentManifestObject.put(highestId);
		DataUpgrader.writeJson(getResourceAssignmentManifestFile(), resourceAssignmentManifestObject.toJson());
		DataUpgrader.writeHighestIdToProjectFile(getJsonDir(), highestId);
		
		return highestId;
	}

	private static ORefList findIndicatorReferrers(EnhancedJsonObject resourceAssignmentJson) throws Exception
	{
		BaseId resourceAssignmentId = resourceAssignmentJson.getId("Id");
		ObjectManifest indicatorManifest = new ObjectManifest(JSONFile.read(getIndicatorManifestFile()));
		BaseId[] indicatorIds = indicatorManifest.getAllKeys();
		ORefList indictorsReferringToResourceAssignment = new ORefList();
		for (int index = 0; index < indicatorIds.length; ++index)
		{
			BaseId indicatorId = indicatorIds[index];
			File indicatorFile = new File(getIndicatorDir(), Integer.toString(indicatorId.asInt()));
			EnhancedJsonObject indicatorJson = DataUpgrader.readFile(indicatorFile);
			IdList resourceAssignmentIds = indicatorJson.optIdList(RESOURCE_ASSIGNMENT_TYPE, ASSIGNMENT_IDS_TAG);
			if (resourceAssignmentIds.contains(resourceAssignmentId))
				indictorsReferringToResourceAssignment.add(new ORef(INDICATOR_TYPE, indicatorId));
		}
		
		return indictorsReferringToResourceAssignment;
	}

	private static File getResourceAssignmentManifestFile()
	{
		return getmanifestFile(getResourceAssignmentDir());	
	}

	private static File getIndicatorDir()
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
		return getmanifestFile(getIndicatorDir());
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
