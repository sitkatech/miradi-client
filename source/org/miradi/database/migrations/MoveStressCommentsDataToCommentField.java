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

public class MoveStressCommentsDataToCommentField
{
	public static void moveCommentsToComment() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		final int STRESS_TYPE = 33;
		
		File stressDir = DataUpgrader.getObjectsDir(jsonDir, STRESS_TYPE);
		if (! stressDir.exists())
			return;
		
		File stressManifestFile = new File(stressDir, "manifest");
		if (! stressManifestFile.exists())
			return;
		
		ObjectManifest stressManifestObject = new ObjectManifest(JSONFile.read(stressManifestFile));
		BaseId[] stressIds = stressManifestObject.getAllKeys();
		for (int i = 0; i < stressIds.length; ++i)
		{
			BaseId stressId = stressIds[i];
			File stressJsonFile = new File(stressDir, Integer.toString(stressId.asInt()));
			EnhancedJsonObject stressJson = DataUpgrader.readFile(stressJsonFile);
			String comments = stressJson.optString("Comments");
			stressJson.put("Comment", comments);
			stressJson.put("Comments", "");
			
			DataUpgrader.writeJson(stressJsonFile, stressJson);
		}
	}
}
