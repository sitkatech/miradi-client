/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandDiagramRemoveFactor extends EAMTestCase
{
	public TestCommandDiagramRemoveFactor(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandDiagramRemoveFactor commandRemove = new CommandDiagramRemoveFactor(new DiagramFactorId(4));
		CommandDiagramAddFactor reverseCommand = (CommandDiagramAddFactor)commandRemove.getReverseCommand();
		
		assertEquals("not same id?", commandRemove.getFactorId(), reverseCommand.getFactorId());
		assertEquals("not same driagram id?", commandRemove.getDiagramNodeId(), reverseCommand.getInsertedId());
	}
}
