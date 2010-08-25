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
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.EnhancedJsonObject;

public class RemoveNonExistingRelatedThreatFromThreatReductionResultMigration
{
	public static void removeNonExistingRelatedThreatRef() throws Exception
	{
		File jsonDir = getJsonDir();
		File threatReductionResultDir = DataUpgrader.getObjectsDir(jsonDir, THREAT_REDUCTION_RESULT_TYPE);
		if (! threatReductionResultDir.exists())
			return;

		if (! getThreatReductionResultManifestFile().exists())
			return;

		ObjectManifest threatReductionResultManifestObject = new ObjectManifest(JSONFile.read(getThreatReductionResultManifestFile()));
		BaseId[] threatReductionResultIds = threatReductionResultManifestObject.getAllKeys();
		for (int index = 0; index < threatReductionResultIds.length; ++index)
		{
			BaseId threatReductionResultId = threatReductionResultIds[index];
			File threatReductionResultJsonFile = new File(threatReductionResultDir, Integer.toString(threatReductionResultId.asInt()));
			EnhancedJsonObject threatReductionResultJson = DataUpgrader.readFile(threatReductionResultJsonFile);
			ORef relatedThreatRef = threatReductionResultJson.optRef(RELATED_THREAT_REF);
			if (shouldClearRelatedThreatRef(relatedThreatRef))
			{
				threatReductionResultJson.put(RELATED_THREAT_REF, "");
				DataUpgrader.writeJson(threatReductionResultJsonFile, threatReductionResultJson);
			}
		}
	}

	private static boolean shouldClearRelatedThreatRef(ORef relatedThreatRef) throws Exception
	{
		if (relatedThreatRef.isInvalid())
			return false;
		
		if (!getCauseDir().exists())
			return true;
		
		if (!getCauseManifestFile().exists())
			return true;
		
		ObjectManifest causeManifestObject = new ObjectManifest(JSONFile.read(getCauseManifestFile()));
		BaseId[] causeIds = causeManifestObject.getAllKeys();
		for (int index = 0; index < causeIds.length; ++index)
		{
			if (causeIds[index].equals(relatedThreatRef.getObjectId()))
				return false;
		}
		
		return true;
	}

	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}
	
	private static File getThreatReductionResultDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), THREAT_REDUCTION_RESULT_TYPE);
	}
	
	private static File getThreatReductionResultManifestFile()
	{
		return new File(getThreatReductionResultDir(), MANIFEST_FILE_NAME);
	}

	private static File getCauseDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), CAUSE_TYPE);
	}

	private static File getCauseManifestFile()
	{
		return new File(getCauseDir(), MANIFEST_FILE_NAME);
	}
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String RELATED_THREAT_REF = "RelatedDirectThreatRef";
	
	private static final int CAUSE_TYPE = 20;
	private static final int THREAT_REDUCTION_RESULT_TYPE = 25;
}
