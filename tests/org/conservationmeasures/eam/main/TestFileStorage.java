/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.commands.CommandInsertThreat;
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
			storage.appendCommand(new CommandInsertGoal());
			fail("Should have thrown since no file was loaded");
		}
		catch(IOException ignoreExpected)
		{
		}

		storage.load(temp);
		assertTrue("no file after loading?", storage.hasFile());
		assertEquals("wrong file name?", temp.getName(), storage.getName());
		
		Command createGoal = new CommandInsertGoal();
		Command createThreat = new CommandInsertThreat();
		storage.appendCommand(createGoal);
		storage.appendCommand(createThreat);
		assertEquals("count doesn't show appended commands?", 2, storage.getCommandCount());
		assertEquals("goal not gettable?", createGoal, storage.getCommand(0));
		assertEquals("threat not gettable?", createThreat, storage.getCommand(1));
		
		FileStorage loader = new FileStorage();
		loader.load(temp);
		assertEquals("didn't load correct count?", 2, loader.getCommandCount());
		assertEquals("goal not loaded?", createGoal, loader.getCommand(0));
		assertEquals("threat not loaded?", createThreat, loader.getCommand(1));
		
		temp.delete();
		try
		{
			new FileStorage().load(temp);
			fail("Should have thrown opening non-existant file");
		}
		catch(IOException ignoreExpected)
		{
		}
	}
}
