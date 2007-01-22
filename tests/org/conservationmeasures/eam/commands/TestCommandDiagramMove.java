/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandDiagramMove extends EAMTestCase
{
	public TestCommandDiagramMove(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		DiagramFactorId [] ids = {new DiagramFactorId(90)};
		CommandDiagramMove commandMove = new CommandDiagramMove(3, 5, ids);
		CommandDiagramMove reverseCommand = (CommandDiagramMove)commandMove.getReverseCommand();
		
		assertEquals("same factor id?", commandMove.getIds()[0], reverseCommand.getIds()[0]);
		assertEquals("same delta X?", commandMove.getDeltaX(), - (reverseCommand.getDeltaX()));
		assertEquals("same delta Y?", commandMove.getDeltaY(), - (reverseCommand.getDeltaY()));
	}

}
