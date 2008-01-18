/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.main.EAMTestCase;

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
