/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.commands.CommandRedo;
import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandExecutedEvents extends EAMTestCase
{
	public TestCommandExecutedEvents(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		Command cmd = new CommandDoNothing();
		CommandExecutedEvent event = new CommandExecutedEvent(cmd);
		assertEquals(new CommandDoNothing(), event.getCommand());
	}
	
	public void testProjectFiresEvent() throws Exception
	{
		class TestListener implements CommandExecutedListener
		{
			public void commandExecuted(CommandExecutedEvent event)
			{
				++timesExecuted;
			}
			
			public int timesExecuted;
		}
		
		Project project = new ProjectForTesting(createTempDirectory());
		TestListener listener = new TestListener();
		project.addCommandExecutedListener(listener);
		assertEquals("not zero to start?", 0, listener.timesExecuted);
		
		Command cmd = new CommandDoNothing();
		project.executeCommand(cmd);
		assertEquals("execute didn't fire?", 1, listener.timesExecuted);
		project.executeCommand(cmd);
		assertEquals("execute didn't fire again?", 2, listener.timesExecuted);

		project.executeCommand(new CommandUndo());
		assertEquals("undo didn't fire?", 3, listener.timesExecuted);

		project.executeCommand(new CommandRedo());
		assertEquals("redo didn't fire two events?", 5, listener.timesExecuted);
	}
}
