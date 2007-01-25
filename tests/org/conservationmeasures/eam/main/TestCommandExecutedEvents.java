/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandJump;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestCommandExecutedEvents extends EAMTestCase
{
	public TestCommandExecutedEvents(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		Command cmd = new CommandJump(5);
		CommandExecutedEvent event = new CommandExecutedEvent(cmd);
		assertEquals(cmd, event.getCommand());
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
		
		Project project = new ProjectForTesting(getName());
		TestListener listener = new TestListener();
		project.addCommandExecutedListener(listener);
		assertEquals("executed not zero to start?", 0, listener.timesExecuted);
		
		Command cmd = new CommandJump(5);
		project.executeCommand(cmd);
		assertEquals("execute didn't fire?", 1, listener.timesExecuted);
		project.executeCommand(cmd);
		assertEquals("execute didn't fire again?", 2, listener.timesExecuted);

		project.undo();
		assertEquals("undo didn't fire an execute?", 3, listener.timesExecuted);
		
		project.redo();
		assertEquals("redo's command didn't fire?", 4, listener.timesExecuted);
		
		try 
		{
			project.redo();
		} 
		catch (CommandFailedException ignoreExpected) 
		{
		}

		project.removeCommandExecutedListener(listener);
		project.close();
	}
}
