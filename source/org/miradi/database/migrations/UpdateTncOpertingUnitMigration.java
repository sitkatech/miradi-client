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
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class UpdateTncOpertingUnitMigration
{
	public static CodeList updateTncOperatingUnitsList() throws Exception
	{
		File jsonDir = DataUpgrader.getTopJsonDir();
		File projectMetadataDir = DataUpgrader.getObjectsDir(jsonDir, PROJECT_METADATA_TYPE);
		if (! projectMetadataDir.exists())
			return new CodeList();
		
		File manifestFile = new File(projectMetadataDir, MANIFEST_FILE_NAME);
		if (! manifestFile.exists())
			return new CodeList();

		ObjectManifest manifestObject = new ObjectManifest(JSONFile.read(manifestFile));
		BaseId[] projectMetadataIds = manifestObject.getAllKeys();
		
		final int SINGLE_PROJECT_METADATA_INDEX = 0;
		BaseId projectMetadataId = projectMetadataIds[SINGLE_PROJECT_METADATA_INDEX];
		File projectMetadataJsonFile = new File(projectMetadataDir, Integer.toString(projectMetadataId.asInt()));
		EnhancedJsonObject projectMetadataJson = DataUpgrader.readFile(projectMetadataJsonFile);
		CodeList updatedTncOperatingUnitCodes = new CodeList(projectMetadataJson.optCodeList(TNC_OPERATING_UNIT_LIST_FIELD_NAME));
		updatedTncOperatingUnitCodes.subtract(getOldOperatingUnitCodesToBeRemoved());
		
		CodeList tncOperatingUnitCodesWithOnlyOldCodes = new CodeList(projectMetadataJson.optCodeList(TNC_OPERATING_UNIT_LIST_FIELD_NAME));
		tncOperatingUnitCodesWithOnlyOldCodes.retainAll(getOldOperatingUnitCodesToBeRemoved());
		if (tncOperatingUnitCodesWithOnlyOldCodes.size() > 0)
			updatedTncOperatingUnitCodes.add(TNC_OPERATING_UNITS_OBSOLETE_CODE);
		
		projectMetadataJson.put(TNC_OPERATING_UNIT_LIST_FIELD_NAME, updatedTncOperatingUnitCodes.toString());
		DataUpgrader.writeJson(projectMetadataJsonFile, projectMetadataJson);
		
		return tncOperatingUnitCodesWithOnlyOldCodes;
	}
	
	private static CodeList getOldOperatingUnitCodesToBeRemoved()
	{
		CodeList oldOperatingUnitsCodes = new CodeList();
		oldOperatingUnitsCodes.add("AL_US");
		oldOperatingUnitsCodes.add("ATLFO");
		oldOperatingUnitsCodes.add("CAMER");
		oldOperatingUnitsCodes.add("CSAVA");
		oldOperatingUnitsCodes.add("CUSRO");
		oldOperatingUnitsCodes.add("EUSRO");
		oldOperatingUnitsCodes.add("MCARO");
		oldOperatingUnitsCodes.add("PNWRO");
		oldOperatingUnitsCodes.add("RMTRO");
		oldOperatingUnitsCodes.add("SAMRO");
		oldOperatingUnitsCodes.add("SUSRO");
		
		return oldOperatingUnitsCodes;
	}
	
	private static final int PROJECT_METADATA_TYPE = 11;
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String TNC_OPERATING_UNIT_LIST_FIELD_NAME = "TNC.OperatingUnitList";
	private static final String TNC_OPERATING_UNITS_OBSOLETE_CODE = "OBSOLETE";
}
