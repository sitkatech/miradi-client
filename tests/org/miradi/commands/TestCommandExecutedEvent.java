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

package org.miradi.commands;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.Cause;
import org.miradi.objects.Strategy;

public class TestCommandExecutedEvent extends TestCaseWithProject
{
	public TestCommandExecutedEvent(String name)
	{
		super(name);
	}
	
	public void testIsSetDataCommandFor() throws Exception
	{
		CommandCreateObject createCommand = new CommandCreateObject(Cause.getObjectType());
		getProject().executeCommand(createCommand);
		CommandExecutedEvent event = new CommandExecutedEvent(createCommand);
		assertFalse("wrong event for command?", event.isSetDataCommandFor(createCommand.getObjectRef()));
		
		Strategy strategy = getProject().createStrategy();
		CommandSetObjectData setLabel = new CommandSetObjectData(strategy, Strategy.TAG_LABEL, "someLabel");
		getProject().executeCommand(setLabel);
		CommandExecutedEvent setLabelEvent = new CommandExecutedEvent(setLabel);
		assertTrue("event should be for strategy?", setLabelEvent.isSetDataCommandFor(strategy.getRef()));
		assertFalse("event is for wrong ref?", setLabelEvent.isSetDataCommandFor(createCommand.getObjectRef()));
	}
}
