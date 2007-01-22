/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandDiagramAddFactor extends EAMTestCase
{
	public TestCommandDiagramAddFactor(String name)
	{
		super(name);		
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandDiagramAddFactor  commandAddFactor = new CommandDiagramAddFactor(new DiagramFactorId(2), new FactorId(33));
		CommandDiagramRemoveFactor reverseCommand = (CommandDiagramRemoveFactor)commandAddFactor.getReverseCommand();
		
		assertEquals("not same diagram id?", commandAddFactor.getInsertedId(), reverseCommand.getDiagramNodeId());
		assertEquals("not same id?", commandAddFactor.getFactorId(), reverseCommand.getFactorId());
	}
}