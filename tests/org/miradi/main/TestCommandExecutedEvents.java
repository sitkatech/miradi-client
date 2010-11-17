/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.main;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;

public class TestCommandExecutedEvents extends EAMTestCase
{
	public TestCommandExecutedEvents(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		Command cmd = new CommandCreateObject(Stress.getObjectType());
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
		
		project.executeCommand(new CommandCreateObject(Stress.getObjectType()));
		assertEquals("execute didn't fire?", 1, listener.timesExecuted);
		project.executeCommand(new CommandCreateObject(Stress.getObjectType()));
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
