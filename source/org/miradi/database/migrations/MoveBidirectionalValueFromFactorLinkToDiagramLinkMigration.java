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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.EnhancedJsonObject;

public class MoveBidirectionalValueFromFactorLinkToDiagramLinkMigration
{
	public static void moveBidirectionalValue() throws Exception
	{
		if (!getDiagramLinkDir().exists())
			return;
		
		if (!getDiagramLinkManifestFile().exists())
			return;
		
		if (!getFactorLinkDir().exists())
			return;

		if (!getFactorLinkManifestFile().exists())
			return;

		ObjectManifest diagramLinkObjectManifest = new ObjectManifest(JSONFile.read(getDiagramLinkManifestFile()));
		BaseId[] diagramLinkIds = diagramLinkObjectManifest.getAllKeys();
		for (int index = 0; index < diagramLinkIds.length; ++index)
		{
			BaseId diagramLinkId = diagramLinkIds[index];
			File diagramLinkFile = new File(getDiagramLinkDir(), Integer.toString(diagramLinkId.asInt()));
			EnhancedJsonObject diagramLinkJson = DataUpgrader.readFile(diagramLinkFile);
			String bidiValue = getBidiValue(diagramLinkJson);
			diagramLinkJson.put(DIAGRAM_LINK_BI_DIRECTIONAL_TAG, bidiValue);
			DataUpgrader.writeJson(diagramLinkFile, diagramLinkJson);
		}
	}

	private static String getBidiValue(EnhancedJsonObject diagramLinkJson) throws Exception
	{
		ORef wrappedRef = getWrappedFactorLinkRef(diagramLinkJson);
		if (wrappedRef.isValid())
			return getFactorLinkBidiValue(wrappedRef);
		
		ORefList groupedDiagramLinkRef = diagramLinkJson.optRefList(GROUPED_DIAGRAM_LINK_REFS_TAG);
		if (groupedDiagramLinkRef.isEmpty())
			return "";
		
		ORef coveredGroupBoxDiagramLinkRef = groupedDiagramLinkRef.getFirstElement();
		File diagramLinkFile = new File(getDiagramLinkDir(), Integer.toString(coveredGroupBoxDiagramLinkRef.getObjectId().asInt()));
		EnhancedJsonObject groupBoxCoveredDiagramLinkJson = DataUpgrader.readFile(diagramLinkFile);
		return getFactorLinkBidiValue(getWrappedFactorLinkRef(groupBoxCoveredDiagramLinkJson));
	}

	private static ORef getWrappedFactorLinkRef(EnhancedJsonObject diagramLinkJson)
	{
		return new ORef(FACTOR_LINK_TYPE, diagramLinkJson.optId(WRAPPED_ID_TAG));
	}

	private static String getFactorLinkBidiValue(ORef wrappedRef) throws Exception
	{
		File factorLinkFile = new File(getFactorLinkDir(), Integer.toString(wrappedRef.getObjectId().asInt()));
		EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
		
		return factorLinkJson.optString(FACTOR_LINK_BI_DIRECTIONAL_TAG);
	}
	
	private static File getDiagramLinkDir()
	{
		return getObjectDir(DIAGRAM_LINK_TYPE);
	}
	
	private static File getFactorLinkDir()
	{
		return getObjectDir(FACTOR_LINK_TYPE);
	}

	private static File getObjectDir(int objectType)
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), objectType);
	}

	private static File getFactorLinkManifestFile()
	{
		return getmanifestFile(getFactorLinkDir());
	}
	
	private static File getDiagramLinkManifestFile()
	{
		return getmanifestFile(getDiagramLinkDir());
	}

	private static File getmanifestFile(File factorLinkDir)
	{
		return new File(factorLinkDir, MANIFEST_FILE_NAME);
	}

	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}

	private static final int FACTOR_LINK_TYPE = 6;
	private static final int DIAGRAM_LINK_TYPE = 13;
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	
	private static final String FACTOR_LINK_BI_DIRECTIONAL_TAG = "BidirectionalLink";
	private static final String DIAGRAM_LINK_BI_DIRECTIONAL_TAG = "IsBidirectionalLink";
	private static final String GROUPED_DIAGRAM_LINK_REFS_TAG = "GroupedDiagramLinkRefs";
	private static final String WRAPPED_ID_TAG = "WrappedLinkId";
}
