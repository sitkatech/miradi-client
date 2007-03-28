/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.utils.EnhancedJsonArray;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;

public class TestDataUpgrader extends EAMTestCase
{
	public TestDataUpgrader(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		tempDirectory = createTempDirectory();
	}
	
	public void tearDown() throws Exception
	{
		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
	
		super.tearDown();
	}
	
	public void testMigrateTooOldProject() throws Exception
	{
		File jsonDirectory = new File(tempDirectory, "json");
		jsonDirectory.mkdirs();
		
		File version = new File(jsonDirectory, "version");
		createFile(version, "{\"Version\":14}");
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		try
		{
			upgrader.upgrade();
			fail("Should have thrown for version too old to migrate");
		}
		catch(DataUpgrader.MigrationTooOldException ignoreExpected)
		{
		}
	}

	public void testRenameNodeTagFromNameToLabelNoManifest() throws Exception
	{
		File nodesDirectory = new File(tempDirectory, "json/nodes");
		nodesDirectory.mkdirs();

		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.renameNodeTagFromNameToLabel();
	}
	
	public void testRenameNodeTagFromNameToLabel() throws Exception
	{
		File nodesDirectory = new File(tempDirectory, "json/nodes");
		nodesDirectory.mkdirs();
		ObjectManifest manifest = new ObjectManifest();

		Version2ConceptualModelTarget target = new Version2ConceptualModelTarget("Target");
		File targetFile = writeNode(tempDirectory, manifest, target);
		
		Version2ConceptualModelThreat threat = new Version2ConceptualModelThreat("Threat");
		File threatFile = writeNode(tempDirectory, manifest, threat);

		Version2ConceptualModelIntervention intervention = new Version2ConceptualModelIntervention("Strategy");
		File interventionFile = writeNode(tempDirectory, manifest, intervention);

		manifest.write(new File(nodesDirectory, "manifest"));

		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.renameNodeTagFromNameToLabel();
		
		int nodeType = ObjectType.FACTOR;
		Target gotTarget = (Target)BaseObject.createFromJson(nodeType, JSONFile.read(targetFile));
		assertEquals(target.getLabel(), gotTarget.getLabel());
		Cause gotThreat = (Cause)BaseObject.createFromJson(nodeType, JSONFile.read(threatFile));
		assertEquals(threat.getLabel(), gotThreat.getLabel());
		Strategy gotIntervention = (Strategy)BaseObject.createFromJson(nodeType, JSONFile.read(interventionFile));
		assertEquals(intervention.getLabel(), gotIntervention.getLabel());
	}

	private File writeNode(File topDirectory, ObjectManifest manifest, Factor node) throws IOException
	{
		manifest.put(node.getId());
		String nodeFilename = Integer.toString(node.getId().asInt());
		File jsonDirectory = new File(topDirectory, "json");
		File nodesDirectory = new File(jsonDirectory, "nodes");
		File targetFile = new File(nodesDirectory, nodeFilename);
		JSONFile.write(targetFile, node.toJson());
		return targetFile;
	}
	
	static class Version2ConceptualModelTarget extends Target
	{
		public Version2ConceptualModelTarget(String label) throws Exception
		{
			super(new FactorId(idAssigner.takeNextId().asInt()));
			setLabel(label);
		}
		
		public EnhancedJsonObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
		
	}
	
	static class Version2ConceptualModelThreat extends Cause
	{
		public Version2ConceptualModelThreat(String label) throws Exception
		{
			super(new FactorId(idAssigner.takeNextId().asInt()));
			setLabel(label);
		}
		
		public EnhancedJsonObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
	}
	
	static class Version2ConceptualModelIntervention extends Strategy
	{
		public Version2ConceptualModelIntervention(String label) throws Exception
		{
			super(new FactorId(idAssigner.takeNextId().asInt()));
			setLabel(label);
		}
		
		public EnhancedJsonObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
	}
	
	public static EnhancedJsonObject makeOld(EnhancedJsonObject json)
	{
		final String TAG_NAME = "Name";

		json.put(TAG_NAME, json.get(Factor.TAG_LABEL));
		json.remove(Factor.TAG_LABEL);
		return json;

	}
	
	
	public void testMoveNodesDirectoryToObjects4() throws Exception
	{
		String oldDirectoryName = "nodes";
		String newDirectoryName = "objects-4";

		File jsonDirectory = new File(tempDirectory, "json");
		jsonDirectory.mkdirs();
		File oldNodesDirectory = new File(jsonDirectory, oldDirectoryName);
		File newNodesDirectory = new File(jsonDirectory, newDirectoryName);

		DataUpgrader upgrader = new DataUpgrader(tempDirectory);

		// missing nodes directory is not a problem
		upgrader.moveNodesDirectoryToObjects4();
		
		oldNodesDirectory.mkdirs();
		String sampleFilename = "testing";
		String fileContents = "This is a test!";
		UnicodeWriter writer = new UnicodeWriter(new File(oldNodesDirectory, sampleFilename));
		writer.writeln(fileContents);
		writer.close();
		
		upgrader.moveNodesDirectoryToObjects4();
		
		File newSampleFile = new File(newNodesDirectory, sampleFilename);
		assertFalse("didn't remove old nodes directory?", oldNodesDirectory.exists());
		assertTrue("didn't create new nodes directory?", newNodesDirectory.exists());
		assertTrue("didn't move files?", newSampleFile.exists());
		UnicodeReader reader = new UnicodeReader(newSampleFile);
		String got = reader.readLine();
		reader.close();
		assertEquals("didn't move contents of file?", fileContents, got);
		
		try
		{
			upgrader.moveNodesDirectoryToObjects4();
			fail("Should have thrown for new directory already existing");
		}
		catch(IOException ignoreExpected)
		{
			assertContains("already exists", ignoreExpected.getMessage());
		}
		
	}
	
	public void testMoveLinkagesDirectoryToObjects6() throws Exception
	{
		String oldDirectoryName = "linkages";
		String newDirectoryName = "objects-6";

		File jsonDirectory = new File(tempDirectory, "json");
		jsonDirectory.mkdirs();
		File oldNodesDirectory = new File(jsonDirectory, oldDirectoryName);
		File newNodesDirectory = new File(jsonDirectory, newDirectoryName);

		DataUpgrader upgrader = new DataUpgrader(tempDirectory);

		// missing nodes directory is not a problem
		upgrader.moveLinkagesDirectoryToObjects6();
		
		oldNodesDirectory.mkdirs();
		String sampleFilename = "testing";
		String fileContents = "This is a test!";
		UnicodeWriter writer = new UnicodeWriter(new File(oldNodesDirectory, sampleFilename));
		writer.writeln(fileContents);
		writer.close();
		
		upgrader.moveLinkagesDirectoryToObjects6();
		
		File newSampleFile = new File(newNodesDirectory, sampleFilename);
		assertFalse("didn't remove old nodes directory?", oldNodesDirectory.exists());
		assertTrue("didn't create new nodes directory?", newNodesDirectory.exists());
		assertTrue("didn't move files?", newSampleFile.exists());
		UnicodeReader reader = new UnicodeReader(newSampleFile);
		String got = reader.readLine();
		reader.close();
		assertEquals("didn't move contents of file?", fileContents, got);
		
		try
		{
			upgrader.moveLinkagesDirectoryToObjects6();
			fail("Should have thrown for new directory already existing");
		}
		catch(IOException ignoreExpected)
		{
			assertContains("already exists", ignoreExpected.getMessage());
		}
		
	}
	
	public void testDeleteOldStresses() throws Exception
	{
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);

		// missing nodes directory is not a problem
		upgrader.dropStressFactors();

		String[] oldNodeFileContents = {
			"{\"Type\":\"Factor\",\"Subtype\":\"DirectThreat\",\"Id\":0}",
			"{\"Type\":\"Factor\",\"Subtype\":\"IndirectFactor\",\"Id\":1}",
			"{\"Type\":\"Factor\",\"Subtype\":\"Stress\",\"Id\":2}",
			"{\"Type\":\"Factor\",\"Subtype\":\"Stress\",\"Id\":3}",
			"{\"Type\":\"Target\",\"Id\":4}",
			"{\"Type\":\"Intervention\",\"Id\":5}",
		};

		File nodesDirectory = new File(tempDirectory, "json/objects-4");
		nodesDirectory.mkdirs();

		for(int i = 0; i < oldNodeFileContents.length; ++i)
			createFile(new File(nodesDirectory, Integer.toString(i)), oldNodeFileContents[i]);

		File manifestFile = new File(nodesDirectory, "manifest");
		createFile(manifestFile, buildManifestContents(new int[] {0, 1, 2, 3, 4, 5}));
		
		File diagramsDirectory = new File(tempDirectory, "json/diagrams");
		diagramsDirectory.mkdirs();
		File diagramFile = new File(diagramsDirectory, "main");
		createFile(diagramFile, "{\"Nodes\":{\"5\":{\"Id\":5},\"2\":{\"Id\":2}},\"Type\":\"Diagram\"}");
		
		upgrader.dropStressFactors();
		
		String migratedManifestContents = readFile(manifestFile);
		JSONObject migrated = new JSONObject(migratedManifestContents);
		JSONObject expected = new JSONObject(buildManifestContents(new int[] {0, 1, 4, 5}));
		assertEquals("Didn't drop stress factors?", expected.toString(), migrated.toString());
		
		String migratedDiagram = readFile(diagramFile);
		EnhancedJsonObject diagram = new EnhancedJsonObject(migratedDiagram);
		JSONObject nodes = diagram.getJson("Nodes");
		assertFalse("Didn't drop stress from diagram?", nodes.has("2"));
		assertTrue("Dropped something else from diagram?", nodes.has("5"));
	}

	public void testDeleteSampleGoalReferences() throws Exception
	{
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);


		String[] oldNodeFileContents = {
			"{\"Type\":\"Factor\",\"Id\":0}",
			"{\"Type\":\"Target\",\"Id\":1}",
			"{\"Type\":\"Target\",\"GoalIds\":[],\"Id\":2}",
			"{\"Type\":\"Target\",\"GoalIds\":[-1],\"Id\":3}",
			"{\"Type\":\"Target\",\"GoalIds\":[33],\"Id\":4}",
			"{\"Type\":\"Intervention\",\"Id\":5}",
		};
		
		int[] allIds = {0, 1, 2, 3, 4, 5, };
		int[] targetIds = {1, 2, 3, 4, };

		File nodesDirectory = new File(tempDirectory, "json/objects-4");
		// missing nodes directory is not a problem
		nodesDirectory.mkdirs();

		for(int i = 0; i < oldNodeFileContents.length; ++i)
			createFile(new File(nodesDirectory, Integer.toString(i)), oldNodeFileContents[i]);
		
		File manifestFile = new File(nodesDirectory, "manifest");
		createFile(manifestFile, buildManifestContents(allIds));
		
		upgrader.dropOldSampleGoals();
		
		for(int i = 0; i < targetIds.length; ++i)
		{
			File existingFile = new File(nodesDirectory, Integer.toString(targetIds[i]));
			EnhancedJsonObject json = JSONFile.read(existingFile);
			EnhancedJsonArray jsonArray = json.getJsonArray("GoalIds");
			
			assertEquals("Is the length the same?", jsonArray.length(), 1);
			assertEquals("Is the content the same?", jsonArray.get(0), new Integer(-1));
		}
		
	}
	
	public void testCaptureExistingValueOptions() throws Exception
	{
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		
		File jsonDirectory = new File(tempDirectory, "json");
		jsonDirectory.mkdirs();
		File threatFrameworkFile = new File(jsonDirectory, "threatframework");
		createFile(threatFrameworkFile, new JSONObject().toString());
		
		File optionsDirectory = new File(tempDirectory, "json/objects-2");
		optionsDirectory.mkdirs();
		
		IdList optionIds = new IdList();
		optionIds.add(4);
		optionIds.add(7);
		optionIds.add(5);
		
		for(int i = 0; i < optionIds.size(); ++i)
		{
			createFile(new File(optionsDirectory, Integer.toString(i)), buildValueOptionFileContents(i));
		}
		
		File manifestFile = new File(optionsDirectory, "manifest");
		createFile(manifestFile, buildManifestContents(optionIds));
		
		upgrader.captureExistingValueOptions();
		
		EnhancedJsonObject json = JSONFile.read(threatFrameworkFile);
		IdList migratedOptionIds = new IdList(json.getString("ValueOptionIds"));
		assertEquals("Wrong option count?", optionIds.size(), migratedOptionIds.size());
		for(int i = 0; i < optionIds.size(); ++i)
			assertTrue("Didn't capture #" + i + "?", migratedOptionIds.contains(optionIds.get(i)));
	}
	
	private String buildValueOptionFileContents(int id)
	{
		return "{\"Color\":-14336,\"Numeric\":" + id + ",\"Label\":\"High\",\"Id\":" + id + "}";
	}

	public void testCaptureExistingThreatRatingCriteria() throws Exception
	{
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		
		File jsonDirectory = new File(tempDirectory, "json");
		jsonDirectory.mkdirs();
		File threatFrameworkFile = new File(jsonDirectory, "threatframework");
		createFile(threatFrameworkFile, new JSONObject().toString());
		
		File optionsDirectory = new File(tempDirectory, "json/objects-1");
		optionsDirectory.mkdirs();
		
		IdList criterionIds = new IdList();
		criterionIds.add(4);
		criterionIds.add(7);
		criterionIds.add(5);
		
		for(int i = 0; i < criterionIds.size(); ++i)
		{
			createFile(new File(optionsDirectory, Integer.toString(i)), buildCriterionFileContents(i));
		}
		
		File manifestFile = new File(optionsDirectory, "manifest");
		createFile(manifestFile, buildManifestContents(criterionIds));
		
		upgrader.captureExistingThreatRatingCriteria();
		
		EnhancedJsonObject json = JSONFile.read(threatFrameworkFile);
		IdList migratedCriterionIds = new IdList(json.getString("CriterionIds"));
		assertEquals("Wrong criterion count?", criterionIds.size(), migratedCriterionIds.size());
		for(int i = 0; i < criterionIds.size(); ++i)
			assertTrue("Didn't capture #" + i + "?", migratedCriterionIds.contains(criterionIds.get(i)));
	}
	
	public void testAddDiagramNodeWrappedIds() throws Exception
	{
		File jsonDirectory = new File(tempDirectory, "json");
		File diagramsDirectory = new File(jsonDirectory, "diagrams");
		diagramsDirectory.mkdirs();
		File diagramFile = new File(diagramsDirectory, "main");
		
		EnhancedJsonObject diagramWithoutWrappedIds = new EnhancedJsonObject();
		diagramWithoutWrappedIds.put("Type", "Diagram");
		EnhancedJsonObject nodes = new EnhancedJsonObject();
		nodes.put("3", createDiagramNodeJson(3));
		nodes.put("79", createDiagramNodeJson(79));
		nodes.put("24", createDiagramNodeJson(24));
		diagramWithoutWrappedIds.put("Nodes", nodes);
		createFile(diagramFile, diagramWithoutWrappedIds.toString());
		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.addDiagramNodeWrappedIds();
		
		EnhancedJsonObject with = new EnhancedJsonObject(readFile(diagramFile));
		EnhancedJsonObject gotNodes = with.getJson("Nodes");
		JSONObject node3 = gotNodes.getJson("3");
		assertEquals(node3.get("Id"), node3.get("WrappedId"));
		JSONObject node79 = gotNodes.getJson("79");
		assertEquals(node79.get("Id"), node79.get("WrappedId"));
		JSONObject node24 = gotNodes.getJson("24");
		assertEquals(node24.get("Id"), node24.get("WrappedId"));
		
		
		try
		{
			upgrader.addDiagramNodeWrappedIds();
			fail("Should have failed if wrapped id already exists");
		}
		catch(Exception ignoreExpected)
		{
			assertContains("WrappedId", ignoreExpected.getMessage());
		}
	}
	
	public void testConvertIndicatorIdToIdList() throws Exception
	{
		String manifest = "{\"Type\":\"NodeManifest\",\"68\":true,}";
		String factor = "{\"Id\":68,\"Type\":\"Factor\",\"Label\":\"This is a test\"," +
				"\"Comment\":\"\",\"TaxonomyCode\":\"\"," +
				"\"GoalIds\":[],\"ObjectiveIds\":[],\"IndicatorId\":21}";
		
		File jsonDirectory = new File(tempDirectory, "json");
		File modelNodeDirectory = new File(jsonDirectory, "objects-4");
		modelNodeDirectory.mkdirs();
		File manifestFile = new File(modelNodeDirectory, "manifest");
		createFile(manifestFile, manifest);
		File factorFile = new File(modelNodeDirectory, "68");
		createFile(factorFile, factor);

		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.convertIndicatorIdToIdList();
		
		EnhancedJsonObject converted = new EnhancedJsonObject(readFile(factorFile));
		assertEquals(68, converted.getId("Id").asInt());
		assertEquals(21, converted.getId("IndicatorId").asInt());
		IdList indicatorIds = new IdList(converted.getString("IndicatorIds"));
		assertEquals(1, indicatorIds.size());
		assertEquals(21, indicatorIds.get(0).asInt());
		
		try
		{
			upgrader.convertIndicatorIdToIdList();
			fail("Should have failed if IndicatorIds already exists");
		}
		catch(Exception ignoreExpected)
		{
			assertContains("IndicatorIds", ignoreExpected.getMessage());
		}	
	}
	
	public void testConvertGoalIdToIdList() throws Exception
	{
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		String[] oldNodeFileContents = {
			"{\"Type\":\"Target\",\"Id\":1}",
			"{\"Type\":\"Target\",\"GoalIds\":[],\"Id\":2}",
			"{\"Type\":\"Target\",\"GoalIds\":[-1],\"Id\":3}",
			"{\"Type\":\"Target\",\"GoalIds\":[33, 44],\"Id\":4}",
			
			"{\"Type\":\"Factor\",\"Id\":0}",
			"{\"Type\":\"Factor\",\"ObjectiveIds\":[],\"Id\":5}",
			"{\"Type\":\"Factor\",\"ObjectiveIds\":[-1],\"Id\":6}",
			"{\"Type\":\"Factor\",\"ObjectiveIds\":[10, 20],\"Id\":7}",
			
			"{\"Type\":\"Intervention\",\"Id\":8}",
		};
		
		int[] allIds = {0, 1, 2, 3, 4, 5, 6, 7, 8, };
		String[] typesToConvert = {"GoalIds", "ObjectiveIds"};
		
		File nodesDirectory = new File(tempDirectory, "json/objects-4");
		// missing nodes directory is not a problem
		nodesDirectory.mkdirs();

		for(int i = 0; i < oldNodeFileContents.length; ++i)
			createFile(new File(nodesDirectory, Integer.toString(i)), oldNodeFileContents[i]);
		
		File manifestFile = new File(nodesDirectory, "manifest");
		createFile(manifestFile, buildManifestContents(allIds));
		
		upgrader.convertGoalsAndObjectivesToIdLists();
		for (int n = 0; n < typesToConvert.length; n++)
		{
			for(int i = 0; i < allIds.length; ++i)
			{
				File existingFile = new File(nodesDirectory, Integer.toString(allIds[i]));
				EnhancedJsonObject json = JSONFile.read(existingFile);
				String jsonListAsString = json.getString(typesToConvert[n]);

				IdList newIdList = new IdList(jsonListAsString);

				EnhancedJsonObject oldObject = new EnhancedJsonObject(oldNodeFileContents[i]);
				EnhancedJsonArray oldArray = oldObject.optJsonArray(typesToConvert[n]);

				assertEquals("different size ?", oldArray.length(), newIdList.size());
				for (int j = 0; j < oldArray.length(); j++)
					assertEquals("wrong id?", oldArray.getInt(j), newIdList.get(j).asInt());
			}
		}
	}
	
	public void testConvertTeamListToRoleCodes() throws Exception
	{
		String[] sampleResources = {
			"{\"Position\":\"position\",\"Label\":\"\",\"Name\":\"Not In Team\",\"Initials\":\"initials\",\"Id\":0}",
			"{\"Position\":\"position\",\"Label\":\"\",\"Name\":\"In Team\",\"Initials\":\"initials\",\"Id\":1}",
		};
		
		File resourcesDirectory = new File(tempDirectory, "json/objects-7");
		// missing nodes directory is not a problem
		resourcesDirectory.mkdirs();

		int[] allIds = new int[sampleResources.length];
		for(int i = 0; i < sampleResources.length; ++i)
		{
			allIds[i] = i;
			createFile(new File(resourcesDirectory, Integer.toString(i)), sampleResources[i]);
		}
		
		File resourceManifestFile = new File(resourcesDirectory, "manifest");
		createFile(resourceManifestFile, buildManifestContents(allIds));
	
		File metadataDirectory = new File(tempDirectory, "json/objects-11");
		metadataDirectory.mkdirs();
		
		String sampleMetadata = "{\"TeamResourceIds\":\"{\\\"Ids\\\":[1]}\"," +
				"\"ProjectScope\":\"Scope\"}";
		createFile(new File(metadataDirectory, "0"), sampleMetadata);
		File metadataManifestFile = new File(metadataDirectory, "manifest");
		createFile(metadataManifestFile, buildManifestContents(allIds));
		
		File projectFile = new File(tempDirectory, "json/project");
		createFile(projectFile, "{\"ProjectMetadataId\":0}");
		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.convertTeamListToRoleCodes();
		
		String teamCode = "\"RoleCodes\":\"{\\\"Codes\\\":[\\\"TeamMember\\\"]}";
		String notInTeam = readFile(new File(resourcesDirectory, "0"));
		assertNotContains("added to team?", "TeamMember", notInTeam);
		String inTeam = readFile(new File(resourcesDirectory, "1"));
		assertContains("didn't add to team?", teamCode, inTeam);
	}
	
	public void testAddParentOref() throws Exception
	{
		String taskWithoutParentRef  = "{\"AssignmentIds\":\"\",\"SubtaskIds\":\"\",\"Label\":\"4\",\"Id\":0}";
		String taskWithParentRef = "{\"AssignmentIds\":\"\",\"SubtaskIds\":\"\",\"Label\":\"4\",\"Id\":1,\"ParentRef\":{\"ObjectType\":4,\"ObjectId\":25}}";
		String taskWithEmptyParentRef = "{\"AssignmentIds\":\"\",\"SubtaskIds\":\"\",\"Label\":\"4\",\"Id\":4,\"ParentRef\":\"\"}";
		String strategy = "{\"Status\":\"Draft\",\"Type\":\"Intervention\",\"ActivityIds\":{\"Ids\":[0,1,4]},\"Comment\":\"\",\"GoalIds\":\"\",\"IndicatorId\":\"-1\",\"IndicatorIds\":\"\",\"Label\":\"Testing Label\",\"Id\":\"2\",\"ObjectiveIds\":\"\"}";  

		int[] allTaskIds = {0, 1, 4, };
		int[] allStrategyids = {2, };
		
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File taskDir = new File(jsonDir, "objects-3");
		taskDir.mkdirs();
		
		File taskManifestFile = new File(taskDir, "manifest");
		createFile(taskManifestFile, buildManifestContents(allTaskIds));
		
		File strategyDir =  new File(jsonDir, "objects-4");
		strategyDir.mkdirs();
		
		File strategyManifestFile = new File(strategyDir, "manifest");
		createFile(strategyManifestFile, buildManifestContents(allStrategyids));

		createFile(new File(taskDir, Integer.toString(0)), taskWithoutParentRef);
		createFile(new File(taskDir, Integer.toString(1)), taskWithParentRef);
		createFile(new File(taskDir, Integer.toString(4)), taskWithEmptyParentRef);
		createFile(new File(strategyDir, Integer.toString(2)), strategy);
		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.addParentRefToTasks();
		
		String migratedTask0 = readFile(new File(taskDir, "0"));
		EnhancedJsonObject migrated0 = new EnhancedJsonObject(migratedTask0);
		EnhancedJsonObject parentRef0 = new EnhancedJsonObject(migrated0.getString("ParentRef"));
		
		assertEquals("same parent type?", 4, parentRef0.getInt("ObjectType"));
		assertEquals("same parent id?", 2, parentRef0.getInt("ObjectId"));
		
		String migratedTask1 = readFile(new File(taskDir, "1"));
		EnhancedJsonObject migrated1 = new EnhancedJsonObject(migratedTask1);
		EnhancedJsonObject parentRef1 = new EnhancedJsonObject(migrated1.getString("ParentRef"));
		
		assertEquals("same parent type?", 4, parentRef1.getInt("ObjectType"));
		assertEquals("same parent id?", 2, parentRef1.getInt("ObjectId"));		
		
		
		String migratedTask4 = readFile(new File(taskDir, "4"));
		EnhancedJsonObject migrated4 = new EnhancedJsonObject(migratedTask4);
		EnhancedJsonObject parentRef4 = new EnhancedJsonObject(migrated4.getString("ParentRef"));
		
		assertEquals("same parent type?", 4, parentRef4.getInt("ObjectType"));
		assertEquals("same parent id?", 2, parentRef4.getInt("ObjectId"));
		
	}
	
	public void testUpgradeTo16WithNoObjects6Directory() throws Exception
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":90}");

		File diagramsDir =  new File(jsonDir, "diagrams");
		diagramsDir.mkdirs();
		
 		String allFactorInfos = " {\"Nodes\":{\"28\":{\"Size\":{\"Width\":120,\"Height\":60},\"WrappedId\":28,\"Location\":{\"Y\":270,\"X\":120},\"Id\":28},\"29\":{\"Size\":{\"Width\":151,\"Height\":60},\"WrappedId\":29,\"Location\":{\"Y\":15,\"X\":375},\"Id\":29}},\"Type\":\"Diagram\"}  ";
		File diagramMainFile = new File(diagramsDir, "main");
		createFile(diagramMainFile, allFactorInfos);

		DataUpgrader upgraderWithNoObjects6 = new DataUpgrader(tempDirectory);
		upgraderWithNoObjects6.upgradeToVersion16();
	}
	
	public void testUpgradeTo16WithObjects6DirectoryButNoManifest() throws Exception
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":90}");

		File diagramsDir =  new File(jsonDir, "diagrams");
		diagramsDir.mkdirs();
		
 		String allFactorInfos = " {\"Nodes\":{\"28\":{\"Size\":{\"Width\":120,\"Height\":60},\"WrappedId\":28,\"Location\":{\"Y\":270,\"X\":120},\"Id\":28},\"29\":{\"Size\":{\"Width\":151,\"Height\":60},\"WrappedId\":29,\"Location\":{\"Y\":15,\"X\":375},\"Id\":29}},\"Type\":\"Diagram\"}  ";
		File diagramMainFile = new File(diagramsDir, "main");
		createFile(diagramMainFile, allFactorInfos);

		File objects6Dir = new File(jsonDir, "objects-6");
		objects6Dir.mkdirs();
		
		DataUpgrader upgraderWithNoObjects6 = new DataUpgrader(tempDirectory);
		upgraderWithNoObjects6.upgradeToVersion16();
	}
	
	public void testCreateDiagramFactorLinksFromRawFactorLinks() throws Exception
	{
		String factorLink1 ="{\"FromId\":\"28\",\"ToId\":\"29\",\"Label\":\"\",\"StressLabel\":\"\",\"Id\":56}";
		String factorLink2 ="{\"FromId\":\"30\",\"ToId\":\"31\",\"Label\":\"\",\"StressLabel\":\"\",\"Id\":57}";
		int[] factorLinkIds = new int[2];
		factorLinkIds[0] = 56;
		factorLinkIds[1] = 57;
		
		HashMap factorToDiamgramFactorIdMap = new HashMap();
		factorToDiamgramFactorIdMap.put(new Integer(28), new Integer(91));
		factorToDiamgramFactorIdMap.put(new Integer(29), new Integer(92));
	
		factorToDiamgramFactorIdMap.put(new Integer(30), new Integer(93));
		factorToDiamgramFactorIdMap.put(new Integer(31), new Integer(94));
		
		
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File objects6Dir = new File(jsonDir, "objects-6");
		objects6Dir.mkdirs();
		
		File factor56File = new File(objects6Dir, "56");
		createFile(factor56File, factorLink1);
		assertTrue(factor56File.exists());
		
		File factor57File = new File(objects6Dir, "57");
		createFile(factor57File, factorLink2);
		assertTrue(factor57File.exists());
		
		String manifestContent = buildManifestContents(factorLinkIds);
		File manifestFile = new File(objects6Dir, "manifest");
		createFile(manifestFile, manifestContent);
		assertTrue(manifestFile.exists());
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":134}");
		DataUpgrader dataUpgrader = new DataUpgrader(tempDirectory);
		dataUpgrader.createDiagramFactorLinksFromRawFactorLinks(factorToDiamgramFactorIdMap);
		
		File objects13Dir = new File(jsonDir, "objects-13");
		assertTrue("objects-13 dir does not exist?", objects13Dir.exists());
		
		File manifest13File = new File(objects13Dir, "manifest");
		assertTrue("manifest exists?", manifest13File.exists());
		
		File file1 = new File(objects13Dir, "135");
		assertTrue("diagram link file 135 exists?", file1.exists());
		
		EnhancedJsonObject json1 = new EnhancedJsonObject(readFile(file1));
		DiagramFactorLink diagramLink = new DiagramFactorLink(135, json1);
		assertEquals("same wrapped id?", 57, diagramLink.getWrappedId().asInt());
		String fromDiagramLinkId = diagramLink.getData(DiagramFactorLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		String toDiagramLinkId = diagramLink.getData(DiagramFactorLink.TAG_TO_DIAGRAM_FACTOR_ID);
		assertEquals("same from diagram link id?", Integer.toString(93), fromDiagramLinkId);
		assertEquals("same from diagram link id?", Integer.toString(94), toDiagramLinkId);
	
		
		File file2 = new File(objects13Dir, "136");
		assertTrue("diagram link file 136 exists?", file2.exists());
		
		EnhancedJsonObject json2 = new EnhancedJsonObject(readFile(file2));
		DiagramFactorLink diagramLink2 = new DiagramFactorLink(136, json2);
		assertEquals("same wrapped id?", 56, diagramLink2.getWrappedId().asInt());
		String fromDiagramLinkId2 = diagramLink2.getData(DiagramFactorLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		String toDiagramLinkId2 = diagramLink2.getData(DiagramFactorLink.TAG_TO_DIAGRAM_FACTOR_ID);
		assertEquals("same from diagram link id?", Integer.toString(91), fromDiagramLinkId2);
		assertEquals("same from diagram link id?", Integer.toString(92), toDiagramLinkId2);
	
	
	}
	
	public void testCreateDiagramFactorsFromRawFactors() throws Exception
	{
 		String allFactorInfos = " {\"Nodes\":{\"28\":{\"Size\":{\"Width\":120,\"Height\":60},\"WrappedId\":28,\"Location\":{\"Y\":270,\"X\":120},\"Id\":28},\"29\":{\"Size\":{\"Width\":151,\"Height\":60},\"WrappedId\":29,\"Location\":{\"Y\":15,\"X\":375},\"Id\":29}},\"Type\":\"Diagram\"}  ";
		String expected91Content = "{\"Size\":\"{\\\"Width\\\":120,\\\"Height\\\":60}\",\"WrappedFactorId\":\"28\",\"Location\":\"{\\\"Y\\\":270,\\\"X\\\":120}\",\"Id\":91}";
		String expected92Content = "{\"Size\":\"{\\\"Width\\\":151,\\\"Height\\\":60}\",\"WrappedFactorId\":\"29\",\"Location\":\"{\\\"Y\\\":15,\\\"X\\\":375}\",\"Id\":92}";
		String expectedManifestContent = "{\"Type\":\"ObjectManifest\",\"91\":true,\"92\":true}";
		
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":90}");
		
		File diagramsDir =  new File(jsonDir, "diagrams");
		diagramsDir.mkdirs();
		
		File diagramMainFile = new File(diagramsDir, "main");
		createFile(diagramMainFile, allFactorInfos);
		
		DataUpgrader dataUpgrader = new DataUpgrader(tempDirectory);
		dataUpgrader.createDiagramFactorsFromRawFactors();
		
		File objects18Dir = new File(jsonDir, "objects-18");
		assertTrue("objects-18 dir does not exist?", objects18Dir.exists());
		
		File file1 = new File(objects18Dir, "91");
		File file2 = new File(objects18Dir, "92");
		assertTrue("file 91 exists?", file1.exists());
		assertTrue("file 92 exists?", file2.exists());
		
		//TODO create the Json and compare each item within the json.
		String file91Content = readFile(file1);
		String file92Content = readFile(file2);
		assertEquals("file 91 content the same?", expected91Content.trim(), file91Content.trim());
		assertEquals("file 92 content the same?", expected92Content.trim(), file92Content.trim());
		
		File manifestFile = new File(objects18Dir, "manifest");
		String migratedManifestContents = readFile(manifestFile);
		assertTrue("has manifest file?", manifestFile.exists());
		assertEquals("manifests has same content?", expectedManifestContent.trim(), migratedManifestContents.trim());
	}

	private EnhancedJsonObject createDiagramNodeJson(int id)
	{
		EnhancedJsonObject node = new EnhancedJsonObject();
		node.put("Id", id);
		node.putPoint("Location", new Point(5, 7));
		node.putDimension("Size", new Dimension(110,50));
		return node;
	}
	
	private String buildCriterionFileContents(int id)
	{
		return "{\"Label\":\"High\",\"Id\":" + id + "}";
	}

	private String readFile(File file) throws IOException
	{
		UnicodeReader reader = new UnicodeReader(file);
		String contents = reader.readAll();
		reader.close();
		return contents;
	}
	
	private String buildManifestContents(IdList idList)
	{
		int[] ids = new int[idList.size()];
		for(int i = 0; i < ids.length; ++i)
			ids[i] = idList.get(i).asInt();
		return buildManifestContents(ids);
	}
	
	private String buildManifestContents(int[] ids)
	{
		String contents = "{\"Type\":\"NodeManifest\"";
		for(int i = 0; i < ids.length; ++i)
		{
			contents += ",\"" + ids[i] + "\":true";
		}
		contents += "}";
		return contents;
	}
	
	void createFile(File file, String contents) throws Exception
	{
		UnicodeWriter writer = new UnicodeWriter(file);
		writer.writeln(contents);
		writer.close();
	}

	public static IdAssigner idAssigner = new IdAssigner();
	File tempDirectory;
}
