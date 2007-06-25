/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.database;

import java.io.File;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class DataUpgraderDiagramObjectLinkAdder
{
	public DataUpgraderDiagramObjectLinkAdder(File topDirectoryToUse)
	{
		topDirectory = topDirectoryToUse;
	}
	
	public void addLinksInAllDOsWhereNeeded() throws Exception
	{
		File jsonDir = new File(topDirectory, "json");
		
		//factor
		File factorDir = new File(jsonDir, "objects-4");
		if (!factorDir.exists())
			return;		
		File factorManifestFile = new File(factorDir, "manifest");
		if (! factorManifestFile.exists())
			throw new RuntimeException("manifest for objects-4 directory does not exist " + factorManifestFile.getAbsolutePath());
		
		//factorLink
		File factorLinkDir = new File(jsonDir, "objects-6");
		if (! factorLinkDir.exists())
			return;
		File factorLinkManifestFile = new File(factorLinkDir, "manifest");
		if (! factorLinkManifestFile.exists())
			throw new RuntimeException("manifest for objects-6 directory does not exist " + factorLinkManifestFile.getAbsolutePath());
		
		//diagramLink
		File diagramLinkDir = new File(jsonDir, "objects-13");
		if (! diagramLinkDir.exists())
			return;
		File diagramLinkManifestFile = new File(diagramLinkDir, "manifest");
		if (! diagramLinkManifestFile.exists())
			throw new RuntimeException("manifest for objects-13 directory does not exist " + diagramLinkManifestFile.getAbsolutePath());

		//diagramFactor
		File diagramFactorDir = new File(jsonDir, "objects-18");
		if (! diagramFactorDir.exists())
			return;
		File diagramFactorManifestFile = new File(diagramFactorDir, "manifest");
		if (! diagramFactorManifestFile.exists())
			throw new RuntimeException("manifest for objects-18 directory does not exist " + diagramFactorManifestFile.getAbsolutePath());

		//conceptualModel
		File conceptualModelDir = new File(jsonDir, "objects-19");
		if (! conceptualModelDir.exists())
			throw new RuntimeException("manifest for objects-19 directory does not exist " + conceptualModelDir.getAbsolutePath());
		File conceptualModelManifestFile = new File(conceptualModelDir, "manifest");
		if (! conceptualModelManifestFile.exists())
			throw new RuntimeException("manifest for objects-19 directory does not exist " + conceptualModelManifestFile.getAbsolutePath());

		//resultsChain
		File resultsChainDir = new File(jsonDir, "objects-24");
		if (! resultsChainDir.exists())
			return;
		File resultsChainManifestFile = new File(resultsChainDir, "manifest");
		if (! resultsChainManifestFile.exists())
			throw new RuntimeException("manifest for objects-24 directory does not exist " + resultsChainManifestFile.getAbsolutePath());

		BaseId[] linksStratsAndTargets = areStratsDirectlyLinkedToTargets(factorLinkDir, factorLinkManifestFile); 
		if (linksStratsAndTargets.length == 0)
			return;
		
		DiagramObject[] diagramObjects = getAllDiagramObjects(conceptualModelDir, conceptualModelManifestFile, resultsChainDir, resultsChainManifestFile);
		if (hasNoResultsChainObjects(diagramObjects))
			return;
		
		DiagramLink[] allDiagramLinks = getAllDiagramLinks(diagramLinkDir, diagramLinkManifestFile);
		
		for (int i = 0; i < linksStratsAndTargets.length; ++i)
		{
			BaseId factorLinkId = linksStratsAndTargets[i];
			String idAsString = Integer.toString(factorLinkId.asInt());
			File factorLinkFile = new File(factorLinkDir, idAsString);
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
			ORef fromRef = new ORef(factorLinkJson.getJson("FromRef"));
			ORef toRef = new ORef(factorLinkJson.getJson("ToRef"));
			BaseId[] fromDiagramFactorIds = findDiagramFactor(diagramFactorDir, diagramFactorManifestFile, fromRef);
			BaseId[] toDiagramFactorIds = findDiagramFactor(diagramFactorDir, diagramFactorManifestFile, toRef);

			possiblyLinkToAndFrom(jsonDir, diagramObjects, factorLinkId, diagramLinkDir, allDiagramLinks, diagramLinkManifestFile, fromDiagramFactorIds, toDiagramFactorIds);
		}	
		
	}
	
	private void possiblyLinkToAndFrom(File jsonDir, DiagramObject[] diagramObjects, BaseId wrappedLinkId, File diagramLinkDir, DiagramLink[] allDiagramLinks, File diagramLinkManifestFile, BaseId[] fromDiagramFactorIds, BaseId[] toDiagramFactorIds) throws Exception
	{
		for (int i = 0; i < diagramObjects.length; ++i)
		{
			DiagramObject diagramObject = diagramObjects[i];
			IdList allDiagramFactorIds = diagramObject.getAllDiagramFactorIds();
			BaseId from = null;
			BaseId to = null;
			for (int fromIndex = 0; fromIndex < fromDiagramFactorIds.length; ++fromIndex)
			{
				if (allDiagramFactorIds.contains(fromDiagramFactorIds[fromIndex]))
					from = fromDiagramFactorIds[fromIndex];
			}
			
			for (int toIndex = 0; toIndex < toDiagramFactorIds.length; ++toIndex)
			{
				if (allDiagramFactorIds.contains(toDiagramFactorIds[toIndex]))
					to = toDiagramFactorIds[toIndex];
			}
			
			if (to == null || from == null)
				continue;
			
			if (areLinked(allDiagramLinks, from, to))
				continue;
			
			EnhancedJsonObject diagramObjectJson = diagramObject.toJson();
			linkThem(jsonDir, diagramObjectJson, diagramLinkDir, wrappedLinkId, diagramLinkManifestFile, from, to);
		}
	}
	
	private void linkThem(File jsonDir, EnhancedJsonObject diagramObjectJson, File diagramLinkDir, BaseId wrappedLinkId, File diagramLinkManifestFile, BaseId from, BaseId to) throws Exception
	{
		int highestId = DataUpgrader.readHighestIdInProjectFile(jsonDir);
		highestId++;
		ObjectManifest diagramLinkManifest = new ObjectManifest(JSONFile.read(diagramLinkManifestFile));
		diagramLinkManifest.put(new BaseId(highestId));
		
		EnhancedJsonObject diagramFactorLinkJson = new EnhancedJsonObject();
		diagramFactorLinkJson.put("WrappedLinkId", wrappedLinkId.toString());
		diagramFactorLinkJson.put("Id", highestId);
		diagramFactorLinkJson.put("ToDiagramFactorId", to.toString());
		diagramFactorLinkJson.put("FromDiagramFactorId", from.toString());
		
		File idFile = new File(diagramLinkDir, Integer.toString(highestId));
		DataUpgrader.createFile(idFile, diagramFactorLinkJson.toString());
		
		
		DataUpgrader.writeHighestIdToProjectFile(jsonDir, highestId);
		DataUpgrader.writeJson(idFile, diagramFactorLinkJson);
		DataUpgrader.writeJson(diagramLinkManifestFile, diagramLinkManifest.toJson());
		
		String diagramObjectFileName = diagramObjectJson.getString("Id");
		String diagramObjectLinksAsString = diagramObjectJson.getString("DiagramFactorLinkIds");
		IdList diagramObjectLinks = new IdList(diagramObjectLinksAsString);
		diagramObjectLinks.add(new BaseId(highestId));
		diagramObjectJson.put("DiagramFactorLinkIds", diagramObjectLinks.toJson());
		File diagramObjectDir = findCorrectDiagramObjectDir(jsonDir, diagramObjectJson, diagramObjectFileName);
		File diagramObjectFile = new File(diagramObjectDir, diagramObjectFileName);
		DataUpgrader.writeJson(diagramObjectFile, diagramObjectJson);
	}

	private File findCorrectDiagramObjectDir(File jsonDir, EnhancedJsonObject diagramObjectJson, String diagramObjectFileName) throws Exception
	{ 
		File conceptualModelDir = new File(jsonDir, "objects-19");
		File possibleConceptualModel = new File(conceptualModelDir, diagramObjectFileName);
		if (possibleConceptualModel.exists())
			return conceptualModelDir;
		
		File resultsChainDir = new File(jsonDir, "objects-24");
		File possibleResultsChain = new File(resultsChainDir, diagramObjectFileName);
		if (possibleResultsChain.exists())
			return resultsChainDir;
		
		throw new RuntimeException("could not find diagram object file " +  diagramObjectFileName + " in either the Conceptual model or Results chain diagram");
	}

	private boolean areLinked(DiagramLink[] allDiagramLinks, BaseId from, BaseId to)
	{
		for (int i = 0; i < allDiagramLinks.length; ++i)
		{
			DiagramLink diagramLink = allDiagramLinks[i];
			DiagramFactorId fromId = diagramLink.getFromDiagramFactorId();
			DiagramFactorId toId = diagramLink.getToDiagramFactorId();
			if (from.equals(fromId) && to.equals(toId))
				return true;
		}
		
		return false;
	}

	private DiagramLink[] getAllDiagramLinks(File diagramLinkDir, File diagramLinkManifestFile) throws Exception
	{
		ObjectManifest diagramLinkManifest = new ObjectManifest(JSONFile.read(diagramLinkManifestFile));
		BaseId[] diagramLinkIds = diagramLinkManifest.getAllKeys();
		DiagramLink[] allDiagramLinks = new DiagramLink[diagramLinkIds.length];
		for (int i = 0; i < diagramLinkIds.length; ++i)
		{
			BaseId diagramLinkId = diagramLinkIds[i];
			String idAsString = Integer.toString(diagramLinkId.asInt());
			File diagramLinkFile = new File(diagramLinkDir, idAsString);
			EnhancedJsonObject diagramLinkJson = DataUpgrader.readFile(diagramLinkFile);
			allDiagramLinks[i] = new DiagramLink(diagramLinkId.asInt(), diagramLinkJson);
		}
		
		return allDiagramLinks;
	}

	private boolean hasNoResultsChainObjects(DiagramObject[] diagramObjects)
	{
		return diagramObjects.length == 1;
	}

	private DiagramObject[] getAllDiagramObjects(File conceptualModelDir, File conceptualModelManifestFile, File resultsChainDir, File resultsChainManifestFile) throws Exception
	{
		Vector allDiagramObjects = new Vector();
		
		ObjectManifest conceptualModelManifest = new ObjectManifest(JSONFile.read(conceptualModelManifestFile));
		BaseId conceptualModelId = conceptualModelManifest.getAllKeys()[0];
		String idAsString = Integer.toString(conceptualModelId.asInt());
		File conceptualModelFile = new File(conceptualModelDir, idAsString);
		EnhancedJsonObject conceptualModelJson = DataUpgrader.readFile(conceptualModelFile);
		ConceptualModelDiagram conceptualModel = new ConceptualModelDiagram(conceptualModelId.asInt(), conceptualModelJson);
		allDiagramObjects.add(conceptualModel);

		ObjectManifest resultsChainManifest = new ObjectManifest(JSONFile.read(resultsChainManifestFile));
		BaseId[] resultsChainIds = resultsChainManifest.getAllKeys();
		for (int i = 0; i < resultsChainIds.length; ++i)
		{
			BaseId resultsChainId = resultsChainIds[i];
			String asString = Integer.toString(resultsChainId.asInt());
			File resultsChainFile = new File(resultsChainDir, asString);
			EnhancedJsonObject resultsChainJson = DataUpgrader.readFile(resultsChainFile);
			ResultsChainDiagram resultsChain = new ResultsChainDiagram(resultsChainId.asInt(), resultsChainJson);
			allDiagramObjects.add(resultsChain);
		}
		
		return (DiagramObject[]) allDiagramObjects.toArray(new DiagramObject[0]);
	}

	private BaseId[] findDiagramFactor(File diagramFactorDir, File diagramFactorManifestFile, ORef wrappedRef) throws Exception
	{
		ObjectManifest diagramFactorManifest = new ObjectManifest(JSONFile.read(diagramFactorManifestFile));
		BaseId[] diagramFactorIds = diagramFactorManifest.getAllKeys();
		Vector allDiagramFactorsThatWrap = new Vector();
		for (int i = 0; i < diagramFactorIds.length; ++i)
		{
			BaseId diagramFactorId = diagramFactorIds[i];
			String idAsString = Integer.toString(diagramFactorId.asInt());
			File diagramFactorFile = new File(diagramFactorDir, idAsString);
			EnhancedJsonObject diagramFactorJson = DataUpgrader.readFile(diagramFactorFile);
			DiagramFactor diagramFactor = new DiagramFactor(diagramFactorId.asInt(), diagramFactorJson);
			if (diagramFactor.getWrappedId().equals(wrappedRef.getObjectId()))
				allDiagramFactorsThatWrap.add(diagramFactor.getId());
		}

		return (BaseId[]) allDiagramFactorsThatWrap.toArray(new BaseId[0]);
	}

	private BaseId[] areStratsDirectlyLinkedToTargets(File factorLinkDir, File factorLinkManifestFile) throws Exception
	{
		ObjectManifest factorLinkManifest = new ObjectManifest(JSONFile.read(factorLinkManifestFile));
		BaseId[] factorLinkIds = factorLinkManifest.getAllKeys();
		Vector allLinksBetweenStratsAndTargets = new Vector(); 
		for (int i = 0; i < factorLinkIds.length; ++i)
		{
			BaseId factorLinkId = factorLinkIds[i];
			String idAsString = Integer.toString(factorLinkId.asInt());
			File factorLinkFile = new File(factorLinkDir, idAsString);
			EnhancedJsonObject factorLinkJson = DataUpgrader.readFile(factorLinkFile);
			
			ORef fromRef = new ORef(factorLinkJson.getJson("FromRef"));
			ORef toRef = new ORef(factorLinkJson.getJson("ToRef"));
			if (fromRef.getObjectType() == ObjectType.STRATEGY && toRef.getObjectType() == ObjectType.TARGET)
				allLinksBetweenStratsAndTargets.add(factorLinkId);
			
			if (toRef.getObjectType() == ObjectType.STRATEGY && fromRef.getObjectType() == ObjectType.TARGET)
				allLinksBetweenStratsAndTargets.add(factorLinkId);
		}	
		
		return (BaseId[]) allLinksBetweenStratsAndTargets.toArray(new BaseId[0]);		
	}

	private File topDirectory;
}
