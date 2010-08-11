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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.EnhancedJsonObject;

public class ShareSameLabeledScopeBoxesMigration
{
	public static void shareSameLabeledScopeBoxesAcrossAllDiagrams() throws Exception
	{
		if (!getScopeBoxDir().exists())
			return;
		
		if (!getScopeBoxManifestFile().exists())
			return;

		if (!getDiagramFactorDir().exists())
			return;
		
		if (!getDiagramFactorManifestFile().exists())
			return;
		
		if (!getConceptualModelDir().exists())
			throw new RuntimeException(EAM.text("Project is missing Conceptual Model Diagram Directory."));
		
		if (!getConceptualModelManifestFile().exists())
			throw new RuntimeException(EAM.text("Project is missing Conceptual Model Manifest File"));
		
		shareAllMatchingScopeBoxesInAllDiagrams(getConceptualModelDir(), getConceptualModelIds());
		shareAllMatchingScopeBoxesInAllDiagrams(getResultsChainDir(), getResultsChainIds());
	}

	private static void shareAllMatchingScopeBoxesInAllDiagrams(File diagramDir, BaseId[] diagramIds) throws Exception
	{
		for (int index = 0; index < diagramIds.length; ++index)
		{
			BaseId thisDiagramId = diagramIds[index];
			File diagramJsonFile = new File(diagramDir, Integer.toString(thisDiagramId.asInt()));
			EnhancedJsonObject diagramJson = DataUpgrader.readFile(diagramJsonFile);
			IdList diagramFactorIds = diagramJson.optIdList(DIAGRAM_FACTOR_TYPE, DIAGRAM_FACTOR_IDS_TAG);
			
			Vector<File> diagramFactorScopeBoxJsons = extractScopeBoxDiagramFactorJsonFiles(diagramFactorIds);
			if (diagramFactorScopeBoxJsons.size() == 1)
			{
				File singleDiagramFactorScopeBox = diagramFactorScopeBoxJsons.get(0);
				shareAllMatchingScopeBoxesInDiagrams(getConceptualModelDir(), getConceptualModelIds(), CONCEPTUAL_MODEL_TYPE, thisDiagramId, singleDiagramFactorScopeBox);
				shareAllMatchingScopeBoxesInDiagrams(getResultsChainDir(), getResultsChainIds(), RESULTS_CHAIN_TYPE, thisDiagramId, singleDiagramFactorScopeBox);
			}
		}
	}

	private static void shareAllMatchingScopeBoxesInDiagrams(File diagramDir, BaseId[] diagramIds, final int diagramType, BaseId originatingDiagramId, File scopeBoxDiagramFactorJsonFileToUseToMatch) throws Exception
	{
		for (int index = 0; index < diagramIds.length; ++index)
		{
			BaseId thisId = diagramIds[index];
			if (thisId.equals(originatingDiagramId))
				continue;
			
			File diagramJsonFile = new File(diagramDir, Integer.toString(thisId.asInt()));
			EnhancedJsonObject diagramJson = DataUpgrader.readFile(diagramJsonFile);
			IdList diagramFactorIds = diagramJson.optIdList(DIAGRAM_FACTOR_TYPE, DIAGRAM_FACTOR_IDS_TAG);
			Vector<File> diagramFactorScopeBoxJsons = extractScopeBoxDiagramFactorJsonFiles(diagramFactorIds);
			if (diagramFactorScopeBoxJsons.size() == 1)
			{
				EnhancedJsonObject scopeBoxDiagramFactorJsonToMatch = DataUpgrader.readFile(scopeBoxDiagramFactorJsonFileToUseToMatch);
				EnhancedJsonObject diagramFactorJsonToUpdate = DataUpgrader.readFile(diagramFactorScopeBoxJsons.get(0));
				ORef wrappedRefToMatch = scopeBoxDiagramFactorJsonToMatch.optRef(WRAPPED_REF_TAG);
				ORef oldScopeBoxToDelete  = findDiagramScopeBoxToUpdate(diagramFactorJsonToUpdate, wrappedRefToMatch);
				if (oldScopeBoxToDelete.isValid())
				{
					shareDiagramFactorScopeBox(diagramFactorScopeBoxJsons.get(0), diagramFactorJsonToUpdate, wrappedRefToMatch);
					deleteOrphanedScopeBox(oldScopeBoxToDelete);
				}
			}
		}
	}

	private static ORef findDiagramScopeBoxToUpdate(EnhancedJsonObject diagramFactorJsonToUpdate, ORef wrappedRefToMatch) throws Exception
	{
		String scopeBoxLabelToMatch = getScopeBoxLabel(wrappedRefToMatch);

		ORef wrappedRef = diagramFactorJsonToUpdate.optRef(WRAPPED_REF_TAG);
		String scopeBoxLabel = getScopeBoxLabel(wrappedRef);
		
		boolean sameLabel = scopeBoxLabelToMatch.equals(scopeBoxLabel);
		boolean wasNotAlreadyShared = !wrappedRefToMatch.equals(wrappedRef);
		if (sameLabel && wasNotAlreadyShared)
		{	
			return wrappedRef;
		}
		
		return ORef.INVALID;
	}

	private static void shareDiagramFactorScopeBox(File diagramFactorJsonFileToUpdate, EnhancedJsonObject diagramFactorJsonToUpdate, ORef wrappedRefToMatch) throws Exception
	{
		diagramFactorJsonToUpdate.put(WRAPPED_REF_TAG, wrappedRefToMatch.toString());
		DataUpgrader.writeJson(diagramFactorJsonFileToUpdate, diagramFactorJsonToUpdate);
	}

	private static void deleteOrphanedScopeBox(ORef scopeBoxRefToDelete) throws Exception
	{
		ObjectManifest scopeBoxManifest = new ObjectManifest(JSONFile.read(getScopeBoxManifestFile()));
		scopeBoxManifest.remove(scopeBoxRefToDelete.getObjectId());
		
		DataUpgrader.writeJson(getScopeBoxManifestFile(), scopeBoxManifest.toJson());	
		File scopeBoxFileToDelete = new File(getScopeBoxDir(), Integer.toString(scopeBoxRefToDelete.getObjectId().asInt()));
		scopeBoxFileToDelete.delete();
	}

	private static String getScopeBoxLabel(ORef scopeBoxRef) throws Exception
	{
		File scopeBoxJsonFileToMatch = new File(getScopeBoxDir(), Integer.toString(scopeBoxRef.getObjectId().asInt()));
		EnhancedJsonObject scopeBoxJsonMatch = DataUpgrader.readFile(scopeBoxJsonFileToMatch);
		
		return scopeBoxJsonMatch.optString(LABEL_TAG);
	}

	@SuppressWarnings("unchecked")
	private static Vector<File> extractScopeBoxDiagramFactorJsonFiles(IdList diagramFactorIds) throws Exception
	{
		Vector<File> diagramFactorScopeBoxJsons = new Vector();
		for (int index = 0; index < diagramFactorIds.size(); ++index)
		{
			BaseId diagramFactorId = diagramFactorIds.get(index);
			File diagramFactorJsonFile = new File(getDiagramFactorDir(), Integer.toString(diagramFactorId.asInt()));
			EnhancedJsonObject diagramFactorJson = DataUpgrader.readFile(diagramFactorJsonFile);
			ORef wrappedRef = diagramFactorJson.optRef(WRAPPED_FACTOR_REF_TAG);
			if (wrappedRef.getObjectType() == SCOPE_BOX_TYPE)
				diagramFactorScopeBoxJsons.add(diagramFactorJsonFile);
		}
		
		return diagramFactorScopeBoxJsons;
	}
	
	private static BaseId[] getConceptualModelIds() throws Exception
	{
		ObjectManifest conceptualModelManifest = new ObjectManifest(JSONFile.read(getConceptualModelManifestFile()));
		return conceptualModelManifest.getAllKeys();
	}
	
	private static BaseId[] getResultsChainIds() throws Exception
	{
		if (!getResultsChainDir().exists())
			return new BaseId[0];
		
		if (!getResultsChainManifestFile().exists())
			return new BaseId[0];
		
		ObjectManifest resultsChainManifest = new ObjectManifest(JSONFile.read(getResultsChainManifestFile()));
		return resultsChainManifest.getAllKeys();
	}

	private static File getResultsChainManifestFile()
	{
		return new File(getResultsChainDir(), MANIFEST_FILE_NAME);
	}

	private static File getResultsChainDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), RESULTS_CHAIN_TYPE);
	}

	private static File getConceptualModelManifestFile()
	{
		return new File(getConceptualModelDir(), MANIFEST_FILE_NAME);
	}
	
	private static File getConceptualModelDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), CONCEPTUAL_MODEL_TYPE);
	}
	
	private static File getJsonDir()
	{
		return DataUpgrader.getTopJsonDir();
	}
	
	private static File getScopeBoxDir()
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), SCOPE_BOX_TYPE);
	}

	private static File getDiagramFactorDir()
	{
		File diagramFactorDir = DataUpgrader.getObjectsDir(getJsonDir(), DIAGRAM_FACTOR_TYPE);
		return diagramFactorDir;
	}
	
	private static File getDiagramFactorManifestFile()
	{
		return new File(getDiagramFactorDir(), MANIFEST_FILE_NAME);
	}

	private static File getScopeBoxManifestFile()
	{
		File scopeBoxManifestFile = new File(getScopeBoxDir(), MANIFEST_FILE_NAME);
		return scopeBoxManifestFile;
	}
	
	private static final String MANIFEST_FILE_NAME = "manifest";
	private static final String WRAPPED_REF_TAG = "WrappedFactorRef";
	private static final String LABEL_TAG = "Label";
	private static final String DIAGRAM_FACTOR_IDS_TAG = "DiagramFactorIds";
	private static final String WRAPPED_FACTOR_REF_TAG = "WrappedFactorRef";
	
	private static final int DIAGRAM_FACTOR_TYPE = 18;
	private static final int CONCEPTUAL_MODEL_TYPE = 19;
	private static final int RESULTS_CHAIN_TYPE = 24;
	private static final int SCOPE_BOX_TYPE = 50;
}
