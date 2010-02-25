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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.utils.EnhancedJsonObject;

public class RemoveEmptyMethodsCreatedWhenImportingContproProjectMigration
{
	public static void removeEmptyMethodsInProjectsWithConproId() throws Exception
	{
		if (!getProjectMetadataDir().exists())
			return;
		
		if (!getXenodataDir().exists())
			return;
		
		if (!getIndicatorDir().exists())
			return;
		
		if (!getTaskDir().exists())
			return;
		
		if (!hasConproProjectId())
			return;
		
		BaseId[] indicatorIds = getAllIndicatorIds();
		for (int index = 0; index < indicatorIds.length; ++index)
		{
			BaseId indicatorId = indicatorIds[index];
			File indicatorJsonFile = new File(getIndicatorDir(), Integer.toString(indicatorId.asInt()));
			EnhancedJsonObject indicatorJson = DataUpgrader.readFile(indicatorJsonFile);
			ORefList methodRefs = getMethodRefs(indicatorJson);
			ORefList emptyMethodsToDelete = extractEmtyMethodsToDelete(methodRefs);
			ORefList methodsToRemain = new ORefList(methodRefs);
			methodsToRemain.removeAll(emptyMethodsToDelete);
			updateIndicatorMethods(indicatorJsonFile, indicatorJson, methodsToRemain);
			removeMethods(emptyMethodsToDelete);
		}
	}

	private static ORefList getMethodRefs(EnhancedJsonObject indicatorJson)	throws Exception
	{
		IdList methodIds = indicatorJson.optIdList(TASK_TYPE, INDICATOR_METHOD_IDS_TAG);
		
		return new ORefList(TASK_TYPE, methodIds);
	}
	
	private static void updateIndicatorMethods(File indicatorJsonFile, EnhancedJsonObject indicatorJson, ORefList methodsToRemain) throws Exception
	{
		indicatorJson.put(INDICATOR_METHOD_IDS_TAG, methodsToRemain.convertToIdList(TASK_TYPE).toString());
		DataUpgrader.writeJson(indicatorJsonFile, indicatorJson);
	}
	
	private static void removeMethods(ORefList idsToRemove) throws Exception
	{
		ObjectManifest methodManifestObject = new ObjectManifest(JSONFile.read(getMethodManifestFile()));
		for (int index = 0; index < idsToRemove.size(); ++index)
		{
			BaseId idToRemove = idsToRemove.get(index).getObjectId();
			File jsonFile = new File(getTaskDir(), Integer.toString(idToRemove.asInt()));
			jsonFile.delete();			
			methodManifestObject.remove(idToRemove);
		}
		
		DataUpgrader.writeJson(getMethodManifestFile(), methodManifestObject.toJson());
	}
	
	private static ORefList extractEmtyMethodsToDelete(ORefList methodRefs) throws Exception
	{
		ORefList emptyMethodRefs = new ORefList();
		for (int index = 0; index < methodRefs.size(); ++index)
		{
			ORef methodRef = methodRefs.get(index);
			File methodJsonFile = new File(getTaskDir(), methodRef.getObjectId().toString());
			EnhancedJsonObject methodJson = DataUpgrader.readFile(methodJsonFile);
			if (isMethodEmpty(methodJson) && !isMethodShared(methodRef))
				emptyMethodRefs.add(methodRef);
		}
		
		return emptyMethodRefs;
	}

	private static boolean isMethodShared(ORef methodRef) throws Exception
	{
		ORefSet indicatorReferrerRefs = new ORefSet();
		BaseId[] indicatorIds = getAllIndicatorIds();
		for (int index = 0; index < indicatorIds.length; ++index)
		{
			BaseId indicatorId = indicatorIds[index];
			File indicatorJsonFile = new File(getIndicatorDir(), Integer.toString(indicatorId.asInt()));
			EnhancedJsonObject indicatorJson = DataUpgrader.readFile(indicatorJsonFile);
			ORefList methodRefs = getMethodRefs(indicatorJson);
			if (methodRefs.contains(methodRef))
				indicatorReferrerRefs.add(new ORef(INDICATOR_TYPE, indicatorId));
		}
		
		return indicatorReferrerRefs.size() > 1;
	}

	private static BaseId[] getAllIndicatorIds() throws Exception
	{
		ObjectManifest indicatorManifestObject = new ObjectManifest(JSONFile.read(getIndicatorManifestFile()));
		return indicatorManifestObject.getAllKeys();
	}

	private static boolean isMethodEmpty(EnhancedJsonObject methodJson)
	{
		Vector<String> taskTags = new Vector<String>();
		taskTags.add("Details");
		taskTags.add("ShortLabel");
		taskTags.add("Label");
		taskTags.add("ProgressReportRefs");
		taskTags.add("AssignmentIds");
		taskTags.add("ExpenseRefs");
		taskTags.add("SubtaskIds");
		
		for(String methodTag : taskTags)
		{
			String dataAsString = methodJson.optString(methodTag);
			if (dataAsString.length() > 0)
				return false;
		}
			
		return true;
	}
	
	private static boolean hasConproProjectId() throws Exception
	{
		if (!getProjectMetadataManifestFile().exists())
			return false;
		
		if (!getXenodataManifestFile().exists())
			return false;
		
		ObjectManifest projectMetadataManifestObject = new ObjectManifest(JSONFile.read(getProjectMetadataManifestFile()));
		BaseId[] projectMetadataIds = projectMetadataManifestObject.getAllKeys();
		if (projectMetadataIds.length != 1)
			return false;
		
		BaseId projectMetadataId = projectMetadataIds[0];
		File projectMetadataJsonFile = new File(getProjectMetadataDir(), Integer.toString(projectMetadataId.asInt()));
		EnhancedJsonObject projectMetadataJson = DataUpgrader.readFile(projectMetadataJsonFile);
		String xenodataStringRefMapAsString = projectMetadataJson.optString("XenodataRefs");
		StringRefMap xenodataStringRefMap = new StringRefMap(xenodataStringRefMapAsString);
		ORef xenodataRef = xenodataStringRefMap.getValue(CONPRO_PROJECT_ID_XENODATA_KEY);
		
		ObjectManifest xenodataManifestObject = new ObjectManifest(JSONFile.read(getXenodataManifestFile()));
		IdList  xenodataIds = new IdList(XENODATA_TYPE, xenodataManifestObject.getAllKeys());
		if (!xenodataIds.contains(xenodataRef))
			return false;
		
		File xenodataFile = new File(getXenodataDir(), xenodataRef.getObjectId().toString());
		EnhancedJsonObject xenodataJson = DataUpgrader.readFile(xenodataFile);
		String conproProjectId = xenodataJson.optString(XENODATA_PROJECT_ID_TAG);
		
		return conproProjectId.length() > 0;
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
	
	private static File getTaskDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), TASK_TYPE);
	}
	
	private static File getIndicatorDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), INDICATOR_TYPE);
	}

	private static File getProjectMetadataManifestFile()
	{
		return getManifestFile(getProjectMetadataDir());
	}
	
	private static File getIndicatorManifestFile()
	{
		return getManifestFile(getIndicatorDir());
	}
	
	private static File getXenodataManifestFile()
	{
		return getManifestFile(getXenodataDir());
	}
	
	private static File getMethodManifestFile()
	{
		return getManifestFile(getTaskDir());
	}
	
	private static File getManifestFile(File objectsDir)
	{
		return new File(objectsDir, MANIFEST_FILE_NAME);
	}
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String CONPRO_PROJECT_ID_XENODATA_KEY = "ConPro";
	private static final String XENODATA_PROJECT_ID_TAG = "ProjectId";
	private final static String INDICATOR_METHOD_IDS_TAG = "TaskIds";
	
	private static final int PROJECT_METADATA_TYPE = 11;
	private static final int XENODATA_TYPE = 44;
	private static final int INDICATOR_TYPE = 8;
	private static final int TASK_TYPE = 3;
}
