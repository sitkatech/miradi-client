/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.project.ProjectServerForTesting;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;
import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeWriter;

public class TestProjectServer extends EAMTestCase
{
	public TestProjectServer(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		storage = new ProjectServerForTesting();
		storage.openMemoryDatabase(getName());
	}
	
	public void tearDown() throws Exception
	{
		storage.close();
	}
	
	public void testObjectManifest() throws Exception
	{
		BaseId[] idsToWrite = {new BaseId(19), new BaseId(25), new BaseId(727), };
		for(int i = 0; i < idsToWrite.length; ++i)
		{
			Task task = new Task(idsToWrite[i]);
			storage.writeObject(task);
		}
		RatingCriterion criterion = new RatingCriterion(new BaseId(99));
		storage.writeObject(criterion);

		ObjectManifest manifest = storage.readObjectManifest(ObjectType.TASK);
		assertEquals("wrong number of objects?", idsToWrite.length, manifest.size());
		for(int i = 0; i < idsToWrite.length; ++i)
			assertTrue("missing id " + idsToWrite[i], manifest.has(idsToWrite[i]));
		
		storage.deleteObject(ObjectType.TASK, idsToWrite[0]);
		Manifest afterDelete = storage.readObjectManifest(ObjectType.TASK);
		assertFalse("didn't delete id?", afterDelete.has(idsToWrite[0]));
	}
	
	public void testWriteAndReadNode() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();

		ConceptualModelIntervention intervention = new ConceptualModelIntervention(idAssigner.takeNextId());
		storage.writeObject(intervention);
		ConceptualModelIntervention gotIntervention = (ConceptualModelIntervention)readNode(intervention.getId());
		assertEquals("not an intervention?", intervention.getNodeType(), gotIntervention.getNodeType());
		assertEquals("wrong id?", intervention.getId(), gotIntervention.getId());

		ConceptualModelFactor factor = new ConceptualModelFactor(idAssigner.takeNextId());
		
		storage.writeObject(factor);
		ConceptualModelFactor gotIndirectFactor = (ConceptualModelFactor)readNode(factor.getId());
		assertEquals("not indirect factor?", factor.getNodeType(), gotIndirectFactor.getNodeType());
		
		ConceptualModelTarget target = new ConceptualModelTarget(idAssigner.takeNextId());
		storage.writeObject(target);
		ConceptualModelTarget gotTarget = (ConceptualModelTarget)readNode(target.getId());
		assertEquals("not a target?", target.getNodeType(), gotTarget.getNodeType());
		
		
		ObjectManifest nodeIds = storage.readObjectManifest(ObjectType.MODEL_NODE);
		assertEquals("not three nodes?", 3, nodeIds.size());
		assertTrue("missing a node?", nodeIds.has(target.getId()));
	}
	
	private ConceptualModelNode readNode(BaseId id) throws Exception
	{
		return (ConceptualModelNode)storage.readObject(ObjectType.MODEL_NODE, id);
	}
	
	public void testWriteAndReadLinkage() throws Exception
	{
		ConceptualModelLinkage original = new ConceptualModelLinkage(new BaseId(1), new ModelNodeId(2), new ModelNodeId(3));
		storage.writeObject(original);
		ConceptualModelLinkage got = (ConceptualModelLinkage)storage.readObject(original.getType(), original.getId());
		assertEquals("wrong id?", original.getId(), got.getId());
		assertEquals("wrong from?", original.getFromNodeId(), got.getFromNodeId());
		assertEquals("wrong to?", original.getToNodeId(), got.getToNodeId());
		
		ObjectManifest linkageIds = storage.readObjectManifest(original.getType());
		assertEquals("not one linkage?", 1, linkageIds.size());
		assertTrue("wrong linkage id in manifest?", linkageIds.has(original.getId()));
		
		storage.writeObject(original);
		assertEquals("dupe in manifest?", 1, storage.readObjectManifest(original.getType()).size());
		
	}
	
	public void testDeleteLinkage() throws Exception
	{
		ConceptualModelLinkage original = new ConceptualModelLinkage(new BaseId(1), new ModelNodeId(2), new ModelNodeId(3));
		storage.writeObject(original);
		storage.deleteObject(original.getType(), original.getId());
		assertEquals("didn't delete?", 0, storage.readObjectManifest(original.getType()).size());
		try
		{
			storage.readObject(original.getType(), original.getId());
		}
		catch(IOException ignoreExpected)
		{
		}
	}
	
	public void testWriteThreatRatingBundle() throws Exception
	{
		ModelNodeId threatId = new ModelNodeId(68);
		ModelNodeId targetId = new ModelNodeId(99);
		BaseId defaultId = new BaseId(929);
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, defaultId);
		storage.writeThreatRatingBundle(bundle);
		
		ThreatRatingBundle got = storage.readThreatRatingBundle(threatId, targetId);
		assertEquals("didn't read?", defaultId, got.getValueId(new BaseId(38838)));
		
		assertNull("didn't return null for non-existent bundle?", storage.readThreatRatingBundle(new BaseId(282), new BaseId(2995)));
	}
	
	public void testWriteAndReadDiagram() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();
		ProjectForTesting project = new ProjectForTesting(getName());
		
		try
		{
			storage.readDiagram(new DiagramModel(project));
		}
		catch(Exception e)
		{
			fail("didn't allow reading non-existent diagram?");
		}
		
		DiagramModel model = project.getDiagramModel();
		NodePool nodePool = model.getNodePool();
		storage.writeDiagram(model);

		try
		{
			storage.readDiagram(new DiagramModel(project));
		}
		catch(Exception e)
		{
			fail("didn't allow reading an empty diagram?");
		}
		
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention(idAssigner.takeNextId());
		nodePool.put(cmIntervention);

		ConceptualModelTarget cmTarget = new ConceptualModelTarget(idAssigner.takeNextId());
		nodePool.put(cmTarget);
		
		model.createNode(cmIntervention.getModelNodeId());
		model.createNode(cmTarget.getModelNodeId());
		
		storage.writeDiagram(model);
		
		DiagramModel got = new DiagramModel(project); 
		storage.readDiagram(got);
		Vector gotNodes = got.getAllNodes();
		Vector expectedNodes = model.getAllNodes();
		assertEquals("wrong node count?", expectedNodes.size(), gotNodes.size());
		for(int i=0; i < gotNodes.size(); ++i)
		{
			DiagramNode gotNode = (DiagramNode)gotNodes.get(i);
			BaseId gotId = gotNode.getDiagramNodeId();
			DiagramNode expectedNode = model.getNodeById(gotId);
			assertEquals("node data not right?", expectedNode.getLocation(), gotNode.getLocation());
		}
		
		project.close();
	}
	
	public void testReadAndWriteThreatRatingFramework() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		ProjectServerForTesting db = project.getTestDatabase();
		db.writeThreatRatingFramework(project.getThreatRatingFramework());
		JSONObject got = db.readRawThreatRatingFramework();
		assertEquals(got.toString(), project.getThreatRatingFramework().toJson().toString());
		project.close();
	}
	
	public void testCreateInNonEmptyDirectory() throws Exception
	{
		File tempDirectory = createTempDirectory();
		File anyFile = new File(tempDirectory, "blah");
		anyFile.mkdirs();
		try
		{
			storage.create(tempDirectory);
			fail("Should have thrown");
		}
		catch (Exception ignoreExpected)
		{
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}
	
	public void testOpenNonProjectDirectory() throws Exception
	{
		File tempDirectory = createTempDirectory();
		try
		{
			File nonProjectFile = new File(tempDirectory, "foo");
			UnicodeWriter writer = new UnicodeWriter(nonProjectFile);
			writer.writeln("nothing");
			writer.close();
			
			assertFalse("project exists?", ProjectServer.doesProjectExist(tempDirectory));
			ProjectServer anotherStorage = new ProjectServer();
			try
			{
				anotherStorage.open(tempDirectory);
				fail("Should have thrown trying to open non-empty, non-project directory");
			}
			catch(IOException ignoreExpected)
			{
			}
			
			File nonExistantProjectDirectory = new File(tempDirectory, "DoesNotExist");
			try
			{
				anotherStorage.open(nonExistantProjectDirectory);
				fail("Should throw when opening a non-existant directory");
			}
			catch (Exception ignoreExpected)
			{
			}
			anotherStorage.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}

	private ProjectServerForTesting storage;
}
