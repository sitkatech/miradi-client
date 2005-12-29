/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIndirectFactor;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.ObjectPool;
import org.conservationmeasures.eam.project.ProjectServerForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.martus.util.DirectoryUtils;

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
	
	public void testWriteAndReadNode() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();

		ConceptualModelIntervention intervention = new ConceptualModelIntervention();
		intervention.setId(idAssigner.takeNextId());
		storage.writeNode(intervention);
		ConceptualModelIntervention gotIntervention = (ConceptualModelIntervention)storage.readNode(intervention.getId());
		assertEquals("not an intervention?", intervention.getType(), gotIntervention.getType());
		assertEquals("wrong id?", intervention.getId(), gotIntervention.getId());

		ConceptualModelFactor factor = new ConceptualModelFactor(new NodeTypeIndirectFactor());
		factor.setId(idAssigner.takeNextId());
		
		storage.writeNode(factor);
		ConceptualModelFactor gotIndirectFactor = (ConceptualModelFactor)storage.readNode(factor.getId());
		assertEquals("not indirect factor?", factor.getType(), gotIndirectFactor.getType());
		
		factor.setType(DiagramNode.TYPE_DIRECT_THREAT);
		storage.writeNode(factor);
		ConceptualModelFactor gotDirectThreat = (ConceptualModelFactor)storage.readNode(factor.getId());
		assertEquals("not direct threat?", factor.getType(), gotDirectThreat.getType());
		
		factor.setType(DiagramNode.TYPE_STRESS);
		storage.writeNode(factor);
		ConceptualModelFactor gotStress = (ConceptualModelFactor)storage.readNode(factor.getId());
		assertEquals("not stress?", factor.getType(), gotStress.getType());
		
		ConceptualModelTarget target = new ConceptualModelTarget();
		target.setId(idAssigner.takeNextId());
		storage.writeNode(target);
		ConceptualModelTarget gotTarget = (ConceptualModelTarget)storage.readNode(target.getId());
		assertEquals("not a target?", target.getType(), gotTarget.getType());
		
		
		NodeManifest nodeIds = storage.readNodeManifest();
		assertEquals("not three nodes?", 3, nodeIds.size());
		assertTrue("missing a node?", nodeIds.has(target.getId()));
	}
	
	public void testWriteAndReadLinkage() throws Exception
	{
		ConceptualModelLinkage original = new ConceptualModelLinkage(1, 2, 3);
		storage.writeLinkage(original);
		ConceptualModelLinkage got = storage.readLinkage(original.getId());
		assertEquals("wrong id?", original.getId(), got.getId());
		assertEquals("wrong from?", original.getFromNodeId(), got.getFromNodeId());
		assertEquals("wrong to?", original.getToNodeId(), got.getToNodeId());
		
		LinkageManifest linkageIds = storage.readLinkageManifest();
		assertEquals("not one linkage?", 1, linkageIds.size());
		assertTrue("wrong linkage id in manifest?", linkageIds.has(original.getId()));
		
		storage.writeLinkage(original);
		assertEquals("dupe in manifest?", 1, storage.readLinkageManifest().size());
		
	}
	
	public void testDeleteLinkage() throws Exception
	{
		ConceptualModelLinkage original = new ConceptualModelLinkage(1, 2, 3);
		storage.writeLinkage(original);
		storage.deleteLinkage(original.getId());
		assertEquals("didn't delete?", 0, storage.readLinkageManifest().size());
		try
		{
			storage.readLinkage(original.getId());
		}
		catch(IOException ignoreExpected)
		{
		}
	}
	
	public void testWriteAndReadDiagram() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();
		ObjectPool objectPool = new ObjectPool();
		
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention();
		cmIntervention.setId(idAssigner.takeNextId());
		objectPool.put(cmIntervention);

		ConceptualModelTarget cmTarget = new ConceptualModelTarget();
		cmTarget.setId(idAssigner.takeNextId());
		objectPool.put(cmTarget);
		
		DiagramModel model = new DiagramModel(objectPool);
		model.createNode(cmIntervention.getId());
		model.createNode(cmTarget.getId());
		
		storage.writeDiagram(model);
		
		DiagramModel got = new DiagramModel(objectPool); 
		storage.readDiagram(got);
		Vector gotNodes = got.getAllNodes();
		Vector expectedNodes = model.getAllNodes();
		assertEquals("wrong node count?", expectedNodes.size(), gotNodes.size());
		for(int i=0; i < gotNodes.size(); ++i)
		{
			DiagramNode gotNode = (DiagramNode)gotNodes.get(i);
			int gotId = gotNode.getId();
			DiagramNode expectedNode = model.getNodeById(gotId);
			assertEquals("node data not right?", expectedNode.getText(), gotNode.getText());
		}
	}

	public void testLoadCommands() throws Exception
	{
		ProjectServer anotherStorage = new ProjectServer();
		
		File tempDirectory = createTempDirectory();
		assertEquals("not empty to start?", 0, anotherStorage.getCommandCount());
		assertFalse("already has a file?", ProjectServer.doesProjectExist(tempDirectory));
		
		try
		{
			anotherStorage.appendCommand(new CommandInsertNode(DiagramNode.TYPE_TARGET));
			fail("Should have thrown since no file was loaded");
		}
		catch(IOException ignoreExpected)
		{
		}

		anotherStorage.open(tempDirectory);
		assertTrue("no file?", ProjectServer.doesProjectExist(tempDirectory));
		assertEquals("wrong file name?", tempDirectory.getName(), anotherStorage.getName());
		
		Vector nothingYet = anotherStorage.load();
		assertEquals("brand new file not empty?", 0, nothingYet.size());
		
		Command createTarget = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		Command createFactor = new CommandInsertNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		anotherStorage.appendCommand(createTarget);
		anotherStorage.appendCommand(createFactor);
		assertEquals("count doesn't show appended commands?", 2, anotherStorage.getCommandCount());
		assertEquals("target not gettable?", createTarget, anotherStorage.getCommandAt(0));
		assertEquals("factor not gettable?", createFactor, anotherStorage.getCommandAt(1));
		
		Vector loaded = anotherStorage.load();
		assertEquals("didn't load correct count?", 2, loaded.size());
		assertEquals("target not loaded?", createTarget, loaded.get(0));
		assertEquals("factor not loaded?", createFactor, loaded.get(1));
		anotherStorage.close();
		
		try
		{
			anotherStorage.load();
			fail("Should have thrown loading without a directory specified");
		}
		catch(Exception ignoreExpected)
		{
		}
		anotherStorage.close();
		
		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
	}

	private ProjectServerForTesting storage;
}
