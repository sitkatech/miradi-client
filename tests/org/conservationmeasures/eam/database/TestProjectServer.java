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
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageData;
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
	
	public void testWriteAndReadLinkage() throws Exception
	{
		LinkageData original = new LinkageData(1, 2, 3);
		storage.writeLinkage(original);
		LinkageData got = storage.readLinkage(original.getId());
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
		LinkageData original = new LinkageData(1, 2, 3);
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
