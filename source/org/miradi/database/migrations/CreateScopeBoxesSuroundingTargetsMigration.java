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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Vector;

import org.miradi.database.DataUpgrader;
import org.miradi.database.JSONFile;
import org.miradi.database.ObjectManifest;
import org.miradi.diagram.renderers.MultilineCellRenderer;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.utils.EnhancedJsonObject;

//FIXME this migraiton is still under contruction and its test is fialing.  It also needs a bit of refactoring to remove duplication
// of strings
public class CreateScopeBoxesSuroundingTargetsMigration
{
	public CreateScopeBoxesSuroundingTargetsMigration(File jsonDirToUse)
	{
		jsonDir = jsonDirToUse;
	}

	public void surroundTargetsWithNewScopeBoxType() throws Exception
	{
		File scopeBoxDir = getObjectsDir(SCOPE_BOX_TYPE);
		if (scopeBoxDir.exists())
			throw new RuntimeException("scopeBox dir exists");
		
		File targetDir = getObjectsDir(TARGET_TYPE);
		if (! targetDir.exists())
			return;
		
		File diagramFactorDir = getObjectsDir(DIAGRAM_FACTOR_TYPE);
		if (!diagramFactorDir.exists())
			throw new RuntimeException("There are no diagramFactors.");
			
		
		File diagramFactorManifestFile = new File(diagramFactorDir, "manifest");
		if (! diagramFactorManifestFile.exists())
			throw new RuntimeException("no diagram factor manifest exists");
		
		ObjectManifest indicatorManifestObject = new ObjectManifest(JSONFile.read(diagramFactorManifestFile));
		BaseId[] allDiagramFactorIds = indicatorManifestObject.getAllKeys();
		loadAllDiagramFactorJsons(diagramFactorDir, allDiagramFactorIds);
		
		File conceptualModelDir = getObjectsDir(CONCEPTUAL_MODEL_TYPE);
		File resultsChainDir = getObjectsDir(RESULTS_CHAIN_TYPE);
		if (!resultsChainDir.exists() && !conceptualModelDir.exists())
			throw new RuntimeException("There is no results chain and no conceptual model diagram");

		if (!scopeBoxDir.exists())
			scopeBoxDir.mkdirs();
		
		if (!scopeBoxDir.exists())
			throw new RuntimeException("no scopeBox dir exists");
		
		EnhancedJsonObject scopeBoxManifestJson = new EnhancedJsonObject();
		scopeBoxManifestJson.put("Type", "ObjectManifest");
		File scopeBoxManifestFile = new File(scopeBoxDir, "manifest");
		DataUpgrader.createFile(scopeBoxManifestFile, scopeBoxManifestJson.toString());
		if (! scopeBoxManifestFile.exists())
			throw new RuntimeException("no scopeBox manifest exists");
		
		createScopeBoxes(conceptualModelDir, diagramFactorDir, scopeBoxDir, scopeBoxManifestJson, CONCEPTUAL_MODEL_TYPE);		
		createScopeBoxes(resultsChainDir, diagramFactorDir, scopeBoxDir, scopeBoxManifestJson, RESULTS_CHAIN_TYPE);
	}
	
	private void loadAllDiagramFactorJsons(File diagramFactorDir, BaseId[] diagramFactorIds) throws Exception
	{
		allDiagramFactorJsons = new Vector();
		for (int index = 0; index < diagramFactorIds.length; ++index)
		{
			File diagramFactorFile = new File(diagramFactorDir, diagramFactorIds[index].toString());
			EnhancedJsonObject diagramFactorJson = new EnhancedJsonObject(readFile(diagramFactorFile));
			allDiagramFactorJsons.add(diagramFactorJson);
		}
	}
	
	private void createScopeBoxes(File diagramObjectDir, File diagramFactorDir, File scopeBoxDir, EnhancedJsonObject scopeBoxManifestJson, final int diagramObjectType) throws Exception
	{
		File diagramObjectManifestFile = new File(diagramObjectDir, "manifest");
		if (! diagramObjectManifestFile.exists())
			return;
		
		ObjectManifest diagramObjectManifestObject = new ObjectManifest(JSONFile.read(diagramObjectManifestFile));
		BaseId[] diagramObjectIds = diagramObjectManifestObject.getAllKeys();
		for (int i = 0; i < diagramObjectIds.length; ++i)
		{
			BaseId thisDiagramObjectId = diagramObjectIds[i];
			File diagramObjectJsonFile = new File(diagramObjectDir, Integer.toString(thisDiagramObjectId.asInt()));
			EnhancedJsonObject diagramObjectJson = readFile(diagramObjectJsonFile);
			IdList diagramFactorIds = new IdList(diagramObjectType, diagramObjectJson.optString("DiagramFactorIds"));
			Rectangle scopeBoxBounds = getScopeBoxBounds(diagramFactorDir, diagramFactorIds);
			if (!scopeBoxBounds.isEmpty())
			{
				BaseId newlyCreatedScopeBoxId = createScopeBox(scopeBoxDir, scopeBoxManifestJson, scopeBoxBounds);
				createScopeBoxDiagramFactor(diagramObjectJsonFile, diagramObjectJson, diagramObjectType, diagramFactorDir, newlyCreatedScopeBoxId, scopeBoxBounds);
			}
		}		
	}
	
	private Rectangle getScopeBoxBounds(File diagramFactorDir, IdList diagramFactorIdsFromDiagramObject) throws Exception
	{
		Vector<EnhancedJsonObject> targetDiagramFactorJsons = extractTargetDiagramFactorJsons(diagramFactorDir, diagramFactorIdsFromDiagramObject);
		
		Rectangle bounds = null;
		for (int index = 0; index < targetDiagramFactorJsons.size(); ++index)
		{
			EnhancedJsonObject targetDiagramFactorJson = targetDiagramFactorJsons.get(index);
			Rectangle targetBounds = getBoundsOfDiagramFactorOrItsGroupBox(diagramFactorDir, targetDiagramFactorJson);
			bounds = getSafeUnion(bounds, targetBounds);
		}
		
		if(bounds == null)
			return new Rectangle();
	
		double height = bounds.getHeight();
		double y = bounds.getY();
		if (hasVision())
		{
			height += VISION_HEIGHT;
			y -= VISION_HEIGHT;
		}
		
		Rectangle result = new Rectangle();
		result.setRect(bounds.getX(), y, bounds.getWidth(), height);
		
		return result;
	}
	
	private BaseId createScopeBox(File scopeBoxDir, EnhancedJsonObject scopeBoxManifestJson, Rectangle scopeBoxBounds) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(getJsonDir());
		int newScopeBoxId = ++highestId;
		DataUpgrader.writeHighestIdToProjectFile(getJsonDir(), newScopeBoxId);
	
		scopeBoxManifestJson.put(Integer.toString(newScopeBoxId), "true");	
		
		EnhancedJsonObject scopeBoxJson = new EnhancedJsonObject();
		scopeBoxJson.put("Id", Integer.toString(newScopeBoxId));
		File scopeBoxFile = new File(scopeBoxDir, Integer.toString(newScopeBoxId));
		DataUpgrader.createFile(scopeBoxFile, scopeBoxJson.toString());
		
		DataUpgrader.writeManifest(scopeBoxDir, scopeBoxManifestJson);
		
		return new BaseId(newScopeBoxId);
	}
	
	private void createScopeBoxDiagramFactor(File diagramObjectJsonFile, EnhancedJsonObject diagramObjectJson, final int diagramObjectType, File diagramFactorDir, BaseId newlyCreatedScopeBoxId, Rectangle scopeBoxBounds) throws Exception
	{
		File diagramFactorManifestFile = new File(diagramFactorDir, "manifest");
		if (! diagramFactorManifestFile.exists())
			throw new RuntimeException("Diagram factor manifest file does not exist.");
	
		int highestId = DataUpgrader.readHighestIdInProjectFile(getJsonDir());
		int newScopeBoxDiagramFactorId = ++highestId;
		DataUpgrader.writeHighestIdToProjectFile(getJsonDir(), newScopeBoxDiagramFactorId);
		
		EnhancedJsonObject scopeBoxDiagramFactorJson = readFile(diagramFactorManifestFile);
		scopeBoxDiagramFactorJson.put(Integer.toString(newScopeBoxDiagramFactorId), "true");
		
		EnhancedJsonObject scopeBoxJson = new EnhancedJsonObject();
		scopeBoxJson.put("Id", Integer.toString(newScopeBoxDiagramFactorId));
		scopeBoxJson.put("WrappedFactorRef", new ORef(SCOPE_BOX_TYPE, newlyCreatedScopeBoxId).toString());
		scopeBoxJson.put("Size", EnhancedJsonObject.convertFromDimension(scopeBoxBounds.getSize()));
		scopeBoxJson.put("Location", EnhancedJsonObject.convertFromPoint(scopeBoxBounds.getLocation()));
		
		IdList diagramFactorIds = new IdList(diagramObjectType, diagramObjectJson.optString("DiagramFactorIds"));
		diagramFactorIds.add(newScopeBoxDiagramFactorId);
		diagramObjectJson.put("DiagramFactorIds", diagramFactorIds.toString());
		DataUpgrader.writeJson(diagramObjectJsonFile, diagramObjectJson);
		
		File scopeBoxFile = new File(diagramFactorDir, Integer.toString(newScopeBoxDiagramFactorId));
		DataUpgrader.createFile(scopeBoxFile, scopeBoxJson.toString());		
	}
		
	private Rectangle getSafeUnion(Rectangle2D bounds, Rectangle targetBounds)
	{
		if(bounds == null)
			return new Rectangle(targetBounds);

		Rectangle unionResult = new Rectangle();
		Rectangle.union(bounds, targetBounds, unionResult);
		return unionResult;
	}

	private Vector<EnhancedJsonObject> extractTargetDiagramFactorJsons(File diagramFactorDir, IdList diagramFactorIdsFromDiagramObject) throws Exception
	{
		Vector<EnhancedJsonObject> targetDiagramFactorJsons = new Vector();
		for (int index = 0; index < getAllDiagramFactorJsons().size(); ++index)
		{
			EnhancedJsonObject diagramFactorJson = getAllDiagramFactorJsons().get(index);
			BaseId diagramFactorId = diagramFactorJson.getId("Id");
			if (!diagramFactorIdsFromDiagramObject.contains(diagramFactorId))
				continue;
			
			ORef wrappedRef = diagramFactorJson.getRef("WrappedFactorRef");
			if (TARGET_TYPE == wrappedRef.getObjectType())
				targetDiagramFactorJsons.add(diagramFactorJson);
		}

		return targetDiagramFactorJsons;
	}
	
	private Rectangle getBoundsOfDiagramFactorOrItsGroupBox(File diagramFactorDir, EnhancedJsonObject targetDiagramFactorJson) throws Exception
	{
		EnhancedJsonObject targetOrItsGroupBoxDiagramFactorJson = getTargetOrItsGroupBoxJson(diagramFactorDir, targetDiagramFactorJson);
		Point location = targetOrItsGroupBoxDiagramFactorJson.getPoint("Location");
		Dimension size = targetOrItsGroupBoxDiagramFactorJson.getDimension("Size");
		
		return new Rectangle(location.x, location.y, size.width, size.height);
	}
	
	private EnhancedJsonObject getTargetOrItsGroupBoxJson(File diagramFactorDir, EnhancedJsonObject targetDiagramFactorJson) throws Exception
	{
		for (int index = 0; index < getAllDiagramFactorJsons().size(); ++index)
		{
			EnhancedJsonObject diagramFactorJson = getAllDiagramFactorJsons().get(index);
			ORefList groupBoxChildren = diagramFactorJson.optRefList("GroupBoxChildrenRefs");
			BaseId targetDiagramFactorId = targetDiagramFactorJson.getId("Id");
			if (groupBoxChildren.contains(new ORef(DiagramFactor.getObjectType(), targetDiagramFactorId)))
				return diagramFactorJson;
		}
			
		return targetDiagramFactorJson;
	}
	
	private boolean hasVision() throws Exception
	{
		return getVision().length()>0;
	}
	
	private String getVision() throws Exception
	{
		String projectVision = getProjectVision();
		if (projectVision.length() == 0)
			return "";
		
		return EAM.text("Vision");
	}

	private String getProjectVision() throws Exception
	{
		File projectMetadataDir = getObjectsDir(PROJECT_METADATA_TYPE);
		if (! projectMetadataDir.exists())
			throw new RuntimeException("Could not find project metadata folder");

		File projectMetadataManifestFile = new File(projectMetadataDir, "manifest");
		if (!projectMetadataManifestFile.exists())
			throw new RuntimeException("Could not find project metadata manifest file");
		
		ObjectManifest projectMetadataManifestObject = new ObjectManifest(JSONFile.read(projectMetadataManifestFile));
		BaseId[] projectMetadataIds = projectMetadataManifestObject.getAllKeys();
		if (projectMetadataIds.length != 1)
			throw new RuntimeException("Incorrect number of project metadata objects exist. count = " + projectMetadataIds.length);
		
		File singletonProjectMetadataFile = new File(projectMetadataDir, projectMetadataIds[0].toString());
		EnhancedJsonObject projectMetadataJson = new EnhancedJsonObject(readFile(singletonProjectMetadataFile));
		return projectMetadataJson.optString("ProjectVision");
	}

	public Vector<EnhancedJsonObject> getAllDiagramFactorJsons()
	{
		return allDiagramFactorJsons;
	}
	
	private EnhancedJsonObject readFile(File file) throws Exception
	{
		return DataUpgrader.readFile(file);
	}
	
	private File getObjectsDir(int type)
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), type);
	}
	
	private File getJsonDir()
	{
		return jsonDir;
	}
	
	private File jsonDir;
	
	private static final int SCOPE_BOX_TYPE = 50;
	private static final int DIAGRAM_FACTOR_TYPE = 18;
	private static final int CONCEPTUAL_MODEL_TYPE = 19;
	private static final int RESULTS_CHAIN_TYPE = 24;
	private static final int TARGET_TYPE = 22;
	private static final int PROJECT_METADATA_TYPE = 11;
	
	private Vector<EnhancedJsonObject> allDiagramFactorJsons;
	
	private final static int VISION_HEIGHT = 2 * MultilineCellRenderer.ANNOTATIONS_HEIGHT;
}
