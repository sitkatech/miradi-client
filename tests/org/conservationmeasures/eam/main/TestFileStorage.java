/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.martus.util.DirectoryUtils;

public class TestFileStorage extends EAMTestCase
{
	public TestFileStorage(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		File tempDirectory = createTempDirectory();

		EAMDatabase storage = new EAMDatabase();
		assertEquals("not empty to start?", 0, storage.getCommandCount());
		assertFalse("already has a file?", storage.doesProjectExist());
		
		try
		{
			storage.appendCommand(new CommandInsertNode(Node.TYPE_TARGET));
			fail("Should have thrown since no file was loaded");
		}
		catch(IOException ignoreExpected)
		{
		}

		storage.setDirectory(tempDirectory);
		storage.createEmpty();
		assertTrue("no file?", storage.doesProjectExist());
		assertEquals("wrong file name?", tempDirectory.getName(), storage.getName());
		
		Vector nothingYet = storage.load();
		assertEquals("brand new file not empty?", 0, nothingYet.size());
		
		Command createTarget = new CommandInsertNode(Node.TYPE_TARGET);
		Command createFactor = new CommandInsertNode(Node.TYPE_FACTOR);
		storage.appendCommand(createTarget);
		storage.appendCommand(createFactor);
		assertEquals("count doesn't show appended commands?", 2, storage.getCommandCount());
		assertEquals("target not gettable?", createTarget, storage.getCommandAt(0));
		assertEquals("factor not gettable?", createFactor, storage.getCommandAt(1));
		
		Vector loaded = storage.load();
		assertEquals("didn't load correct count?", 2, loaded.size());
		assertEquals("target not loaded?", createTarget, loaded.get(0));
		assertEquals("factor not loaded?", createFactor, loaded.get(1));
		storage.close();
		
		try
		{
			storage.load();
			fail("Should have thrown loading without a directory specified");
		}
		catch(Exception ignoreExpected)
		{
		}

		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		try
		{
			storage.setDirectory(tempDirectory);
			storage.load();
			fail("Should have thrown opening non-existant file");
		}
		catch(IOException ignoreExpected)
		{
		}
	}
}
