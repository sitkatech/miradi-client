/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.project;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnableToBeginTransactionException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.TestCaseWithProject;

public class TestCommandExecutor extends TestCaseWithProject
{
	public TestCommandExecutor(String name)
	{
		super(name);
	}

	public void testExceptionsOccurringDuringABeginTransaction() throws CommandFailedException
	{
		try
		{
			new CommandHandler(getProject());
			getProject().getCommandExecutor().getNormalExecutor().executeCommand(new CommandBeginTransaction());
			fail("Should have thrown for exception during begin transaction");
		}
		catch (UnableToBeginTransactionException ignoreExpectedException)
		{
		}
	}
	
	private class CommandHandler implements CommandExecutedListener
	{
		public CommandHandler(Project projectToUse)
		{
			getProject().addCommandExecutedListener(this);
		}

		public void commandExecuted(CommandExecutedEvent event)
		{
			throw new RuntimeException();
		}
	}	
}
