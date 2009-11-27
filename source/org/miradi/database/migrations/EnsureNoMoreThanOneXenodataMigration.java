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
import java.util.HashSet;
import java.util.Vector;

import org.miradi.database.DataUpgrader;
import org.miradi.database.JSONFile;
import org.miradi.database.ObjectManifest;
import org.miradi.ids.BaseId;
import org.miradi.utils.EnhancedJsonObject;

public class EnsureNoMoreThanOneXenodataMigration
{
	public static Vector<EnhancedJsonObject> enureNoMoreThanOneXenodata() throws Exception
	{
		if (! getXenodataDir().exists())
			return new Vector<EnhancedJsonObject>();

		if (! getXenodataManifestFile().exists())
			return new Vector<EnhancedJsonObject>();

		Vector<EnhancedJsonObject> xenodataJsons = new Vector<EnhancedJsonObject>();
		ObjectManifest xenodataManifestObject = new ObjectManifest(JSONFile.read(getXenodataManifestFile()));
		BaseId[] xenodataIds = xenodataManifestObject.getAllKeys();
		for (int index = 0; index < xenodataIds.length; ++index)
		{
			BaseId xenodataId = xenodataIds[index];
			File xenodataFile = new File(getXenodataDir(), Integer.toString(xenodataId.asInt()));
			EnhancedJsonObject xenodataJson = DataUpgrader.readFile(xenodataFile);
			xenodataJsons.add(xenodataJson);
		}
		
		if (hasAllSameProjectIds(xenodataJsons))
			removeInPlaceAllButOne(getXenodataDir(), xenodataManifestObject, xenodataJsons);

		return xenodataJsons;
	}

	private static boolean hasAllSameProjectIds(Vector<EnhancedJsonObject> xenodatas)
	{
		HashSet<String> projectIds = new HashSet<String>();
		for(EnhancedJsonObject xenodataJson : xenodatas)
		{
			projectIds.add(xenodataJson.optString(TAG_PROJECT_ID)); 
		}
		
		return projectIds.size() == 1;
	}

	private static void removeInPlaceAllButOne(File objectDir, ObjectManifest objectManifestObject, Vector<EnhancedJsonObject> xenodataJsonsToRemove) throws Exception
	{
		if (xenodataJsonsToRemove.size() == 1)
			return;
		
		Vector<EnhancedJsonObject> xenodataJsosRemoved = new Vector();
		final int DONT_REMOVE_FIRST_ELEMENT_INDEX = 1;
		for(int index = DONT_REMOVE_FIRST_ELEMENT_INDEX; index < xenodataJsonsToRemove.size(); ++index)
		{
			EnhancedJsonObject xenodataToRemove = xenodataJsonsToRemove.get(index);
			xenodataJsosRemoved.add(xenodataToRemove);
			BaseId id = xenodataToRemove.getId(TAG_ID);
			File jsonFile = new File(objectDir, Integer.toString(id.asInt()));
			jsonFile.delete();			
			objectManifestObject.remove(id);
		}
		
		xenodataJsonsToRemove.removeAll(xenodataJsosRemoved);
		
		File manifestFile = new File(objectDir, MANIFEST_FILE_NAME);
		DataUpgrader.writeJson(manifestFile, objectManifestObject.toJson());
	}
	
	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}

	private static File getXenodataDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), XENODATA_TYPE);
	}

	private static File getXenodataManifestFile()
	{
		return new File(getXenodataDir(), MANIFEST_FILE_NAME);
	}

	private static final int XENODATA_TYPE = 44;
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String TAG_PROJECT_ID = "ProjectId";
	private static final String TAG_ID = "Id";
}
