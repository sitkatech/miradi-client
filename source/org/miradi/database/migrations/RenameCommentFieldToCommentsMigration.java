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

public class RenameCommentFieldToCommentsMigration
{
	public static void renameCommentFieldToComments() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();		
		int[] typesToMigrate = {TASK_TYPE, 
								CAUSE_TYPE, 
								STRATEGY_TYPE, 
								TARGET_TYPE,
								INTERMEDIATE_RESULT_TYPE,
								THREAT_REDUCTION_RESULT_TYPE, 
								TEXT_BOX_TYPE,
								INDICATOR_TYPE,
								MEASUREMENT_TYPE,
								STRESS_TYPE,
								GROUP_BOX_TYPE,
								REPORT_TEMPLATE_TYPE,
								TAGGED_OBJECT_SET_TYPE,
								SCOPE_BOX_TYPE,
								HUMAN_WELFARE_TARGET_TYPE,
								};

		for (int index = 0; index < typesToMigrate.length; ++index)
		{
			migrateCommentFieldForType(jsonDir, typesToMigrate[index]);
		}
	}
	private static void migrateCommentFieldForType(File jsonDir, final int objectType) throws Exception
	{
		File objectDir = DataUpgrader.getObjectsDir(jsonDir, objectType);
		if (! objectDir.exists())
			return;
		
		File manifestFile = new File(objectDir, "manifest");
		if (! manifestFile.exists())
			return;
		
		ObjectManifest objectManifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] objectIds = objectManifestObject.getAllKeys();
		for (int i = 0; i < objectIds.length; ++i)
		{
			BaseId objectId = objectIds[i];
			File objectJsonFile = new File(objectDir, Integer.toString(objectId.asInt()));
			EnhancedJsonObject objectJson = DataUpgrader.readFile(objectJsonFile);
			String comments = objectJson.optString("Comment");
			objectJson.put("Comments", comments);
			objectJson.remove("Comment");
			
			DataUpgrader.writeJson(objectJsonFile, objectJson);
		}
	}
	
	private static final int TASK_TYPE = 3;
	private static final int CAUSE_TYPE = 20;
	private static final int STRATEGY_TYPE = 21;
	private static final int TARGET_TYPE = 22;
	private static final int INTERMEDIATE_RESULT_TYPE = 23;
	private static final int THREAT_REDUCTION_RESULT_TYPE = 25;
	private static final int TEXT_BOX_TYPE = 26;
	private static final int INDICATOR_TYPE = 8;
	private static final int MEASUREMENT_TYPE = 32;
	private static final int STRESS_TYPE = 33;
	private static final int GROUP_BOX_TYPE = 35;
	private static final int REPORT_TEMPLATE_TYPE = 46;
	private static final int TAGGED_OBJECT_SET_TYPE = 47;
	private static final int SCOPE_BOX_TYPE = 50;
	private static final int HUMAN_WELFARE_TARGET_TYPE = 52;
}
