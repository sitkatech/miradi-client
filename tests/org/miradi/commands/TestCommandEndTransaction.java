/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.main.EAMTestCase;

public class TestCommandEndTransaction extends EAMTestCase
{
	public TestCommandEndTransaction(String name)
	{
		super(name);
	}

	public void testGetReverseCommand() throws Exception
	{
		CommandEndTransaction commandEndTrasaction = new CommandEndTransaction();
		CommandBeginTransaction reverseCommand = (CommandBeginTransaction) commandEndTrasaction.getReverseCommand();
		
		assertEquals("not same command?", new CommandBeginTransaction(), reverseCommand);	
	}
}
