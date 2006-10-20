/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.testall.EAMTestCase;
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

		Version2ConceptualModelIntervention intervention = new Version2ConceptualModelIntervention("Intervention");
		File interventionFile = writeNode(tempDirectory, manifest, intervention);

		manifest.write(new File(nodesDirectory, "manifest"));

		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.renameNodeTagFromNameToLabel();
		
		int nodeType = ObjectType.MODEL_NODE;
		ConceptualModelTarget gotTarget = (ConceptualModelTarget)EAMBaseObject.createFromJson(nodeType, JSONFile.read(targetFile));
		assertEquals(target.getLabel(), gotTarget.getLabel());
		ConceptualModelFactor gotThreat = (ConceptualModelFactor)EAMBaseObject.createFromJson(nodeType, JSONFile.read(threatFile));
		assertEquals(threat.getLabel(), gotThreat.getLabel());
		ConceptualModelIntervention gotIntervention = (ConceptualModelIntervention)EAMBaseObject.createFromJson(nodeType, JSONFile.read(interventionFile));
		assertEquals(intervention.getLabel(), gotIntervention.getLabel());
	}

	private File writeNode(File topDirectory, ObjectManifest manifest, ConceptualModelNode node) throws IOException
	{
		manifest.put(node.getId());
		String nodeFilename = Integer.toString(node.getId().asInt());
		File jsonDirectory = new File(topDirectory, "json");
		File nodesDirectory = new File(jsonDirectory, "nodes");
		File targetFile = new File(nodesDirectory, nodeFilename);
		JSONFile.write(targetFile, node.toJson());
		return targetFile;
	}
	
	static class Version2ConceptualModelTarget extends ConceptualModelTarget
	{
		public Version2ConceptualModelTarget(String label) throws Exception
		{
			super(idAssigner.takeNextId());
			setLabel(label);
		}
		
		public EnhancedJsonObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
		
	}
	
	static class Version2ConceptualModelThreat extends ConceptualModelFactor
	{
		public Version2ConceptualModelThreat(String label) throws Exception
		{
			super(idAssigner.takeNextId());
			setLabel(label);
		}
		
		public EnhancedJsonObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
	}
	
	static class Version2ConceptualModelIntervention extends ConceptualModelIntervention
	{
		public Version2ConceptualModelIntervention(String label) throws Exception
		{
			super(idAssigner.takeNextId());
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

		json.put(TAG_NAME, json.get(ConceptualModelNode.TAG_LABEL));
		json.remove(ConceptualModelNode.TAG_LABEL);
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
		
		IdList noGoals = new IdList();
		noGoals.add(-1);
		final int NODE_TYPE = 4;
		for(int i = 0; i < targetIds.length; ++i)
		{
			BaseId targetId = new BaseId(targetIds[i]);
			ConceptualModelTarget target = (ConceptualModelTarget)upgrader.readObject(NODE_TYPE, targetId);
			assertEquals("Didn't remove old sample goals?", noGoals, target.getGoals());
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
	
	String buildManifestContents(IdList idList)
	{
		int[] ids = new int[idList.size()];
		for(int i = 0; i < ids.length; ++i)
			ids[i] = idList.get(i).asInt();
		return buildManifestContents(ids);
	}
	
	String buildManifestContents(int[] ids)
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
