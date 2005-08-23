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

public class TestFileStorage extends EAMTestCase
{
	public TestFileStorage(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		File temp = createTempFile();

		FileStorage storage = new FileStorage();
		assertEquals("not empty to start?", 0, storage.getCommandCount());
		assertFalse("already has a file?", storage.hasFile());
		
		try
		{
			storage.appendCommand(new CommandInsertNode(Node.TYPE_GOAL));
			fail("Should have thrown since no file was loaded");
		}
		catch(IOException ignoreExpected)
		{
		}

		storage.setFile(temp);
		assertTrue("no file?", storage.hasFile());
		assertEquals("wrong file name?", temp.getName(), storage.getName());
		
		Vector nothingYet = FileStorage.load(temp);
		assertEquals("brand new file not empty?", 0, nothingYet.size());
		
		Command createGoal = new CommandInsertNode(Node.TYPE_GOAL);
		Command createThreat = new CommandInsertNode(Node.TYPE_THREAT);
		storage.appendCommand(createGoal);
		storage.appendCommand(createThreat);
		assertEquals("count doesn't show appended commands?", 2, storage.getCommandCount());
		assertEquals("goal not gettable?", createGoal, storage.getCommandAt(0));
		assertEquals("threat not gettable?", createThreat, storage.getCommandAt(1));
		
		Vector loaded = FileStorage.load(temp);
		assertEquals("didn't load correct count?", 2, loaded.size());
		assertEquals("goal not loaded?", createGoal, loaded.get(0));
		assertEquals("threat not loaded?", createThreat, loaded.get(1));
		
		temp.delete();
		try
		{
			FileStorage.load(temp);
			fail("Should have thrown opening non-existant file");
		}
		catch(IOException ignoreExpected)
		{
		}
	}
}
