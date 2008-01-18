/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandBeginTransaction extends EAMTestCase
{
	public TestCommandBeginTransaction(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandBeginTransaction commandBeginTransaction = new CommandBeginTransaction();
		CommandEndTransaction reverseCommand = (CommandEndTransaction) commandBeginTransaction.getReverseCommand();
		
		assertEquals("not same command?", new CommandEndTransaction(), reverseCommand);
	}
}
