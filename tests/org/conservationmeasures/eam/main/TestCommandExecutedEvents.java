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
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
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

			public void commandUndone(CommandExecutedEvent event)
			{
				++timesUndone;
			}
			
			public void commandFailed(Command command, CommandFailedException e)
			{
				++failureCount;
			}


			
			public int timesExecuted;
			public int timesUndone;
			public int failureCount;
		}
		
		Project project = new ProjectForTesting(getName());
		TestListener listener = new TestListener();
		project.addCommandExecutedListener(listener);
		assertEquals("executed not zero to start?", 0, listener.timesExecuted);
		assertEquals("undone not zero to start?", 0, listener.timesUndone);
		
		Command cmd = new CommandDoNothing();
		project.executeCommand(cmd);
		assertEquals("execute didn't fire?", 1, listener.timesExecuted);
		project.executeCommand(cmd);
		assertEquals("execute didn't fire again?", 2, listener.timesExecuted);

		project.executeCommand(new CommandUndo());
		assertEquals("undo didn't execute?", 3, listener.timesExecuted);
		assertEquals("undo didn't fire?", 1, listener.timesUndone);

		project.executeCommand(new CommandRedo());
		assertEquals("redo didn't fire two events?", 5, listener.timesExecuted);
		assertEquals("undo fired for redo?", 1, listener.timesUndone);
		
		try 
		{
			project.executeCommand(new CommandRedo());
		} 
		catch (CommandFailedException ignoreExpected) 
		{
		}
		assertEquals("redo again didn't fire failure?", 1, listener.failureCount);
		
		project.close();
	}
}
