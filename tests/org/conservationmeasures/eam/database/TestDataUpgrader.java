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
import java.util.NoSuchElementException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;

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

	public void testUpgradeTo20AddORefsInFactorLinks() throws Exception
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File intermediateResultsObjects = new File(jsonDir, "objects-23");
		intermediateResultsObjects.mkdirs();		
		
		ORef expectedFromRef2 = new ORef(23, new BaseId(115));
		int[] intermerdiateResultsIds = {115};
		File intermediateResultsObjectsManifest = createManifestFile(intermediateResultsObjects, intermerdiateResultsIds);
		assertTrue("factor manifest doesnt exist?", intermediateResultsObjectsManifest.exists());

		String intermediateResult =" {\"Type\":\"Intermediate Result\",\"Comment\":\"\",\"TimeStampModified\":\"1181600089656\",\"GoalIds\":\"\",\"KeyEcologicalAttributeIds\":\"\",\"IndicatorIds\":\"\",\"Label\":\"[ New Factor ]\",\"Id\":115,\"ObjectiveIds\":\"\"} ";
		File intermediateResultsFile = new File(intermediateResultsObjects, "115");
		createFile(intermediateResultsFile, intermediateResult);

		
		File factorObjects = new File(jsonDir, "objects-4");
		factorObjects.mkdirs();
		
		ORef expectedFromRef = new ORef(22, new BaseId(23));
		ORef expectedToRef = new ORef(22, new BaseId(45));
		int[] factorIds = {23, 45};
		File factorObjectsManifest = createManifestFile(factorObjects, factorIds);
		assertTrue("factor manifest doesnt exist?", factorObjectsManifest.exists());
		
		String targetString =" {\"Type\":\"Target\",\"CurrentStatusJustification\":\"\",\"ViabilityMode\":\"\",\"Comment\":\"\",\"TimeStampModified\":\"1181599939359\",\"GoalIds\":\"\",\"KeyEcologicalAttributeIds\":\"\",\"TargetStatus\":\"\",\"IndicatorIds\":\"\",\"Label\":\"New Target\",\"Id\":23,\"ObjectiveIds\":\"\"} ";
		String causeString =" {\"Type\":\"Target\",\"CurrentStatusJustification\":\"\",\"ViabilityMode\":\"\",\"Comment\":\"\",\"TimeStampModified\":\"1181599939359\",\"GoalIds\":\"\",\"KeyEcologicalAttributeIds\":\"\",\"TargetStatus\":\"\",\"IndicatorIds\":\"\",\"Label\":\"New Target\",\"Id\":45,\"ObjectiveIds\":\"\"} ";
		
		File targetFile = new File(factorObjects, "23");
		createFile(targetFile, targetString);
		
		File causeFile = new File(factorObjects, "45");
		createFile(causeFile, causeString);
	
		
		File linkObjects = new File(jsonDir, "objects-6");
		linkObjects.mkdirs();
		
		int[] linkIds = {2, 3};
		File linkObjectsManifest = createManifestFile(linkObjects, linkIds);
		assertTrue("link manifest doesnt exist?", linkObjectsManifest.exists());
		
		String linkString = " {\"FromId\":\"23\",\"ToId\":\"45\",\"TimeStampModified\":\"1181600089796\",\"Label\":\"\",\"StressLabel\":\"\",\"BidirectionalLink\":\"0\",\"Id\":2}  ";
		String link115to45 = " {\"FromId\":\"115\",\"ToId\":\"45\",\"TimeStampModified\":\"1181600089796\",\"Label\":\"\",\"StressLabel\":\"\",\"BidirectionalLink\":\"0\",\"Id\":3}  ";
		File linkFile = new File(linkObjects, "2");
		createFile(linkFile, linkString);
		
		File linkFile2 = new File(linkObjects, "3");
		createFile(linkFile2, link115to45);

		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.changeLinkFromToIdsToORefs();
		
		EnhancedJsonObject json = new EnhancedJsonObject(readFile(linkFile));
		checkNewlyWrittenORef(expectedFromRef, json, "FromRef");		
		checkNewlyWrittenORef(expectedToRef, json, "ToRef");
		
		EnhancedJsonObject json2 = new EnhancedJsonObject(readFile(linkFile2));
		checkNewlyWrittenORef(expectedFromRef2, json2, "FromRef");
	}

	private void checkNewlyWrittenORef(ORef expectedFromRef, EnhancedJsonObject json, String tag)
	{
		try
		{
			EnhancedJsonObject fromRefAsString = json.getJson(tag);
			ORef retreivedFromRef = new ORef(fromRefAsString);
			assertEquals("wrong ref", retreivedFromRef, expectedFromRef);
		}
		catch (NoSuchElementException ignore)
		{
			fail("ref does not exist in link?");	
		}
	}
	
	public void testUpgradeTo19RemovingGoalIdsFromIndicators() throws Exception
	{
		File jsonDir = new File(tempDirectory, "json");
		jsonDir.mkdirs();
		
		File objectsIndicator = new File(jsonDir, "objects-8");
		objectsIndicator.mkdirs();

		int[] indicatorIds = {33};
		File objectsIndicatorManifestFile = createManifestFile(objectsIndicator, indicatorIds);
		assertTrue("indicator manifest doesnt exist?", objectsIndicatorManifestFile.exists());

		File objectsGoals = new File(jsonDir, "objects-10");
		objectsGoals.mkdirs();
		
		int[] rawGoalIds = {44, 55};
		File objectsGoalManifestFile = createManifestFile(objectsGoals, rawGoalIds);
		assertTrue("goal manifest doesnt exist?", objectsGoalManifestFile.exists());

		String indicator33 = " {\"Status\":\"\",\"MeasurementDate\":\"\",\"RatingSource\":\"\",\"MeasurementStatus\":\"\",\"ShortLabel\":\"\",\"GoalIds\":\"{\\\"Ids\\\":[44]}\",\"MeasurementDetail\":\"\",\"Priority\":\"\",\"Label\":\"24\",\"MeasurementTrend\":\"\",\"MeasurementStatusConfidence\":\"\",\"MeasurementSummary\":\"\",\"TaskIds\":\"\",\"IndicatorThresholds\":\"\",\"Id\":33} ";
		File indicator33File = new File(objectsIndicator, "33");
		createFile(indicator33File, indicator33);
		
		String goal44 = "{\"ShortLabel\":\"\",\"FullText\":\"\",\"ByWhen\":\"\",\"DesiredStatus\":\"\",\"DesiredDetail\":\"\",\"Label\":\"\",\"DesiredSummary\":\"\",\"Id\":44}";
		File goal44File = new File(objectsGoals, "44");
		createFile(goal44File, goal44);
		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.removeGoalsFromIndicators();
		
		EnhancedJsonObject json = new EnhancedJsonObject(readFile(indicator33File));
		String goalIdsAsString = json.getString("GoalIds");
		IdList goalIds = new IdList(goalIdsAsString);
		assertEquals("has no goals", 0, goalIds.size());
		
		File goalDirManifest = new File(objectsGoals, "manifest");
		ObjectManifest manifest10 = new ObjectManifest(JSONFile.read(goalDirManifest));
		BaseId[] allGoalIds = manifest10.getAllKeys();
		assertEquals("failed to delete one goal?", 1, allGoalIds.length);
		assertEquals("failed to delete correct goal?", 55, allGoalIds[0].asInt());
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
		
		createManifestFile(objects19, new int[] {30});
		
		DataUpgrader upgrader = new DataUpgrader(tempDirectory);
		upgrader.upgradeToVersion18();
		
		File newFile30 = new File(objects19, "30");
		EnhancedJsonObject readIn30 = JSONFile.read(newFile30);

		String factorIdsAsString = readIn30.getString("DiagramFactorIds");
		IdList diagramFactorIds = new IdList(factorIdsAsString);
		assertEquals("same size?", 3, diagramFactorIds.size());
		assertContains(1, diagramFactorIds.toIntArray());
		assertContains(2, diagramFactorIds.toIntArray());
		assertContains(3, diagramFactorIds.toIntArray());
		
		String linkIdsAsString = readIn30.getString("DiagramFactorLinkIds");
		IdList diagramFactorLinkIds = new IdList(linkIdsAsString);
		assertEquals("same size?", 2, diagramFactorLinkIds.size());
		assertContains(20, diagramFactorLinkIds.toIntArray());
		assertContains(21, diagramFactorLinkIds.toIntArray());
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
		DiagramLink diagramLink = new DiagramLink(135, json1);
		assertEquals("same wrapped id?", 57, diagramLink.getWrappedId().asInt());
		String fromDiagramLinkId = diagramLink.getData(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		String toDiagramLinkId = diagramLink.getData(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
		assertEquals("same from diagram link id?", Integer.toString(93), fromDiagramLinkId);
		assertEquals("same from diagram link id?", Integer.toString(94), toDiagramLinkId);
	
		
		File file2 = new File(objects13Dir, "136");
		assertTrue("diagram link file 136 exists?", file2.exists());
		
		EnhancedJsonObject json2 = new EnhancedJsonObject(readFile(file2));
		DiagramLink diagramLink2 = new DiagramLink(136, json2);
		assertEquals("same wrapped id?", 56, diagramLink2.getWrappedId().asInt());
		String fromDiagramLinkId2 = diagramLink2.getData(DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
		String toDiagramLinkId2 = diagramLink2.getData(DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
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
		
		EnhancedJsonObject readIn1 = JSONFile.read(file1);
		String factorIdsAsString = readIn1.getString("WrappedFactorId");
		BaseId wrappedId1 = new BaseId(factorIdsAsString);
		Dimension size1 = EnhancedJsonObject.convertToDimension(readIn1.getString("Size"));
		Point location1 = EnhancedJsonObject.convertToPoint(readIn1.getString("Location")); 
		assertEquals(wrappedId1.asInt(),  28);
		assertEquals(new Dimension(120,60),	size1);
		assertEquals(new Point(120, 270), location1);

		EnhancedJsonObject readIn2 = JSONFile.read(file2);
		String factorIdsAsString2 = readIn2.getString("WrappedFactorId");
		BaseId wrappedId2 = new BaseId(factorIdsAsString2);
		Dimension size2 = EnhancedJsonObject.convertToDimension(readIn2.getString("Size"));
		Point location2 = EnhancedJsonObject.convertToPoint(readIn2.getString("Location")); 
		assertEquals(wrappedId2.asInt(), 29);
		assertEquals(new Dimension(151,60),	size2);
		assertEquals(new Point(375, 15), location2);
		
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
	
	private File createManifestFile(File parent, int[] ids) throws Exception
	{
		return DataUpgrader.createManifestFile(parent, ids);
	}
	
	private String buildManifestContents(int[] ids)
	{
		return DataUpgrader.buildManifestContents(ids);
	}
	
	private void createFile(File file, String contents) throws Exception
	{
		DataUpgrader.createFile(file, contents);
	}

	public static IdAssigner idAssigner = new IdAssigner();
	File tempDirectory;
}
