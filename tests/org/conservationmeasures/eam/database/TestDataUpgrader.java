/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
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

	public void testUpgradeTo18AddingLinksToObject19() throws Exception
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();

		File objects13 = new File(jsonDir, "objects-13");
		objects13.mkdirs();
		
		String objects13ManifestContent = " {\"Type\":\"ObjectManifest\",\"20\":true,\"21\":true}";
		File objects13ManifestFile = new File(objects13, "manifest");
		createFile(objects13ManifestFile, objects13ManifestContent);
		assertTrue("manifest doesnt exist?", objects13ManifestFile.exists());
		
		File objects19 = new File(jsonDir, "objects-19");
		objects19.mkdirs();
		
		String objects19Content = " {\"Label\":\"\",\"DiagramFactorIds\":\"{\\\"Ids\\\":[1,2,3]}\",\"Id\":30}";
		File file30 = new File(objects19, "30");
		createFile(file30, objects19Content);
		
		//TODO when fixing the below FIXME use the method to build the manifest instead of the below 
		String objects19ManifestContent = " {\"Type\":\"ObjectManifest\",\"30\":true}";
		File objects19ManifestFile = new File(objects19, "manifest");
		createFile(objects19ManifestFile, objects19ManifestContent);
		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.upgradeToVersion18();
		
		File newFile30 = new File(objects19, "30");
		EnhancedJsonObject readIn30 = JSONFile.read(newFile30);

		String factorIdsAsString = readIn30.getString("DiagramFactorIds");
		IdList diagramFactorIds = new IdList(factorIdsAsString);
		assertEquals("same size?", 3, diagramFactorIds.size());
		
		String linkIdsAsString = readIn30.getString("DiagramFactorLinkIds");
		IdList diagramFactorLinkIds = new IdList(linkIdsAsString);
		assertEquals("same size?", 2, diagramFactorLinkIds.size());
		//FIXME: write test to check the content of both lists (nima)
	}
	
	public void testUpgradeTo17creatingObjects19FromDiagramsMainFile() throws Exception
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();

		File diagramsDir = new File(jsonDir, "diagrams");
		diagramsDir.mkdirs();
		
		String diagramFactorIds = " {\"Type\":\"Diagram\",\"DiagramFactorIds\":{\"Ids\":[676,691,664]}} ";
		File diagramMain = new File(diagramsDir, "main");
		createFile(diagramMain, diagramFactorIds);
		
		File projectFile = new File(jsonDir, "project");
		createFile(projectFile, "{\"HighestUsedNodeId\":13}");
		
		DataUpgrader upgraderWithNoObjects19 = new DataUpgrader(tempDirectory);
		upgraderWithNoObjects19.upgradeToVersion17();
		
		File objects19Dir = new File(jsonDir, "objects-19");
		assertTrue("didn't create objects-19 dir?", objects19Dir.exists());
		
		File manifest19File = new File(objects19Dir, "manifest");
		assertTrue("didn't create manifest file?", manifest19File.exists());
		
		String expectedManifestContent = "{\"Type\":\"ObjectManifest\",\"14\":true}";
		String migratedManifestContents = readFile(manifest19File);
		assertEquals("manifest contents wrong?", expectedManifestContent.trim(), migratedManifestContents.trim());
		
		File object14File = new File(objects19Dir, "14");
		assertTrue("didn't create object 14 file?", object14File.exists());
		
		EnhancedJsonObject json = new EnhancedJsonObject(readFile(object14File));
		int id = json.getInt("Id");
		assertEquals("wrong object id?", id, 14);
		ConceptualModelDiagram diagramContents = new ConceptualModelDiagram(id, json);
		IdList allDiagramFactorIds = diagramContents.getAllDiagramFactorIds();
		assertEquals("wrong id count?", 3, allDiagramFactorIds.size());
		
		assertTrue("missing 676?", allDiagramFactorIds.contains(new BaseId(676)));
		assertTrue("missing 691?", allDiagramFactorIds.contains(new BaseId(691)));
		assertTrue("missing 664?", allDiagramFactorIds.contains(new BaseId(664)));
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

	private String readFile(File file) throws IOException
	{
		UnicodeReader reader = new UnicodeReader(file);
		String contents = reader.readAll();
		reader.close();
		return contents;
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
