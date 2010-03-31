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
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.utils.EnhancedJsonObject;

public class CreateScopeBoxesSuroundingTargetsMigration
{
	private static final String DIAGRAM_FACTOR_IDS = "DiagramFactorIds";

	private static final String LOCATION = "Location";

	private static final String SIZE = "Size";

	private static final String WRAPPED_FACTOR_REF = "WrappedFactorRef";

	private static final String TRUE = "true";

	private static final String ID = "Id";

	private static final String MANIFEST_LABEL = "manifest";
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
		
		diagramFactorDir = getObjectsDir(DIAGRAM_FACTOR_TYPE);
		if (!getDiagramFactorDir().exists())
			throw new RuntimeException("There are no diagramFactors.");
			
		
		File diagramFactorManifestFile = new File(getDiagramFactorDir(), MANIFEST_LABEL);
		if (! diagramFactorManifestFile.exists())
			throw new RuntimeException("no diagram factor manifest exists");
		
		ObjectManifest diagramFactorManifestObject = new ObjectManifest(JSONFile.read(diagramFactorManifestFile));
		BaseId[] allDiagramFactorIds = diagramFactorManifestObject.getAllKeys();
		allDiagramFactorJsons = loadAllDiagramFactorJsons(allDiagramFactorIds);
		projectMetadataJson = loadProjectMetadataJson();
		
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
		File scopeBoxManifestFile = new File(scopeBoxDir, MANIFEST_LABEL);
		DataUpgrader.createFile(scopeBoxManifestFile, scopeBoxManifestJson.toString());
		if (! scopeBoxManifestFile.exists())
			throw new RuntimeException("no scopeBox manifest exists");
		
		createScopeBoxes(conceptualModelDir, scopeBoxDir, scopeBoxManifestJson, CONCEPTUAL_MODEL_TYPE);		
		createScopeBoxes(resultsChainDir, scopeBoxDir, scopeBoxManifestJson, RESULTS_CHAIN_TYPE);
	}
	
	private Vector<EnhancedJsonObject> loadAllDiagramFactorJsons(BaseId[] diagramFactorIds) throws Exception
	{
		Vector<EnhancedJsonObject> diagramFactorJsons = new Vector();
		for (int index = 0; index < diagramFactorIds.length; ++index)
		{
			File diagramFactorFile = new File(getDiagramFactorDir(), diagramFactorIds[index].toString());
			EnhancedJsonObject diagramFactorJson = new EnhancedJsonObject(readFile(diagramFactorFile));
			diagramFactorJsons.add(diagramFactorJson);
		}
		
		return diagramFactorJsons;
	}
	
	private void createScopeBoxes(File diagramObjectDir, File scopeBoxDir, EnhancedJsonObject scopeBoxManifestJson, final int diagramObjectType) throws Exception
	{
		File diagramObjectManifestFile = new File(diagramObjectDir, MANIFEST_LABEL);
		if (! diagramObjectManifestFile.exists())
			return;
		
		ObjectManifest diagramObjectManifestObject = new ObjectManifest(JSONFile.read(diagramObjectManifestFile));
		BaseId[] diagramObjectIds = diagramObjectManifestObject.getAllKeys();
		for (int i = 0; i < diagramObjectIds.length; ++i)
		{
			BaseId thisDiagramObjectId = diagramObjectIds[i];
			File diagramObjectJsonFile = new File(diagramObjectDir, Integer.toString(thisDiagramObjectId.asInt()));
			EnhancedJsonObject diagramObjectJson = readFile(diagramObjectJsonFile);
			IdList diagramFactorIds = new IdList(diagramObjectType, diagramObjectJson.optString(DIAGRAM_FACTOR_IDS));
			Rectangle scopeBoxBounds = getScopeBoxBounds(diagramFactorIds);
			if (!scopeBoxBounds.isEmpty())
			{
				BaseId newlyCreatedScopeBoxId = createScopeBox(scopeBoxDir, scopeBoxManifestJson, scopeBoxBounds);
				createScopeBoxDiagramFactor(diagramObjectJsonFile, diagramObjectJson, diagramObjectType, newlyCreatedScopeBoxId, scopeBoxBounds);
			}
		}		
	}
	
	public Rectangle getScopeBoxBounds(IdList diagramFactorIdsFromDiagramObject) throws Exception
	{
		Rectangle targetBounds = getTargetBounds(diagramFactorIdsFromDiagramObject);
		Rectangle newBounds = new Rectangle(0,0,0,0);
		if(!targetBounds.equals(newBounds))
		{
			Point location = new Point((int)targetBounds.getX() - 2 * DEFAULT_GRID_SIZE, (int)targetBounds.getY()  - SHORT_SCOPE_HEIGHT);
			location = getSnapped(location);
			Dimension size = new Dimension((int)targetBounds.getWidth() + 4 * DEFAULT_GRID_SIZE, (int)targetBounds.getHeight() + SHORT_SCOPE_HEIGHT  + 2 * DEFAULT_GRID_SIZE);
			newBounds = new Rectangle(location, size);
		}
		
		return newBounds;
	}
	
	public Point getSnapped(Point point)
	{
		int gridSize = DEFAULT_GRID_SIZE;
		return new Point(roundTo(point.x, gridSize), roundTo(point.y, gridSize));
	}
	
	int roundTo(int valueToRound, int incrementToRoundTo)
	{
		int sign = 1;
		if(valueToRound < 0)
			sign = -1;
		valueToRound = Math.abs(valueToRound);
		
		int half = incrementToRoundTo / 2;
		valueToRound += half;
		valueToRound -= (valueToRound % incrementToRoundTo);
		return valueToRound * sign;
	}
	
	private Rectangle getTargetBounds(IdList diagramFactorIdsFromDiagramObject) throws Exception
	{
		Vector<EnhancedJsonObject> targetDiagramFactorJsons = extractTargetDiagramFactorJsons(diagramFactorIdsFromDiagramObject);
		
		Rectangle bounds = null;
		for (int index = 0; index < targetDiagramFactorJsons.size(); ++index)
		{
			EnhancedJsonObject targetDiagramFactorJson = targetDiagramFactorJsons.get(index);
			Rectangle targetBounds = getBoundsOfDiagramFactorOrItsGroupBox(targetDiagramFactorJson);
			bounds = getSafeUnion(bounds, targetBounds);
		}
		
		if(bounds == null)
			return new Rectangle();
	
		double height = bounds.getHeight();
		double y = bounds.getY();
		Rectangle result = new Rectangle();
		result.setRect(bounds.getX(), y, bounds.getWidth(), height);
		
		return result;
	}
	
	private BaseId createScopeBox(File scopeBoxDir, EnhancedJsonObject scopeBoxManifestJson, Rectangle scopeBoxBounds) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(getJsonDir());
		int newScopeBoxId = ++highestId;
		DataUpgrader.writeHighestIdToProjectFile(getJsonDir(), newScopeBoxId);
	
		scopeBoxManifestJson.put(Integer.toString(newScopeBoxId), TRUE);	
		
		EnhancedJsonObject scopeBoxJson = new EnhancedJsonObject();
		scopeBoxJson.put(ID, Integer.toString(newScopeBoxId));
		scopeBoxJson.put("Label", getShortScope());
		scopeBoxJson.put("Text", getScope());
		
		File scopeBoxFile = new File(scopeBoxDir, Integer.toString(newScopeBoxId));
		DataUpgrader.createFile(scopeBoxFile, scopeBoxJson.toString());
		
		MigrationsOlderThanMiradiVersion2.writeManifest(scopeBoxDir, scopeBoxManifestJson);
		
		return new BaseId(newScopeBoxId);
	}
	
	private void createScopeBoxDiagramFactor(File diagramObjectJsonFile, EnhancedJsonObject diagramObjectJson, final int diagramObjectType, BaseId newlyCreatedScopeBoxId, Rectangle scopeBoxBounds) throws Exception
	{
		File diagramFactorManifestFile = new File(getDiagramFactorDir(), MANIFEST_LABEL);
		if (! diagramFactorManifestFile.exists())
			throw new RuntimeException("Diagram factor manifest file does not exist.");
	
		EnhancedJsonObject diagramFactorManifestJson = readFile(diagramFactorManifestFile);
		int highestId = DataUpgrader.readHighestIdInProjectFile(getJsonDir());
		int newScopeBoxDiagramFactorId = ++highestId;
		DataUpgrader.writeHighestIdToProjectFile(getJsonDir(), newScopeBoxDiagramFactorId);
		diagramFactorManifestJson.put(Integer.toString(newScopeBoxDiagramFactorId), TRUE);
		
		EnhancedJsonObject scopeBoxDiagramFactorJson = readFile(diagramFactorManifestFile);
		scopeBoxDiagramFactorJson.put(Integer.toString(newScopeBoxDiagramFactorId), TRUE);
		
		EnhancedJsonObject scopeBoxJson = new EnhancedJsonObject();
		scopeBoxJson.put(ID, Integer.toString(newScopeBoxDiagramFactorId));
		scopeBoxJson.put(WRAPPED_FACTOR_REF, new ORef(SCOPE_BOX_TYPE, newlyCreatedScopeBoxId).toString());
		scopeBoxJson.put(SIZE, EnhancedJsonObject.convertFromDimension(scopeBoxBounds.getSize()));
		scopeBoxJson.put(LOCATION, EnhancedJsonObject.convertFromPoint(scopeBoxBounds.getLocation()));
		
		IdList diagramFactorIds = new IdList(diagramObjectType, diagramObjectJson.optString(DIAGRAM_FACTOR_IDS));
		diagramFactorIds.add(newScopeBoxDiagramFactorId);
		diagramObjectJson.put(DIAGRAM_FACTOR_IDS, diagramFactorIds.toString());
		DataUpgrader.writeJson(diagramObjectJsonFile, diagramObjectJson);
		
		File scopeBoxFile = new File(getDiagramFactorDir(), Integer.toString(newScopeBoxDiagramFactorId));
		DataUpgrader.createFile(scopeBoxFile, scopeBoxJson.toString());
		
		MigrationsOlderThanMiradiVersion2.writeManifest(getDiagramFactorDir(), diagramFactorManifestJson);
	}
		
	private Rectangle getSafeUnion(Rectangle2D bounds, Rectangle targetBounds)
	{
		if(bounds == null)
			return new Rectangle(targetBounds);

		Rectangle unionResult = new Rectangle();
		Rectangle.union(bounds, targetBounds, unionResult);
		return unionResult;
	}

	private Vector<EnhancedJsonObject> extractTargetDiagramFactorJsons(IdList diagramFactorIdsFromDiagramObject) throws Exception
	{
		Vector<EnhancedJsonObject> targetDiagramFactorJsons = new Vector();
		for (int index = 0; index < getAllDiagramFactorJsons().size(); ++index)
		{
			EnhancedJsonObject diagramFactorJson = getAllDiagramFactorJsons().get(index);
			BaseId diagramFactorId = diagramFactorJson.getId(ID);
			if (!diagramFactorIdsFromDiagramObject.contains(diagramFactorId))
				continue;
			
			ORef wrappedRef = diagramFactorJson.getRef(WRAPPED_FACTOR_REF);
			if (TARGET_TYPE == wrappedRef.getObjectType())
				targetDiagramFactorJsons.add(diagramFactorJson);
		}

		return targetDiagramFactorJsons;
	}
	
	private Rectangle getBoundsOfDiagramFactorOrItsGroupBox(EnhancedJsonObject targetDiagramFactorJson) throws Exception
	{
		EnhancedJsonObject targetOrItsGroupBoxDiagramFactorJson = getTargetOrItsGroupBoxJson(targetDiagramFactorJson);
		Point location = targetOrItsGroupBoxDiagramFactorJson.getPoint(LOCATION);
		Dimension size = targetOrItsGroupBoxDiagramFactorJson.getDimension(SIZE);
		
		return new Rectangle(location.x, location.y, size.width, size.height);
	}
	
	private EnhancedJsonObject getTargetOrItsGroupBoxJson(EnhancedJsonObject targetDiagramFactorJson) throws Exception
	{
		for (int index = 0; index < getAllDiagramFactorJsons().size(); ++index)
		{
			EnhancedJsonObject diagramFactorJson = getAllDiagramFactorJsons().get(index);
			ORefList groupBoxChildren = diagramFactorJson.optRefList("GroupBoxChildrenRefs");
			BaseId targetDiagramFactorId = targetDiagramFactorJson.getId(ID);
			if (groupBoxChildren.contains(new ORef(DiagramFactor.getObjectType(), targetDiagramFactorId)))
				return diagramFactorJson;
		}
			
		return targetDiagramFactorJson;
	}
	
	private String getShortScope()
	{
		String label = getProjectMetadataJson().optString("ShortProjectScope");
		if (label.length() == 0)
			return "";
		
		return EAM.text("Project Scope") + " :" + label;
	}
	
	private String getScope()
	{
		return getProjectMetadataJson().optString("ProjectScope");
	}

	private EnhancedJsonObject loadProjectMetadataJson() throws Exception
	{
		File projectMetadataDir = getObjectsDir(PROJECT_METADATA_TYPE);
		if (! projectMetadataDir.exists())
			throw new RuntimeException("Could not find project metadata folder");

		File projectMetadataManifestFile = new File(projectMetadataDir, MANIFEST_LABEL);
		if (!projectMetadataManifestFile.exists())
			throw new RuntimeException("Could not find project metadata manifest file");
		
		ObjectManifest projectMetadataManifestObject = new ObjectManifest(JSONFile.read(projectMetadataManifestFile));
		BaseId[] projectMetadataIds = projectMetadataManifestObject.getAllKeys();
		if (projectMetadataIds.length != 1)
			throw new RuntimeException("Incorrect number of project metadata objects exist. count = " + projectMetadataIds.length);
		
		File singletonProjectMetadataFile = new File(projectMetadataDir, projectMetadataIds[0].toString());
		
		return new EnhancedJsonObject(readFile(singletonProjectMetadataFile));
	}

	private Vector<EnhancedJsonObject> getAllDiagramFactorJsons()
	{
		return allDiagramFactorJsons;
	}
	
	private EnhancedJsonObject getProjectMetadataJson()
	{
		return projectMetadataJson;
	}
	
	private EnhancedJsonObject readFile(File file) throws Exception
	{
		return DataUpgrader.readFile(file);
	}
	
	private File getObjectsDir(int type)
	{
		return DataUpgrader.getObjectsDir(getJsonDir(), type);
	}
	
	public File getDiagramFactorDir()
	{
		return diagramFactorDir;
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
	
	private static final int DEFAULT_GRID_SIZE = 15;
	private static final int SHORT_SCOPE_HEIGHT = 27;
	
	private File diagramFactorDir;
	private Vector<EnhancedJsonObject> allDiagramFactorJsons;
	private EnhancedJsonObject projectMetadataJson;
}
